package com.tallerwebi.dominio.locker;


import com.tallerwebi.dominio.DatosLocker;

import java.util.List;

public interface RepositorioDatosLocker  {

    void guardar(DatosLocker locker);

    DatosLocker actualizar(DatosLocker locker);

    void eliminar(Long idLocker);

    DatosLocker obtenerPorId(Long idLocker);


    List<DatosLocker> obtenerLockersPorTipo(TipoLocker tipoLocker);
}
