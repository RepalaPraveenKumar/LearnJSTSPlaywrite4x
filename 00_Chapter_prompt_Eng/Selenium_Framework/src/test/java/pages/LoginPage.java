package pages;

import java.time.Duration;
import java.util.Objects;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class LoginPage {
    private static final String LOGIN_URL = "https://login.salesforce.com/?locale=in";
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(15);

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(xpath = "//input[@id='username']")
    private WebElement usernameInput;

    @FindBy(xpath = "//input[@id='password']")
    private WebElement passwordInput;

    @FindBy(xpath = "//input[@id='Login' or @id='login']")
    private WebElement loginButton;

    @FindBy(xpath = "//input[@id='rememberUn']")
    private WebElement rememberMeCheckbox;

    @FindBy(xpath = "//*[@id='error' or @id='login_error' or contains(@class,'loginError')]")
    private WebElement loginError;

    public LoginPage(WebDriver driver) {
        this.driver = Objects.requireNonNull(driver, "WebDriver must not be null");
        this.wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        PageFactory.initElements(driver, this);
    }

    public void open() {
        try {
            driver.get(LOGIN_URL);
            wait.until(ExpectedConditions.visibilityOf(usernameInput));
            wait.until(ExpectedConditions.visibilityOf(passwordInput));
        } catch (TimeoutException | NoSuchElementException e) {
            throw new IllegalStateException("Salesforce login page did not load expected login fields.", e);
        }
    }

    public void enterUsername(String username) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOf(usernameInput));
            element.clear();
            element.sendKeys(Objects.requireNonNull(username, "Username must not be null"));
        } catch (TimeoutException | StaleElementReferenceException e) {
            throw new IllegalStateException("Unable to enter the Salesforce username.", e);
        }
    }

    public void enterPassword(String password) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOf(passwordInput));
            element.clear();
            element.sendKeys(Objects.requireNonNull(password, "Password must not be null"));
        } catch (TimeoutException | StaleElementReferenceException e) {
            throw new IllegalStateException("Unable to enter the Salesforce password.", e);
        }
    }

    public void setRememberMe(boolean shouldRemember) {
        try {
            WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(rememberMeCheckbox));
            if (checkbox.isSelected() != shouldRemember) {
                checkbox.click();
            }
        } catch (TimeoutException | NoSuchElementException | StaleElementReferenceException e) {
            throw new IllegalStateException("Unable to update the Remember Me option.", e);
        }
    }

    public void submitLogin() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        } catch (TimeoutException | NoSuchElementException | StaleElementReferenceException e) {
            throw new IllegalStateException("Unable to submit the Salesforce login form.", e);
        }
    }

    public void login(String username, String password, boolean rememberMe) {
        enterUsername(username);
        enterPassword(password);
        setRememberMe(rememberMe);
        submitLogin();
    }

    public boolean isLoginPageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(usernameInput)).isDisplayed()
                    && wait.until(ExpectedConditions.visibilityOf(passwordInput)).isDisplayed();
        } catch (TimeoutException | NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public boolean isLoginErrorDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(loginError)).isDisplayed();
        } catch (TimeoutException | NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public String getLoginErrorText() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(loginError)).getText().trim();
        } catch (TimeoutException | NoSuchElementException | StaleElementReferenceException e) {
            throw new IllegalStateException("Salesforce login error message was not displayed.", e);
        }
    }

    public boolean isRememberMeSelected() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(rememberMeCheckbox)).isSelected();
        } catch (TimeoutException | NoSuchElementException | StaleElementReferenceException e) {
            throw new IllegalStateException("Unable to determine Remember Me selection state.", e);
        }
    }

    public boolean hasNavigatedAwayFromLoginPage() {
        try {
            wait.until(ExpectedConditions.not(
                    ExpectedConditions.urlContains("login.salesforce.com")));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isLoginErrorTextPresent() {
        try {
            return !getLoginErrorText().isBlank();
        } catch (IllegalStateException e) {
            return false;
        }
    }
}
