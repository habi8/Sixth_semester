import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.DashboardPage;
import org.example.SignInPage;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.interactions.Actions;

import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;

public class InvalidLoginTest {
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
    public void invalidLogin() {
        signInPage.navigateToSignIn();
        driver.manage().window().setSize(new Dimension(602, 933));
        signInPage.enterEmail("invalid@invalid.com");
        signInPage.enterPassword("wrongpass");
        signInPage.clickLoginButton();
        // Assuming an error message is displayed in an element with id 'error-message'
        WebElement errorMessage = driver.findElement(By.id("error-message"));
        assertTrue(errorMessage.isDisplayed());
    }
}