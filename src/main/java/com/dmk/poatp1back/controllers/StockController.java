package com.dmk.poatp1back.controllers;

import com.dmk.poatp1back.models.Stock;
import com.dmk.poatp1back.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/stock")
public class StockController {
    @Autowired
    StockService stockService;

    @GetMapping( path = "/lugar")
    public ArrayList<Stock> obtenerStocksPorLugar(@RequestParam Map<String,String> allParams){
        Optional<String> pageParam=Optional.ofNullable(allParams.get("page"));
        Optional<String> sizeParam=Optional.ofNullable(allParams.get("size"));
        Optional<String> lugarIdParam=Optional.ofNullable(allParams.get("lugarId"));
        if(pageParam.isPresent()&&sizeParam.isPresent()&&lugarIdParam.isPresent()){
            Integer page;
            Integer size;
            Long lugarId;
            try{
                page=Integer.valueOf(pageParam.get());
                size=Integer.valueOf(sizeParam.get());
                lugarId=Long.valueOf(lugarIdParam.get());
            }catch (NumberFormatException e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong page formats");
            }
            return stockService.obtenerStocksPorLugar(lugarId, page, size);
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Missing Parameters");
        }
    }

    @GetMapping()
    public ArrayList<Stock> obtenerStocksGlobal(@RequestParam Map<String,String> allParams){
        Optional<String> pageParam=Optional.ofNullable(allParams.get("page"));
        Optional<String> sizeParam=Optional.ofNullable(allParams.get("size"));
        if(pageParam.isPresent()&&sizeParam.isPresent()){
            Integer page;
            Integer size;
            try{
                page=Integer.valueOf(pageParam.get());
                size=Integer.valueOf(sizeParam.get());
            }catch (NumberFormatException e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong page formats");
            }
            return stockService.obtenerStocksGlobal(page,size);
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing Parameters");
        }
    }



}
