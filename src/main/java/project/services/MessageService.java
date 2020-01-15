package project.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import project.entities.Following;
import project.entities.Message;
import project.entities.MessageComment;
import project.entities.MessageLike;
import project.entities.Profile;
import project.repositories.FollowingRepository;
import project.repositories.MessageCommentRepository;
import project.repositories.MessageLikeRepository;
import project.repositories.MessageRepository;
import project.repositories.ProfileRepository;

@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private MessageCommentRepository messageCommentRepository;
    
    @Autowired
    private MessageLikeRepository messageLikeRepository;
    
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
    
    public void addLikeToMessage(String profileAlias, Long id) {  
        System.out.println("profileAlias:" + profileAlias);
        System.out.println("id:" + id);
        Message message = messageRepository.getOne(id);   
        MessageLike messageLike = new MessageLike();
        messageLike.setMessage(message);
        messageLike.setProfile(profileService.findProfileForCurrentUser());
        messageLikeRepository.save(messageLike);
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
        model.addAttribute("profileheader", profileUsedNow.getName() + " - " + profileUsedNow.getAlias());        
        getMessagesWithAlias(model, profileUsedNow.getAlias()); 
    }
    
    public void getMessagesWithAlias(Model model, String alias) {
        Profile profile = profileRepository.findByAlias(alias);
        Profile profileUsedNow = profileService.findProfileForCurrentUser();
        List<Following> followings = followingRepository.findByFollower(profile);
        List<Profile> profiles = new ArrayList<>();
        for (Following following : followings) {
            profiles.add(following.getFollowed());
        }
        profiles.add(profile);
        Pageable pageable = PageRequest.of(0, 25, Sort.by("localDateTime").descending());
        Page<Message> messages = messageRepository.findByProfileIn(profiles, pageable);
        model.addAttribute("messages", messages);
        
        pageable = PageRequest.of(0, 10, Sort.by("localDateTime").descending());
        for (Message message : messages) {
            List<MessageComment> messageComments = messageCommentRepository.findByMessage(message, pageable);
            message.setMessageComment(messageComments);
        }
        
        List<Message> messagesForLikes = new ArrayList<>();
        for (MessageLike messageLike : messageLikeRepository.findByProfile(profileUsedNow)) {
            messagesForLikes.add(messageLike.getMessage());
        }
        model.addAttribute("messagesforpersonlikes", messagesForLikes);
    }
}
