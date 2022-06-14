package com.dmk.poatp1back.controllers;

import com.dmk.poatp1back.models.Movimiento;
import com.dmk.poatp1back.models.Usuario;
import com.dmk.poatp1back.services.MovimientoService;
import com.dmk.poatp1back.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("/movimiento")
public class MovimientoController {
    @Autowired
    MovimientoService movimientoService;

    @GetMapping()
    public ArrayList<Movimiento> obtenerMovimientos(@RequestParam Map<String,String> allParams) {
        Optional<String> desdeParam=Optional.ofNullable(allParams.get("desde"));
        Optional<String> hastaParam=Optional.ofNullable(allParams.get("hasta"));
        Optional<String> pageParam=Optional.ofNullable(allParams.get("page"));
        Optional<String> sizeParam=Optional.ofNullable(allParams.get("size"));
        if(desdeParam.isPresent()&&hastaParam.isPresent()&&pageParam.isPresent()&&sizeParam.isPresent()){
            LocalDateTime desde;
            LocalDateTime hasta;
            Integer page;
            Integer size;
            try{
                desde=LocalDateTime.parse(desdeParam.get(), DateTimeFormatter.ofPattern("dd/mm/YYYY"));
                hasta=LocalDateTime.parse(hastaParam.get(), DateTimeFormatter.ofPattern("dd/mm/YYYY"));
            }catch (DateTimeParseException e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong date formats");
            }
            try{
                page=Integer.valueOf(allParams.get("page"));
                size=Integer.valueOf(allParams.get("size"));
            }catch (NumberFormatException e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong page formats");
            }
            return movimientoService.obtenerMovimientos(desde, hasta, page, size);
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Missing Parameters");
        }
    }

    @GetMapping(path = "/parte/lugar")
    public ArrayList<Movimiento> obtenerMovimientosPorParteYLugar(@RequestParam Map<String,String> allParams) {

        Optional<String> desdeParam=Optional.ofNullable(allParams.get("desde"));
        Optional<String> hastaParam=Optional.ofNullable(allParams.get("hasta"));
        Optional<String> pageParam=Optional.ofNullable(allParams.get("page"));
        Optional<String> sizeParam=Optional.ofNullable(allParams.get("size"));
        Optional<String> parteIdParam=Optional.ofNullable(allParams.get("parteId"));
        Optional<String> lugarIdParam=Optional.ofNullable(allParams.get("lugarId"));

        if(desdeParam.isPresent()&&hastaParam.isPresent()&&pageParam.isPresent()&&sizeParam.isPresent()&&parteIdParam.isPresent()&&lugarIdParam.isPresent()){
            LocalDateTime desde;
            LocalDateTime hasta;
            Integer page;
            Integer size;
            Long parteId;
            Long lugarId;
            try{
                desde=LocalDateTime.parse(desdeParam.get(), DateTimeFormatter.ofPattern("dd/mm/YYYY"));
                hasta=LocalDateTime.parse(hastaParam.get(), DateTimeFormatter.ofPattern("dd/mm/YYYY"));
            }catch (DateTimeParseException e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong date formats");
            }
            try{
                page=Integer.valueOf(pageParam.get());
                size=Integer.valueOf(sizeParam.get());
                parteId=Long.valueOf(parteIdParam.get());
                lugarId=Long.valueOf(lugarIdParam.get());
            }catch (NumberFormatException e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong page formats");
            }

            return movimientoService.obtenerMovimientosPorParteYLugar(desde, hasta, page, size, parteId, lugarId);
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Missing Parameters");
        }
    }

    @GetMapping(path = "/parte")
    public ArrayList<Movimiento> obtenerMovimientosPorParte(@RequestParam Map<String,String> allParams) {

        Optional<String> desdeParam=Optional.ofNullable(allParams.get("desde"));
        Optional<String> hastaParam=Optional.ofNullable(allParams.get("hasta"));
        Optional<String> pageParam=Optional.ofNullable(allParams.get("page"));
        Optional<String> sizeParam=Optional.ofNullable(allParams.get("size"));
        Optional<String> parteIdParam=Optional.ofNullable(allParams.get("parteId"));

        if(desdeParam.isPresent()&&hastaParam.isPresent()&&pageParam.isPresent()&&sizeParam.isPresent()&&parteIdParam.isPresent()){
            LocalDateTime desde;
            LocalDateTime hasta;
            Integer page;
            Integer size;
            Long parteId;
            try{
                desde=LocalDateTime.parse(desdeParam.get(), DateTimeFormatter.ofPattern("dd/mm/YYYY"));
                hasta=LocalDateTime.parse(hastaParam.get(), DateTimeFormatter.ofPattern("dd/mm/YYYY"));
            }catch (DateTimeParseException e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong date formats");
            }
            try{
                page=Integer.valueOf(pageParam.get());
                size=Integer.valueOf(sizeParam.get());
                parteId=Long.valueOf(parteIdParam.get());
            }catch (NumberFormatException e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong page formats");
            }

            return movimientoService.obtenerMovimientosPorParte(desde, hasta, page, size, parteId);
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Missing Parameters");
        }
    }

    @GetMapping(path = "/lugar")
    public ArrayList<Movimiento> obtenerMovimientosPorLugar(@RequestParam Map<String,String> allParams) {

        Optional<String> desdeParam=Optional.ofNullable(allParams.get("desde"));
        Optional<String> hastaParam=Optional.ofNullable(allParams.get("hasta"));
        Optional<String> pageParam=Optional.ofNullable(allParams.get("page"));
        Optional<String> sizeParam=Optional.ofNullable(allParams.get("size"));
        Optional<String> lugarIdParam=Optional.ofNullable(allParams.get("parteId"));

        if(desdeParam.isPresent()&&hastaParam.isPresent()&&pageParam.isPresent()&&sizeParam.isPresent()&&lugarIdParam.isPresent()){
            LocalDateTime desde;
            LocalDateTime hasta;
            Integer page;
            Integer size;
            Long lugarId;
            try{
                desde=LocalDateTime.parse(desdeParam.get(), DateTimeFormatter.ofPattern("dd/mm/YYYY"));
                hasta=LocalDateTime.parse(hastaParam.get(), DateTimeFormatter.ofPattern("dd/mm/YYYY"));
            }catch (DateTimeParseException e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong date formats");
            }
            try{
                page=Integer.valueOf(pageParam.get());
                size=Integer.valueOf(sizeParam.get());
                lugarId=Long.valueOf(lugarIdParam.get());
            }catch (NumberFormatException e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Wrong page formats");
            }

            return movimientoService.obtenerMovimientosPorLugar(desde, hasta, page, size, lugarId);
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Missing Parameters");
        }
    }

}
