package com.tallerwebi.dominio;

import java.sql.Date;
import java.util.List;

import com.tallerwebi.dominio.Locker;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;

public interface ServicioReserva {


Usuario consultarUsuarioDeReserva(String email, String password);
Usuario consultarLockerDeReserva(Usuario id, Locker idLocker);
    void registrarReserva(Usuario id, Locker idLocker) throws UsuarioExistente;

}
