package projekti.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.services.ProfileService;

@Controller
public class ProfilesController {

    @Autowired
    private ProfileService profileService;

    String textToFind = "";

    @GetMapping("/profiles")
    public String find(Model model) {
        model.addAttribute("currentProfile", profileService.findProfileForCurrentUser());
        if (!textToFind.equals("")) {
            profileService.findProfilesWithString(model, textToFind);
            model.addAttribute("oldText", textToFind);
            textToFind = "";
        }
        return "profiles";
    }

    @PostMapping("/profiles")
    public String add(@RequestParam String findtext) {
        if (findtext != null) {
            textToFind = findtext.trim();
        }
        return "redirect:/profiles";
    }
}
