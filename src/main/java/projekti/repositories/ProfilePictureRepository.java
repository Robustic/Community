package projekti.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import projekti.entities.Profile;
import projekti.entities.ProfilePicture;

public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Long> {
    ProfilePicture findByProfile(Profile profile);
}
