package projekti.services;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;
import projekti.entities.FileObject;
import projekti.entities.FileObjectComment;
import projekti.entities.Following;
import projekti.entities.Profile;
import projekti.repositories.FileObjectCommentRepository;
import projekti.repositories.FileObjectRepository;
import projekti.repositories.FollowingRepository;

@Service
public class FileObjectService {
    
    @Autowired
    private FileObjectRepository fileObjectRepository;
    
    @Autowired
    private FileObjectCommentRepository fileObjectCommentRepository;
    
    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private FollowingRepository followingRepository;
    
    public FileObject copyFileObjectForProfile(FileObject fileObject, Profile profile) {
        FileObject newFileObject = new FileObject();
        
        newFileObject.setFilename(fileObject.getFilename());
        newFileObject.setContentType(fileObject.getContentType());
        newFileObject.setContentLength(fileObject.getContentLength());
        newFileObject.setLocalDateTime(LocalDateTime.now());
        
        newFileObject.setDescription(fileObject.getDescription());
        newFileObject.setProfile(profile);
        newFileObject.setContent(fileObject.getContent());
        
        return newFileObject;
    }
    
    public void savePicture(MultipartFile file, @RequestParam String text) {
        try {
            FileObject fo = new FileObject();

            fo.setFilename(file.getOriginalFilename());
            fo.setContentType(file.getContentType());
            fo.setContentLength(file.getSize());
            fo.setLocalDateTime(LocalDateTime.now());
            
            fo.setDescription(text);
            fo.setProfile(profileService.findProfileForCurrentUser());
            
            fo.setContent(file.getBytes());

            fileObjectRepository.save(fo);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    public void getMyPictures(Model model) {
        Profile profileUsedNow = profileService.findProfileForCurrentUser();
        model.addAttribute("profileheader", profileUsedNow.getName() + " - " + profileUsedNow.getAlias());        
        getPicturesWithAlias(model, profileUsedNow.getAlias()); 
    }
    
    public void getPicturesWithAlias(Model model, String alias) {
        Profile profile = profileService.getProfileByAlias(alias);
        
        List<Following> followings = followingRepository.findByFollower(profile);
        List<Profile> profiles = new ArrayList<>();
        for (Following following : followings) {
            profiles.add(following.getFollowed());
        }
        profiles.add(profile);
        Pageable pageable = PageRequest.of(0, 25, Sort.by("localDateTime").descending());
        model.addAttribute("pictures", fileObjectRepository.findByProfileIn(profiles, pageable)); 
    }
        
    public void addCommentToFileObject(String profileAlias, Long id, String comment) {
        FileObject fileObject = fileObjectRepository.getOne(id);   
        FileObjectComment fileObjectComment = new FileObjectComment();
        fileObjectComment.setComment(comment);
        fileObjectComment.setFileobject(fileObject);
        fileObjectComment.setProfile(profileService.findProfileForCurrentUser());
        fileObjectComment.setLocalDateTime(LocalDateTime.now());
        fileObjectCommentRepository.save(fileObjectComment);
    }
}
