package com.dmk.poatp1back.controllers;

import com.dmk.poatp1back.models.Stock;
import com.dmk.poatp1back.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/stock")
public class StockController {
    @Autowired
    StockService stockService;

    @GetMapping()
    public ArrayList<Stock> obtenerStocks(){
        return stockService.obtenerStocks();
    }

    @PostMapping()
    public Stock guardarStock(@RequestBody Stock stock){
        return this.stockService.guardarStock(stock);
    }

}
