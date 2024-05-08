package com.tallerwebi.dominio.locker;

import com.tallerwebi.dominio.DatosLocker;

import java.util.List;

public interface ServicioLocker {

    void crearLocker(TipoLocker tipoLocker);
    void actualizarLocker(Long idLocker, TipoLocker tipoLocker);
    void eliminarLocker(Long idLocker);

    List<DatosLocker> obtenerLockersPorTipo(TipoLocker tipoLocker);
}
