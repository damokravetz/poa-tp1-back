package com.dmk.poatp1back.controllers;

import com.dmk.poatp1back.services.ParteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/parte")
public class ParteController {
    @Autowired
    ParteService parteService;

}
