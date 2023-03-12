package edu.oakland.arttour.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FavoriteDAO {

  @Autowired private JdbcTemplate jdbcTemplate;

  public void favoriteArtwork(String email, String artworkId) {
    jdbcTemplate.update(Constants.FAVORITE_ARTWORK, email, artworkId);
  }

  public void favoriteCreator(String email, String creatorId) {}

  public void favoriteTour(String email, String tourId) {
    jdbcTemplate.update(Constants.FAVORITE_TOUR, email, tourId);
  }

  public List<String> getFavoriteTourIds(String email) throws DataAccessException {
    return jdbcTemplate.queryForList(Constants.GET_FAVORITE_TOUR_IDS, String.class, email);
  }

  public List<String> getFavoriteArtworkIds(String email) throws DataAccessException {
    return jdbcTemplate.queryForList(Constants.GET_FAVORITE_ARTWORK_IDS, String.class, email);
  }

  public List<String> getFavoriteCreatorIds(String email) throws DataAccessException {
    return jdbcTemplate.queryForList(Constants.GET_FAVORITE_CREATOR_IDS, String.class, email);
  }

  public void deleteFavoriteArtwork(String email, String artworkId) {
    jdbcTemplate.update(Constants.DELETE_FAVORITE_ARTWORK, email, artworkId);
  }

  public void deleteFavoriteCreator(String email, String creatorId) {
    jdbcTemplate.update(Constants.DELETE_FAVORITE_CREATOR, email, creatorId);
  }

  public void deleteFavoriteTour(String email, String tourId) {
    jdbcTemplate.update(Constants.DELETE_FAVORITE_TOUR, email, tourId);
  }
}
