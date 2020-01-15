package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entities.Profile;
import project.entities.ProfilePicture;

public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Long> {
    ProfilePicture findByProfile(Profile profile);
}
