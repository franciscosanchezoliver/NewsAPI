package org.spuzi.service;

import java.util.List;
import java.util.Optional;

import org.spuzi.domain.News;


public interface NewsService {
	public List<News> listAllNews();
	public Optional<News> findNews(int id);
}
