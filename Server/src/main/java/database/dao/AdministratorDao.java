package database.dao;

import database.model.AdministratorEntity;

import javax.persistence.TypedQuery;
import java.util.List;

public class AdministratorDao implements DaoI<AdministratorEntity> {
    private static AdministratorDao instance = null;
    private AdministratorDao() {
    }

    public static AdministratorDao getInstance() {
        if(instance == null) {
            instance = new AdministratorDao();
        }
        return instance;
    }
    @Override
    public AdministratorEntity get(Long id) {
        return connection.getEntityManager().find(AdministratorEntity.class,id);
    }

    @Override
    public List<AdministratorEntity> getAll() {
        TypedQuery<AdministratorEntity> query = connection.getEntityManager().createQuery("SELECT a FROM AdministratorEntity a", AdministratorEntity.class);
        return query.getResultList();
    }

    @Override
    public void create(AdministratorEntity administratorEntity) {
        connection.executeTransaction(entityManager -> entityManager.persist(administratorEntity));
    }

    @Override
    public void update(AdministratorEntity administratorEntity) {
        connection.executeTransaction(entityManager -> entityManager.merge(administratorEntity));
    }

    @Override
    public void delete(AdministratorEntity administratorEntity) {
        connection.executeTransaction(entityManager -> entityManager.remove(administratorEntity));
    }
}
