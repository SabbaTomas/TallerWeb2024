package com.tallerwebi.dominio;

import com.tallerwebi.dominio.locker.ServicioLocker;
import com.tallerwebi.dominio.locker.TipoLocker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioLockerImpl implements ServicioLocker {

    private final RepositorioDatosLockerImpl lockerRepository;

    @Autowired
    public ServicioLockerImpl(RepositorioDatosLockerImpl lockerRepository) {
        this.lockerRepository = lockerRepository;
    }


    @Override
    public void crearLocker(TipoLocker tipoLocker) {
        DatosLocker nuevoLocker = new DatosLocker(tipoLocker);
        lockerRepository.guardar(nuevoLocker);
    }

    @Override
    public void actualizarLocker(Long idLocker, TipoLocker tipoLocker) {
        DatosLocker locker = lockerRepository.obtenerPorId(idLocker);
        if (locker != null) {
            locker.setTipoLocker(tipoLocker);
            lockerRepository.guardar(locker);
        } else {
                 throw new IllegalArgumentException("No se encontró ningún locker con el ID proporcionado: " + idLocker);
        }
    }



    @Override
    public void eliminarLocker(Long idLocker) {
        lockerRepository.eliminar(idLocker);
    }

    @Override
    public List<DatosLocker> obtenerLockersPorTipo(TipoLocker tipoLocker) {
        return lockerRepository.obtenerLockersPorTipo(tipoLocker);
    }




}
