package projekti.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.entities.Following;
import projekti.entities.Message;
import projekti.entities.MessageComment;
import projekti.entities.Profile;
import projekti.repositories.FollowingRepository;
import projekti.repositories.MessageCommentRepository;
import projekti.repositories.MessageRepository;
import projekti.repositories.ProfileRepository;
import projekti.services.ProfileService;

@Controller
public class ProfilesController {
    
    @Autowired
    private ProfileRepository profileRepository;
    
    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private FollowingRepository followingRepository;
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private MessageCommentRepository messageCommentRepository;
    
    @GetMapping("/profiles/{profileAlias}")
    public String getProfile(Model model, @PathVariable String profileAlias) {        
        Profile profile = profileRepository.findByAlias(profileAlias);    
        model.addAttribute("profileheader", profile.getName() + " - " + profileAlias + ", Seinä");
        
        List<Following> followings = followingRepository.findByFollower(profile);
        List<Profile> profiles = new ArrayList<>();
        for (Following following : followings) {
            profiles.add(following.getFollowed());
        }
        profiles.add(profile);
        Pageable pageable = PageRequest.of(0, 25, Sort.by("localDateTime").descending());
        model.addAttribute("messages", messageRepository.findByProfileIn(profiles, pageable));
        return "profile";
    }
    
    @PostMapping("/profiles/{profileAlias}/messages/{id}")
    public String postCommentToMessage(@PathVariable String profileAlias, @PathVariable Long id, @RequestParam String comment) {
        Message message = messageRepository.getOne(id);   
        MessageComment messageComment = new MessageComment();
        messageComment.setComment(comment);
        messageComment.setMessage(message);
        messageComment.setLocalDateTime(LocalDateTime.now());
        messageCommentRepository.save(messageComment);
        return "redirect:/profiles/" + profileAlias;
    } 
}
