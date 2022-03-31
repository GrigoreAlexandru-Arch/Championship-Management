package database.dao;

import database.model.PersonEntity;

import javax.persistence.TypedQuery;
import java.util.List;

public class PersonDao implements DaoI<PersonEntity> {
    private static PersonDao instance = null;
    private PersonDao() {
    }

    public static PersonDao getInstance() {
        if(instance == null) {
            instance = new PersonDao();
        }
        return instance;
    }

    @Override
    public PersonEntity get(Long id) {
        return connection.getEntityManager().find(PersonEntity.class, id);
    }

    @Override
    public void create(PersonEntity personEntity) {
        connection.executeTransaction(entityManager -> entityManager.persist(personEntity));
    }

    @Override
    public List<PersonEntity> getAll() {
        TypedQuery<PersonEntity> query = connection.getEntityManager().createQuery("SELECT a FROM PersonEntity a", PersonEntity.class);
        return query.getResultList();
    }

    @Override
    public void update(PersonEntity people) {
        connection.executeTransaction(entityManager -> entityManager.merge(people));
    }

    @Override
    public void delete(PersonEntity people) {
        connection.executeTransaction(entityManager -> entityManager.remove(people));
    }
}