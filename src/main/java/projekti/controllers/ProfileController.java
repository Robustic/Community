package projekti.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.services.FileObjectService;
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
    
    @Autowired
    private FileObjectService fileObjectService;   
    
    @GetMapping("/profiles/{profileAlias}")
    public String getWallContentWithAlias(Model model, @PathVariable String profileAlias) {
        model.addAttribute("currentProfile", profileService.findProfileForCurrentUser());
        profileService.getWallContentWithAlias(model, profileAlias);
        return "profile";
    }
    
    @PostMapping("/profiles/{profiletofollow}/tofollow")
    public String addFollowed(@PathVariable String profiletofollow, 
            @RequestParam String redirect, @RequestParam String aliastoredirect) {
        followingService.addFollowed(redirect, profiletofollow);
        return "redirect:" + profileService.redirectWithParameters(redirect, aliastoredirect);
    }

    @PostMapping("/profiles/{profiletoleavefollowing}/leavefollowing")
    public String deleteFollowed(@PathVariable String profiletoleavefollowing, 
            @RequestParam String redirect, @RequestParam String aliastoredirect) {
        followingService.deleteFollowed(redirect, profiletoleavefollowing);
        return "redirect:" + profileService.redirectWithParameters(redirect, aliastoredirect);
    }
    
    @PostMapping("/profiles/{profileAlias}/messages/{id}/comment")
    public String addCommentToMessage(@PathVariable String profileAlias, @PathVariable Long id, @RequestParam String comment, 
            @RequestParam String redirect, @RequestParam String aliastoredirect) {
        messageService.addCommentToMessage(profileAlias, id, comment);
        return "redirect:" + profileService.redirectWithParameters(redirect, aliastoredirect);
    }
    
    @PostMapping("/profiles/{profileAlias}/pictures/{id}/comment")
    public String addCommentToFileObject(@PathVariable String profileAlias, @PathVariable Long id, @RequestParam String comment, 
            @RequestParam String redirect, @RequestParam String aliastoredirect) {
        fileObjectService.addCommentToFileObject(profileAlias, id, comment);
        return "redirect:" + profileService.redirectWithParameters(redirect, aliastoredirect);
    }
}
