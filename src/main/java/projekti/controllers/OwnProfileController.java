package projekti.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.entities.Blocked;
import projekti.entities.Message;
import projekti.entities.Following;
import projekti.entities.Profile;
import projekti.repositories.BlockedRepository;
import projekti.repositories.FollowingRepository;
import projekti.repositories.MessageRepository;
import projekti.repositories.ProfileRepository;
import projekti.services.DataPacketServices;
import projekti.services.ProfileService;

@Controller
public class OwnProfileController {

    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private DataPacketServices dataPacketService;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private FollowingRepository followingRepository;
    
    @Autowired
    private BlockedRepository blockedRepository;

    @GetMapping("/ownprofile")
    public String getOwnProfile(Model model) {
        Profile profileUsedNow = profileService.findProfileForCurrentUser();
        model.addAttribute("ownprofileheader", "Tervetuloa " + profileUsedNow.getName());
        model.addAttribute("ownprofilemessages", messageRepository.findAll());       
        
        model.addAttribute("whoIamFollowing", dataPacketService.convertFollowerListToDataPackets(followingRepository.findByFollower(profileUsedNow)));
        model.addAttribute("whoAreFollowingMe", dataPacketService.convertFollowedListToDataPackets(followingRepository.findByFollowed(profileUsedNow)));
        model.addAttribute("blockeds", dataPacketService.convertBlockedListToDataPackets(blockedRepository.findByBlocker(profileUsedNow)));        
        return "ownprofile";
    }
    
    @PostMapping("/ownprofile")
    public String add(@RequestParam String text) {
        if (text != null && !text.trim().isEmpty()) {
            Message message = new Message();
            message.setText(text.trim());
            Profile profileWhichUsedNow = profileService.findProfileForCurrentUser();
            message.setProfile(profileWhichUsedNow);
            message.setAlias(profileWhichUsedNow.getAlias());

            messageRepository.save(message);
        }

        return "redirect:/ownprofile";
    }

    @PostMapping("/ownprofile/{stopfollowerprofile}/stopfollower")
    public String removeFollower(@PathVariable String stopfollowerprofile) {
        Profile profileToRemoveFromFollowed = profileRepository.findByAlias(stopfollowerprofile);
        Following followingToDelete = followingRepository.findByFollowerAndFollowed(
                profileToRemoveFromFollowed, profileService.findProfileForCurrentUser());
        followingRepository.delete(followingToDelete);
        Blocked blocked = new Blocked();
        blocked.setBlocked(profileToRemoveFromFollowed);
        blocked.setBlocker(profileService.findProfileForCurrentUser());
        blockedRepository.save(blocked);
        return "redirect:/ownprofile";
    }
    
    @PostMapping("/ownprofile/{removeblock}/removeblock")
    public String removeBlock(@PathVariable String removeblock) {
        Profile profileToRemoveBlock = profileRepository.findByAlias(removeblock);
        Blocked blockedToDelete = blockedRepository.findByBlockerAndBlocked(
                profileService.findProfileForCurrentUser(), profileToRemoveBlock);
        blockedRepository.delete(blockedToDelete);
        return "redirect:/ownprofile";
    }
}
