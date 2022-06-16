package com.dmk.poatp1back.services;

import com.dmk.poatp1back.models.*;
import com.dmk.poatp1back.repositories.LugarRepository;
import com.dmk.poatp1back.repositories.MovimientoRepository;
import com.dmk.poatp1back.repositories.ParteRepository;
import com.dmk.poatp1back.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ArrayList<Stock> obtenerStocksPorLugar(Long lugarId, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        ArrayList<Parte> partes= parteRepository.findAll(pageable);
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
        return stocks;
    }

    @Transactional
    public ArrayList<Stock> obtenerStocksGlobal(Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        ArrayList<Parte> partes= parteRepository.findAll(pageable);
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
        return resStocks;
    }

    @Transactional
    public Stock ingresarStock(Long parteId, Long lugarId, Integer cantidad, Integer estadoDestino){
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

    private Movimiento crearMovimiento(Integer cantidad, Integer estadoDestino, Stock stock) {
        Movimiento movimiento=new Movimiento();
        movimiento.setEstadoInicial(null);
        movimiento.setOrigen(null);
        movimiento.setParte(stock.getParte());
        movimiento.setDestino(stock.getLugar());
        movimiento.setEstadoFinal(Estado.values()[estadoDestino]);
        movimiento.setCantidad(cantidad);
        movimiento.setFecha(LocalDateTime.now());
        return movimiento;
    }

    private void realizarIngreso(Integer cantidad, Integer estadoDestino, Stock stock) {
        switch (estadoDestino) {
            case 0:
                stock.setCantidadDesuso((stock.getCantidadDesuso() + cantidad));
                break;
            case 1:
                stock.setCantidadUso((stock.getCantidadUso() + cantidad));
                break;
            case 2:
                stock.setCantidadDesechado((stock.getCantidadDesechado() + cantidad));
                break;
        }
    }

    @Transactional
    public Stock transferirStock(Long stockId, Integer cantidad, Long lugarId, Integer estadoOrigen, Integer estadoDestino) {
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

    private void realizarTransferencia(Integer cantidad, Integer estadoOrigen, Integer estadoDestino, Stock origen, Stock destino) {
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

    private Movimiento crearMovimiento(Integer cantidad, Integer estadoOrigen, Integer estadoDestino, Stock origen, Stock destino) {
        Movimiento movimiento=new Movimiento();
        movimiento.setParte(origen.getParte());
        movimiento.setCantidad(cantidad);
        movimiento.setOrigen(origen.getLugar());
        movimiento.setDestino(destino.getLugar());
        movimiento.setEstadoInicial(Estado.values()[estadoOrigen]);
        movimiento.setEstadoFinal(Estado.values()[estadoDestino]);
        movimiento.setFecha(LocalDateTime.now());
        return movimiento;
    }

    private Boolean ejecutarTransferencia(Stock stockOrigen, Stock stockDestino, Integer cantidad, Integer estadoOrigen, Integer estadoDestino){
        Boolean res=false;
        if(cantidad<=0) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Cantidad debe ser mayor a 0");
        switch (estadoOrigen){
            case 0:
                if(stockOrigen.getCantidadDesuso()>=cantidad) {
                    stockOrigen.setCantidadDesuso((stockOrigen.getCantidadDesuso()-cantidad));
                    ejecutarTransferenciaDestino(stockDestino, cantidad, estadoDestino);
                    res=true;
                }else{
                    res=false;
                }
                break;
            case 1:
                if(stockOrigen.getCantidadUso()>=cantidad) {
                    stockOrigen.setCantidadUso((stockOrigen.getCantidadUso()-cantidad));
                    ejecutarTransferenciaDestino(stockDestino, cantidad, estadoDestino);
                    res=true;
                }else{
                    res=false;
                }
                break;
            case 2:
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

    private void ejecutarTransferenciaDestino(Stock stockDestino, Integer cantidad, Integer estadoDestino){
        switch (estadoDestino){
            case 0:
                stockDestino.setCantidadDesuso((stockDestino.getCantidadDesuso()+cantidad));
                break;
            case 1:
                stockDestino.setCantidadUso((stockDestino.getCantidadUso()+cantidad));
                break;
            case 2:
                stockDestino.setCantidadDesechado((stockDestino.getCantidadDesuso()+cantidad));
                break;
        }
    }

}