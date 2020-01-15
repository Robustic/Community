package project.controllers;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.entities.Profile;
import project.services.ProfileService;


@Controller
public class ProfilesController {

    @Autowired
    private ProfileService profileService;

    @Transactional
    @GetMapping("/profiles")
    public String find(Model model, @ModelAttribute("oldTextToFind") String oldTextToFind) {
        model.addAttribute("currentProfile", profileService.findProfileForCurrentUser());
        model.addAttribute("showProfile", profileService.findProfileForCurrentUser());
        Profile profile = profileService.findProfileForCurrentUser();
        model.addAttribute("profileheader", profile.getName() + " - " + profile.getAlias());
        if (!oldTextToFind.equals("")) {
            profileService.findProfilesWithString(model, oldTextToFind);
        }
        return "profiles";
    }

    @Transactional
    @PostMapping("/profiles")
    public String findtext(@RequestParam String findtext, RedirectAttributes redirectAttributes) {
        if (findtext != null) {
            String textToFind = findtext.trim();
            redirectAttributes.addFlashAttribute("oldTextToFind", textToFind);
        }
        return "redirect:/profiles";
    }
}
