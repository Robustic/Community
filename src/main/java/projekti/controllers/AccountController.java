package projekti.controllers;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projekti.entities.Account;
import projekti.services.ProfileService;
import projekti.services.AccountService;

@Controller
public class AccountController {
    
    @Autowired
    AccountService accountService;

    @GetMapping("/createnewaccount")
    public String index() {
        return "index";
    }

    @PostMapping("/createnewaccount")
    public String createnewaccount(
            @RequestParam String username, 
            @RequestParam String password,
            @RequestParam String name, 
            @RequestParam String alias,
            RedirectAttributes redirectAttributes
//          ,  BindingResult bindingResult
    ) {
        
//        if(bindingResult.hasErrors()) {
//            return "redirect:/createnewaccount";
//        }
        accountService.createNewAccount(username, password, name, alias, redirectAttributes);
        return "redirect:/createnewaccount";        
    }
}
