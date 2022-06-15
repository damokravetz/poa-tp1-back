package com.dmk.poatp1back.repositories;

import com.dmk.poatp1back.models.Lugar;
import com.dmk.poatp1back.models.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface LugarRepository extends CrudRepository<Lugar, Long> {

}