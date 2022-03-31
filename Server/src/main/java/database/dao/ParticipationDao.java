package database.dao;

import database.DataBaseConnection;
import database.model.ParticipationEntity;
import database.model.ParticipationEntityPK;

import javax.persistence.TypedQuery;
import java.util.List;

public class ParticipationDao {
    DataBaseConnection connection = new DataBaseConnection();
    private static ParticipationDao instance = null;
    private ParticipationDao() {
    }

    public static ParticipationDao getInstance() {
        if(instance == null) {
            instance = new ParticipationDao();
        }
        return instance;
    }

    public ParticipationEntity get(ParticipationEntityPK pk) {
        return connection.getEntityManager().find(ParticipationEntity.class, pk);
    }

    public List<ParticipationEntity> getAll() {
        TypedQuery<ParticipationEntity> query = connection.getEntityManager().createQuery("SELECT a FROM ParticipationEntity a", ParticipationEntity.class);
        return query.getResultList();
    }

    public void create(ParticipationEntity participationEntity) {
        connection.executeTransaction(entityManager -> entityManager.persist(participationEntity));
    }

    public void update(ParticipationEntity participationEntity) {
        connection.executeTransaction(entityManager -> entityManager.merge(participationEntity));
    }

    public void delete(ParticipationEntity participationEntity) {
        connection.executeTransaction(entityManager -> entityManager.remove(participationEntity));
    }
}
