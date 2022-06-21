package com.dmk.poatp1back.repositories;

import com.dmk.poatp1back.models.Stock;
import com.dmk.poatp1back.models.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends CrudRepository<Stock, Long> {
    @Query("select s from Stock s where s.lugar.id = :lugarId and s.parte.id IN :parteIds")
    public abstract ArrayList<Stock> findByLugarIdAndParteIds(@Param("lugarId")Long lugarId, @Param("parteIds")List<Long> parteIds);

    @Query("select s from Stock s where s.parte.id IN :parteIds")
    public abstract ArrayList<Stock> findByParteIds(@Param("parteIds")List<Long> parteIds);

    public abstract Optional<Stock> findByParteIdAndLugarId(Long parteId, Long lugarId);

}