package com.tallerwebi.dominio;

import com.tallerwebi.dominio.locker.RepositorioDatosLocker;
import com.tallerwebi.dominio.locker.TipoLocker;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioDatosLockerImpl implements RepositorioDatosLocker {

    private final SessionFactory sessionFactory;

    public RepositorioDatosLockerImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Locker locker) {
        if (locker == null || locker.getTipo() == null || locker.getLatitud() == null || locker.getLongitud() == null || locker.getCodigo_postal() == null) {
            throw new IllegalArgumentException("Locker no puede tener parámetros nulos");
        }
        this.sessionFactory.getCurrentSession().save(locker);
    }


    @Override
    public Locker actualizar(Locker locker) {
        if (locker == null || locker.getId() == null || locker.getId() <= 0) {
            throw new IllegalArgumentException("No se encontró ningún locker con el ID proporcionado: " + (locker != null ? locker.getId() : "nulo"));
        }
        Session session = sessionFactory.getCurrentSession();
        session.update(locker);
        return locker;
    }

    @Override
    public void eliminar(Long idLocker) {
        if (idLocker == null || idLocker <= 0) {
            throw new IllegalArgumentException("No se encontró ningún locker con el ID proporcionado: " + idLocker);
        }
        Session session = sessionFactory.getCurrentSession();
        Locker locker = session.load(Locker.class, idLocker);
        if (locker != null) {
            session.delete(locker);
        }
    }

    @Override
    public Locker obtenerPorId(Long idLocker) {
        if (idLocker == null || idLocker <= 0) {
            throw new IllegalArgumentException("ID de locker no puede ser nulo o negativo: " + idLocker);
        }
        return (Locker) this.sessionFactory.getCurrentSession()
                .createQuery("FROM Locker WHERE id = :id")
                .setParameter("id", idLocker)
                .uniqueResult();
    }

    @Override
    public List<Locker> obtenerLockersPorTipo(TipoLocker tipoLocker) {
        if (tipoLocker == null) {
            throw new IllegalArgumentException("Tipo de locker no puede ser nulo");
        }
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM Locker WHERE tipo = :tipoLocker", Locker.class)
                .setParameter("tipoLocker", tipoLocker)
                .getResultList();
    }

    @Override
    public List<Locker> obtenerSeleccionados() {
        String hql = "FROM Locker WHERE seleccionado = true";
        return sessionFactory.getCurrentSession().createQuery(hql, Locker.class).list();
    }

    @Override
    public void eliminarTodos() {
        String hql = "DELETE FROM Locker";
        sessionFactory.getCurrentSession().createQuery(hql).executeUpdate();
    }

    @Override
    public List<Locker> obtenerLockersPorCodigoPostal(String codigoPostal) {
        if (codigoPostal == null) {
            throw new IllegalArgumentException("Código postal no puede ser nulo");
        }
        String hql = "FROM Locker WHERE codigo_postal = :codigo_postal";
        return sessionFactory.getCurrentSession().createQuery(hql, Locker.class)
                .setParameter("codigo_postal", codigoPostal)
                .list();
    }
}
