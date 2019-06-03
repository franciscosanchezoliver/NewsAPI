package org.spuzi.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.spuzi.domain.News;
import org.spuzi.service.NewsService;

@Controller
public class NewsController {

	@Autowired
	NewsService newsService;

	/**
	 * Return a list with all the news.
	 * 
	 * @return 200 OK. List of news. <br>
	 *         204 No Content. If there aren't any news.
	 */
	@RequestMapping(value = "/news", method = RequestMethod.GET)
	public ResponseEntity<List<News>> listAllNews() {
		List<News> allNews = newsService.listAllNews();
		HttpStatus httpStatus = allNews.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		return new ResponseEntity<List<News>>(allNews, httpStatus);
	}
	/**
	 * Return one piece of news.
	 * 
	 * @return 200 OK.	 		Info of the piece of news. <br>
	 *         204 No Content. 	If the piece of news doesn't exist.
	 */
	@RequestMapping(value = "/news/{id}", method = RequestMethod.GET)
	public ResponseEntity<News> getNews(@PathVariable("id") int id) {
		Optional<News> pieceOfNews = newsService.findNews(id);
		return pieceOfNews.isPresent() ? new ResponseEntity<News>(pieceOfNews.get(), HttpStatus.OK) : new ResponseEntity<News>(HttpStatus.NO_CONTENT);
	}

}
