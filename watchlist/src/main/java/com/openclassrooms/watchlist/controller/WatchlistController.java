package com.openclassrooms.watchlist.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.openclassrooms.watchlist.domain.WatchlistItem;
import com.openclassrooms.watchlist.exception.DuplicateTitleException;
import com.openclassrooms.watchlist.exception.ListCompleteException;
import com.openclassrooms.watchlist.service.WatchlistService;

@Controller
public class WatchlistController {

    private WatchlistService watchlistService;
    private final Logger logger = LoggerFactory.getLogger(WatchlistController.class);
	
	@Autowired
	public WatchlistController(WatchlistService watchlistService) {
		super();
		this.watchlistService = watchlistService;
	}

	@GetMapping("/watchlist")
	public ModelAndView getList() {

		logger.info("GET /watchlist called");
		
		String viewName = "watchlist";
		Map<String, Object> model = new HashMap<String, Object>();

		model.put("watchlistItems", watchlistService.getWatchlistItems());
		model.put("numberOfMovies", watchlistService.getWatchlistItemsSize());

		return new ModelAndView(viewName, model);
	}

	@PostMapping("/watchlistItemForm")
	public ModelAndView submitWatchlistItemForm(@Valid WatchlistItem watchlistItem, BindingResult bindingResult) {

		logger.info("POST /watchlistItemForm called");
		
		if (bindingResult.hasErrors()) {
			return new ModelAndView("watchlistItemForm");
		}

		try {
			watchlistService.addOrUpdateWatchlistItem(watchlistItem);
		} catch (DuplicateTitleException e) {
			bindingResult.rejectValue("title", "", "This movie is already on your watchlist");
			return new ModelAndView("watchlistItemForm");
		} catch (ListCompleteException e) {
			bindingResult.rejectValue(null, "", "The list is already complete");
			return new ModelAndView("watchlistItemForm");
		}

		RedirectView redirect = new RedirectView();
		redirect.setUrl("/watchlist");

		return new ModelAndView(redirect);
	}

	@GetMapping("/watchlistItemForm")
	public ModelAndView showWatchlistItemForm(@RequestParam(required = false) Integer id) {
		
		logger.info("GET /watchlistItemForm called");
		
		Map<String, Object> model = new HashMap<String, Object>();

		WatchlistItem watchlistItem = watchlistService.findWatchlistItemById(id);
		if (watchlistItem == null) {
			model.put("watchlistItem", new WatchlistItem());
		} else {
			model.put("watchlistItem", watchlistItem);
		}
		return new ModelAndView("watchlistItemForm", model);
	}

}
