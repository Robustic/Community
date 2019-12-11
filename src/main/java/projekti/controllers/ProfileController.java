package projekti.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.services.FollowingService;
import projekti.services.MessageService;
import projekti.services.ProfileService;

@Controller
public class ProfileController {
    
    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private FollowingService followingService;
    
    @Autowired
    private MessageService messageService;    
    
    @GetMapping("/profiles/{profileAlias}")
    public String getProfileWithAlias(Model model, @PathVariable String profileAlias) {
        model.addAttribute("currentProfile", profileService.findProfileForCurrentUser());
        profileService.getProfileWithAlias(model, profileAlias);
        return "profile";
    }
    
    @PostMapping("/profiles/{profiletofollow}/tofollow")
    public String addFollowed(@PathVariable String profiletofollow, 
            @RequestParam String redirect, @RequestParam String aliastoredirect, @RequestParam Long redirectid) {
        followingService.addFollowed(redirect, profiletofollow);
        return "redirect:" + profileService.redirectWithParameters(redirect, aliastoredirect, redirectid);
    }

    @PostMapping("/profiles/{profiletoleavefollowing}/leavefollowing")
    public String deleteFollowed(@PathVariable String profiletoleavefollowing, 
            @RequestParam String redirect, @RequestParam String aliastoredirect, @RequestParam Long redirectid) {
        followingService.deleteFollowed(redirect, profiletoleavefollowing);
        return "redirect:" + profileService.redirectWithParameters(redirect, aliastoredirect, redirectid);
    }
    
    @PostMapping("/profiles/{profileAlias}/messages/{id}/comment")
    public String postCommentToMessage(@PathVariable String profileAlias, @PathVariable Long id, @RequestParam String comment, 
            @RequestParam String redirect, @RequestParam String aliastoredirect, @RequestParam Long redirectid) {
        messageService.addCommentToMessage(profileAlias, id, comment);
        System.out.println("redirect:" + redirect);
        System.out.println("aliastoredirect:" + aliastoredirect);
        System.out.println("redirectid:" + redirectid);
        return "redirect:" + profileService.redirectWithParameters(redirect, aliastoredirect, redirectid);
    }
}
