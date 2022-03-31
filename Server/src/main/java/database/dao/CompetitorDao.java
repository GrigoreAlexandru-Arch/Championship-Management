package database.dao;

import database.model.CompetitorEntity;

import javax.persistence.TypedQuery;
import java.util.List;

public class CompetitorDao implements DaoI<CompetitorEntity> {
    private static CompetitorDao instance = null;
    private CompetitorDao() {
    }

    public static CompetitorDao getInstance() {
        if(instance == null) {
            instance = new CompetitorDao();
        }
        return instance;
    }

    @Override
    public CompetitorEntity get(Long id) {
        return connection.getEntityManager().find(CompetitorEntity.class, id);
    }

    @Override
    public List<CompetitorEntity> getAll() {
        TypedQuery<CompetitorEntity> query = connection.getEntityManager().createQuery("SELECT a FROM CompetitorEntity a", CompetitorEntity.class);
        return query.getResultList();
    }

    @Override
    public void create(CompetitorEntity competitorEntity) {
        connection.executeTransaction(entityManager -> entityManager.persist(competitorEntity));
    }

    @Override
    public void update(CompetitorEntity competitorEntity) {
        connection.executeTransaction(entityManager -> entityManager.merge(competitorEntity));
    }

    @Override
    public void delete(CompetitorEntity competitorEntity) {
        connection.executeTransaction(entityManager -> entityManager.remove(competitorEntity));
    }
}
