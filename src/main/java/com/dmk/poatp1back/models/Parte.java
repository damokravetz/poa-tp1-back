package com.dmk.poatp1back.models;

import javax.persistence.*;

@Entity
@Table(name = "parte")
public class Parte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private String modelo;
    private String descripcion;
    private TipoParte tipo;

    public Parte() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoParte getTipo() {
        return tipo;
    }

    public void setTipo(TipoParte tipo) {
        this.tipo = tipo;
    }
}