package database.dao;

import database.DataBaseConnection;

import java.util.List;

public interface DaoI<T> {
    DataBaseConnection connection = new DataBaseConnection();

    T get(Long id);
    List<T> getAll ();
    void create (T t);
    void update(T t);
    void delete(T t);
}