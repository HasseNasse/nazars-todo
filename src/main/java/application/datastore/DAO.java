package application.datastore;

import org.bson.types.ObjectId;

import java.util.List;

public interface DAO<T> {
    List<T> findAll();
    T findByID(String id);
    boolean insert(T obj);
    boolean update(T obj);
    boolean delete(T obj);
    boolean deleteByID(String id);
}
