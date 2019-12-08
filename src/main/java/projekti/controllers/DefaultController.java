package projekti.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import projekti.InitializeDatabase;
import projekti.services.AccountService;

@Controller
public class DefaultController {
    @Autowired
    private InitializeDatabase initializeDatabase;
    
    @Autowired
    AccountService accountService;

    @GetMapping("*")
    public String start(Model model) {
        initializeDatabase.setToDataBase();
        if (accountService.checkIfLoggedIn()) {
            return "redirect:/ownprofile";
        }
        return "redirect:/createnewaccount";
    }
}
