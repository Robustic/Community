package projekti.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import projekti.entities.Blocked;
import projekti.entities.Following;
import projekti.entities.Profile;
import projekti.repositories.AccountRepository;
import projekti.repositories.BlockedRepository;
import projekti.repositories.FollowingRepository;
import projekti.repositories.ProfileRepository;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private BlockedRepository blockedRepository;
    
    @Autowired
    private FollowingRepository followingRepository;
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private FileObjectService fileObjectService;
    
    public Profile findProfileForCurrentUser() {        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String usernameWhichUsedNow = auth.getName();        
        Profile profileUsedNow = profileRepository.findByAccount(accountRepository.findByUsername(usernameWhichUsedNow));
        return profileUsedNow;
    }   
    
    public String findAliasForCurrentUser() {
        return findProfileForCurrentUser().getAlias();
    }
        
    public boolean isAliasReserved(String alias) {
        return profileRepository.findByAlias(alias) != null;
    }
    
    public String redirectWithParameters(String redirect, String alias) {
        if (redirect.equals("profiles")) {
            return "/profiles";
        }
        if (redirect.equals("profilewithalias")) {
            return "/profiles/" + alias;
        }
        if (redirect.equals("mysettings")) {
            return "/mysettings";
        }
        if (redirect.equals("mymessages")) {
            return "/mymessages";
        }
        if (redirect.equals("mypictures")) {
            return "/mypictures";
        }
        if (redirect.equals("messageswithalias")) {
            return "/profiles/" + alias + "/messages";
        }
        if (redirect.equals("pictureswithalias")) {
            return "/profiles/" + alias + "/pictures";
        }
        return "/";
    }
    
    public Profile getProfileByAlias(String alias) {
        return profileRepository.findByAlias(alias);
    }
    
    public void getWallContentWithAlias(Model model, String profileAlias) {        
        Profile profile = profileRepository.findByAlias(profileAlias);
        model.addAttribute("profileheader", profile.getName() + " - " + profile.getAlias());
        messageService.getMessagesWithAlias(model, profileAlias);
        fileObjectService.getPicturesWithAlias(model, profileAlias);
    }
    
    public void findProfilesWithString(Model model, String textToFind) {
        Profile currentProfile = findProfileForCurrentUser();
        Pageable pageable = PageRequest.of(0, 25, Sort.by("name").ascending());
        model.addAttribute("findedProfiles", profileRepository.findByNameContainingAndAliasNot(textToFind, currentProfile.getAlias(), pageable));
        List<Following> follewed = currentProfile.getWhoIamFollowing();
        List<Profile> follewedProfiles = new ArrayList<>();
        for (Following following : follewed) {
            follewedProfiles.add(following.getFollowed());
        }
        model.addAttribute("followedProfiles", follewedProfiles);
        List<Blocked> blockeds = blockedRepository.findByBlocked(currentProfile);
        List<Profile> blockerProfiles = new ArrayList<>();
        for (Blocked blocked : blockeds) {
            blockerProfiles.add(blocked.getBlocker());
        }

        model.addAttribute("blockedMe", blockerProfiles);
    }
    
    public void getMySettings(Model model) {
        Profile profileUsedNow = findProfileForCurrentUser();
        model.addAttribute("profileheader", profileUsedNow.getName() + " - " + profileUsedNow.getAlias() + ", Oma profiili");     
        
        model.addAttribute("whoIamFollowing", followingRepository.findByFollower(profileUsedNow));
        model.addAttribute("whoAreFollowingMe", followingRepository.findByFollowed(profileUsedNow));
        model.addAttribute("blockeds", blockedRepository.findByBlocker(profileUsedNow));    
    }
}
