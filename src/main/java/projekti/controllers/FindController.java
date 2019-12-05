package projekti.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.repositories.FollowingRepository;
import projekti.repositories.ProfileRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import projekti.entities.Blocked;
import projekti.entities.Following;
import projekti.entities.Profile;
import projekti.repositories.BlockedRepository;
import projekti.services.DataPacketServices;
import projekti.services.ProfileService;

@Controller
public class FindController {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private FollowingRepository followingRepository;

    @Autowired
    private BlockedRepository blockedRepository;
    
    @Autowired
    private DataPacketServices dataPacketService;

    String textToFind = "";

    @GetMapping("/find")
    public String find(Model model) {
        Profile currentProfile = profileService.findProfileForCurrentUser();
        Pageable pageable = PageRequest.of(0, 25, Sort.by("name").descending());
        model.addAttribute("findedProfiles", profileRepository.findByNameContainingAndAliasNot(textToFind, currentProfile.getAlias(), pageable));
        List<Following> follewed = currentProfile.getWhoIamFollowing();
        List<Profile> follewedProfiles = new ArrayList<>();
        for (Following following : follewed) {
            follewedProfiles.add(following.getFollowed());
        }
        model.addAttribute("followedProfiles", follewedProfiles);
        List<Blocked> blockeds = blockedRepository.findByBlocked(currentProfile);
        List<Profile> blockerProfiles = new ArrayList<>();
        for (Blocked blocked : blockeds) {
            blockerProfiles.add(blocked.getBlocker());
        }
        
        model.addAttribute("blockedMe", blockerProfiles);
        return "find";
    }

    @PostMapping("/find")
    public String add(@RequestParam String findtext) {
        if (findtext != null) {
            textToFind = findtext.trim();
        }
        return "redirect:/find";
    }

    @PostMapping("/find/{profiletofollow}/tofollow")
    public String addFollowed(@PathVariable String profiletofollow) {
        Profile currentProfile = profileService.findProfileForCurrentUser();
        Profile profileToFollow = profileRepository.findByAlias(profiletofollow);
        if (blockedRepository.findByBlockerAndBlocked(profileToFollow, currentProfile) == null) {
            Following newFollowing = new Following();
            newFollowing.setFollower(currentProfile);
            newFollowing.setFollowed(profileToFollow);
            newFollowing.setLocalDateTime(LocalDateTime.now());
            followingRepository.save(newFollowing);
        }
        return "redirect:/find";
    }

    @PostMapping("/find/{profiletoleavefollowing}/leavefollowing")
    public String deleteFollowed(@PathVariable String profiletoleavefollowing) {
        Profile profileToRemoveFromFollowed = profileRepository.findByAlias(profiletoleavefollowing);
        Following followingToDelete = followingRepository.findByFollowerAndFollowed(
                profileService.findProfileForCurrentUser(), profileToRemoveFromFollowed);
        followingRepository.delete(followingToDelete);
        return "redirect:/find";
    }

}
