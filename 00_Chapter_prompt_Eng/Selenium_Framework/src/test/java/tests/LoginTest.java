package tests;

import java.time.Duration;
import java.util.Objects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import pages.LoginPage;

public class LoginTest {
    private WebDriver driver;
    private LoginPage loginPage;

    @BeforeTest(alwaysRun = true)
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        String headless = System.getProperty("headless", "false");
        if (Boolean.parseBoolean(headless)) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1440,1000");
        options.addArguments("--disable-notifications");

        try {
            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ZERO);
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(45));
            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(20));
            loginPage = new LoginPage(driver);
            loginPage.open();
        } catch (RuntimeException e) {
            if (driver != null) {
                try {
                    driver.quit();
                } catch (RuntimeException cleanupException) {
                    e.addSuppressed(cleanupException);
                } finally {
                    driver = null;
                }
            }
            throw new IllegalStateException("Salesforce login test setup failed.", e);
        }
    }

    @Test(priority = 1, description = "Reject an invalid username and password")
    public void invalidCredentialsShouldDisplayError() {
        try {
            loginPage.open();
            loginPage.login("invalid.user@example.invalid", "InvalidPassword!123", false);

            Assert.assertTrue(loginPage.isLoginErrorDisplayed(),
                    "Expected Salesforce to display an error for invalid credentials.");
            Assert.assertTrue(loginPage.isLoginErrorTextPresent(),
                    "Expected the invalid-login error message to contain text.");
            Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                    "Expected the user to remain on the login page after invalid credentials.");
        } catch (AssertionError e) {
            throw e;
        } catch (RuntimeException e) {
            throw new AssertionError("Invalid-credentials test could not complete.", e);
        }
    }

    @Test(priority = 2, description = "Authenticate using an authorized Salesforce test account")
    public void validCredentialsShouldAuthenticate() {
        String username = System.getenv("SALESFORCE_USERNAME");
        String password = System.getenv("SALESFORCE_PASSWORD");

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new SkipException(
                    "Set SALESFORCE_USERNAME and SALESFORCE_PASSWORD environment variables to run the valid-login test.");
        }

        try {
            loginPage.open();
            loginPage.login(username, password, true);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            boolean leftLoginHost = wait.until(webDriver ->
                    !Objects.requireNonNull(webDriver.getCurrentUrl()).contains("login.salesforce.com")
                    || webDriver.getCurrentUrl().contains("error"));

            Assert.assertTrue(leftLoginHost,
                    "Expected an authentication response after submitting valid test-account credentials.");
            Assert.assertFalse(loginPage.isLoginErrorDisplayed(),
                    "Expected no login error for authorized test-account credentials.");
        } catch (AssertionError | SkipException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new AssertionError("Valid-credentials test could not complete.", e);
        }
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (RuntimeException e) {
                throw new IllegalStateException("Failed to close the Chrome browser cleanly.", e);
            } finally {
                driver = null;
            }
        }
    }
}
