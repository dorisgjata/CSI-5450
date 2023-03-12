package edu.oakland.arttour.dao;

import edu.oakland.arttour.model.Location;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LocationDAO {

  @Autowired private JdbcTemplate jdbcTemplate;

  public void addLocation(Location location) {
    jdbcTemplate.update(Constants.ADD_LOCATION, location);
  }

  public void updateLocation(Location location) {
    jdbcTemplate.update(Constants.UPDATE_LOCATION, location);
  }

  public Location getLocationById(String locationId) {
    return jdbcTemplate.queryForObject(Constants.GET_LOCATION, Location.mapper, locationId);
  }

  public void deleteLocation(String locationId) {
    jdbcTemplate.update(Constants.DELETE_LOCATION, locationId);
  }

  public List<Location> getAllLocations() {
    return jdbcTemplate.query(Constants.GET_ALL_LOCATIONS, Location.mapper);
  }
}
