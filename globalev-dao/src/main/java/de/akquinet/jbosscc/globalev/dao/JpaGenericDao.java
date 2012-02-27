package de.akquinet.jbosscc.globalev.dao;

import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import de.akquinet.jbosscc.globalev.model.AbstractEntity;

public abstract class JpaGenericDao<E extends AbstractEntity> implements GenericDao<E> {
  private transient Class<E> _persistentClass;

  @PersistenceContext
  private EntityManager _entityManager;

  public void persist(final E e) {
    _entityManager.persist(e);
  }

  public Long getVersion(final Long id) {
    final Class<E> persistenceClass = getPersistentClass();
    final Query query = _entityManager.createQuery("SELECT version FROM " + persistenceClass.getSimpleName() + " e WHERE e.id = :id")
        .setParameter("id", id);
    return (Long) query.getSingleResult();
  }

  public boolean contains(final E e) {
    return _entityManager.contains(e);
  }

  public E merge(final E entity) {
    final E mergedEntity = _entityManager.merge(entity);
    _entityManager.flush();
    _entityManager.refresh(mergedEntity);
    return mergedEntity;
  }

  public void delete(final E e) {
    _entityManager.remove(e);
  }

  public E load(final Long id) {
    assert id != null : "id != null";
    return _entityManager.find(getPersistentClass(), id);
  }

  public boolean exists(final Long id) {
    return load(id) != null;
  }

  public List<E> loadAll() {
    final Class<E> persistenceClass = getPersistentClass();
    final TypedQuery<E> query = _entityManager.createQuery("SELECT e FROM " + persistenceClass.getSimpleName() + " e", persistenceClass);
    return query.getResultList();
  }

  @SuppressWarnings("unchecked")
  protected Class<E> getPersistentClass() {
    if (_persistentClass == null) {
      _persistentClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    return _persistentClass;
  }

  public EntityManager getEntityManager() {
    return _entityManager;
  }

  protected E loadByQuery(final String stmt) {
    return _entityManager.createQuery(stmt, getPersistentClass()).getSingleResult();
  }

  protected List<E> findByQuery(final String stmt, final Map<String, Object> parameters, final int maxResults) {
    checkMaxResult(maxResults);

    final TypedQuery<E> query = _entityManager.createQuery(stmt, getPersistentClass());
    addParameters(query, parameters);
    return query.setMaxResults(maxResults).getResultList();
  }

  protected List<E> findByQuery(final String stmt, final Map<String, Object> parameters) {
    final TypedQuery<E> query = _entityManager.createQuery(stmt, getPersistentClass());
    addParameters(query, parameters);
    return query.getResultList();
  }

  protected List<E> findByNamedQueryWithNestedOrderBy(final String stmt, final Map<String, Object> parameters) {
    final Query query = _entityManager.createNamedQuery(stmt);
    addParameters(query, parameters);

    return getFirstResultsFromQueryList(query);
  }

  @SuppressWarnings("unchecked")
  protected List<E> getFirstResultsFromQueryList(final Query query) {
    final List<Object[]> list = query.getResultList();

    final List<E> result = new ArrayList<E>();
    for (final Object[] values : list) {
      result.add((E) values[0]);
    }

    return result;
  }

  protected E loadByNamedQuery(final String queryName, final Map<String, Object> parameters) {
    final TypedQuery<E> namedQuery = _entityManager.createNamedQuery(queryName, getPersistentClass());
    addParameters(namedQuery, parameters);
    return namedQuery.getSingleResult();
  }

  protected E findSingleByNamedQuery(final String queryName, final Map<String, Object> parameters) {
    final TypedQuery<E> namedQuery = _entityManager.createNamedQuery(queryName, getPersistentClass());
    addParameters(namedQuery, parameters);
    final List<E> resultList = namedQuery.getResultList();
    return resultList.isEmpty() ? null : resultList.get(0);
  }

  protected List<E> findByNamedQuery(final String queryName, final Map<String, Object> parameters) {
    final TypedQuery<E> query = _entityManager.createNamedQuery(queryName, getPersistentClass());
    addParameters(query, parameters);
    return query.getResultList();
  }

  public List<E> findByNamedQuery(final String queryName, final Map<String, Object> parameters, final int firstResult, final int maxResults) {
    checkMaxResult(maxResults);

    final TypedQuery<E> query = _entityManager.createNamedQuery(queryName, getPersistentClass());
    addParameters(query, parameters).setFirstResult(firstResult);
    return query.setMaxResults(maxResults).getResultList();
  }

  @SuppressWarnings("unchecked")
  protected List<Object> findByNativeQuery(final String stmt, final Map<String, Object> parameters) {
    return addParameters(_entityManager.createNativeQuery(stmt), parameters).getResultList();
  }

  @SuppressWarnings("unchecked")
  protected List<Object> findObjectsByNamedQuery(final String stmt, final Map<String, Object> parameters) {
    return addParameters(_entityManager.createNamedQuery(stmt), parameters).getResultList();
  }

  protected int executeUpdate(final String queryName, final Map<String, Object> parameters) {
    return addParameters(_entityManager.createNamedQuery(queryName), parameters).executeUpdate();
  }

  private void checkMaxResult(final int maxResults) {
    if (maxResults < 1) {
      final String msg = MessageFormat.format("maxResults darf nicht kleiner als 1 sein [{0}]", Integer.valueOf(maxResults));
      throw new IllegalArgumentException(msg);
    }
  }

  private static Query addParameters(final Query stmt, final Map<String, Object> parameters) {
    if (null == parameters) {
      return stmt;
    }
    for (final Map.Entry<String, Object> entry : parameters.entrySet()) {
      stmt.setParameter(entry.getKey(), entry.getValue());
    }
    return stmt;
  }

  public static Map<String, Object> createParameterMap(final Object... params) {
    final Map<String, Object> result = new HashMap<String, Object>();

    for (int i = 0; i < params.length; i += 2) {
      assert i + 1 < params.length : "Not enough parameters " + Arrays.asList(params);

      final Object key = params[i];
      final Object value = params[i + 1];

      assert key != null : "key != null";

      result.put(key.toString(), value);
    }

    return result;
  }
}
