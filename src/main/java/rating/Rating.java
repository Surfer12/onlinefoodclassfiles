package rating;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import CustomException.ValidationException;
import model.Driver;

public class Rating {
    private final Long id;
    private final Long customerId;
    private final Long driverId;
    private final int score;
    private final String comment;
    private final LocalDateTime timestamp;

    private Rating(final Builder builder) {
        this.id = builder.id;
        this.customerId = builder.customerId;
        this.driverId = builder.driverId;
        this.score = builder.score;
        this.comment = builder.comment;
        this.timestamp = LocalDateTime.now();

        this.validate();
    }

    public Rating(final Long customerId, final Long driverId, final int score, final String comment) {
        this.id = generateId();
        this.customerId = customerId;
        this.driverId = driverId;
        this.score = score;
        this.comment = comment;
        this.timestamp = LocalDateTime.now();

        this.validate();
    }

    public long generateId() {
        return ((long) (Math.random() * 10000));
    }

    private void validate() {
        final List<String> errors = new ArrayList<>();

        if (this.score < 1 || this.score > 5) {
            errors.add("Rating score must be between 1 and 5");
        }
        if (this.customerId == null || this.customerId <= 0) {
            errors.add("Invalid customer ID");
        }
        if (this.driverId == null || this.driverId <= 0) {
            errors.add("Invalid driver ID");
        }
        if (this.comment != null && this.comment.length() > 500) {
            errors.add("Comment cannot exceed 500 characters");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException("Rating validation failed: " +
                    String.join(", ", errors));
        }
    }

    public static class Builder {
        private Long id;
        private Long customerId;
        private Long driverId;
        private int score;
        private String comment;

        public Builder customerId(final Long customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder driverId(final Long driverId) {
            this.driverId = driverId;
            return this;
        }

        public Builder score(final int score) {
            this.score = score;
            return this;
        }

        public Builder comment(final String comment) {
            this.comment = comment;
            return this;
        }

        public Rating build() {
            return new Rating(this);
        }
    }

    // Getters only - Rating is immutable
    public Long getId() {
        return this.id;
    }

    public Long getCustomerId() {
        return this.customerId;
    }

    public Long getDriverId() {
        return this.driverId;
    }

    public int getScore() {
        return this.score;
    }

    public Optional<String> getComment() {
        return Optional.ofNullable(this.comment);
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    // New methods to handle driver ratings and ensure only 10 ratings are stored at
    // a time
    public static void addDriverRating(final Driver driver, final Rating rating) {
        if (driver != null && rating != null) {
            driver.addRating(rating);
            Rating.ensureMaxRatings(driver);
        } else {
            throw new IllegalArgumentException("Invalid driver or rating");
        }
    }

    public static void ensureMaxRatings(final Driver driver) {
        if (driver != null) {
            while (driver.getRatings().size() > 10) {
                driver.getRatings().remove(0);
            }
        } else {
            throw new IllegalArgumentException("Invalid driver");
        }
    }
}
