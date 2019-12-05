package projekti.controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.entities.Message;
import projekti.entities.Account;
import projekti.entities.Profile;
import projekti.repositories.AccountRepository;
import projekti.repositories.FollowingRepository;
import projekti.repositories.MessageRepository;
import projekti.repositories.ProfileRepository;
import projekti.services.ProfileService;

@Controller
public class OwnProfileController {
    
    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private ProfileRepository profileRepository;
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private FollowingRepository followingRepository;
        
    @GetMapping("/ownprofile")
    public String getOwnProfile(Model model) {
        Profile profileUsedNow = profileService.findProfileForCurrentUser();
        model.addAttribute("ownprofileheader", "Tervetuloa " + profileUsedNow.getName());
        model.addAttribute("ownprofilemessages", messageRepository.findAll());
        model.addAttribute("whoIamFollowing", followingRepository.findByFollowerAlias(profileUsedNow.getAlias()));
        model.addAttribute("whoAreFollowingMe", followingRepository.findByFollowedAlias(profileUsedNow.getAlias()));
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
}
