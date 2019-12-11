package projekti.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.entities.Following;
import projekti.entities.Message;
import projekti.entities.MessageComment;
import projekti.entities.Profile;
import projekti.repositories.FollowingRepository;
import projekti.repositories.MessageCommentRepository;
import projekti.repositories.MessageRepository;
import projekti.repositories.ProfileRepository;

@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private MessageCommentRepository messageCommentRepository;
    
    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private ProfileRepository profileRepository;
    
    @Autowired
    private FollowingRepository followingRepository;
    
    public void addCommentToMessage(String profileAlias, Long id, @RequestParam String comment) {        
        Message message = messageRepository.getOne(id);   
        MessageComment messageComment = new MessageComment();
        messageComment.setComment(comment);
        messageComment.setMessage(message);
        messageComment.setProfile(profileService.findProfileForCurrentUser());
        messageComment.setLocalDateTime(LocalDateTime.now());
        messageCommentRepository.save(messageComment);
    }
    
    public void addMessage(String text) {
        if (text != null && !text.trim().isEmpty()) {
            Message message = new Message();
            message.setText(text.trim());
            Profile profileWhichUsedNow = profileService.findProfileForCurrentUser();
            message.setProfile(profileWhichUsedNow);
            message.setLocalDateTime(LocalDateTime.now());
            messageRepository.save(message);
        }
    }
    
    public void getMyMessages(Model model) {
        Profile profileUsedNow = profileService.findProfileForCurrentUser();
        model.addAttribute("profileheader", profileUsedNow.getName() + " - " + profileUsedNow.getAlias() + ", Omat viestit");        
        getMessagesWithAlias(model, profileUsedNow.getAlias()); 
    }
    
    public void getMessagesWithAlias(Model model, String alias) {
        Profile profile = profileRepository.findByAlias(alias);
        
        List<Following> followings = followingRepository.findByFollower(profile);
        List<Profile> profiles = new ArrayList<>();
        for (Following following : followings) {
            profiles.add(following.getFollowed());
        }
        profiles.add(profile);
        Pageable pageable = PageRequest.of(0, 25, Sort.by("localDateTime").descending());
        model.addAttribute("messages", messageRepository.findByProfileIn(profiles, pageable)); 
    }
}
