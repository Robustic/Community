package projekti.controllers;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.services.MessageService;
import projekti.services.ProfileService;

@Controller
public class MyMessagesController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private MessageService messageService; 

    @GetMapping("/mymessages")
    public String getMyMessages(Model model) {
        model.addAttribute("currentProfile", profileService.findProfileForCurrentUser());
        model.addAttribute("showProfile", profileService.findProfileForCurrentUser());
        messageService.getMyMessages(model);
        return "mymessages";
    }
    
    @Transactional
    @PostMapping("/mymessages/newmessage")
    public String addMessage(@RequestParam String text, 
            @RequestParam String redirect, @RequestParam String aliastoredirect) {
        messageService.addMessage(text);
        return "redirect:" + profileService.redirectWithParameters(redirect, aliastoredirect);
    }
}
