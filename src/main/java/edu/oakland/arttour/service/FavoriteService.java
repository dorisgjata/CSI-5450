package edu.oakland.arttour.service;

import edu.oakland.arttour.dao.FavoriteDAO;
import edu.oakland.arttour.model.Artwork;
import edu.oakland.arttour.model.Tour;
import edu.oakland.arttour.model.Favorite;
import edu.oakland.arttour.model.Creator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

  @Autowired
  private FavoriteDAO dao;

  @Autowired
  private ArtworkService artworkService;

  @Autowired
  private CreatorService creatorService;

  @Autowired
  private TourService tourService;

  public void favoriteArtwork(String email, String artworkId) {
    dao.favoriteArtwork(email, artworkId);
  }

  public void favoriteCreator(String email, String creatorId) {
    dao.favoriteCreator(email, creatorId);
  }

  public void favoriteTour(String email, String tourId) {
    dao.favoriteTour(email, tourId);
  }

  public List<Tour> getFavoriteToursForUser(String email) {
    List<String> favoriteTourIds = dao.getFavoriteTourIds(email);
    List<Tour> tours = tourService.getToursForEmail(email, favoriteTourIds);
    return tours;
  }

  public List<Artwork> getFavoriteArtworksForUser(String email) {
    List<Artwork> favoriteArtworks = new ArrayList<Artwork>();
    List<String> favoriteArtworkIds = dao.getFavoriteArtworkIds(email);
    favoriteArtworkIds.stream()
        .forEach(
            favoriteArtworkId -> {
              Artwork favoriteArtwork = artworkService.getArtwork(favoriteArtworkId);
              favoriteArtworks.add(favoriteArtwork);
            });

    return favoriteArtworks;
  }

  public List<Creator> getFavoriteCreatorsForUser(String email) {
    List<Creator> favoriteCreators = new ArrayList<Creator>();
    List<String> favoriteCreatorIds = dao.getFavoriteCreatorIds(email);
    favoriteCreatorIds.stream()
        .forEach(
            favoriteCreatorId -> {
              Creator favoriteCreator = creatorService.getCreatorById(favoriteCreatorId);
              favoriteCreators.add(favoriteCreator);
            });

    return favoriteCreators;
  }

  public Favorite getFavoritesForUser(String email) {
    List<Artwork> favoriteArtworks = getFavoriteArtworksForUser(email);
    List<Tour> favoriteTours = getFavoriteToursForUser(email);
    List<Creator> favoriteCreators = getFavoriteCreatorsForUser(email);

    Favorite favorites = new Favorite();

    favorites.setFavoriteArtworks(favoriteArtworks);
    favorites.setFavoriteTours(favoriteTours);
    favorites.setFavoriteCreators(favoriteCreators);

    return favorites;
  }

  public void deleteFavoriteArtwork(String email, String artworkId) {
    dao.deleteFavoriteArtwork(email, artworkId);
  }

  public void deleteFavoriteCreator(String email, String creatorId) {
    dao.deleteFavoriteCreator(email, creatorId);
  }

  public void deleteFavoriteTour(String email, String tourId) {
    dao.deleteFavoriteTour(email, tourId);
  }

}
