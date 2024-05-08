package com.tallerwebi.dominio;


import com.tallerwebi.dominio.locker.RepositorioDatosLocker;
import com.tallerwebi.dominio.locker.TipoLocker;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class RepositorioDatosLockerImpl implements RepositorioDatosLocker {


    private final SessionFactory sessionFactory;


    public RepositorioDatosLockerImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override

    public void guardar(DatosLocker locker) {
        this.sessionFactory.getCurrentSession().save(locker);

    }

    @Override

    public DatosLocker actualizar(DatosLocker locker) {
        Session session = sessionFactory.getCurrentSession();
        session.update(locker);
        return locker;
    }

    @Override
    public void eliminar(Long idLocker) {
        Session session = sessionFactory.getCurrentSession();
        DatosLocker locker = session.load(DatosLocker.class, idLocker);
        if (locker != null) {
            session.delete(locker);
        }
    }

    @Override
    public DatosLocker obtenerPorId(Long idLocker) {
        return (DatosLocker) this.sessionFactory.getCurrentSession()
                .createQuery("FROM DatosLocker WHERE id = :id")
                .setParameter("id", idLocker)
                .uniqueResult();
    }

    @Override
    public List<DatosLocker> obtenerLockersPorTipo(TipoLocker tipoLocker) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM DatosLocker WHERE tipo = :tipoLocker", DatosLocker.class)
                .setParameter("tipoLocker", tipoLocker)
                .getResultList();
    }


}
