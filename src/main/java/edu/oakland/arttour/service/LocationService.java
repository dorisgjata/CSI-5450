package edu.oakland.arttour.service;

import edu.oakland.arttour.dao.LocationDAO;
import edu.oakland.arttour.model.Location;
import edu.oakland.arttour.util.UniqueIdGenerator;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

  @Autowired
  private LocationDAO dao;

  public void addLocation(Location location) {
    if (location.getLocationId() == null) {
      location.setLocationId(UniqueIdGenerator.generateUniqueId("LOC-"));
    }
    dao.addLocation(location);
  }

  public void updateLocation(Location updatedLocation) throws Exception {
    Location currentLocation = dao.getLocationById(updatedLocation.getLocationId());
    if (currentLocation == null) {
      throw new Exception("Location not found");
    }

    dao.updateLocation(updatedLocation);
  }

  public Location getLocationById(String locationId) {
    return dao.getLocationById(locationId);
  }

  public List<Location> getAllLocations() {
    return dao.getAllLocations();
  }

  public void deleteLocation(String locationId) {
    dao.deleteLocation(locationId);
  }

}
