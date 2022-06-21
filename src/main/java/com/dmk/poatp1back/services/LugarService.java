package com.dmk.poatp1back.services;

import com.dmk.poatp1back.models.Lugar;
import com.dmk.poatp1back.repositories.LugarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class LugarService {
    @Autowired
    LugarRepository lugarRepository;

    public ArrayList<Lugar> obtenerLugares(){
        return (ArrayList<Lugar>) lugarRepository.findAll();
    }

}