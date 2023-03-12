package edu.oakland.arttour.dao;

import edu.oakland.arttour.model.Tour;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TourDAO {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public void createTour(Tour tour) {
    jdbcTemplate.update(Constants.CREATE_TOUR, tour.getTourId(), tour.getTourName(), tour.getEmail());
  }

  public void addToTour(String tourId, String artworkId) {
    jdbcTemplate.update(Constants.ADD_TO_TOUR, tourId, artworkId);
  }

  public String getTourName(String tourId) {
    return jdbcTemplate.queryForObject(Constants.GET_TOUR_NAME, String.class, tourId);
  }

  public String getTourEmail(String tourId) {
    return jdbcTemplate.queryForObject(Constants.GET_TOUR_EMAIL, String.class, tourId);
  }

  public List<String> getTourIds(String email) throws DataAccessException {
    return jdbcTemplate.queryForList(Constants.GET_TOUR_IDS, String.class, email);
  }

  public void deleteTour(String tourId) {
    jdbcTemplate.update(Constants.DELETE_TOUR, tourId);
  }

  public void deleteFromTour(String tourId, String artworkId) {
    jdbcTemplate.update(Constants.DELETE_FROM_TOUR, tourId, artworkId);
  }

  public void updateTour(Tour tour) {
    jdbcTemplate.update(Constants.UPDATE_TOUR, tour.getTourId(), tour.getTourName());
  }

}
