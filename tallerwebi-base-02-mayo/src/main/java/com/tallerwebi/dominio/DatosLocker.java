package com.tallerwebi.dominio;

import com.tallerwebi.dominio.locker.TipoLocker;

import javax.persistence.*;

@Entity
public class DatosLocker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TipoLocker tipo;

    public DatosLocker() {

    }

    public DatosLocker(TipoLocker tipo) {
        this.tipo = tipo;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoLocker getTipoLocker() {
        return tipo;
    }

    public void setTipoLocker(TipoLocker tipo) {
        this.tipo = tipo;
    }
}