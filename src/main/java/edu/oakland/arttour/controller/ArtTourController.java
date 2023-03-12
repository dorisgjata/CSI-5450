package edu.oakland.arttour.controller;

import edu.oakland.arttour.dao.UserDAO;
import edu.oakland.arttour.model.Artwork;
import edu.oakland.arttour.model.Creator;
import edu.oakland.arttour.model.Favorite;
import edu.oakland.arttour.model.Location;
import edu.oakland.arttour.model.Tour;
import edu.oakland.arttour.model.User;
import edu.oakland.arttour.service.*;
import edu.oakland.soffit.auth.AuthService;
import edu.oakland.soffit.auth.SoffitAuthException;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class ArtTourController {

  private final Logger log = LoggerFactory.getLogger("arttour");
  @Autowired
  private ArtworkService artworkService;
  @Autowired
  private CreatorService creatorService;
  @Autowired
  private FavoriteService favoriteService;
  @Autowired
  private LocationService locationService;
  @Autowired
  private TourService tourService;
  @Autowired
  private UserService userService;
  @Autowired
  private UserDAO userDao;
  @Autowired
  private AuthService authorizer;

  ////////// error handling //////////

  @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Invalid JWT")
  @ExceptionHandler(JWTVerificationException.class)
  public void verificationError(Exception e) {
    log.error("Throwing Invalid JWT Error");
    log.error("", e);
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Illegal Arguments given")
  @ExceptionHandler({ IllegalArgumentException.class, DataAccessException.class })
  public void illegalArgumentError(Exception e) {
    log.error("Throwing Illegal Argument or Data Access error", e);
  }

  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Unspecified exception")
  @ExceptionHandler(Exception.class)
  public void generalError(Exception e) {
    log.error("Unspecified exception", e);
  }

  ////////// health check endpoint //////////

  @GetMapping("health-check")
  public boolean healthCheck() {
    return true;
  }

  ////////// general queries/reports //////////

  @CrossOrigin
  @GetMapping("artworks")
  public List<Artwork> getCollection(@RequestParam("offset") int offset) {
    return artworkService.getCollection(offset);
  }

  @CrossOrigin
  @GetMapping("tours")
  public List<Tour> getPublicTours() {
    return tourService.getPublicTours();
  }

  @CrossOrigin
  @GetMapping("locations")
  public List<Location> getAllLocations() {
    return locationService.getAllLocations();
  }

  /////////// general insert/delete //////////

  ///// inserts /////
  @CrossOrigin
  @PostMapping("artworks/creators/addition")
  public void addArtwork(HttpServletRequest request, @RequestBody Artwork artwork)
      throws Exception {
    String email = authorizer.getClaimFromJWT(request, "email").asString();
    userService.checkAdmin(email);
    artworkService.addArtwork(artwork);
  }

  @CrossOrigin
  @PostMapping("locations/addition")
  public void addLocation(HttpServletRequest request, @RequestBody Location location)
      throws SoffitAuthException {
    String email = authorizer.getClaimFromJWT(request, "email").asString();
    userService.checkAdmin(email);
    locationService.addLocation(location);
  }

  ///// updates /////
  @CrossOrigin
  @PostMapping("artworks/update")
  public void updateArtwork(HttpServletRequest request, @RequestBody Artwork artwork)
      throws Exception {
    String email = authorizer.getClaimFromJWT(request, "email").asString();

    userService.checkAdmin(email);
    artworkService.updateArtwork(artwork);
  }

  @CrossOrigin
  @PostMapping("creators/update")
  public void updateCreator(HttpServletRequest request, @RequestBody Creator creator)
      throws Exception {
    String email = authorizer.getClaimFromJWT(request, "email").asString();
    userService.checkAdmin(email);
    creatorService.updateCreator(creator);
  }

  @CrossOrigin
  @PostMapping("locations/update")
  public void updateLocation(HttpServletRequest request, @RequestBody Location location)
      throws Exception {
    String email = authorizer.getClaimFromJWT(request, "email").asString();
    userService.checkAdmin(email);
    locationService.updateLocation(location);
  }

  ///// deletes /////
  @CrossOrigin
  @PostMapping("artworks/{artworkId}/removal")
  public void deleteArtwork(HttpServletRequest request, @PathVariable String artworkId)
      throws SoffitAuthException {
    String email = authorizer.getClaimFromJWT(request, "email").asString();
    userService.checkAdmin(email);
    // todo: check admin
    artworkService.deleteArtwork(artworkId);
  }

  @CrossOrigin
  @PostMapping("creator/{creatorId}/removal")
  public void deleteCreator(HttpServletRequest request, @PathVariable String creatorId)
      throws SoffitAuthException {
    String email = authorizer.getClaimFromJWT(request, "email").asString();
    userService.checkAdmin(email);
    creatorService.deleteCreator(creatorId);
  }

  @CrossOrigin
  @PostMapping("location/{locationId}/removal")
  public void deleteLocation(HttpServletRequest request, @PathVariable String locationId)
      throws SoffitAuthException {
    String email = authorizer.getClaimFromJWT(request, "email").asString();
    userService.checkAdmin(email);
    // todo: check if it's admin
    locationService.deleteLocation(locationId);
  }

  @CrossOrigin
  @PostMapping("tour/{tourId}/removal")
  public void deleteTour(HttpServletRequest request, @PathVariable String tourId)
      throws SoffitAuthException {
    String email = authorizer.getClaimFromJWT(request, "email").asString();
    tourService.deleteTour(tourId);
  }

  ////////// consumer specific actions //////////

  ///// favorites /////
  @CrossOrigin
  @GetMapping("consumers/favorites")
  public Favorite getUserFavorites(HttpServletRequest request) throws SoffitAuthException {
    String email = authorizer.getClaimFromJWT(request, "email").asString();
    return favoriteService.getFavoritesForUser(email);
  }

  @CrossOrigin
  @PostMapping("consumers/favorites/artworks/{artworkId}")
  public void favoriteArtwork(HttpServletRequest request, @PathVariable String artworkId)
      throws SoffitAuthException {
    String email = authorizer.getClaimFromJWT(request, "email").asString();
    favoriteService.favoriteArtwork(email, artworkId);
  }

  @CrossOrigin
  @PostMapping("consumers/favorites/creators/{creatorId}")
  public void favoriteCreator(HttpServletRequest request, @PathVariable String creatorId)
      throws SoffitAuthException {
    String email = authorizer.getClaimFromJWT(request, "email").asString();
    favoriteService.favoriteCreator(email, creatorId);
  }

  @CrossOrigin
  @PostMapping("consumers/favorites/tours/{tourId}")
  public void favoriteTour(HttpServletRequest request, @PathVariable String tourId)
      throws SoffitAuthException {
    String email = authorizer.getClaimFromJWT(request, "email").asString();
    favoriteService.favoriteTour(email, tourId);
  }

  @CrossOrigin
  @PostMapping("consumers/favorites/artworks/{artworkId}/removal")
  public void deleteFavoriteArtwork(HttpServletRequest request, @PathVariable String artworkId)
      throws SoffitAuthException {
    String email = authorizer.getClaimFromJWT(request, "email").asString();
    favoriteService.deleteFavoriteArtwork(email, artworkId);
  }

  @CrossOrigin
  @PostMapping("consumers/favorites/creators/{creatorId}/removal")
  public void deleteFavoriteCreator(HttpServletRequest request, @PathVariable String creatorId)
      throws SoffitAuthException {
    String email = authorizer.getClaimFromJWT(request, "email").asString();
    favoriteService.deleteFavoriteCreator(email, creatorId);
  }

  @CrossOrigin
  @PostMapping("consumers/favorites/tours/{tourId}/removal")
  public void deleteFavoriteTour(HttpServletRequest request, @PathVariable String tourId)
      throws SoffitAuthException {
    String email = authorizer.getClaimFromJWT(request, "email").asString();
    favoriteService.deleteFavoriteTour(email, tourId);
  }

  ///// tours /////
  @CrossOrigin
  @GetMapping("consumers/tours")
  public List<Tour> getToursForUser(HttpServletRequest request) throws SoffitAuthException {
    String email = authorizer.getClaimFromJWT(request, "email").asString();
    return tourService.getToursForUser(email);
  }

  @CrossOrigin
  @PostMapping("consumers/tours")
  public void createTour(HttpServletRequest request, @RequestBody Tour tour)
      throws SoffitAuthException {
    String email = authorizer.getClaimFromJWT(request, "email").asString();
    tourService.createTour(tour);
  }

  @CrossOrigin
  @PostMapping("tours/{tourId}/artworks/{artworkId}/addition")
  public void addToTour(
      HttpServletRequest request, @PathVariable String tourId, @PathVariable String artworkId)
      throws SoffitAuthException {
    String email = authorizer.getClaimFromJWT(request, "email").asString(); // just to check if token is valid
    tourService.addToTour(tourId, artworkId);
  }

  @CrossOrigin
  @PostMapping("tours/{tourId}/artworks/removal")
  public void deleteFromTour(
      HttpServletRequest request, @PathVariable String tourId, @RequestBody List<String> artworkIds)
      throws SoffitAuthException {
    String email = authorizer.getClaimFromJWT(request, "email").asString(); // just to check if token is valid
    artworkIds.stream().forEach(artworkId -> tourService.deleteFromTour(tourId, artworkId));
  }

  @CrossOrigin
  @PostMapping("tours/{tourId}/update")
  public void updateTour(
      HttpServletRequest request, @RequestBody Tour tour, @PathVariable String tourId)
      throws SoffitAuthException {
    String email = authorizer.getClaimFromJWT(request, "email").asString(); // just to check if token is valid
    tourService.updateTour(tour);
  }

  ////////// general user //////////
  @CrossOrigin
  @PostMapping("user/register")
  public Map<String, String> registerUser(@RequestBody Map<String, String> user) {
    return userService.registerUser(user);
  }

  @CrossOrigin
  @GetMapping("user/{email}/")
  public Map<String, String> login(@PathVariable String email) {
    return userService.login(email);
  }

  @CrossOrigin
  @PostMapping("user/{userEmail}/removal")
  public void deleteUser(HttpServletRequest request, @PathVariable String userEmail)
      throws SoffitAuthException {
    String email = authorizer.getClaimFromJWT(request, "email").asString();
    userService.checkAdmin(email);
    userService.deleteUser(userEmail);
  }

  // todo:check why it's using dao here
  @CrossOrigin
  @PostMapping("user")
  public User updateUser(HttpServletRequest request, @RequestBody User userInfo)
      throws SoffitAuthException {
    String email = authorizer.getClaimFromJWT(request, "email").asString();
    userService.updateUser(email, userInfo);
    return userDao.login(email);
  }
}
