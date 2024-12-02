package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import rating.Rating;

public class Driver {
   private final Long id;
   private final String name;
   private final String vehicleType;
   private final String licensePlate;
   private String vehicle;
   private boolean available;
   private final List<Integer> ratings;
   private static final int MAX_RATINGS = 10;

   public Driver(final Long id, final String name, final String vehicleType, final String licensePlate) {
      this.id = Objects.requireNonNull(id, "Driver ID cannot be null");
      this.name = Objects.requireNonNull(name, "Driver name cannot be null");
      this.vehicleType = Objects.requireNonNull(vehicleType, "Vehicle type cannot be null");
      this.licensePlate = Objects.requireNonNull(licensePlate, "License plate cannot be null");
      this.ratings = new ArrayList<>();
      this.available = true;
   }

   public Long getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public String getVehicleType() {
      return this.vehicleType;
   }

   public String getLicensePlate() {
      return this.licensePlate;
   }

   public String getVehicle() {
      return this.vehicle;
   }

   public void setVehicle(String vehicle) {
      this.vehicle = vehicle;
   }

   public boolean isAvailable() {
      return this.available;
   }

   public void setAvailable(boolean available) {
      this.available = available;
   }

   public void addRating(Integer rating) {
      if (rating == null || rating < 1 || rating > 5) {
         throw new IllegalArgumentException("Rating must be between 1 and 5");
      }

      if (this.ratings.size() >= MAX_RATINGS) {
         this.ratings.remove(0);
      }

      this.ratings.add(rating);
   }

   public void addRating(Rating rating) {
      if (rating == null) {
         throw new IllegalArgumentException("Rating cannot be null");
      }

      this.addRating(rating.getScore());
   }

   public List<Integer> getRatings() {
      return Collections.unmodifiableList(new ArrayList<>(this.ratings));
   }

   public double getAverageRating() {
      if (this.ratings.isEmpty()) {
         return 0.0;
      }

      return this.ratings.stream()
            .mapToInt(Integer::intValue)
            .average()
            .orElse(0.0);
   }
}
