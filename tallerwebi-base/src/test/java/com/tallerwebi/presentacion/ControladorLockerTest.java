package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Locker;
import com.tallerwebi.dominio.RepositorioDatosLockerImpl;
import com.tallerwebi.dominio.locker.ServicioLocker;
import com.tallerwebi.dominio.locker.TipoLocker;

import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


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
        assertThat(mav.getViewName(), equalToIgnoringCase("crear-locker"));
    }

    @Test
    public void queNoSePuedaCrearLockerConTipoInvalido(){
        // Preparación
        TipoLocker tipoLocker = null; // Tipo inválido

        // Ejecución
        assertThrows(IllegalArgumentException.class, () -> {
            controladorLocker.crearLocker(tipoLocker);
        });
    }


    @Test
    public void queSePuedaActualizarUnLocker() {
        // Preparación
        Long idLocker = 1L;
        TipoLocker tipoLocker = TipoLocker.GRANDE;

        // Ejecución
        ModelAndView mav = this.controladorLocker.actualizarLocker(idLocker, tipoLocker);

        // Verificación
        assertThat(mav.getViewName(), equalToIgnoringCase("redirect:/actualizar-locker"));
    }

    @Test
    public void queNoSePuedaActualizarLockerConIdInvalido() {
        // Preparación
        Long idLocker = -1L; // ID inválido
        TipoLocker tipoLocker = TipoLocker.GRANDE;

        // Mockear el servicio para lanzar excepción
        doThrow(new IllegalArgumentException("No se encontró ningún locker con el ID proporcionado: " + idLocker))
                .when(controladorLocker.servicioLocker).actualizarLocker(idLocker, tipoLocker);

        // Ejecución y Verificación
        assertThrows(IllegalArgumentException.class, () -> {
            controladorLocker.actualizarLocker(idLocker, tipoLocker);
        });
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
    public void queNoSePuedaEliminarLockerConIdInvalido() {
        // Preparación
        Long idLocker = -1L; // ID inválido

        // Mockear el servicio para lanzar excepción
        doThrow(new IllegalArgumentException("No se encontró ningún locker con el ID proporcionado: " + idLocker))
                .when(controladorLocker.servicioLocker).eliminarLocker(idLocker);

        // Ejecución y Verificación
        assertThrows(IllegalArgumentException.class, () -> {
            controladorLocker.eliminarLocker(idLocker);
        });
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

    @Test
    public void testMostrarLockers() {
        // Mockear el servicio
        ServicioLocker servicioLocker = Mockito.mock(ServicioLocker.class);
        String codigo_postal = String.valueOf(1704);

        // Preparar los lockers de prueba
        Locker locker1 = new Locker(TipoLocker.PEQUEÑO, 40.7128, -74.0060,codigo_postal);
        Locker locker2 = new Locker(TipoLocker.MEDIANO, 40.7129, -74.0061,codigo_postal);
        Locker locker3 = new Locker(TipoLocker.GRANDE, 40.7130, -74.0062,codigo_postal);
        List<Locker> lockers = Arrays.asList(locker1, locker2, locker3);

        // Simular el comportamiento del servicio
        when(servicioLocker.obtenerLockersSeleccionados()).thenReturn(lockers);

        // Instanciar el controlador y usar inyección de dependencias
        ControladorLocker controladorLocker = new ControladorLocker(servicioLocker);

        // Llamar al método
        ModelAndView mav = controladorLocker.mostrarLockers();

        // Verificar la respuesta del controlador
        assertEquals("lockers", mav.getViewName());
        assertEquals(lockers, mav.getModel().get("lockers"));
    }

    @Test
    public void queNoSeMuestrenLockersSiNoHayDatos() {
        // Mockear el servicio para devolver una lista vacía
        when(controladorLocker.servicioLocker.obtenerLockersSeleccionados()).thenReturn(new ArrayList<>());

        // Ejecución
        ModelAndView mav = controladorLocker.mostrarLockers();

        // Verificación
        assertThat(mav.getViewName(), equalToIgnoringCase("lockers"));
        assertThat(mav.getModel().get("lockers"), instanceOf(List.class));
        assertTrue(((List) mav.getModel().get("lockers")).isEmpty());
    }

    @Test
    public void queSePuedanObtenerLockersPorCodigoPostal() {
        // Preparación
        String codigoPostal = "1704";
        List<Locker> lockers = Arrays.asList(
                new Locker(TipoLocker.PEQUEÑO, 40.7128, -74.0060, codigoPostal),
                new Locker(TipoLocker.MEDIANO, 40.7129, -74.0061, codigoPostal)
        );

        // Mockear el servicio
        when(controladorLocker.servicioLocker.obtenerLockersPorCodigoPostal(codigoPostal)).thenReturn(lockers);

        // Ejecución
        ModelAndView mav = controladorLocker.buscarLockersPorCodigoPostal(codigoPostal);

        // Verificación
        assertThat(mav.getViewName(), equalToIgnoringCase("lockers"));
        assertThat(mav.getModel().get("lockers"), equalTo(lockers));
        assertThat(mav.getModel().get("codigoPostal"), equalTo(codigoPostal));
    }

    @Test
    public void queNoSeEncuentrenLockersConCodigoPostalInvalido() {
        // Preparación
        String codigoPostal = "9999";
        List<Locker> lockers = new ArrayList<>();

        // Mockear el servicio
        when(controladorLocker.servicioLocker.obtenerLockersPorCodigoPostal(codigoPostal)).thenReturn(lockers);

        // Ejecución
        ModelAndView mav = controladorLocker.buscarLockersPorCodigoPostal(codigoPostal);

        // Verificación
        assertThat(mav.getViewName(), equalToIgnoringCase("lockers"));
        assertThat(mav.getModel().get("lockers"), equalTo(lockers));
        assertThat(mav.getModel().get("codigoPostal"), equalTo(codigoPostal));
    }





}
