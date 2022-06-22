package com.dmk.poatp1back.models;

import javax.persistence.*;

@Entity
@Table(name = "lugar")
public class Lugar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private String codLugar;
    private String descripcion;
    private Integer capacidad;
    private Boolean esDeposito;

    public Lugar() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodLugar() {
        return codLugar;
    }

    public void setCodLugar(String codLugar) {
        this.codLugar = codLugar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public Boolean getEsDeposito() {
        return esDeposito;
    }

    public void setEsDeposito(Boolean esDeposito) {
        this.esDeposito = esDeposito;
    }
}