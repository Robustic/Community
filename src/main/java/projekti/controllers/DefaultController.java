package projekti.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import projekti.InitializeDatabase;
import projekti.services.AccountService;
import projekti.services.ProfileService;

@Controller
public class DefaultController {
    @Autowired
    private InitializeDatabase initializeDatabase;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private ProfileService profileService;

    @GetMapping("*")
    public String start(Model model) {
        initializeDatabase.setToDataBase();
        if (accountService.checkIfLoggedIn()) {
            return "redirect:/profiles";
        }
        return "redirect:/createnewaccount";
    }
    
    @GetMapping("/mywall")
    public String getOwnProfile() {
        return "redirect:/profiles/" + profileService.findProfileForCurrentUser().getAlias();
    }
        
//    @GetMapping("/mymessages")
//    public String getOwnProfileMessages() {
//        return "redirect:/profiles/" + profileService.findProfileForCurrentUser().getAlias() + "/messages";
//    }
//    
//    @GetMapping("/mypictures")
//    public String getOwnProfilePictures() {
//        return "redirect:/profiles/" + profileService.findProfileForCurrentUser().getAlias() + "/pictures";
//    }
//    
//    @GetMapping("/mysettings")
//    public String getOwnProfileSettings() {
//        return "redirect:/profiles/" + profileService.findProfileForCurrentUser().getAlias() + "/settings";
//    }
}
