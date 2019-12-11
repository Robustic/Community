package projekti.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.repositories.FollowingRepository;
import projekti.repositories.ProfileRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import projekti.entities.Blocked;
import projekti.entities.Following;
import projekti.entities.Profile;
import projekti.repositories.BlockedRepository;
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
//            textToFind = "";
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
