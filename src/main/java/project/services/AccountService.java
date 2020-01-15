package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.entities.Account;
import project.entities.Profile;
import project.repositories.AccountRepository;
import project.repositories.ProfileRepository;

@Service
public class AccountService {
    
    @Autowired
    AccountRepository accountRepository;
    
    @Autowired
    ProfileRepository profileRepository;
    
    @Autowired
    ProfileService profileService;
    
    @Autowired
    private ProfilePictureService profilePictureService;

    @Autowired
    PasswordEncoder passwordEncoder;
    
    public boolean checkIfLoggedIn() {
        return (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !(SecurityContextHolder.getContext().getAuthentication() 
                    instanceof AnonymousAuthenticationToken));
    }
    
    public void createNewAccount(String username, String password, String name, String alias, RedirectAttributes redirectAttributes) {
        
        if (username.length() < 4 || password.length() < 10 || name.length() < 1 || alias.length() < 1) {
            if (username.length() < 4) {
                redirectAttributes.addFlashAttribute("messageusername", "Käyttäjätunnuksen täytyy olla vähintään 4 merkkiä pitkä.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            }
            if (password.length() < 10) {
                redirectAttributes.addFlashAttribute("messagepassword", "Salasanan täytyy olla vähintään 10 merkkiä pitkä.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            }
            if (name.length() < 1) {
                redirectAttributes.addFlashAttribute("messagename", "Nimi ei saa olla tyhjä.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            }
            if (alias.length() < 1) {
                redirectAttributes.addFlashAttribute("messagealias", "Alias ei saa olla tyhjä.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            }   
            return;
        }
        
        if (isUsernameReserved(username) || profileService.isAliasReserved(alias)) {
            if (isUsernameReserved(username)) {
                redirectAttributes.addFlashAttribute("messageusername", "Käyttäjätunnus " + username + " on jo käytössä.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            }
            if (profileService.isAliasReserved(alias)) {
                redirectAttributes.addFlashAttribute("messagealias", "Aliasnimi " + alias + " on jo käytössä.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            }
            return;
        }        
        
        redirectAttributes.addFlashAttribute("messagesuccess", "Käyttäjätunnus " + username + " luotu. Voit nyt kirjautua sisään.");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success"); 
        createNewAccountIfOk(username, password, name, alias);
    }
    
    public void createNewAccountIfOk(String username, String password, String name, String alias) {
        Account newAccount = new Account(username, passwordEncoder.encode(password));        
        accountRepository.save(newAccount);
        
        Profile profile = new Profile(name, alias);
        Account accountFromDataBase = accountRepository.findByUsername(username);
        profile.setAccount(accountFromDataBase);
        profile = profileRepository.save(profile);
        profilePictureService.setDefaultProfilePictureForProfile(profile);
    }
    
    public boolean isUsernameReserved(String username) {
        return accountRepository.findByUsername(username) != null;
    }
}
