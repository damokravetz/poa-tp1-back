package com.dmk.poatp1back.services;

import com.dmk.poatp1back.models.Lugar;
import com.dmk.poatp1back.models.Parte;
import com.dmk.poatp1back.repositories.LugarRepository;
import com.dmk.poatp1back.repositories.ParteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ParteService {
    @Autowired
    ParteRepository parteRepository;

}