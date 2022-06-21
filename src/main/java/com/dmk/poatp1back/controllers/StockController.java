package com.dmk.poatp1back.controllers;

import com.dmk.poatp1back.models.Stock;
import com.dmk.poatp1back.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/stock")
public class StockController {
    @Autowired
    StockService stockService;

    @GetMapping( path = "/lugar")
    public PageImpl<Stock> obtenerStocksPorLugar(@RequestParam Map<String,String> allParams){
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

    @GetMapping("/global")
    public PageImpl<Stock> obtenerStocksGlobal(@RequestParam Map<String,String> allParams){
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

    @PostMapping(path = "/ingreso")
    public Stock ingresarStock(@RequestParam Map<String,String> allParams) {
        Optional<String> parteIdParam=Optional.ofNullable(allParams.get("parteId"));
        Optional<String> lugarIdParam=Optional.ofNullable(allParams.get("lugarId"));
        Optional<String> cantidadParam=Optional.ofNullable(allParams.get("cantidad"));
        Optional<String> estadoDestinoParam=Optional.ofNullable(allParams.get("estadoDestino"));
        if(parteIdParam.isPresent()&&lugarIdParam.isPresent()&&cantidadParam.isPresent()&& estadoDestinoParam.isPresent()){
            try{
                Long parteId=Long.valueOf(parteIdParam.get());
                Long lugarId=Long.valueOf(lugarIdParam.get());
                Integer cantidad=Integer.valueOf(cantidadParam.get());
                return stockService.ingresarStock(parteId, lugarId, cantidad, estadoDestinoParam.get());
            }catch (NumberFormatException e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong Parameter Format");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing Parameters");
        }
    }

    @PostMapping(path = "/transferencia")
    public Stock transferirStock(@RequestParam Map<String,String> allParams) {
        Optional<String> stockIdParam=Optional.ofNullable(allParams.get("stockId"));
        Optional<String> cantidadParam=Optional.ofNullable(allParams.get("cantidad"));
        Optional<String> lugarIdParam=Optional.ofNullable(allParams.get("lugarId"));
        Optional<String> estadoOrigenParam=Optional.ofNullable(allParams.get("estadoOrigen"));
        Optional<String> estadoDestinoParam=Optional.ofNullable(allParams.get("estadoDestino"));
        if(stockIdParam.isPresent()&&cantidadParam.isPresent()&&lugarIdParam.isPresent()&&estadoOrigenParam.isPresent()&&estadoDestinoParam.isPresent()){
            try{
                Long stockId=Long.valueOf(stockIdParam.get());
                Integer cantidad=Integer.valueOf(cantidadParam.get());
                Long lugarId=Long.valueOf(lugarIdParam.get());
                return stockService.transferirStock(stockId, cantidad, lugarId, estadoOrigenParam.get(), estadoDestinoParam.get());
            }catch (NumberFormatException e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong Parameter Format");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing Parameters");
        }
    }

}
