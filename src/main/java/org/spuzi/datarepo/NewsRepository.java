package org.spuzi.datarepo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.spuzi.domain.News;

// CRUD operations
public interface NewsRepository extends CrudRepository<News, Integer> {
	List<News> findByTitle(String title);
}
