package projekti.controllers;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projekti.entities.Profile;
import projekti.services.AccountService;
import projekti.services.ProfileService;

@Controller
public class AccountController {
    
    @Autowired
    AccountService accountService;
    
    @Autowired
    ProfileService profileService;

    @GetMapping("/createnewaccount")
    public String index(Model model) {
        Profile currentProfile = profileService.findProfileForCurrentUser();
        if (currentProfile != null) {
            model.addAttribute("currentProfile", profileService.findProfileForCurrentUser());
        } else {
            String noLoggedIn = "(Et ole kirjautunut vielä sisään)";
            model.addAttribute("noprofile", noLoggedIn);
        }
        return "index";
    }

    @Transactional
    @PostMapping("/createnewaccount")
    public String createnewaccount(
            @RequestParam String username, 
            @RequestParam String password,
            @RequestParam String name, 
            @RequestParam String alias,
            RedirectAttributes redirectAttributes
    ) {
        accountService.createNewAccount(username, password, name, alias, redirectAttributes);
        return "redirect:/createnewaccount";        
    }
}
