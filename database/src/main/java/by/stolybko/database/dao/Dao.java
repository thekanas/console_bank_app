package by.stolybko.database.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class Dao<K extends Serializable, E> {

    public abstract List<E> findAll();
    public abstract Optional<E> findById(K id);
    public abstract Optional<E> save(E entity);
    public abstract Optional<E> update(E entity);
    public abstract boolean delete(K id);
}
