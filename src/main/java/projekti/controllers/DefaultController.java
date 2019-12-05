package projekti.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import projekti.InitializeDatabase;

@Controller
public class DefaultController {
    @Autowired
    private InitializeDatabase initializeDatabase;

    @GetMapping("*")
    public String start(Model model) {
//        model.addAttribute("message", "World!");
//        return "index";
        initializeDatabase.setToDataBase();
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !(SecurityContextHolder.getContext().getAuthentication() 
                    instanceof AnonymousAuthenticationToken) ) {
            return "redirect:/ownprofile";
        }
        return "redirect:/maju";
    }
}
