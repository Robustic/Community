package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.entities.FileObject;
import project.entities.Profile;
import project.entities.ProfilePicture;
import project.repositories.FileObjectRepository;
import project.repositories.ProfilePictureRepository;

@Service
public class ProfilePictureService {
    
    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private ProfilePictureRepository profilePictureRepository;
    
    @Autowired
    private FileObjectRepository fileObjectRepository;
    
    public void setProfilePicture(Long picid) {
        Profile currentProfile = profileService.findProfileForCurrentUser();
        ProfilePicture currentProfilePicture = profilePictureRepository.findByProfile(currentProfile);
        if (currentProfilePicture != null) {
            profilePictureRepository.delete(currentProfilePicture);
        }
        ProfilePicture newProfilePicture = new ProfilePicture();
        newProfilePicture.setProfile(currentProfile);
        newProfilePicture.setFileobject(fileObjectRepository.findOneById(picid));
        profilePictureRepository.save(newProfilePicture);
    }
    
    public void setDefaultProfilePictureForProfile(Profile profile) {
        FileObject fileObject = fileObjectRepository.findByProfileIsNullAndFilename("default.png");
        ProfilePicture currentProfilePicture = profilePictureRepository.findByProfile(profile);
        if (currentProfilePicture != null) {
            profilePictureRepository.delete(currentProfilePicture);
        }
        ProfilePicture newProfilePicture = new ProfilePicture();
        newProfilePicture.setProfile(profile);
        newProfilePicture.setFileobject(fileObject);
        profilePictureRepository.save(newProfilePicture);
    }
    
    public FileObject getProfilePictureFileObject() {
        Profile currentProfile = profileService.findProfileForCurrentUser();
        return profilePictureRepository.findByProfile(currentProfile).getFileobject();
    }
    
    public void deleteProfilePicture() {
        Profile currentProfile = profileService.findProfileForCurrentUser();
        ProfilePicture profilePicture = profilePictureRepository.findByProfile(currentProfile);        
        if (profilePicture != null) {
            profilePictureRepository.delete(profilePicture);
        }
        setDefaultProfilePictureForProfile(currentProfile);
    }
}
