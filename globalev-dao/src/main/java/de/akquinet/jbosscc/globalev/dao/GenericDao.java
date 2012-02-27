package de.akquinet.jbosscc.globalev.dao;

import java.util.List;

import de.akquinet.jbosscc.globalev.model.AbstractEntity;

public interface GenericDao<E extends AbstractEntity> {
  E load(Long id);

  List<E> loadAll();

  void persist(E entity);

  E merge(E entity);

  void delete(E entity);

  boolean exists(Long id);

  boolean contains(final E e);

  Long getVersion(Long id);
}
