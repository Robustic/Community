package projekti.controllers;

import java.time.LocalDateTime;
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
import projekti.services.ProfileService;

@Controller
public class MyProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private FollowingRepository followingRepository;
    
    @Autowired
    private BlockedRepository blockedRepository;
    
    @GetMapping("/myprofile/mywall")
    public String getOwnProfile() {  
        Profile profileUsedNow = profileService.findProfileForCurrentUser();
        return "redirect:/profiles/" +  profileUsedNow.getAlias();
    }

    @GetMapping("/myprofile")
    public String getOwnProfile(Model model) {
        Profile profileUsedNow = profileService.findProfileForCurrentUser();
        model.addAttribute("profileheader", profileUsedNow.getName() + " - " + profileUsedNow.getAlias() + ", Oma profiili");
        model.addAttribute("messages", messageRepository.findAll());       
        
        model.addAttribute("whoIamFollowing", followingRepository.findByFollower(profileUsedNow));
        model.addAttribute("whoAreFollowingMe", followingRepository.findByFollowed(profileUsedNow));
        model.addAttribute("blockeds", blockedRepository.findByBlocker(profileUsedNow));    
        
        return "myprofile";
    }
    
    @PostMapping("/myprofile")
    public String add(@RequestParam String text) {
        if (text != null && !text.trim().isEmpty()) {
            Message message = new Message();
            message.setText(text.trim());
            Profile profileWhichUsedNow = profileService.findProfileForCurrentUser();
            message.setProfile(profileWhichUsedNow);
            message.setLocalDateTime(LocalDateTime.now());
            messageRepository.save(message);
        }

        return "redirect:/myprofile";
    }

    @PostMapping("/myprofile/{stopfollowerprofile}/stopfollower")
    public String removeFollower(@PathVariable String stopfollowerprofile) {
        Profile profileToRemoveFromFollowed = profileRepository.findByAlias(stopfollowerprofile);
        Following followingToDelete = followingRepository.findByFollowerAndFollowed(
                profileToRemoveFromFollowed, profileService.findProfileForCurrentUser());
        followingRepository.delete(followingToDelete);
        Blocked blocked = new Blocked();
        blocked.setBlocked(profileToRemoveFromFollowed);
        blocked.setBlocker(profileService.findProfileForCurrentUser());
        blocked.setLocalDateTime(LocalDateTime.now());
        blockedRepository.save(blocked);
        return "redirect:/myprofile";
    }
    
    @PostMapping("/myprofile/{removeblock}/removeblock")
    public String removeBlock(@PathVariable String removeblock) {
        Profile profileToRemoveBlock = profileRepository.findByAlias(removeblock);
        Blocked blockedToDelete = blockedRepository.findByBlockerAndBlocked(
                profileService.findProfileForCurrentUser(), profileToRemoveBlock);
        blockedRepository.delete(blockedToDelete);
        return "redirect:/myprofile";
    }
}
