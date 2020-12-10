package com.openclassrooms.watchlist.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.openclassrooms.watchlist.controller.WatchlistController;

@ConditionalOnProperty(name = "app.environment", havingValue = "prod")
@Service
public class MovieRatingServiceImpl implements MovieRatingService {

	String apiUrl = "http://www.omdbapi.com/?apikey=56b585e1&t=";

	private final Logger logger = LoggerFactory.getLogger(MovieRatingServiceImpl.class);

	@Override
	public String getMovieRating(String title) {

		try {
			RestTemplate template = new RestTemplate();

			logger.info("Calling omdbapi with url " + apiUrl + title);

			ResponseEntity<ObjectNode> response = template.getForEntity(apiUrl + title, ObjectNode.class);

			ObjectNode jsonObject = response.getBody();
			
			logger.debug("Omdb api response : "+jsonObject);

			return jsonObject.path("imdbRating").asText();
		} catch (Exception e) {
			logger.warn("Something went wront while calling OMDb API" + e.getMessage());
			return null;
		}
	}

}
