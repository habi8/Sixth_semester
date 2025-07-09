package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class SignInPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private By loginButton = By.cssSelector("button");
    private By createAccountLink = By.linkText("Create new account");
    private By firstNameField = By.id("user_first_name");
    private By lastNameField = By.id("user_last_name");
    private By emailField = By.id("user_email");
    private By passwordField = By.id("user_password");
    private By passwordConfirmationField = By.id("user_password_confirmation");

    public SignInPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateToSignIn() {
        driver.get("http://localhost:5000/sign_in");
    }

    public void clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    public void clickCreateAccountLink() {
        wait.until(ExpectedConditions.elementToBeClickable(createAccountLink)).click();
    }

    public void enterFirstName(String firstName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField)).sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameField)).sendKeys(lastName);
    }

    public void enterEmail(String email) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).sendKeys(email);
    }

    public void enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).sendKeys(password);
    }

    public void enterPasswordConfirmation(String passwordConfirmation) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordConfirmationField)).sendKeys(passwordConfirmation);
    }
}