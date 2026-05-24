package es.upm.fi.love2day.dto;

import java.time.LocalDate;

import es.upm.fi.love2day.model.Gender;
import es.upm.fi.love2day.model.Location;
import es.upm.fi.love2day.model.Orientation;

public record UpdateProfileRequest(
    String displayName,
    Gender gender,
    Orientation orientation,
    Location location,
    LocalDate birthDate,
    String bio,
    Integer minAge,
    Integer maxAge,
    Integer maxDistanceKm
) {}
