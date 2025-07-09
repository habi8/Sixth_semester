import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.DashboardPage;
import org.example.SignInPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import java.util.*;


public class InvalidBoardCreationTest {
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
    public void invalidBoardCreation() {
        signInPage.navigateToSignIn();
        driver.manage().window().setSize(new Dimension(602, 971));
        signInPage.clickLoginButton();
        dashboardPage.clickAddNewBoard();
        dashboardPage.enterBoardName("");
        dashboardPage.clickSubmitButton();
        assertNull(dashboardPage.getBoardTitle());
    }
}