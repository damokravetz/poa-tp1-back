package com.dmk.poatp1back.repositories;

import com.dmk.poatp1back.models.Movimiento;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@Repository
public interface MovimientoRepository extends CrudRepository<Movimiento, Long> {

    @Query("select m from Movimiento m where (m.fecha > :desde and m.fecha < :hasta)")
    public abstract Page<Movimiento> findByFechaBetween(@Param("desde") LocalDateTime desde, @Param("hasta")LocalDateTime hasta, Pageable pageable);

    @Query("select m from Movimiento m where (m.parte.id = :parteId) and (m.fecha > :desde and m.fecha < :hasta)")
    public abstract Page<Movimiento> findByParteIdAndFechaBetween(@Param("parteId")Long parteId, @Param("desde")LocalDateTime desde, @Param("hasta")LocalDateTime hasta, Pageable pageable);

    @Query("select m from Movimiento m where (m.origen.id= :lugarId or m.destino.id= :lugarId) and (m.fecha > :desde and m.fecha < :hasta)")
    public abstract Page<Movimiento> findByLugarIdAndFechaBetween(@Param("lugarId")Long lugarId, @Param("desde")LocalDateTime desde, @Param("hasta")LocalDateTime hasta, Pageable pageable);

    @Query("select m from Movimiento m where (m.parte.id = :parteId) and (m.origen.id= :lugarId or m.destino.id= :lugarId) and (m.fecha > :desde and m.fecha < :hasta)")
    public abstract Page<Movimiento> findByParteIdAndLugarIdAndFechaBetween(@Param("parteId")Long parteId, @Param("lugarId")Long lugarId,
                                                                                 @Param("desde") LocalDateTime desde, @Param("hasta")LocalDateTime hasta, Pageable pageable);

}