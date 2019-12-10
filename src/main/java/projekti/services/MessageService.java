package projekti.services;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.entities.Message;
import projekti.entities.MessageComment;
import projekti.repositories.MessageCommentRepository;
import projekti.repositories.MessageRepository;

@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private MessageCommentRepository messageCommentRepository;
    
    public void addCommentToMessage(String profileAlias, Long id, @RequestParam String comment) {
        Message message = messageRepository.getOne(id);   
        MessageComment messageComment = new MessageComment();
        messageComment.setComment(comment);
        messageComment.setMessage(message);
        messageComment.setLocalDateTime(LocalDateTime.now());
        messageCommentRepository.save(messageComment);
    }
}
