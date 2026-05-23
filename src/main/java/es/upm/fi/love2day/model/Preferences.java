package es.upm.fi.love2day.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Preferences {
    @Column
    private Integer minAge;

    @Column
    private Integer maxAge;

    @Column
    private Integer maxDistanceKm;

    public Preferences() {}

    private Preferences(Integer minAge, Integer maxAge, Integer maxDistanceKm) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.maxDistanceKm = maxDistanceKm;
    }

    public static Preferences create() {
        return new Preferences(null, null, null);
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public Integer getMaxDistanceKm() {
        return maxDistanceKm;
    }

    public void setMaxDistanceKm(Integer maxDistanceKm) {
        this.maxDistanceKm = maxDistanceKm;
    }
}
