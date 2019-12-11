package projekti.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekti.entities.FileObject;
import projekti.entities.Profile;
import projekti.entities.ProfilePicture;
import projekti.repositories.FileObjectRepository;
import projekti.repositories.ProfilePictureRepository;

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
}
