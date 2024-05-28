package com.tallerwebi.controlador;
import com.tallerwebi.presentacion.ReservaDatos;
import com.tallerwebi.dominio.ServicioReserva;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.Locker;
import com.tallerwebi.dominio.Reserva;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ControladorReserva {

    @Autowired
    private ServicioReserva servicioReserva;

    // Endpoint para consultar un usuario de reserva por email y password
    @GetMapping("/usuario")
    public Usuario consultarUsuarioDeReserva(@RequestParam String email, @RequestParam String password) {
        return servicioReserva.consultarUsuarioDeReserva(email, password);
    }

    // Endpoint para consultar locker de reserva por usuario y locker id
    @GetMapping("/locker")
    public Usuario consultarLockerDeReserva(@RequestParam String email, @RequestParam Long idLocker) {
        Usuario usuario = servicioReserva.consultarUsuarioDeReserva(email, null);
        if (usuario != null) {
            Locker locker = new Locker();
            locker.setId(idLocker);
            return servicioReserva.consultarLockerDeReserva(usuario, locker);
        }
        return null;
    }

    // Endpoint para registrar una nueva reserva
    @PostMapping("/registrar")
    public void registrarReserva(@RequestBody ReservaDatos ReservaDatos) {
        try {
            Usuario usuario = new Usuario();
            usuario.setEmail(ReservaDatos.getEmail());
            usuario.setPassword(ReservaDatos.getPassword());

            Locker locker = new Locker();
            locker.setId(ReservaDatos.getIdLocker());

            servicioReserva.registrarReserva(usuario, locker);
        } catch (UsuarioExistente e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuario ya existe");
        }
    }
}