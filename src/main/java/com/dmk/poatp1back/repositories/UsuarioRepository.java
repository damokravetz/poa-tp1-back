package com.dmk.poatp1back.repositories;

import com.dmk.poatp1back.models.Stock;
import com.dmk.poatp1back.models.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    public abstract Optional<Usuario> findByEmailAndPassword(String email, String password);
    public abstract Optional<Usuario> findByEmail(String email);
}