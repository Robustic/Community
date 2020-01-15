package project.controllers;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.services.BlockedService;
import project.services.ProfileService;

@Controller
public class MySettingsController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private BlockedService blockedService; 

    @GetMapping("/mysettings")
    public String getMySettings(Model model) {
        model.addAttribute("currentProfile", profileService.findProfileForCurrentUser());
        model.addAttribute("showProfile", profileService.findProfileForCurrentUser());
        profileService.getMySettings(model);        
        return "mysettings";
    }

    @Transactional
    @PostMapping("/mysettings/{setblock}/setblock")
    public String setBlock(@PathVariable String setblock, 
            @RequestParam String redirect, @RequestParam String aliastoredirect) {
        blockedService.setBlock(setblock);
        return "redirect:" + profileService.redirectWithParameters(redirect, aliastoredirect);
    }
    
    @Transactional
    @PostMapping("/mysettings/{removeblock}/removeblock")
    public String removeBlock(@PathVariable String removeblock, 
            @RequestParam String redirect, @RequestParam String aliastoredirect) {
        blockedService.removeBlock(removeblock);
        return "redirect:" + profileService.redirectWithParameters(redirect, aliastoredirect);
    }
}
