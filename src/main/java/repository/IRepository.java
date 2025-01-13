package repository;

import domain.Entity;

import java.util.List;

public interface IRepository <T extends Entity>  {
    void add(T itemToAdd) throws Exception;
    void delete(int itemId) throws Exception;
    void update(T itemToUpdate) throws Exception;
    List<T> getAll();
    T getItemByID(int id) throws Exception;
}
