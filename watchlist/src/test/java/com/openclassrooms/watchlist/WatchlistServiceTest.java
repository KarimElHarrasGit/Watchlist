package com.openclassrooms.watchlist;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.openclassrooms.watchlist.domain.WatchlistItem;
import com.openclassrooms.watchlist.exception.DuplicateTitleException;
import com.openclassrooms.watchlist.exception.ListCompleteException;
import com.openclassrooms.watchlist.repository.WatchlistRepository;
import com.openclassrooms.watchlist.service.MovieRatingService;
import com.openclassrooms.watchlist.service.WatchlistService;

@RunWith(MockitoJUnitRunner.class)
public class WatchlistServiceTest {

	@Mock
	private WatchlistRepository watchlistRepositoryMock;

	@Mock
	private MovieRatingService movieRatingServiceMock;

	@InjectMocks
	private WatchlistService watchlistService;

//	@Test
	public void testGetWatchlistItemsReturnsAllItemsFromRepository() {

		// Arrange
		WatchlistItem item1 = new WatchlistItem("Star Wars", "7.7", "M", "");
		WatchlistItem item2 = new WatchlistItem("Star Treck", "8.8", "M", "");
		List<WatchlistItem> mockItems = Arrays.asList(item1, item2);

		when(watchlistRepositoryMock.getList()).thenReturn(mockItems);

		// Act
		List<WatchlistItem> result = watchlistService.getWatchlistItems();

		// Assert
		assertTrue(result.size() == 2);
		assertTrue(result.get(0).getTitle().equals("Star Wars"));
		assertTrue(result.get(1).getTitle().equals("Star Treck"));
	}

//	@Test
	public void testGetwatchlistItemsRatingFormOmdbServiceOverrideTheValueInItems() {

		// Arrange
		WatchlistItem item1 = new WatchlistItem("Star Wars", "7.7", "M", "");
		List<WatchlistItem> mockItems = Arrays.asList(item1);

		when(watchlistRepositoryMock.getList()).thenReturn(mockItems);
		when(movieRatingServiceMock.getMovieRating(any(String.class))).thenReturn("10");

		// Act
		List<WatchlistItem> result = watchlistService.getWatchlistItems();

		// Assert
		assertTrue(result.get(0).getRating().equals("10"));
	}

//	@Test
	public void testGetWatchlistItemsSize() {
		// Arrange
		WatchlistItem item1 = new WatchlistItem("Star Wars", "7.7", "M", "");
		WatchlistItem item2 = new WatchlistItem("Star Treck", "8.8", "M", "");
		List<WatchlistItem> mockItems = Arrays.asList(item1, item2);

		when(watchlistRepositoryMock.getList()).thenReturn(mockItems);

		// Act
		int result = watchlistService.getWatchlistItemsSize();

		// Assert
		assertTrue(result == 2);
	}

//	@Test
	public void testFindWatchlistItemById() {
		// Arrange
		WatchlistItem item1 = new WatchlistItem("Star Wars", "7.7", "M", "");

		when(watchlistRepositoryMock.findById(0)).thenReturn(item1);

		// Act
		WatchlistItem result = watchlistService.findWatchlistItemById(0);

		// Assert
		assertTrue(result.getId().equals(item1.getId()));
		assertTrue(result.getPriority().equals(item1.getPriority()));
		assertTrue(result.getRating().equals(item1.getRating()));
		assertTrue(result.getTitle().equals(item1.getTitle()));
	}

	@Test
	public void testDuplicateTitleException() {
		// Arrange
		WatchlistItem item1 = new WatchlistItem("Star Wars", "7.7", "M", "");
		WatchlistItem item2 = new WatchlistItem("Star Wars", "8.7", "H", "");
		when(watchlistRepositoryMock.findById(1)).thenReturn(null);
		when(watchlistRepositoryMock.findByTitle("Star Wars")).thenReturn(item1);

		// Act
		try {
			watchlistService.addOrUpdateWatchlistItem(item2);
		} catch (Exception e) {
			// Assert
			assertTrue(e instanceof DuplicateTitleException);
		}
	}
	
	@Test
	public void testListCompleteException() {
		// Arrange
		WatchlistItem item1 = new WatchlistItem("Star Wars", "7.7", "M", "");
//		when(watchlistRepositoryMock.findById(0)).thenReturn(null);
		when(watchlistRepositoryMock.findByTitle("Star Wars")).thenReturn(null);
		when(watchlistRepositoryMock.isListComplete()).thenReturn(true);

		// Act
		try {
			watchlistService.addOrUpdateWatchlistItem(item1);
		} catch (Exception e) {
			// Assert
			assertTrue(e instanceof ListCompleteException);
		}
	}

//	@Test
	public void testUpdateWatchlistItem() {

		// Arrange
		WatchlistItem item1 = new WatchlistItem("Star Wars", "7.7", "M", "");
		watchlistRepositoryMock.addItem(item1);
		WatchlistItem item2 = new WatchlistItem("Star Wars", "8.7", "H", "");
		item2.setId(0);
		when(watchlistRepositoryMock.findById(0)).thenReturn(item1);

		// Act
		try {
			watchlistService.addOrUpdateWatchlistItem(item2);
		} catch (DuplicateTitleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ListCompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		WatchlistItem result = watchlistRepositoryMock.findById(0);

		// Assert
		assertTrue(result.getId().equals(item2.getId()));
		assertTrue(result.getPriority().equals(item2.getPriority()));
		assertTrue(result.getRating().equals(item2.getRating()));
		assertTrue(result.getTitle().equals(item2.getTitle()));
	}
}