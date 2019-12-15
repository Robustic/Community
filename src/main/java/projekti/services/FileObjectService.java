package projekti.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import projekti.entities.FileObjectLike;
import projekti.entities.Following;
import projekti.entities.Profile;
import projekti.repositories.FileObjectCommentRepository;
import projekti.repositories.FileObjectLikeRepository;
import projekti.repositories.FileObjectRepository;
import projekti.repositories.FollowingRepository;

@Service
public class FileObjectService {

    @Autowired
    private FileObjectRepository fileObjectRepository;

    @Autowired
    private FileObjectCommentRepository fileObjectCommentRepository;

    @Autowired
    private FileObjectLikeRepository fileObjectLikeRepository;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfilePictureService profilePictureService;

    @Autowired
    private FollowingRepository followingRepository;
    
    @Autowired
    Environment enviroment;

    public void readDefaultFile() {
        FileObject fileObject = fileObjectRepository.findByProfileIsNullAndFilename("default.png");
        if (fileObject == null) {
//            String filename = "default.png";
            String filename = "";
            String[] profiles = enviroment.getActiveProfiles();            
            if (profiles.length > 0 && profiles[0].equals("application")) {
                filename = "default.png";
            } else if (profiles.length > 0 && profiles[0].equals("application-test")) {
                filename = "default.png";
            } else if (profiles.length > 0 && profiles[0].equals("application-production")) {
                filename = "src/main/resources/default.png";
            }
            Long size = 1693L;
            String contentType = "image/png";
            String description = "default";
            try {
                FileObject fo = new FileObject();
                fo.setFilename(filename);
                fo.setContentType(contentType);
                fo.setContentLength(size);
                fo.setDescription(description);

                Path path = Paths.get(getClass().getClassLoader().getResource(filename).toURI());
                fo.setContent(Files.readAllBytes(path));
                fileObjectRepository.save(fo);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

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

    public int countPicturesForCurrentUser() {
        Profile currentProfile = profileService.findProfileForCurrentUser();
        List<FileObject> fileObjects = fileObjectRepository.findByProfile(currentProfile);
        return fileObjects.size();
    }

    public void savePicture(MultipartFile file, @RequestParam String text) {
        if (countPicturesForCurrentUser() < 10) {
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
    }

    public void getMyPictures(Model model) {
        Profile profileUsedNow = profileService.findProfileForCurrentUser();
        model.addAttribute("profileheader", profileUsedNow.getName() + " - " + profileUsedNow.getAlias());
        getPicturesWithAlias(model, profileUsedNow.getAlias());
    }

    public void addLikeToFileObject(String profileAlias, Long id) {
        FileObject fileObject = fileObjectRepository.getOne(id);
        FileObjectLike fileObjectLike = new FileObjectLike();
        fileObjectLike.setFileObject(fileObject);
        fileObjectLike.setProfile(profileService.findProfileForCurrentUser());
        fileObjectLikeRepository.save(fileObjectLike);
    }

    public void getPicturesWithAlias(Model model, String alias) {
        Profile profile = profileService.getProfileByAlias(alias);
        Profile profileUsedNow = profileService.findProfileForCurrentUser();
        List<Following> followings = followingRepository.findByFollower(profile);
        List<Profile> profiles = new ArrayList<>();
        for (Following following : followings) {
            profiles.add(following.getFollowed());
        }
        profiles.add(profile);
        Pageable pageable = PageRequest.of(0, 25, Sort.by("localDateTime").descending());
        model.addAttribute("pictures", fileObjectRepository.findByProfileIn(profiles, pageable));
        List<FileObject> fileObjects = new ArrayList<>();
        for (FileObjectLike fileObjectLike : fileObjectLikeRepository.findByProfile(profileUsedNow)) {
            fileObjects.add(fileObjectLike.getFileObject());
        }
        model.addAttribute("picturesforpersonlikes", fileObjects);
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

    public boolean isPictureCurrentUserPicture(Long picid) {
        Profile currentProfile = profileService.findProfileForCurrentUser();
        if (fileObjectRepository.getOne(picid).getProfile() == currentProfile) {
            return true;
        }
        return false;
    }

    public void deleteCommentsForPicture(Long picid) {
        FileObject fileObject = fileObjectRepository.getOne(picid);
        List<FileObjectComment> fileObjectComments = fileObjectCommentRepository.findByFileobject(fileObject);
        for (FileObjectComment fileObjectComment : fileObjectComments) {
            fileObjectCommentRepository.delete(fileObjectComment);
        }
    }

    public void deleteLikesForPicture(Long picid) {
        FileObject fileObject = fileObjectRepository.getOne(picid);
        List<FileObjectLike> fileObjectLikes = fileObjectLikeRepository.findByFileObject(fileObject);
        for (FileObjectLike fileObjectLike : fileObjectLikes) {
            fileObjectLikeRepository.delete(fileObjectLike);
        }
    }

    public boolean pictureReallyExists(Long picid) {
        FileObject fileObject = fileObjectRepository.getOne(picid);
        if (fileObject != null) {
            return true;
        }
        return false;
    }

    public void deletePictureReally(Long picid) {
        if (pictureReallyExists(picid) == false) {
            return;
        }
        if (isPictureCurrentUserPicture(picid) == false) {
            return;
        }
        FileObject profilePictureFileObject = profilePictureService.getProfilePictureFileObject();
        if (profilePictureFileObject.getId() == picid) {
            profilePictureService.deleteProfilePicture();
        }
        deleteCommentsForPicture(picid);
        deleteLikesForPicture(picid);
        FileObject fileObject = fileObjectRepository.getOne(picid);
        fileObjectRepository.delete(fileObject);
    }
}
