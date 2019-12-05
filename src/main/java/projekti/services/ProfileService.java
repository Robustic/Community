package projekti.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import projekti.entities.Following;
import projekti.entities.Profile;
import projekti.repositories.AccountRepository;
import projekti.repositories.ProfileRepository;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    public Profile findProfileForCurrentUser() {        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String usernameWhichUsedNow = auth.getName();        
        Profile profileUsedNow = profileRepository.findByAccount(accountRepository.findByUsername(usernameWhichUsedNow));
        return profileUsedNow;
    }
    
    public String findAliasForCurrentUser() {
        return findProfileForCurrentUser().getAlias();
    }
}
