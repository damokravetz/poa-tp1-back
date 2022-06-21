package com.dmk.poatp1back.services;

import com.dmk.poatp1back.repositories.ParteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParteService {
    @Autowired
    ParteRepository parteRepository;

}