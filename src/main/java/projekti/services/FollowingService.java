package projekti.services;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekti.entities.Following;
import projekti.entities.Profile;
import projekti.repositories.BlockedRepository;
import projekti.repositories.FollowingRepository;
import projekti.repositories.ProfileRepository;

@Service
public class FollowingService {
    
    @Autowired
    private ProfileRepository profileRepository;
    
    @Autowired
    private FollowingRepository followingRepository;
    
    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private BlockedRepository blockedRepository;
    
    public void addFollowed(String redirect, String profiletofollow) {
        Profile currentProfile = profileService.findProfileForCurrentUser();
        Profile profileToFollow = profileRepository.findByAlias(profiletofollow);
        if (blockedRepository.findByBlockerAndBlocked(profileToFollow, currentProfile) == null) {
            Following newFollowing = new Following();
            newFollowing.setFollower(currentProfile);
            newFollowing.setFollowed(profileToFollow);
            newFollowing.setLocalDateTime(LocalDateTime.now());
            followingRepository.save(newFollowing);            
        }
    }
    
    public void deleteFollowed(String redirect, String profiletoleavefollowing) {
        Profile profileToRemoveFromFollowed = profileRepository.findByAlias(profiletoleavefollowing);
        Following followingToDelete = followingRepository.findByFollowerAndFollowed(
                profileService.findProfileForCurrentUser(), profileToRemoveFromFollowed);
        followingRepository.delete(followingToDelete);
    }
}
