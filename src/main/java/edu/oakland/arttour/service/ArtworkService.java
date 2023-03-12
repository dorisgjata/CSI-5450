package edu.oakland.arttour.service;

import edu.oakland.arttour.dao.ArtworkDAO;
import edu.oakland.arttour.model.Artwork;
import edu.oakland.arttour.model.Creator;
import edu.oakland.arttour.model.Location;
import edu.oakland.arttour.util.UniqueIdGenerator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArtworkService {

  @Autowired private ArtworkDAO dao;

  public void addArtwork(Artwork artwork) throws Exception {
    // The dataset comes with ids for the artwork so we could have them input it in
    // the form when they're adding a new one
    if (artwork.getArtworkId() == null) {
      artwork.setArtworkId(UniqueIdGenerator.generateUniqueId("ART-"));
    }
    Location location = artwork.getLocation();
    Creator creator = artwork.getCreator();
    // Check if the location exists
    if (location == null) {
      throw new Exception("Location not found");
    }
    // Check if the creator exists
    if (creator == null) {
      throw new Exception("Creator not found");
    }

    dao.addArtwork(artwork);
    dao.addArtworkAndCreator(artwork.getArtworkId(), creator.getCreatorId());
  }

  public void updateArtwork(Artwork updatedArtwork) throws Exception {
    Artwork currentArtwork = dao.getArtwork(updatedArtwork.getArtworkId());

    Location location = updatedArtwork.getLocation();
    Creator creator = updatedArtwork.getCreator();

    // Check if the artwork exists
    if (currentArtwork == null) {
      throw new Exception("Artwork with ID " + updatedArtwork.getArtworkId() + " not found");
    }
    // Check if the location exists
    if (location == null) {
      throw new Exception("Location not found");
    }
    // Check if the creator exists
    if (creator == null) {
      throw new Exception("Creator not found");
    }

    dao.updateArtwork(updatedArtwork);
  }

  // todo: update offset, add extra params
  public List<Artwork> getCollection(int offset) {
    return dao.getCollection(offset);
  }

  public List<String> getArtworkIds(String tourId) {
    return dao.getArtworkIds(tourId);
  }

  public Artwork getArtwork(String artworkId) {
    return dao.getArtwork(artworkId);
  }

  public void deleteArtwork(String artworkId) {
    dao.deleteArtwork(artworkId);
  }

  // public bolean isCreatorPresent(){
  // Location location = locationService.getLocationById(locationId);
  // Creator creator = creatorService.getCreatorById(creatorId);
  // // todo: check if location and creator exist
  // if (location == null) {
  // // throw exception?
  // return false;
  // }
  // if (creator == null) {
  // return false;
  // }
  // artwork.setCreator(creator);
  // artwork.setLocation(location);
  // }
}
