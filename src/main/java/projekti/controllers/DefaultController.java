package projekti.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import projekti.services.AccountService;
import projekti.services.ProfileService;

@Controller
public class DefaultController {
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private ProfileService profileService;

    @GetMapping("*")
    public String start(Model model) {
        if (accountService.checkIfLoggedIn()) {
            return "redirect:/profiles";
        }
        return "redirect:/createnewaccount";
    }
    
    @GetMapping("/mywall")
    public String getOwnProfile() {
        return "redirect:/profiles/" + profileService.findProfileForCurrentUser().getAlias();
    }
}
