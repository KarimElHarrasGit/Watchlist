package com.openclassrooms.watchlist.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@ConditionalOnProperty(name = "app.environment", havingValue = "dev")
@Service
public class MovieRatingServiceDummyImpl implements MovieRatingService {

	@Override
	public String getMovieRating(String title) {
		
		return "9.99";
	}
}