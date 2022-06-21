package com.dmk.poatp1back.services;

import com.dmk.poatp1back.models.Usuario;
import com.dmk.poatp1back.repositories.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository usuarioRepository;

    public Map<String,String> autenticarUsuario(Usuario usuario){
        Optional<Usuario>myUsuarioOptional=usuarioRepository.findByEmail(usuario.getEmail());
        if(myUsuarioOptional.isPresent()){
            Usuario myUsuario=myUsuarioOptional.get();
            String token= getJWTToken(usuario.getEmail());
            Map<String, String> myMap=new HashMap<>();
            myMap.put("nombre", myUsuario.getNombre());
            myMap.put("email", usuario.getEmail());
            myMap.put("token", token);
            return myMap;
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized");
        }
    }

    private String getJWTToken(String username) {
        String secretKey = "2dae84f846e4f4b158a8d26681707f4338495bc7ab68151d7f7679cc5e56202dd3da0d356da007a7c28cb0b780418f4f3246769972d6feaa8f610c7d1e7ecf6a";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("softtekJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Bearer " + token;
    }
}