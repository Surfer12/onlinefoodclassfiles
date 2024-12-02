package rating;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import CustomException.ValidationException;

public class Rating {
    private final Long id;
    private final Long customerId;
    private final Long driverId;
    private final int score;
    private final String comment;
    private final LocalDateTime timestamp;

    private Rating(Builder builder) {
        this.id = builder.id;
        this.customerId = builder.customerId;
        this.driverId = builder.driverId;
        this.score = builder.score;
        this.comment = builder.comment;
        this.timestamp = LocalDateTime.now();
        
        validate();
    }

    public Rating(Long customerId, Long driverId, int score, String comment) {
        this.id = null;
        this.customerId = customerId;
        this.driverId = driverId;
        this.score = score;
        this.comment = comment;
        this.timestamp = LocalDateTime.now();
        
        validate();
    }

    private void validate() {
        List<String> errors = new ArrayList<>();
        
        if (score < 1 || score > 5) {
            errors.add("Rating score must be between 1 and 5");
        }
        if (customerId == null || customerId <= 0) {
            errors.add("Invalid customer ID");
        }
        if (driverId == null || driverId <= 0) {
            errors.add("Invalid driver ID");
        }
        if (comment != null && comment.length() > 500) {
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

        public Builder customerId(Long customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder driverId(Long driverId) {
            this.driverId = driverId;
            return this;
        }

        public Builder score(int score) {
            this.score = score;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public Rating build() {
            return new Rating(this);
        }
    }

    // Getters only - Rating is immutable
    public Long getId() { return id; }
    public Long getCustomerId() { return customerId; }
    public Long getDriverId() { return driverId; }
    public int getScore() { return score; }
    public Optional<String> getComment() { return Optional.ofNullable(comment); }
    public LocalDateTime getTimestamp() { return timestamp; }
}
