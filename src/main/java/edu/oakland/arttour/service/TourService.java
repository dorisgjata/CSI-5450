package edu.oakland.arttour.service;

import edu.oakland.arttour.dao.TourDAO;
import edu.oakland.arttour.model.Tour;
import edu.oakland.arttour.model.Artwork;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.oakland.arttour.util.UniqueIdGenerator;

@Service
public class TourService {

    @Autowired
    private TourDAO dao;

    @Autowired
    private UserService userService;

    @Autowired
    private ArtworkService artworkService;

    public void createTour(Tour tour) {
        tour.setTourId(UniqueIdGenerator.generateUniqueId("TUR-"));
        dao.createTour(tour);
    }

    public void addToTour(String tourId, String artworkId) {
        dao.addToTour(tourId, artworkId);
    }

    public List<Tour> getToursForUser(String email) {
        List<String> tourIds = dao.getTourIds(email);
        List<Tour> tours = getToursForEmail(email, tourIds);
        return tours;
    }

    public List<Tour> getPublicTours() {
        List<String> adminEmails = userService.getAdminEmails();
        List<Tour> publicTours = new ArrayList<Tour>();
        adminEmails.stream()
                .forEach(
                        email -> {
                            List<String> tourIds = dao.getTourIds(email);
                            List<Tour> tours = getToursForEmail(email, tourIds);

                            tours.stream()
                                    .forEach(
                                            tour -> {
                                                publicTours.add(tour);
                                            });
                        });

        return publicTours;
    }

    public List<Tour> getToursForEmail(String email, List<String> tourIds) {
        List<Tour> tours = new ArrayList<Tour>();

        tourIds.stream()
                .forEach(
                        tourId -> {
                            Tour tour = new Tour();
                            tour.setTourId(tourId);
                            tour.setEmail(dao.getTourEmail(tourId));
                            tour.setTourName(dao.getTourName(tourId));
                            List<Artwork> artworks = new ArrayList<Artwork>();
                            List<String> artworkIds = artworkService.getArtworkIds(tourId);
                            artworkIds.stream()
                                    .forEach(
                                            artworkId -> {
                                                Artwork artwork = artworkService.getArtwork(artworkId);
                                                artworks.add(artwork);
                                            });

                            tour.setArtworks(artworks);
                            tours.add(tour);
                        });
        return tours;
    }

    public void deleteFromTour(String tourId, String artworkId) {
        dao.deleteFromTour(tourId, artworkId);
    }

    public void updateTour(Tour updatedTour) {

        // todo: trying to update name and artworks at the same time
        // Tour currentTour = dao.updateTour(updatedTour);
        // Artwork currentArtwork = dao.getArtwork(updatedArtwork.getArtworkId());

        dao.updateTour(updatedTour);

    }

    public void deleteTour(String tourId) {
        dao.deleteTour(tourId);
    }

}
