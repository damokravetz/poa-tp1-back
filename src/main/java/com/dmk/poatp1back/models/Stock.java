package com.dmk.poatp1back.models;

import javax.persistence.*;

@Entity
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @ManyToOne
    private Parte parte;
    @ManyToOne
    private Lugar lugar;
    private Integer cantidadUso;
    private Integer cantidadDesuso;
    private Integer cantidadDesechado;

    public Stock() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Parte getParte() {
        return parte;
    }

    public void setParte(Parte parte) {
        this.parte = parte;
    }

    public Lugar getLugar() {
        return lugar;
    }

    public void setLugar(Lugar lugar) {
        this.lugar = lugar;
    }

    public Integer getCantidadUso() {
        return cantidadUso;
    }

    public void setCantidadUso(Integer cantidadUso) {
        this.cantidadUso = cantidadUso;
    }

    public Integer getCantidadDesuso() {
        return cantidadDesuso;
    }

    public void setCantidadDesuso(Integer cantidadDesuso) {
        this.cantidadDesuso = cantidadDesuso;
    }

    public Integer getCantidadDesechado() {
        return cantidadDesechado;
    }

    public void setCantidadDesechado(Integer cantidadDesechado) {
        this.cantidadDesechado = cantidadDesechado;
    }
}