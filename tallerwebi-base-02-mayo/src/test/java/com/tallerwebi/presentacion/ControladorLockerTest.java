package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.locker.ServicioLocker;
import com.tallerwebi.dominio.locker.TipoLocker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

public class ControladorLockerTest {

    private ControladorLocker controladorLocker;

    @BeforeEach
    public void init() {
        ServicioLocker servicioLocker = mock(ServicioLocker.class);
        this.controladorLocker = new ControladorLocker(servicioLocker);
    }

    @Test
    public void queSePuedaCrearUnNuevoLocker() {
        // Preparación
        TipoLocker tipoLocker = TipoLocker.PEQUEÑO;

        // Ejecución
        ModelAndView mav = this.controladorLocker.crearLocker(tipoLocker);

        // Verificación
        // Verificar que se redirige correctamente a la página de mensaje
        assertThat(mav.getViewName(), equalToIgnoringCase("redirect:/mensaje-creacion-locker"));
    }


    @Test
    public void queSePuedaActualizarUnLocker() {
        // Preparación
        Long idLocker = 1L;
        TipoLocker tipoLocker = TipoLocker.GRANDE;

        // Ejecución
        ModelAndView mav = this.controladorLocker.actualizarLocker(idLocker, tipoLocker);

        // Verificación
        assertThat(mav.getViewName(), equalToIgnoringCase("redirect:/mensaje-actualizacion-locker"));
    }

    @Test
    public void queSePuedaEliminarUnLocker() {
        // Preparación
        Long idLocker = 1L;

        // Ejecución
        ModelAndView mav = this.controladorLocker.eliminarLocker(idLocker);

        // Verificación
        assertThat(mav.getViewName(), equalToIgnoringCase("redirect:/mensaje-eliminacion-locker"));
    }



    @Test
    public void queSePuedaObtenerUnaListaDeLockersFiltradaPorTipo() {
        // Preparación
        TipoLocker tipoLocker = TipoLocker.GRANDE;

        // Ejecución
        ModelAndView mav = this.controladorLocker.buscarLockersPorTipo(tipoLocker);

        // Verificación
        assertThat(mav.getViewName(), equalToIgnoringCase("lockers-por-tipo")); // Vista correcta
        assertThat(mav.getModel().get("lockers"), instanceOf(List.class)); // Lista de lockers presente en el modelo
    }

}
