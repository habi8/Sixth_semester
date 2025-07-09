import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.DashboardPage;
import org.example.SignInPage;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;

public class ValidSignUpTest {
    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;
    private SignInPage signInPage;
    private DashboardPage dashboardPage;

    @Before
    public void setUp() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        js = (JavascriptExecutor) driver;
        signInPage = new SignInPage(driver);
        dashboardPage = new DashboardPage(driver);
        driver.manage().window().setSize(new Dimension(654, 751));
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void validSignUp() {
        signInPage.navigateToSignIn();
        driver.manage().window().setSize(new Dimension(602, 781));
        signInPage.clickCreateAccountLink();
        signInPage.enterFirstName("habibur");
        signInPage.enterLastName("rahman");
        signInPage.enterEmail("bsse1422@iit.du.ac.bd");
        signInPage.enterPassword("112344");
        signInPage.enterPasswordConfirmation("112344");
        signInPage.clickLoginButton();
        assertThat(dashboardPage.getUserName(), is("Habibur Rahman"));
    }
}