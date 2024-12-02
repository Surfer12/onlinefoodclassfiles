package model;

public enum OrderStatus {
    SUBMITTED,
    IN_PROGRESS, // Added status
    PROCESSING,
    PREPARING,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED
}
