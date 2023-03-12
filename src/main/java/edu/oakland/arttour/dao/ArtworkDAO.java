package edu.oakland.arttour.dao;

import edu.oakland.arttour.model.Artwork;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ArtworkDAO {

  @Autowired private JdbcTemplate jdbcTemplate;

  public void addArtwork(Artwork artwork) {
    jdbcTemplate.update(
        Constants.ADD_ARTWORK,
        artwork.getArtworkId(),
        artwork.getTitle(),
        artwork.getCreationDate(),
        artwork.getMedium(),
        artwork.getCreditLine(),
        artwork.getDateAcquired(),
        artwork.getItemWidth(),
        artwork.getItemHeight(),
        artwork.getItemDepth(),
        artwork.getItemDiameter(),
        artwork.getProvenanceText(),
        artwork.getClassification(),
        artwork.getCreator().getCreatorId(),
        artwork.getLocation().getLocationId());
  }

  public void updateArtwork(Artwork artwork) {
    jdbcTemplate.update(
        Constants.UPDATE_ARTWORK,
        artwork.getArtworkId(),
        artwork.getTitle(),
        artwork.getCreationDate(),
        artwork.getMedium(),
        artwork.getCreditLine(),
        artwork.getDateAcquired(),
        artwork.getItemWidth(),
        artwork.getItemHeight(),
        artwork.getItemDepth(),
        artwork.getItemDiameter(),
        artwork.getProvenanceText(),
        artwork.getClassification(),
        artwork.getLocation().getLocationId());
  }

  public void addArtworkAndCreator(String artworkId, String creatorId) {
    jdbcTemplate.update(Constants.ADD_ARTWORK_AND_CREATOR, artworkId, creatorId);
  }

  public List<Artwork> getCollection(int page) throws DataAccessException {
    return jdbcTemplate.query(Constants.GET_COLLECTION, Artwork.mapper, page);
  }

  public List<String> getArtworkIds(String tourId) throws DataAccessException {
    return jdbcTemplate.queryForList(Constants.GET_ARTWORK_IDS, String.class, tourId);
  }

  public Artwork getArtwork(String artworkId) throws EmptyResultDataAccessException {
    return jdbcTemplate.queryForObject(Constants.GET_ARTWORK, Artwork.mapper, artworkId);
  }

  public void deleteArtwork(String artworkId) {
    jdbcTemplate.update(Constants.DELETE_ARTWORK, artworkId);
  }
}
