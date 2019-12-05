package projekti.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.entities.Account;
import projekti.entities.Profile;
import projekti.repositories.AccountRepository;
import projekti.repositories.ProfileRepository;

@Controller
public class AccountController {

//    @GetMapping("/maju")
//    public String account(Model model) {
//        model.addAttribute("message", "World!");
//        return "index";
//    }
    
    @Autowired
    AccountRepository accountRepository;
    
    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    
    String info = "";

    @GetMapping("/maju")
    public String getMaju(Model model) {
        model.addAttribute("info", this.info);
        this.info = "";
        return "index";
    }

    @PostMapping("/maju")
    public String addMaju(@RequestParam String username, @RequestParam String password, @RequestParam String name, @RequestParam String alias) {
        if (accountRepository.findByUsername(username) != null) {
            this.info = "Käyttäjätunnus " + username + " on jo käytössä.";            
            return "redirect:/maju";
        } else if (profileRepository.findByAlias(alias) != null) {
            this.info = "Alias-nimi " + alias + " on jo käytössä.";
            return "redirect:/maju";
        }
        Account newAccount = new Account(username, passwordEncoder.encode(password));        
        accountRepository.save(newAccount);
        
        Profile profile = new Profile(name, alias);
        Account accountFromDataBase = accountRepository.findByUsername(username);
        profile.setAccount(accountFromDataBase);
        profileRepository.save(profile);
        
            this.info = "Käyttäjätunnus " + username + " luotu. Voit nyt kirjautua sisään.";
        return "redirect:/maju";
        
    }
}
