package com.dmk.poatp1back.repositories;

import com.dmk.poatp1back.models.Parte;
import com.dmk.poatp1back.models.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ParteRepository extends CrudRepository<Parte, Long> {
    public abstract ArrayList<Parte> findByPrioridad(Integer prioridad);

}