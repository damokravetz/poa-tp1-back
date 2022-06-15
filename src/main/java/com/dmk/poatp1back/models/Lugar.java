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
}