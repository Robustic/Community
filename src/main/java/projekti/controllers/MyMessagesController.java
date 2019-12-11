package projekti.controllers;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import projekti.entities.Blocked;
import projekti.entities.FileObject;
import projekti.entities.Profile;
import projekti.repositories.BlockedRepository;
import projekti.repositories.FileObjectRepository;
import projekti.repositories.ProfileRepository;
import projekti.services.BlockedService;
import projekti.services.MessageService;
import projekti.services.ProfileService;

@Controller
public class MyMessagesController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private BlockedService blockedService;
    
    @Autowired
    private FileObjectRepository fileObjectRepository;    

    @GetMapping("/mymessages")
    public String getMyMessages(Model model) {
        model.addAttribute("currentProfile", profileService.findProfileForCurrentUser());
        messageService.getMyMessages(model);        
        return "mymessages";
    }
    
    @PostMapping("/mymessages/newmessage")
    public String addMessage(@RequestParam String text, 
            @RequestParam String redirect, @RequestParam String aliastoredirect, @RequestParam Long redirectid) {
        messageService.addMessage(text);
        return "redirect:" + profileService.redirectWithParameters(redirect, aliastoredirect, redirectid);
    }
}
