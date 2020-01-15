package project.unittests.services;

import project.services.AccountService;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import project.entities.Account;
import project.entities.Profile;
import project.repositories.AccountRepository;
import project.repositories.ProfileRepository;
import project.services.AccountService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountServicesTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private ProfileRepository profileRepository;
    
    @Test
    public void newUsernameCreatedAndFounded() {
        Account accountBefore = accountRepository.findByUsername("testuser");
        Profile profileBefore = profileRepository.findByAlias("tester");
        assertEquals(null, accountBefore);
        assertEquals(null, profileBefore);
        accountService.createNewAccountIfOk("testuser", "testuserpassword", "Test User", "tester");        
        Account account = accountRepository.findByUsername("testuser");
        assertEquals("testuser", account.getUsername());
        assertNotEquals("testuserpassword", account.getPassword());        
        Profile profile = profileRepository.findByAlias("tester");
        assertEquals("Test User", profile.getName());
        assertEquals("tester", profile.getAlias());
        assertEquals(profile.getId(), account.getProfile().getId());
    }
}
