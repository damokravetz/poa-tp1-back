package com.dmk.poatp1back.services;

import com.dmk.poatp1back.models.*;
import com.dmk.poatp1back.repositories.LugarRepository;
import com.dmk.poatp1back.repositories.MovimientoRepository;
import com.dmk.poatp1back.repositories.ParteRepository;
import com.dmk.poatp1back.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StockService {
    @Autowired
    StockRepository stockRepository;
    @Autowired
    ParteRepository parteRepository;
    @Autowired
    MovimientoRepository movimientoRepository;
    @Autowired
    LugarRepository lugarRepository;

    @Transactional
    public PageImpl<Stock> obtenerStocksPorLugar(Long lugarId, Integer page, Integer size){

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Parte> partes= parteRepository.findAll(pageable);
        List<Long> partesIds=partes.stream().map(Parte::getId).collect(Collectors.toList());
        ArrayList<Stock> stocks= stockRepository.findByLugarIdAndParteIds(lugarId, partesIds);
        partes.forEach(parte -> {
            Optional<Stock>optionalStock=stocks.stream().filter(x-> x.getParte().getId().equals(parte.getId())).findFirst();
            if(optionalStock.isEmpty()){
                Stock myStock= new Stock();
                myStock.setParte(parte);
                myStock.setCantidadUso(0);
                myStock.setCantidadDesuso(0);
                myStock.setCantidadDesechado(0);
                stocks.add(myStock);
            }
        });
        return new PageImpl<Stock>(stocks, pageable, partes.getTotalElements());
    }

    @Transactional
    public PageImpl<Stock> obtenerStocksGlobal(Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Parte> partes= parteRepository.findAll(pageable);
        List<Long> partesIds=partes.stream().map(Parte::getId).collect(Collectors.toList());
        ArrayList<Stock> stocks= stockRepository.findByParteIds(partesIds);
        ArrayList<Stock> resStocks= new ArrayList<>();
        partes.forEach(parte -> {
            List<Stock>myStocks=stocks.stream().filter(x-> x.getParte().getId().equals(parte.getId())).collect(Collectors.toList());
            Stock myStock= new Stock();
            if(myStocks.isEmpty()){
                myStock.setParte(parte);
                myStock.setCantidadUso(0);
                myStock.setCantidadDesuso(0);
                myStock.setCantidadDesechado(0);
            }else {
                myStock.setParte(parte);
                myStock.setCantidadUso(myStocks.stream().mapToInt(Stock::getCantidadUso).sum());
                myStock.setCantidadDesuso(myStocks.stream().mapToInt(Stock::getCantidadDesuso).sum());
                myStock.setCantidadDesechado(myStocks.stream().mapToInt(Stock::getCantidadDesechado).sum());
            }
            resStocks.add(myStock);
        });
        return new PageImpl<Stock>(resStocks, pageable, partes.getTotalElements());
    }

    @Transactional
    public Stock ingresarStock(Long parteId, Long lugarId, Integer cantidad, String estadoDestino){
        Optional<Stock> stockOptional=stockRepository.findByParteIdAndLugarId(parteId, lugarId);
        Stock stock;
        if(stockOptional.isPresent()){
            stock=stockOptional.get();
        }else{
            Optional<Lugar> lugarOptional=lugarRepository.findById(lugarId);
            Optional<Parte> parteOptional=parteRepository.findById(parteId);
            if(lugarOptional.isPresent()&&parteOptional.isPresent()){
                stock= new Stock();
                stock.setLugar(lugarOptional.get());
                stock.setParte(parteOptional.get());
                stock.setCantidadDesuso(0);
                stock.setCantidadUso(0);
                stock.setCantidadDesechado(0);
            }else{
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Lugar de ingreso o parte invalido/a");
            }
        }
        realizarIngreso(cantidad, estadoDestino, stock);
        Movimiento movimiento = crearMovimiento(cantidad, estadoDestino, stock);
        stockRepository.save(stock);
        movimientoRepository.save(movimiento);
        return stock;
    }

    private Movimiento crearMovimiento(Integer cantidad, String estadoDestino, Stock stock) {
        Movimiento movimiento=new Movimiento();
        movimiento.setEstadoInicial(null);
        movimiento.setOrigen(null);
        movimiento.setParte(stock.getParte());
        movimiento.setDestino(stock.getLugar());
        movimiento.setEstadoFinal(Estado.valueOf(estadoDestino));
        movimiento.setCantidad(cantidad);
        movimiento.setFecha(LocalDateTime.now());
        return movimiento;
    }

    private void realizarIngreso(Integer cantidad, String estadoDestino, Stock stock) {
        switch (Estado.valueOf(estadoDestino)) {
            case DESUSO:
                stock.setCantidadDesuso((stock.getCantidadDesuso() + cantidad));
                break;
            case USO:
                stock.setCantidadUso((stock.getCantidadUso() + cantidad));
                break;
            case DESECHADO:
                stock.setCantidadDesechado((stock.getCantidadDesechado() + cantidad));
                break;
        }
    }

    @Transactional
    public Stock transferirStock(Long stockId, Integer cantidad, Long lugarId, String estadoOrigen, String estadoDestino) {
        Optional<Stock> origenOptional= stockRepository.findById(stockId);
        Stock origen;
        if(origenOptional.isPresent()){
            origen= origenOptional.get();
            Optional<Stock> destinoOptional= stockRepository.findByParteIdAndLugarId(origen.getParte().getId(), lugarId);
            Stock destino;
            if(destinoOptional.isPresent()){
                destino =destinoOptional.get();
            }else{
                Optional<Lugar> lugarOptional= lugarRepository.findById(lugarId);
                if(lugarOptional.isPresent()){
                    destino= new Stock();
                    destino.setCantidadDesuso(0);
                    destino.setCantidadUso(0);
                    destino.setCantidadDesechado(0);
                    destino.setParte(origen.getParte());
                    destino.setLugar(lugarOptional.get());
                }else{
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Lugar de destino invalido");
                }
            }
            realizarTransferencia(cantidad, estadoOrigen, estadoDestino, origen, destino);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Lugar de origen invalido");
        }
        return origen;
    }

    private void realizarTransferencia(Integer cantidad, String estadoOrigen, String estadoDestino, Stock origen, Stock destino) {
        Boolean resultado=ejecutarTransferencia(origen, destino, cantidad, estadoOrigen, estadoDestino);
        if(resultado){
            Movimiento movimiento=crearMovimiento(cantidad, estadoOrigen, estadoDestino, origen, destino);
            stockRepository.save(origen);
            stockRepository.save(destino);
            movimientoRepository.save(movimiento);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Cantidad insuficiente en lugar de origen");
        }
    }

    private Movimiento crearMovimiento(Integer cantidad, String estadoOrigen, String estadoDestino, Stock origen, Stock destino) {
        Movimiento movimiento=new Movimiento();
        movimiento.setParte(origen.getParte());
        movimiento.setCantidad(cantidad);
        movimiento.setOrigen(origen.getLugar());
        movimiento.setDestino(destino.getLugar());
        movimiento.setEstadoInicial(Estado.valueOf(estadoOrigen));
        movimiento.setEstadoFinal(Estado.valueOf(estadoDestino));
        movimiento.setFecha(LocalDateTime.now());
        return movimiento;
    }

    private Boolean ejecutarTransferencia(Stock stockOrigen, Stock stockDestino, Integer cantidad, String estadoOrigen, String estadoDestino){
        Boolean res=false;
        if(cantidad<=0) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Cantidad debe ser mayor a 0");
        switch (Estado.valueOf(estadoOrigen)){
            case DESUSO:
                if(stockOrigen.getCantidadDesuso()>=cantidad) {
                    stockOrigen.setCantidadDesuso((stockOrigen.getCantidadDesuso()-cantidad));
                    ejecutarTransferenciaDestino(stockDestino, cantidad, estadoDestino);
                    res=true;
                }else{
                    res=false;
                }
                break;
            case USO:
                if(stockOrigen.getCantidadUso()>=cantidad) {
                    stockOrigen.setCantidadUso((stockOrigen.getCantidadUso()-cantidad));
                    ejecutarTransferenciaDestino(stockDestino, cantidad, estadoDestino);
                    res=true;
                }else{
                    res=false;
                }
                break;
            case DESECHADO:
                if(stockOrigen.getCantidadDesechado()>=cantidad) {
                    stockOrigen.setCantidadDesechado((stockOrigen.getCantidadDesuso()-cantidad));
                    ejecutarTransferenciaDestino(stockDestino, cantidad, estadoDestino);
                    res=true;
                }else{
                    res=false;
                }
                break;
        }
        return res;
    }

    private void ejecutarTransferenciaDestino(Stock stockDestino, Integer cantidad, String estadoDestino){
        switch (Estado.valueOf(estadoDestino)){
            case DESUSO:
                stockDestino.setCantidadDesuso((stockDestino.getCantidadDesuso()+cantidad));
                break;
            case USO:
                stockDestino.setCantidadUso((stockDestino.getCantidadUso()+cantidad));
                break;
            case DESECHADO:
                stockDestino.setCantidadDesechado((stockDestino.getCantidadDesuso()+cantidad));
                break;
        }
    }

}