package projekti.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
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
import projekti.entities.Message;
import projekti.entities.Following;
import projekti.entities.Profile;
import projekti.repositories.BlockedRepository;
import projekti.repositories.FileObjectRepository;
import projekti.repositories.FollowingRepository;
import projekti.repositories.MessageRepository;
import projekti.repositories.ProfileRepository;
import projekti.services.ProfileService;

@Controller
public class MyProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private FollowingRepository followingRepository;
    
    @Autowired
    private BlockedRepository blockedRepository;
    
    @Autowired
    private FileObjectRepository fileObjectRepository;
    
    

    @GetMapping("/myprofile")
    public String getOwnProfile(Model model) {
        Profile profileUsedNow = profileService.findProfileForCurrentUser();
        model.addAttribute("profileheader", profileUsedNow.getName() + " - " + profileUsedNow.getAlias() + ", Oma profiili");
        model.addAttribute("messages", messageRepository.findAll());       
        
        model.addAttribute("whoIamFollowing", followingRepository.findByFollower(profileUsedNow));
        model.addAttribute("whoAreFollowingMe", followingRepository.findByFollowed(profileUsedNow));
        model.addAttribute("blockeds", blockedRepository.findByBlocker(profileUsedNow));    
        
        return "myprofile";
    }
    
    @PostMapping("/myprofile")
    public String add(@RequestParam String text) {
        if (text != null && !text.trim().isEmpty()) {
            Message message = new Message();
            message.setText(text.trim());
            Profile profileWhichUsedNow = profileService.findProfileForCurrentUser();
            message.setProfile(profileWhichUsedNow);
            message.setLocalDateTime(LocalDateTime.now());
            messageRepository.save(message);
        }

        return "redirect:/myprofile";
    }

    @PostMapping("/myprofile/{stopfollowerprofile}/stopfollower")
    public String removeFollower(@PathVariable String stopfollowerprofile) {
        Profile profileToRemoveFromFollowed = profileRepository.findByAlias(stopfollowerprofile);
        Following followingToDelete = followingRepository.findByFollowerAndFollowed(
                profileToRemoveFromFollowed, profileService.findProfileForCurrentUser());
        followingRepository.delete(followingToDelete);
        Blocked blocked = new Blocked();
        blocked.setBlocked(profileToRemoveFromFollowed);
        blocked.setBlocker(profileService.findProfileForCurrentUser());
        blocked.setLocalDateTime(LocalDateTime.now());
        blockedRepository.save(blocked);
        return "redirect:/myprofile";
    }
    
    @PostMapping("/myprofile/{removeblock}/removeblock")
    public String removeBlock(@PathVariable String removeblock) {
        Profile profileToRemoveBlock = profileRepository.findByAlias(removeblock);
        Blocked blockedToDelete = blockedRepository.findByBlockerAndBlocked(
                profileService.findProfileForCurrentUser(), profileToRemoveBlock);
        blockedRepository.delete(blockedToDelete);
        return "redirect:/myprofile";
    }
    
    @PostMapping("/myprofile/files")
    public String save(@RequestParam("file") MultipartFile file) throws IOException {
        FileObject fo = new FileObject();

        fo.setFilename(file.getOriginalFilename());
        fo.setContentType(file.getContentType());
        fo.setContentLength(file.getSize());
        fo.setContent(file.getBytes());

        fileObjectRepository.save(fo);

        return "redirect:/files";
    }
}
