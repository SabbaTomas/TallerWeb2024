package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Locker;
import com.tallerwebi.dominio.RepositorioDatosLockerImpl;
import com.tallerwebi.dominio.locker.RepositorioDatosLocker;
import com.tallerwebi.dominio.locker.TipoLocker;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestInfraestructuraConfig.class})
public class RepositorioLockerTest {

    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioDatosLocker repolocker;

    @BeforeEach
    public void init() {
        this.repolocker = new RepositorioDatosLockerImpl(this.sessionFactory);
    }

    @Test
    @Rollback
    @Transactional
    public void testCrearLocker() {
        // Preparación
        TipoLocker tipoLocker = TipoLocker.PEQUEÑO;
        String codigoPostal = "1704";
        Locker nuevoLocker = new Locker(tipoLocker, 40.7128, -74.0060, codigoPostal);

        // Ejecución
        repolocker.guardar(nuevoLocker);

        // Verificación
        Locker lockerObtenido = (Locker) this.sessionFactory.getCurrentSession()
                .createQuery("FROM Locker where id = :id")
                .setParameter("id", nuevoLocker.getId())
                .getSingleResult();

        assertThat(lockerObtenido, equalTo(nuevoLocker));
    }

    @Test
    @Rollback
    @Transactional
    public void testCrearLockerConParametrosNulos() {
        // Preparación
        Locker locker = new Locker();

        // Ejecución y Verificación
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            repolocker.guardar(locker);
        });

        assertEquals("Locker no puede tener parámetros nulos", exception.getMessage());
    }

    @Test
    @Rollback
    @Transactional
    public void testActualizarLocker() {
        // Preparación
        TipoLocker tipoLockerInicial = TipoLocker.PEQUEÑO;
        TipoLocker tipoLockerNuevo = TipoLocker.MEDIANO;
        String codigoPostal = "1704";
        Locker nuevoLocker = new Locker(tipoLockerInicial, 40.7128, -74.0060, codigoPostal);
        repolocker.guardar(nuevoLocker);

        // Ejecución
        nuevoLocker.setTipo(tipoLockerNuevo);
        repolocker.actualizar(nuevoLocker);

        // Verificación
        Locker lockerActualizado = repolocker.obtenerPorId(nuevoLocker.getId());
        assertThat(lockerActualizado.getTipo(), equalTo(tipoLockerNuevo));
    }

    @Test
    @Rollback
    @Transactional
    public void testActualizarLockerConIdInvalido() {
        // Preparación
        Long idLocker = -1L;
        TipoLocker tipoLockerNuevo = TipoLocker.GRANDE;

        // Ejecución y Verificación
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            repolocker.actualizar(new Locker(idLocker, tipoLockerNuevo));
        });

        assertEquals("No se encontró ningún locker con el ID proporcionado: " + idLocker, exception.getMessage());
    }

    @Test
    @Rollback
    @Transactional
    public void testEliminarLocker() {
        // Preparación
        TipoLocker tipoLocker = TipoLocker.PEQUEÑO;
        String codigoPostal = "1704";
        Locker nuevoLocker = new Locker(tipoLocker, 40.7128, -74.0060, codigoPostal);
        repolocker.guardar(nuevoLocker);

        // Ejecución
        repolocker.eliminar(nuevoLocker.getId());

        // Verificación
        Locker lockerEliminado = repolocker.obtenerPorId(nuevoLocker.getId());
        assertNull(lockerEliminado);
    }

    @Test
    @Rollback
    @Transactional
    public void testEliminarLockerConIdInvalido() {
        // Preparación
        Long idLocker = -1L;

        // Ejecución y Verificación
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            repolocker.eliminar(idLocker);
        });

        assertEquals("No se encontró ningún locker con el ID proporcionado: " + idLocker, exception.getMessage());
    }

    @Test
    @Rollback
    @Transactional
    public void testObtenerLockersPorTipo() {
        // Preparación
        TipoLocker tipoLocker = TipoLocker.PEQUEÑO;
        String codigoPostal = "1704";
        Locker nuevoLocker1 = new Locker(tipoLocker, 40.7128, -74.0060, codigoPostal);
        Locker nuevoLocker2 = new Locker(tipoLocker, 40.7129, -74.0061, codigoPostal);
        repolocker.guardar(nuevoLocker1);
        repolocker.guardar(nuevoLocker2);

        // Ejecución
        List<Locker> lockersPorTipo = repolocker.obtenerLockersPorTipo(tipoLocker);

        // Verificación
        assertTrue(lockersPorTipo.contains(nuevoLocker1));
        assertTrue(lockersPorTipo.contains(nuevoLocker2));
    }


    @Test
    @Rollback
    @Transactional
    public void testObtenerLockersPorTipoInvalido() {
        // Ejecución y Verificación
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            repolocker.obtenerLockersPorTipo(null);
        });

        assertEquals("Tipo de locker no puede ser nulo", exception.getMessage());
    }

    @Test
    @Rollback
    @Transactional
    public void testObtenerLockersPorCodigoPostal() {
        // Preparación
        String codigoPostal = "1704";
        Locker nuevoLocker1 = new Locker(TipoLocker.PEQUEÑO, 40.7128, -74.0060, codigoPostal);
        Locker nuevoLocker2 = new Locker(TipoLocker.MEDIANO, 40.7129, -74.0061, codigoPostal);
        repolocker.guardar(nuevoLocker1);
        repolocker.guardar(nuevoLocker2);

        // Ejecución
        List<Locker> lockersPorCodigoPostal = repolocker.obtenerLockersPorCodigoPostal(codigoPostal);

        // Verificación
        assertTrue(lockersPorCodigoPostal.contains(nuevoLocker1));
        assertTrue(lockersPorCodigoPostal.contains(nuevoLocker2));
    }

    @Test
    @Rollback
    @Transactional
    public void testObtenerLockersPorCodigoPostalInvalido() {
        // Ejecución y Verificación
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            repolocker.obtenerLockersPorCodigoPostal(null);
        });

        assertEquals("Código postal no puede ser nulo", exception.getMessage());
    }
}
