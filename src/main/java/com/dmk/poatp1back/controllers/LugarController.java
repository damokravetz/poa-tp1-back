package com.dmk.poatp1back.controllers;

import com.dmk.poatp1back.models.Lugar;
import com.dmk.poatp1back.services.LugarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/lugar")
public class LugarController {
    @Autowired
    LugarService lugarService;

    @GetMapping()
    public ArrayList<Lugar> obtenerLugares(){
        return lugarService.obtenerLugares();
    }
}
