package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Locker;
import com.tallerwebi.dominio.RepositorioDatosLockerImpl;
import com.tallerwebi.dominio.locker.RepositorioDatosLocker;
import com.tallerwebi.dominio.locker.ServicioLocker;
import com.tallerwebi.dominio.locker.TipoLocker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ControladorLockerTest {
    private ControladorLocker controladorLocker;
    private ServicioLocker servicioLocker;

    @BeforeEach
    public void init() {
        servicioLocker = mock(ServicioLocker.class);
        this.controladorLocker = new ControladorLocker(servicioLocker);
    }

    @Test
    public void queSePuedaCrearUnNuevoLocker() {
        // Preparación
        TipoLocker tipoLocker = TipoLocker.PEQUEÑO;
        List<Locker> lockers = new ArrayList<>();
        when(servicioLocker.obtenerLockersPorTipo(tipoLocker)).thenReturn(lockers);

        // Ejecución
        ModelAndView mav = this.controladorLocker.crearLocker(tipoLocker);

        // Verificación
        assertThat(mav.getViewName(), equalToIgnoringCase("crear-locker"));
        assertThat(mav.getModel().get("lockers"), equalTo(lockers));
    }

    @Test
    public void queSePuedaActualizarUnLocker() {
        // Preparación
        Long idLocker = 1L;
        TipoLocker tipoLocker = TipoLocker.GRANDE;

        // Ejecución
        ModelAndView mav = this.controladorLocker.actualizarLocker(idLocker, tipoLocker);

        // Verificación
        assertThat(mav.getViewName(), equalToIgnoringCase("redirect:/lockers/actualizar-locker"));
        verify(servicioLocker).actualizarLocker(idLocker, tipoLocker);
    }

    @Test
    public void queSePuedaEliminarUnLocker() {
        // Preparación
        Long idLocker = 1L;

        // Ejecución
        ModelAndView mav = this.controladorLocker.eliminarLocker(idLocker);

        // Verificación
        assertThat(mav.getViewName(), equalToIgnoringCase("redirect:/lockers/mensaje-eliminacion-locker"));
        verify(servicioLocker).eliminarLocker(idLocker);
    }

    @Test
    public void queSePuedaObtenerUnaListaDeLockersFiltradaPorTipo() {
        // Preparación
        TipoLocker tipoLocker = TipoLocker.GRANDE;
        List<Locker> lockers = new ArrayList<>();
        when(servicioLocker.obtenerLockersPorTipo(tipoLocker)).thenReturn(lockers);

        // Ejecución
        ModelAndView mav = this.controladorLocker.buscarLockersPorTipo(tipoLocker);

        // Verificación
        assertThat(mav.getViewName(), equalToIgnoringCase("lockers-por-tipo"));
        assertThat(mav.getModel().get("lockers"), equalTo(lockers));
    }

    @Test
    public void queSeMuestrenLockersSeleccionados() {
        // Preparación
        List<Locker> lockers = Arrays.asList(
                new Locker(TipoLocker.PEQUEÑO, 40.7128, -74.0060, "1704"),
                new Locker(TipoLocker.MEDIANO, 40.7129, -74.0061, "1704")
        );
        when(servicioLocker.obtenerLockersSeleccionados()).thenReturn(lockers);

        // Ejecución
        ModelAndView mav = this.controladorLocker.mostrarLockers();

        // Verificación
        assertThat(mav.getViewName(), equalToIgnoringCase("lockers"));
        assertThat(mav.getModel().get("lockers"), equalTo(lockers));
    }

    @Test
    public void buscarLockersPorCodigoPostal_CuandoSeProporcionaCodigoPostal() {
        // Preparación
        String codigoPostal = "1704";
        List<Locker> lockers = Arrays.asList(
                new Locker(TipoLocker.PEQUEÑO, 40.7128, -74.0060, codigoPostal),
                new Locker(TipoLocker.MEDIANO, 40.7129, -74.0061, codigoPostal)
        );
        when(servicioLocker.obtenerLockersPorCodigoPostal(codigoPostal)).thenReturn(lockers);

        // Ejecución
        ModelAndView mav = controladorLocker.buscarLockersPorCodigoPostal(codigoPostal, null, null);

        // Verificación
        assertEquals("lockers", mav.getViewName());
        assertEquals(lockers, mav.getModel().get("lockers"));
        assertEquals(codigoPostal, mav.getModel().get("codigoPostal"));
        assertNull(mav.getModel().get("latitud"));
        assertNull(mav.getModel().get("longitud"));
        assertFalse((Boolean) mav.getModel().get("mostrarAlternativos"));
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
    public void buscarLockersPorCodigoPostal_DevuelveLockersCorrectos() {
        String codigoPostal = "1704";
        List<Locker> lockers = Arrays.asList(
                new Locker(TipoLocker.PEQUEÑO, 40.7128, -74.0060, codigoPostal),
                new Locker(TipoLocker.MEDIANO, 40.7129, -74.0061, codigoPostal)
        );

        when(servicioLocker.obtenerLockersPorCodigoPostal(codigoPostal)).thenReturn(lockers);

        ModelAndView mav = controladorLocker.buscarLockersPorCodigoPostal(codigoPostal, null, null);

        assertEquals("lockers", mav.getViewName());
        assertEquals(lockers, mav.getModel().get("lockers"));
        verify(servicioLocker).obtenerLockersPorCodigoPostal(codigoPostal);
    }

    @Test
    public void buscarLockersPorLatitudYLongitud_DevuelveLockersCorrectos() {
        Double latitud = -34.6821;
        Double longitud = -58.5638;
        List<Locker> lockers = Arrays.asList(
                new Locker(TipoLocker.PEQUEÑO, latitud, longitud, "1704"),
                new Locker(TipoLocker.MEDIANO, latitud + 0.001, longitud - 0.001, "1704")
        );

        when(servicioLocker.obtenerLockersCercanos(latitud, longitud, 5.0)).thenReturn(lockers);

        ModelAndView mav = controladorLocker.buscarLockersPorCodigoPostal(null, latitud, longitud);

        assertEquals("lockers", mav.getViewName());
        assertEquals(lockers, mav.getModel().get("lockers"));
        verify(servicioLocker).obtenerLockersCercanos(latitud, longitud, 5.0);
    }

    @Test
    public void buscarLockers_SinParametros_DevuelveLockersSeleccionados() {
        List<Locker> lockersSeleccionados = Arrays.asList(
                new Locker(TipoLocker.PEQUEÑO, 40.7128, -74.0060, "1704"),
                new Locker(TipoLocker.MEDIANO, 40.7129, -74.0061, "1704")
        );

        when(servicioLocker.obtenerLockersSeleccionados()).thenReturn(lockersSeleccionados);

        ModelAndView mav = controladorLocker.buscarLockersPorCodigoPostal(null, null, null);

        assertEquals("lockers", mav.getViewName());
        assertEquals(lockersSeleccionados, mav.getModel().get("lockers"));
        verify(servicioLocker).obtenerLockersSeleccionados();
    }

    @Test
    public void buscarLockers_SinResultados_DevuelveLockersAlternativos() {
        String codigoPostal = "1234";
        List<Locker> lockersSeleccionados = Arrays.asList(
                new Locker(TipoLocker.PEQUEÑO, 40.7128, -74.0060, codigoPostal),
                new Locker(TipoLocker.MEDIANO, 40.7129, -74.0061, codigoPostal)
        );

        when(servicioLocker.obtenerLockersPorCodigoPostal(codigoPostal)).thenReturn(Collections.emptyList());
        when(servicioLocker.obtenerLockersSeleccionados()).thenReturn(lockersSeleccionados);

        ModelAndView mav = controladorLocker.buscarLockersPorCodigoPostal(codigoPostal, null, null);

        assertEquals("lockers", mav.getViewName());
        assertEquals(lockersSeleccionados, mav.getModel().get("lockers"));
        assertTrue((Boolean) mav.getModel().get("mostrarAlternativos"));
    }

    @Test
    public void buscarLockersPorCodigoPostal_ModeloIncluyeParametrosDeEntrada() {
        String codigoPostal = "1704";
        Double latitud = -34.6821;
        Double longitud = -58.5638;
        List<Locker> lockers = Arrays.asList(
                new Locker(TipoLocker.PEQUEÑO, 40.7128, -74.0060, codigoPostal),
                new Locker(TipoLocker.MEDIANO, 40.7129, -74.0061, codigoPostal)
        );

        when(servicioLocker.obtenerLockersPorCodigoPostal(codigoPostal)).thenReturn(lockers);

        ModelAndView mav = controladorLocker.buscarLockersPorCodigoPostal(codigoPostal, latitud, longitud);

        assertEquals("lockers", mav.getViewName());
        assertEquals(lockers, mav.getModel().get("lockers"));
        assertEquals(codigoPostal, mav.getModel().get("codigoPostal"));
        assertEquals(latitud, mav.getModel().get("latitud"));
        assertEquals(longitud, mav.getModel().get("longitud"));
    }

    @Test
    public void buscarLockersPorCodigoPostal_SiNoHayResultadosUsaAlternativos() {
        String codigoPostal = "1704";
        List<Locker> lockers = Collections.emptyList();
        List<Locker> lockersSeleccionados = Arrays.asList(
                new Locker(TipoLocker.PEQUEÑO, 40.7128, -74.0060, codigoPostal),
                new Locker(TipoLocker.MEDIANO, 40.7129, -74.0061, codigoPostal)
        );

        when(servicioLocker.obtenerLockersPorCodigoPostal(codigoPostal)).thenReturn(lockers);
        when(servicioLocker.obtenerLockersSeleccionados()).thenReturn(lockersSeleccionados);

        ModelAndView mav = controladorLocker.buscarLockersPorCodigoPostal(codigoPostal, null, null);

        assertEquals("lockers", mav.getViewName());
        assertEquals(lockersSeleccionados, mav.getModel().get("lockers"));
        assertTrue((Boolean) mav.getModel().get("mostrarAlternativos"));
    }

    @Test
    public void buscarLockersPorLatitudYLongitud_SiNoHayResultadosUsaAlternativos() {
        Double latitud = -34.6821;
        Double longitud = -58.5638;
        List<Locker> lockers = Collections.emptyList();
        List<Locker> lockersSeleccionados = Arrays.asList(
                new Locker(TipoLocker.PEQUEÑO, latitud, longitud, "1704"),
                new Locker(TipoLocker.MEDIANO, latitud + 0.001, longitud - 0.001, "1704")
        );

        when(servicioLocker.obtenerLockersCercanos(latitud, longitud, 5.0)).thenReturn(lockers);
        when(servicioLocker.obtenerLockersSeleccionados()).thenReturn(lockersSeleccionados);

        ModelAndView mav = controladorLocker.buscarLockersPorCodigoPostal(null, latitud, longitud);

        assertEquals("lockers", mav.getViewName());
        assertEquals(lockersSeleccionados, mav.getModel().get("lockers"));
        assertTrue((Boolean) mav.getModel().get("mostrarAlternativos"));
    }

}
