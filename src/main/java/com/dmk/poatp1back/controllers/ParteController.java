package com.dmk.poatp1back.controllers;

import com.dmk.poatp1back.models.Usuario;
import com.dmk.poatp1back.services.ParteService;
import com.dmk.poatp1back.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/parte")
public class ParteController {
    @Autowired
    ParteService parteService;

}
