package com.tallerwebi.infraestructura;

import com.tallerwebi.config.HibernateConfig;
import com.tallerwebi.dominio.DatosLocker;
import com.tallerwebi.dominio.RepositorioDatosLockerImpl;
import com.tallerwebi.dominio.locker.RepositorioDatosLocker;
import com.tallerwebi.dominio.locker.TipoLocker;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateConfig.class})
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

        // Ejecución
        DatosLocker nuevoLocker = new DatosLocker(tipoLocker);

        repolocker.guardar(nuevoLocker);

        // Verificación
        // Verificar que se ha creado el locker correctamente
        DatosLocker lockerObtenido = (DatosLocker) this.sessionFactory.getCurrentSession()
                .createQuery("FROM DatosLocker where id = :id")
                .setParameter("id", nuevoLocker.getId())
                .getSingleResult();

        assertThat(lockerObtenido, equalTo(nuevoLocker));
    }


    @Test
    @Rollback
    @Transactional

    public void testActualizarLocker() {
        // Preparación
        TipoLocker tipoLockerInicial = TipoLocker.PEQUEÑO;
        TipoLocker tipoLockerNuevo = TipoLocker.MEDIANO;
        DatosLocker nuevoLocker = new DatosLocker(tipoLockerInicial);
        repolocker.guardar(nuevoLocker);

        // Ejecución
        nuevoLocker.setTipoLocker(tipoLockerNuevo);
        repolocker.actualizar(nuevoLocker);

        // Verificación
        DatosLocker lockerActualizado = repolocker.obtenerPorId(nuevoLocker.getId());
        assertThat(lockerActualizado.getTipoLocker(), equalTo(tipoLockerNuevo));
    }

    @Test
    @Rollback
    @Transactional
    public void testEliminarLocker() {
        // Preparación
        TipoLocker tipoLocker = TipoLocker.PEQUEÑO;
        DatosLocker nuevoLocker = new DatosLocker(tipoLocker);
        repolocker.guardar(nuevoLocker);

        // Ejecución
        repolocker.eliminar(nuevoLocker.getId());

        // Verificación
        DatosLocker lockerEliminado = repolocker.obtenerPorId(nuevoLocker.getId());
        assertNull(lockerEliminado);
    }

    @Test
    @Rollback
    @Transactional

    public void testObtenerLockersPorTipo() {
        // Preparación
        TipoLocker tipoLocker = TipoLocker.PEQUEÑO;
        DatosLocker nuevoLocker1 = new DatosLocker(tipoLocker);
        DatosLocker nuevoLocker2 = new DatosLocker(tipoLocker);
        repolocker.guardar(nuevoLocker1);
        repolocker.guardar(nuevoLocker2);

        // Ejecución
        List<DatosLocker> lockersPorTipo = repolocker.obtenerLockersPorTipo(tipoLocker);

        // Verificación
        assertTrue(lockersPorTipo.contains(nuevoLocker1));
        assertTrue(lockersPorTipo.contains(nuevoLocker2));
    }

}
