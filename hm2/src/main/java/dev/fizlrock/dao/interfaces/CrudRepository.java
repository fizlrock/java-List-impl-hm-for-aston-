package dev.fizlrock.dao.interfaces;

import java.util.List;
import java.util.Optional;

/**
 * 
 */

public interface CrudRepository<A, B> {

  public List<A> findAll();

  public Optional<A> findById(B id);

  public boolean deleteById(B id);

  public boolean updateById(B id, A entity);

  public A save(A entity);
}
