R: Role: You are a QA Automation Tester with 15+ years of experiance.You are a very good understanding of the IT CRM projects like the salesforce.com your need to creat a enterprise level Selenium with Java maven TestNG framework, it should follow the proper patterns a and should be production ready and enterprice levels grade.

I: Instructions:
1. Generate a complete selenium with java automation script following the standerd of enterprice level standerds.
2. Automate and veryfy the results of the login page login.salesforce.com/?locale=in, ensure that UI is throughly tested with valid and invalid testcases.
3. [Critical]- Apply the TestNG annotations, @Test, @BeforeTest and other and necessary setup/teardown.
4. [Critical]- Implement robost experter handling with in the both page Objec model and test scripts using structured try-catch blocks or explicit exception signature.
5. [Mandatory] use Page Object Model with pageFactory, including @FindBy, constructor initialization, and reusable action methods.
6. [Mandatory]- It is important that you use only the xpath not the css selectors.
7. [Don't]- Don't add the comments, Thread.sleep and other bad coding practice.
8. [Genarate]- Generate the 2 scripts only with the valid and invalid testcases of the login page.
9. [DoNOtUse] Thread.sleep()anyware; rely on WebdriverWait or implicit waits.

C: Context
You are creating a login page scripts with proper framework for the salesforce login, which is a AB Testing website with valid and invalid login page where in the login page you hae the email, passwoerd and submit button with remember me functionality.

E: Example
Example structure for PageFactory:

public class LoginPage{
    @FindBy(xpath = "//input[@id='username']") WebElement Username;
    @FindBy(xpath = "//input[@id='password']") WebElement password;
    @FindBy(xpath = "//input[@id='login']") WebElement loginButton;

    public LoginPage(WebDriver driver) { PageFactory.initElements (driver,this);}

    public void dologin (String user, String pass){
        username.sendkeys(user);
        password.sendkeys(pass);
        loginButton.click();
    }
} 

P: Parametors
with production level automation script expert with pin point accuracy and almost zero bad coding practice.

O: Output
provide only:
1. Page Object file
2. TestNG Test Scripts
Mavan Project 
No Explanations or additional content.

T: Tone 
Technical, precisly, enterprise-grade, code-one.

Please make the entire step by step process and ask me what you are ging and explain to me also what you are doing step by step. Make sure that you firest plan everything and show me what exactly you are going to create. Then only you are going to create afterwords step by step.
