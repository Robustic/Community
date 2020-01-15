package project.systemtests;

import static org.fluentlenium.core.filter.FilterConstructor.containingText;
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
public class AccountControllerTest extends org.fluentlenium.adapter.junit.FluentTest {

    @LocalServerPort
    private Integer port;

    private static boolean setUpIsDone = false;

    public void setUp() throws InterruptedException {
        if (setUpIsDone) {
            return;
        }
        goTo("http://localhost:" + port + "/createnewaccount");

        find("#username").fill().with("rolle");
        find("#password").fill().with("xxxxxxxxxx");
        find("#name").fill().with("Rolle Rol");
        find("#alias").fill().with("relaa");

        find("form").first().submit();

        assertTrue(pageSource().contains("Käyttäjätunnus " + "rolle" + " luotu. Voit nyt kirjautua sisään."));
        
        setUpIsDone = true;
    }

    @Before
    public void before() throws InterruptedException {
        setUp();
    }

    @Test
    public void logInWithCorrectAccount() throws InterruptedException {
        goTo("http://localhost:" + port + "/createnewaccount");

        assertTrue(pageSource().contains("Login"));

        find("a", containingText("Login - Kirjaudu sisään")).click();

        find("#username").fill().with("rolle");
        find("#password").fill().with("xxxxxxxxxx");

        find("form").first().submit();

        assertTrue(pageSource().contains("Rolle Rol - relaa"));
        assertTrue(pageSource().contains("Etsi syöttämällä kokonaan tai osa haettavasta nimestä."));
        
        goTo("http://localhost:" + port + "/createnewaccount");
        
        assertTrue(pageSource().contains("Communityn käyttö vaatii rekisteröitymisen."));
        assertTrue(pageSource().contains("Olet jo kirjautunut sisään."));
        
        assertTrue(pageSource().contains("Rolle Rol"));
        assertTrue(pageSource().contains("relaa"));
    }
    
    @Test
    public void logInWithAccountNotExists() throws InterruptedException {
        goTo("http://localhost:" + port + "/createnewaccount");

        assertTrue(pageSource().contains("Login"));

        find("a", containingText("Login - Kirjaudu sisään")).click();

        find("#username").fill().with("accountnotexists");
        find("#password").fill().with("xxxxxxxxxx");

        find("form").first().submit();

        assertTrue(pageSource().contains("Bad credentials"));
        assertTrue(pageSource().contains("Please sign in"));
    }
    
    @Test
    public void logInWithUncorrectPassword() throws InterruptedException {
        goTo("http://localhost:" + port + "/createnewaccount");

        assertTrue(pageSource().contains("Login"));

        find("a", containingText("Login - Kirjaudu sisään")).click();

        find("#username").fill().with("rolle");
        find("#password").fill().with("xxxxxxxxyx");

        find("form").first().submit();

        assertTrue(pageSource().contains("Bad credentials"));
        assertTrue(pageSource().contains("Please sign in"));
    }

    @Test
    public void tryToCreateAccountWhereUsernameAlreadyExists() throws InterruptedException {
        goTo("http://localhost:" + port + "/createnewaccount");

        find("#username").fill().with("rolle");
        find("#password").fill().with("xxxxxxxxxx");
        find("#name").fill().with("Roltsu");
        find("#alias").fill().with("relaa");

        find("form").first().submit();

        assertTrue(pageSource().contains("Käyttäjätunnus " + "rolle" + " on jo käytössä."));
    }

    @Test
    public void tryToCreateAccountWhereAliasAlreadyExists() throws InterruptedException {
        goTo("http://localhost:" + port + "/createnewaccount");

        find("#username").fill().with("rolle2");
        find("#password").fill().with("xxxxxxxxxx");
        find("#name").fill().with("Rolle Rol");
        find("#alias").fill().with("relaa");

        find("form").first().submit();

        assertTrue(pageSource().contains("Aliasnimi " + "relaa" + " on jo käytössä."));
    }
    
    @Test
    public void tryToCreateAccountWhereUsernameIsTooShort() throws InterruptedException {
        goTo("http://localhost:" + port + "/createnewaccount");

        find("#username").fill().with("aaa");
        find("#password").fill().with("xxxxxxxxxx");
        find("#name").fill().with("b");
        find("#alias").fill().with("c");

        find("form").first().submit();

        assertTrue(pageSource().contains("Käyttäjätunnuksen täytyy olla vähintään 4 merkkiä pitkä."));
    }
    
    @Test
    public void tryToCreateAccountWherePasswordIsTooShort() throws InterruptedException {
        goTo("http://localhost:" + port + "/createnewaccount");

        find("#username").fill().with("aaaa");
        find("#password").fill().with("xxxxxxxxx");
        find("#name").fill().with("b");
        find("#alias").fill().with("c");

        find("form").first().submit();

        assertTrue(pageSource().contains("Salasanan täytyy olla vähintään 10 merkkiä pitkä."));
    }
    
    @Test
    public void tryToCreateAccountWhereNameIsEmpty() throws InterruptedException {
        goTo("http://localhost:" + port + "/createnewaccount");

        find("#username").fill().with("aaaa");
        find("#password").fill().with("xxxxxxxxxx");
        find("#name").fill().with("");
        find("#alias").fill().with("c");

        find("form").first().submit();

        assertTrue(pageSource().contains("Nimi ei saa olla tyhjä."));
    }
    
    @Test
    public void tryToCreateAccountWhereAliasIsEmpty() throws InterruptedException {
        goTo("http://localhost:" + port + "/createnewaccount");

        find("#username").fill().with("aaaa");
        find("#password").fill().with("xxxxxxxxxx");
        find("#name").fill().with("b");
        find("#alias").fill().with("");

        find("form").first().submit();

        assertTrue(pageSource().contains("Alias ei saa olla tyhjä."));
    }
    
    @Test
    public void tryToCreateAccountWhereMinimumRequirementsForLenghts() throws InterruptedException {
        goTo("http://localhost:" + port + "/createnewaccount");

        find("#username").fill().with("aaaa");
        find("#password").fill().with("xxxxxxxxxx");
        find("#name").fill().with("b");
        find("#alias").fill().with("c");

        find("form").first().submit();

        assertTrue(pageSource().contains("Käyttäjätunnus " + "aaaa" + " luotu. Voit nyt kirjautua sisään."));
    }
    
    
}
