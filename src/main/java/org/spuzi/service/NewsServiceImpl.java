package org.spuzi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.spuzi.datarepo.NewsRepository;
import org.spuzi.domain.News;

@Service
public class NewsServiceImpl implements NewsService {

	@Autowired
	private NewsRepository newsRepository;

	@Override
	public List<News> listAllNews() {
		List<News> listOfNews = new ArrayList<News>();
		newsRepository.findAll().forEach(listOfNews::add);
		return listOfNews;
	}

	@Override
	public Optional<News> findNews(int id) {
		return newsRepository.findById(id);
	}

}
