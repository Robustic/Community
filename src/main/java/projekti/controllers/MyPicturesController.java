package projekti.controllers;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projekti.entities.FileObject;
import projekti.entities.Profile;
import projekti.repositories.FileObjectRepository;
import projekti.services.FileObjectService;
import projekti.services.ProfilePictureService;
import projekti.services.ProfileService;

@Controller
public class MyPicturesController {
    
    @Autowired
    private FileObjectService fileObjectService;
    
    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private ProfilePictureService profilePictureService;
    
    @Autowired
    private FileObjectRepository fileObjectRepository;  
    
    @GetMapping("/mypictures")
    public String getMyPictures(Model model) {
        model.addAttribute("currentProfile", profileService.findProfileForCurrentUser());
        model.addAttribute("showProfile", profileService.findProfileForCurrentUser());
        profileService.getProfilesWhoIamFollowing(model);
        fileObjectService.getMyPictures(model);
        return "mypictures";
    }
    
    @Transactional
    @PostMapping("/mypictures/picture")
    public String savePicture(@RequestParam("file") MultipartFile file, @RequestParam String text) {
        fileObjectService.savePicture(file, text);
        return "redirect:/mypictures";
    }    
        
    @Transactional
    @PostMapping("/profiles/{alias}/setprofilepicture/{picid}")
    public String setProfilePicture(@PathVariable String alias, @PathVariable Long picid,
            @RequestParam String redirect, @RequestParam String aliastoredirect) {
        profilePictureService.setProfilePicture(picid);
        return "redirect:" + profileService.redirectWithParameters(redirect, aliastoredirect);
    }
    
    @Transactional
    @PostMapping("/profiles/{alias}/deletepicture/{picid}")
    public String deletePicture(@PathVariable String alias, @PathVariable Long picid,
            @RequestParam String redirect, @RequestParam String aliastoredirect, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("deletepicture", "Haluatko varmasti poistaa tämän kuvan?");
        redirectAttributes.addFlashAttribute("deletepictureAlertClass", "alert-danger");
        redirectAttributes.addFlashAttribute("deletepictureid", picid);
        return "redirect:" + profileService.redirectWithParameters(redirect, aliastoredirect);
    }
    
    @Transactional
    @PostMapping("/profiles/{alias}/deletepicturereally/{picid}")
    public String deletePictureReally(@PathVariable String alias, @PathVariable Long picid,
            @RequestParam String redirect, @RequestParam String aliastoredirect) {
        fileObjectService.deletePictureReally(picid);
        return "redirect:" + profileService.redirectWithParameters(redirect, aliastoredirect);
    }
    
    @GetMapping("/profiles/{alias}/pictures/{pictureid}")
    public ResponseEntity<byte[]> viewFileWithId(@PathVariable String alias, @PathVariable Long pictureid) {
        Profile profile = profileService.getProfileByAlias(alias);        
        FileObject fileObject = fileObjectRepository.getOne(pictureid);
        if (fileObject == null) {
            fileObject = fileObjectRepository.findByProfileIsNullAndFilename("default.png");
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(fileObject.getContentType()));
        headers.setContentLength(fileObject.getContentLength());
        headers.add("Content-Disposition", "attachment; filename=" + fileObject.getFilename());
        return new ResponseEntity<>(fileObject.getContent(), headers, HttpStatus.CREATED);        
    }
    
    @Transactional
    @PostMapping("/profiles/{alias}/commentpicture/{id}")
    public String addCommentToMessage(@PathVariable String alias, @PathVariable Long id, @RequestParam String comment, 
            @RequestParam String redirect, @RequestParam String aliastoredirect) {
        fileObjectService.addCommentToFileObject(alias, id, comment);
        return "redirect:" + profileService.redirectWithParameters(redirect, aliastoredirect);
    }
}
