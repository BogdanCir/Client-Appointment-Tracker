package repository;

import domain.Entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Repository <T extends Entity> implements IRepository <T>{
    ArrayList <T> items;
    public Repository() {
        this.items = new ArrayList<>();
    }

    @Override
    public void add(T itemToAdd) throws Exception {
        for (T item : items)
            if (item.getId() == itemToAdd.getId())
                throw new Exception("Item with ID " + itemToAdd.getId() + " already exists in repository!");
        items.add(itemToAdd);
    }


    @Override
    public void delete(int itemId) throws Exception {
        for (T item : items)
            if(item.getId() == itemId) {
                items.remove(item);
                return;
            }
        throw new Exception("Item is not in repository!");
    }

    @Override
    public void update(T itemToUpdate) throws Exception {
        int positionToUpdate = -1;
        for(int i = 0; i < items.size(); i++)
            if(itemToUpdate.getId() == items.get(i).getId())
                positionToUpdate = i;

        if (positionToUpdate == -1)
            throw new Exception("Item is not in repository!");

        items.set(positionToUpdate, itemToUpdate);
    }

    @Override
    public List<T> getAll() {
        return items;
    }

    @Override
    public T getItemByID(int id) throws Exception {
        for (T item : items)
            if (item.getId() == id)
                return item;
        throw  new Exception("Item is not in repository!");
    }

//    public Iterator<T> iterator() {
//        return new ArrayList<T>(items).iterator();
//    }
}
