package project.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import project.entities.Following;
import project.entities.Profile;

public interface FollowingRepository extends JpaRepository<Following, Long> {
    Following findByFollowerAndFollowed(Profile follower, Profile followed);
    List<Following> findByFollower(Profile follower);
    List<Following> findByFollowed(Profile followed);
}
