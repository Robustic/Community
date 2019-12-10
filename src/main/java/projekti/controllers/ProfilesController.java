package projekti.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.entities.Following;
import projekti.entities.Message;
import projekti.entities.MessageComment;
import projekti.entities.Profile;
import projekti.repositories.FollowingRepository;
import projekti.repositories.MessageCommentRepository;
import projekti.repositories.MessageRepository;
import projekti.repositories.ProfileRepository;
import projekti.services.FollowingService;
import projekti.services.ProfileService;

@Controller
public class ProfilesController {
    
    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private FollowingService followingService;
    
    @GetMapping("/mywall")
    public String getOwnProfile() {
        return "redirect:/profiles/" + profileService.findProfileForCurrentUser().getAlias();
    }
    
    @GetMapping("/profiles/{profileAlias}")
    public String getProfileWithAlias(Model model, @PathVariable String profileAlias) {
        model.addAttribute("currentProfile", profileService.findProfileForCurrentUser());
        profileService.getProfileWithAlias(model, profileAlias);
        return "profile";
    }
    
    @PostMapping("/profiles/{profiletofollow}/tofollow")
    public String addFollowed(@PathVariable String profiletofollow, @RequestParam String redirect) {
        followingService.addFollowed(redirect, profiletofollow);
        return "redirect:" + profileService.redirectWithParameters(redirect, profiletofollow, -1L);
    }

    @PostMapping("/profiles/{profiletoleavefollowing}/leavefollowing")
    public String deleteFollowed(@PathVariable String profiletoleavefollowing, @RequestParam String redirect) {
        followingService.deleteFollowed(redirect, profiletoleavefollowing);
        return "redirect:" + profileService.redirectWithParameters(redirect, profiletoleavefollowing, -1L);
    }
}
