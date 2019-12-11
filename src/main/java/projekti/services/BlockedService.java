package projekti.services;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekti.entities.Blocked;
import projekti.entities.Following;
import projekti.entities.Profile;
import projekti.repositories.BlockedRepository;
import projekti.repositories.FollowingRepository;
import projekti.repositories.ProfileRepository;

@Service
public class BlockedService {
    
    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private FollowingRepository followingRepository;
    
    @Autowired
    private BlockedRepository blockedRepository;
    
    public void setBlock(String setblock) {
        Profile profileToRemoveFromFollowed = profileRepository.findByAlias(setblock);
        Following followingToDelete = followingRepository.findByFollowerAndFollowed(
                profileToRemoveFromFollowed, profileService.findProfileForCurrentUser());
        followingRepository.delete(followingToDelete);
        Blocked blocked = new Blocked();
        blocked.setBlocked(profileToRemoveFromFollowed);
        blocked.setBlocker(profileService.findProfileForCurrentUser());
        blocked.setLocalDateTime(LocalDateTime.now());
        blockedRepository.save(blocked);
    }
    
    public void removeBlock(String removeblock) {
        Profile profileToRemoveBlock = profileRepository.findByAlias(removeblock);
        Blocked blockedToDelete = blockedRepository.findByBlockerAndBlocked(
                profileService.findProfileForCurrentUser(), profileToRemoveBlock);
        blockedRepository.delete(blockedToDelete);
    }
    
    
}
