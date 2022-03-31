package database.dao;

import database.model.StageEntity;

import javax.persistence.TypedQuery;
import java.util.List;

public class StageDao implements DaoI<StageEntity> {

    private static StageDao instance = null;
    private StageDao() {
    }

    public static StageDao getInstance() {
        if(instance == null) {
            instance = new StageDao();
        }
        return instance;
    }

    @Override
    public StageEntity get(Long id) {
        return connection.getEntityManager().find(StageEntity.class,id);
    }

    @Override
    public List<StageEntity> getAll() {
        TypedQuery<StageEntity> query = connection.getEntityManager().createQuery("SELECT a FROM StageEntity a", StageEntity.class);
        return query.getResultList();
    }

    @Override
    public void create(StageEntity stageEntity) {
        connection.executeTransaction(entityManager -> entityManager.persist(stageEntity));
    }

    @Override
    public void update(StageEntity stageEntity) {
        connection.executeTransaction(entityManager -> entityManager.merge(stageEntity));
    }

    @Override
    public void delete(StageEntity stageEntity) {
        connection.executeTransaction(entityManager -> entityManager.remove(stageEntity));
    }
}
