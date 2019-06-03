package org.spuzi.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.spuzi.datarepo.NewsRepository;
import org.spuzi.domain.News;

/**
 * Reads and Saves the News every X seconds
 */
@Component
public class NewsReader {
	private static final Logger log = LoggerFactory.getLogger(NewsReader.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	// Choose the url and format from the properties, so it is customizable
	@Value("${url-feed}")
	private String feedUrl;
	@Value("${url-feed-format}")
	private String feedFormat;
	// Use the reader necessary depending on the format of the news
	private IReader reader;
	// Repository to persist data in DB.
	@Autowired
	private NewsRepository newsRepository;

	// Use a cron to read the feed a customize period
	@Scheduled(cron = "${news-interval-reader}")
	public void reportCurrentTime() {
		// Choose to read the feed using a XML reader or a Json reader
		reader = feedFormat.equals("xml") ? new XmlReader() : new JsonReader();
		log.info("Reading the feed at {}", dateFormat.format(new Date()));
		// Read the feed
		List<News> news = reader.getNews(feedUrl);
		// Prints the news read for debugging purpose
		news.forEach(pieceOfNews -> {
			// Save new into the database if it doesn't exist already
			if (newsRepository.findByTitle(pieceOfNews.getTitle()).size() == 0) {
				log.info("Saving the piece of news into database");
				newsRepository.save(pieceOfNews);
			}
		});
	}
}
