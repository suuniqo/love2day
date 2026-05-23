package es.upm.fi.love2day.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private Gender orientation;

    @Embedded
    private Location location;

    @Column
    private LocalDate birthDate;

    @Column
    private String bio;

    @Embedded
    private Preferences preferences;

	// necesario para JPA
	public Profile() {}

    private Profile(
        Long userId,
        String displayName,
        Gender gender,
        Gender orientation,
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
    
    public Profile create(
        Long userId,
        String displayName,
        Gender gender,
        Gender orientation,
        Location location
    ) {
        return new Profile(
            userId,
            displayName,
            gender,
            orientation,
            location,
            null,
            null,
            Preferences.create()
        );
    }
}
