package es.upm.fi.love2day.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;

import java.time.LocalDate;

@Entity
@Table(name = "Profiles")  
public class Profile {
    @Id
    @Column(nullable = false, unique = true)
	private Long userId;

    @Column
    private String displayName;

    @Column
    private Gender gender;

    @Column
    private Orientation orientation;

    @Embedded
    private Location location;

    @Column
    private LocalDate birthDate;

    @Column
    private String bio;

    @Embedded
    private Preferences preferences;

	// necesario para JPA
	public Profile() { }

    @PostLoad
    private void postLoad() {
        if (this.preferences == null) {
            this.preferences = Preferences.create();
        }
    }

    private Profile(
        Long userId,
        String displayName,
        Gender gender,
        Orientation orientation,
        Location location,
        LocalDate birthDate,
        String bio,
        Preferences preferences
    ) {
        this.userId = userId;
        this.displayName = displayName;
        this.gender = gender;
        this.orientation = orientation;
        this.birthDate = birthDate;
        this.bio = bio;
        this.location = location;
        this.preferences = preferences;
    }
    
    public static Profile create(Long userId) {
        return new Profile(
            userId,
            null,
            null,
            null,
            null,
            null,
            null,
            Preferences.create()
        );
    }

    public Long getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setMinAge(Integer minAge) {
        this.preferences.setMinAge(minAge);
    }

    public void setMaxAge(Integer maxAge) {
        this.preferences.setMaxAge(maxAge);
    }

    public void setMaxDistanceKm(Integer maxDistanceKm) {
        this.preferences.setMaxDistanceKm(maxDistanceKm);
    }
}
