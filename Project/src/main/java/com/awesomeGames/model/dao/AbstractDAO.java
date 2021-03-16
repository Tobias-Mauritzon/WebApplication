package com.awesomeGames.model.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

/**
 * Abstract DAO for other DAOs to extend
 *
 * @author Matteus
 * @param <K> Primary key
 * @param <T> Entity
 */
@RequiredArgsConstructor
public abstract class AbstractDAO<K, T> {

    final Class<T> entityType;

    protected abstract EntityManager getEntityManager();

    /**
     * Counts the number of entitys on the db
     *
     * @return the number of entitys on the db
     */
    public long count() {
        final CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        final CriteriaQuery cq = builder.createQuery();
        final Root<T> rt = cq.from(entityType);

        cq.select(builder.count(rt));

        final Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult());
    }

    /**
     * Adds the given entity to the db
     *
     * @param entity the entity to be added
     */
    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    /**
     * Returns a list of all the entitys of this type
     *
     * @return List of all the entitys of this type
     */
    public List<T> findAll() {
        final CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityType));
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Removes the given entity to the db
     *
     * @param entity the entity to be removed
     */
    public void remove(T entity) {
        getEntityManager().remove(entity);
    }

    /**
     * Finds the enitity from the db where the key is the same
     *
     * @param key the key of the entity to be found
     * @return T the entity
     */
    public T find(K key) {
        return getEntityManager().find(entityType, key);
    }

    /**
     * Checked if an enitiy with the given key exists on the db
     *
     * @param key the key of the entity to be checked if it exists
     * @return True if it exists false otherwise
     */
    public boolean exists(K key) {
        return getEntityManager().find(entityType, key) != null;
    }
}
