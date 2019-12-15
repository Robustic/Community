package projekti.controllers;

import static org.fluentlenium.core.filter.FilterConstructor.containingText;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerTest extends org.fluentlenium.adapter.junit.FluentTest {
    
    @LocalServerPort
    private Integer port;
    
    @Test
    public void fillEditDialog() throws InterruptedException {         
        goTo("http://localhost:" + port + "/createnewaccount");

        find("#username").fill().with("rolle");
        find("#password").fill().with("xxxxxxxxxx");
        find("#name").fill().with("Rolle Rol");
        find("#alias").fill().with("relaa");

        find("form").first().submit();
        
        assertTrue(pageSource().contains("Login"));
        
        find("a", containingText("Login - Kirjaudu sisään")).click();
        
//        assertTrue(pageSource().contains("Login"));
        find("#username").fill().with("rolle");
        find("#password").fill().with("xxxxxxxxxx");
        
        find("form").first().submit();
        
        assertTrue(pageSource().contains("Rolle Rol - relaa"));
        assertTrue(pageSource().contains("Etsi syöttämällä kokonaan tai osa haettavasta nimestä."));
    }
    
}
