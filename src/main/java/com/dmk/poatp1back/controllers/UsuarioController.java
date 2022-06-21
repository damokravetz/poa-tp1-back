package com.dmk.poatp1back.controllers;

import com.dmk.poatp1back.models.Usuario;
import com.dmk.poatp1back.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    @Autowired
    UsuarioService usuarioService;

    @PostMapping(path = "/autenticar")
    public Map<String,String> autenticatUsuario(@RequestBody Usuario usuario){
        return usuarioService.autenticarUsuario(usuario);
    }

}
