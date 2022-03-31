package database.dao;

import database.model.TeamEntity;

import javax.persistence.TypedQuery;
import java.util.List;

public class TeamDao implements DaoI<TeamEntity> {

    private static TeamDao instance = null;
    private TeamDao() {
    }

    public static TeamDao getInstance() {
        if(instance == null) {
            instance = new TeamDao();
        }
        return instance;
    }

    @Override
    public TeamEntity get(Long id) {
        return connection.getEntityManager().find(TeamEntity.class,id);
    }

    @Override
    public List<TeamEntity> getAll() {
        TypedQuery<TeamEntity> query = connection.getEntityManager().createQuery("SELECT a FROM TeamEntity a", TeamEntity.class);
        return query.getResultList();
    }

    @Override
    public void create(TeamEntity teamEntity) {
        connection.executeTransaction(entityManager -> entityManager.persist(teamEntity));
    }

    @Override
    public void update(TeamEntity teamEntity) {
        connection.executeTransaction(entityManager -> entityManager.merge(teamEntity));
    }

    @Override
    public void delete(TeamEntity teamEntity) {
        connection.executeTransaction(entityManager -> entityManager.remove(teamEntity));
    }
}
