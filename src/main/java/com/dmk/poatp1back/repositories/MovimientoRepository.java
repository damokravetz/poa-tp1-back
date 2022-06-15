package com.dmk.poatp1back.repositories;

import com.dmk.poatp1back.models.Movimiento;
import com.dmk.poatp1back.models.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Repository
public interface MovimientoRepository extends CrudRepository<Movimiento, Long> {

    @Query("select m from Movimiento m where m.fecha between :desde and :hasta")
    public abstract ArrayList<Movimiento> findByFechaBetween(@Param("desde")LocalDateTime desde, @Param("hasta")LocalDateTime hasta, Pageable pageable);

    @Query("select m from Movimiento m where m.parte.id = :parteId and m.fecha between :desde and :hasta")
    public abstract ArrayList<Movimiento> findByParteIdAndFechaBetween(@Param("parteId")Long parteId, @Param("desde")LocalDateTime desde, @Param("hasta")LocalDateTime hasta, Pageable pageable);

    @Query("select m from Movimiento m where m.origen.id= :lugarId or m.destino.id= :lugarId and m.fecha between :desde and :hasta")
    public abstract ArrayList<Movimiento> findByLugarIdAndFechaBetween(@Param("lugarId")Long lugarId, @Param("desde")LocalDateTime desde, @Param("hasta")LocalDateTime hasta, Pageable pageable);

    @Query("select m from Movimiento m where m.parte.id = :parteId and m.origen.id= :lugarId or m.destino.id= :lugarId and m.fecha between :desde and :hasta")
    public abstract ArrayList<Movimiento> findByParteIdAndLugarIdAndFechaBetween(@Param("parteId")Long parteId, @Param("lugarId")Long lugarId,
                                                                                 @Param("desde")LocalDateTime desde, @Param("hasta")LocalDateTime hasta, Pageable pageable);

}