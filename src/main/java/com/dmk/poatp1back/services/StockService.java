package com.dmk.poatp1back.services;

import com.dmk.poatp1back.models.Lugar;
import com.dmk.poatp1back.models.Parte;
import com.dmk.poatp1back.models.Stock;
import com.dmk.poatp1back.repositories.LugarRepository;
import com.dmk.poatp1back.repositories.ParteRepository;
import com.dmk.poatp1back.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

}