package projekti.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import projekti.entities.Following;
import projekti.entities.Profile;

public interface FollowingRepository extends JpaRepository<Following, Long> {
//    Following findByFollowerAliasAndFollowedAlias(String follower, String followed);
    Following findByFollowerAndFollowed(Profile follower, Profile followed);
//    List<Following> findByFollowerAlias(String follower);
//    List<Following> findByFollowedAlias(String followed);
    List<Following> findByFollower(Profile follower);
    List<Following> findByFollowed(Profile followed);
}
