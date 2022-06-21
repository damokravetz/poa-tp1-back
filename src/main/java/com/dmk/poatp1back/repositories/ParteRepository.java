package com.dmk.poatp1back.repositories;

import com.dmk.poatp1back.models.Parte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParteRepository extends CrudRepository<Parte, Long> {
    @Query("select p from Parte p")
    public abstract Page<Parte> findAll(Pageable pageable);

}