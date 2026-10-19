package com.hotel.model;

/**
 * Immutable reservation record (max 5 attributes).
 */
public record ScenarioEntity(int id,
                             String guestName,
                             String roomType,
                             double nightlyRate,
                             String payment) {
}
