package es.upm.fi.love2day.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Location {
    @Column
    private double latitude;

    @Column
    private double longitude;

    @Column
    private String city;

    public Location() {}

    private Location(double latitude, double longitude, String city) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
    }

    public static Location create(double latitude, double longitude, String city) {
        return new Location(latitude, longitude, city);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCity() {
        return city;
    }
}
