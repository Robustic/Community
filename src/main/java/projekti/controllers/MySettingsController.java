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
public class MySettingsController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private BlockedService blockedService;
    
    @Autowired
    private FileObjectRepository fileObjectRepository;    

    @GetMapping("/mysettings")
    public String getMyProfile(Model model) {
        model.addAttribute("currentProfile", profileService.findProfileForCurrentUser());
        profileService.getMySettings(model);        
        return "mysettings";
    }
    
    @PostMapping("/mysettings/newmessage")
    public String addMessage(@RequestParam String text, 
            @RequestParam String redirect, @RequestParam String aliastoredirect, @RequestParam Long redirectid) {
        messageService.addMessage(text);
        return "redirect:" + profileService.redirectWithParameters(redirect, aliastoredirect, redirectid);
    }

    @PostMapping("/mysettings/{setblock}/setblock")
    public String setBlock(@PathVariable String setblock, 
            @RequestParam String redirect, @RequestParam String aliastoredirect, @RequestParam Long redirectid) {
        blockedService.setBlock(setblock);
        return "redirect:" + profileService.redirectWithParameters(redirect, aliastoredirect, redirectid);
    }
    
    @PostMapping("/mysettings/{removeblock}/removeblock")
    public String removeBlock(@PathVariable String removeblock, 
            @RequestParam String redirect, @RequestParam String aliastoredirect, @RequestParam Long redirectid) {
        blockedService.removeBlock(removeblock);
        return "redirect:" + profileService.redirectWithParameters(redirect, aliastoredirect, redirectid);
    }
    
    @PostMapping("/mysettings/files")
    public String save(@RequestParam("file") MultipartFile file) throws IOException {
        FileObject fo = new FileObject();

        fo.setFilename(file.getOriginalFilename());
        fo.setContentType(file.getContentType());
        fo.setContentLength(file.getSize());
        fo.setContent(file.getBytes());

        fileObjectRepository.save(fo);

        return "redirect:" + profileService.redirectWithParameters("mysettings", "", 0L);
    }
}
