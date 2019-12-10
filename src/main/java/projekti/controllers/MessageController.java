package projekti.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.services.MessageService;
import projekti.services.ProfileService;

@Controller
public class MessageController {
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private ProfileService profileService;
    
    @PostMapping("/community/{profileAlias}/messages/{id}")
    public String postCommentToMessage(@PathVariable String profileAlias, @PathVariable Long id, @RequestParam String comment, @RequestParam String redirect) {
        messageService.addCommentToMessage(profileAlias, id, comment);
        return "redirect:" + profileService.redirectWithParameters(redirect, profileAlias, id);
    }
}
