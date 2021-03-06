package com.dmk.poatp1back.services;

import com.dmk.poatp1back.models.Movimiento;
import com.dmk.poatp1back.repositories.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;


@Service
public class MovimientoService {
    @Autowired
    MovimientoRepository movimientoRepository;

    public ArrayList<Movimiento> obtenerUsuarios(){
        return (ArrayList<Movimiento>) movimientoRepository.findAll();
    }

    public Movimiento guardarUsuario(Movimiento movimiento){
        return movimientoRepository.save(movimiento);
    }

    public Page<Movimiento> obtenerMovimientos(LocalDateTime desde, LocalDateTime hasta, Integer page, Integer size ){
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return movimientoRepository.findByFechaBetween(desde, hasta, pageable);
    }

    public Page<Movimiento> obtenerMovimientosPorParteYLugar(LocalDateTime desde, LocalDateTime hasta, Integer page, Integer size, Long parteId, Long lugarId ){
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return movimientoRepository.findByParteIdAndLugarIdAndFechaBetween(parteId, lugarId, desde, hasta, pageable);
    }

    public Page<Movimiento> obtenerMovimientosPorParte(LocalDateTime desde, LocalDateTime hasta, Integer page, Integer size, Long parteId ){
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return movimientoRepository.findByParteIdAndFechaBetween(parteId, desde, hasta, pageable);
    }

    public Page<Movimiento> obtenerMovimientosPorLugar(LocalDateTime desde, LocalDateTime hasta, Integer page, Integer size, Long lugarId ){
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return movimientoRepository.findByLugarIdAndFechaBetween(lugarId, desde, hasta, pageable);
    }

}