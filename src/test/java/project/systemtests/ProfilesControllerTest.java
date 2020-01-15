package project.systemtests;

import static org.fluentlenium.core.filter.FilterConstructor.containingText;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProfilesControllerTest extends org.fluentlenium.adapter.junit.FluentTest {

    @LocalServerPort
    private Integer port;

    private static boolean setUpIsDone = false;

    public void setUp() throws InterruptedException {
        if (setUpIsDone) {
            return;
        }
        goTo("http://localhost:" + port);

        find("#username").fill().with("user1");
        find("#password").fill().with("yyyyyyyyyy");
        find("#name").fill().with("User One");
        find("#alias").fill().with("oner");

        find("form").first().submit();

        assertTrue(pageSource().contains("Käyttäjätunnus " + "user1" + " luotu. Voit nyt kirjautua sisään."));
        
        find("#username").fill().with("user2");
        find("#password").fill().with("yyyyyyyyy2");
        find("#name").fill().with("User2");
        find("#alias").fill().with("second");

        find("form").first().submit();

        assertTrue(pageSource().contains("Käyttäjätunnus " + "user2" + " luotu. Voit nyt kirjautua sisään."));
        
        setUpIsDone = true;
    }

    @Before
    public void before() throws InterruptedException {
        setUp();
    }

    @Test
    public void logInAsUser1AndFindOtherUsers() throws InterruptedException {
        goTo("http://localhost:" + port + "/createnewaccount");

        assertTrue(pageSource().contains("Login"));

        find("a", containingText("Login - Kirjaudu sisään")).click();

        find("#username").fill().with("user1");
        find("#password").fill().with("yyyyyyyyyy");

        find("form").first().submit();

        assertTrue(pageSource().contains("User One - oner"));
        assertTrue(pageSource().contains("Etsi syöttämällä kokonaan tai osa haettavasta nimestä."));
        
        assertFalse(pageSource().contains("User2"));
        
        find("#findtext").fill().with("us");
        
        find("form").first().submit();
        
        assertTrue(pageSource().contains("User2"));
    }
    
    @Test
    public void logInAsUser1AndFindOtherUsersWithEmptyString() throws InterruptedException {
        goTo("http://localhost:" + port + "/login");

        find("#username").fill().with("user1");
        find("#password").fill().with("yyyyyyyyyy");

        find("form").first().submit();

        assertTrue(pageSource().contains("User One - oner"));
        assertTrue(pageSource().contains("Etsi syöttämällä kokonaan tai osa haettavasta nimestä."));
        
        assertFalse(pageSource().contains("User2"));
        
        find("form").first().submit();
        
        assertFalse(pageSource().contains("User2"));
    }
    
}
