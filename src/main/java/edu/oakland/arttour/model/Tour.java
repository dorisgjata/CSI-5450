package edu.oakland.arttour.model;

import java.util.List;

import lombok.Data;

@Data
public class Tour {
  private String email;
  private String tourName;
  private String tourId;
  private List<Artwork> artworks;
}
