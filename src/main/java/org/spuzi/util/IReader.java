package org.spuzi.util;

import java.util.List;

import org.spuzi.domain.News;

public interface IReader {
	/**
	 * Return all the news from a feed
	 */
	List<News> getNews(String feedUrl);
}
