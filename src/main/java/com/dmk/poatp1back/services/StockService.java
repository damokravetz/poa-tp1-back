package com.dmk.poatp1back.services;

import com.dmk.poatp1back.models.Lugar;
import com.dmk.poatp1back.models.Stock;
import com.dmk.poatp1back.repositories.LugarRepository;
import com.dmk.poatp1back.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class StockService {
    @Autowired
    StockRepository stockRepository;

    public ArrayList<Stock> obtenerStocks(){
        return (ArrayList<Stock>) stockRepository.findAll();
    }

    public Stock guardarStock(Stock stock){
        return stockRepository.save(stock);
    }

}