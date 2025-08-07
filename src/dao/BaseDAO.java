package dao;

import java.util.List;

public interface BaseDAO<T, ID> {
	List<T> findAll();

	T findById(ID id);

	T save(T entity);

	T update(T entity);

	void delete(ID id);
}
