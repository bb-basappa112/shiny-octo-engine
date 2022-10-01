package admin.pages;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.utilities.AutomationUtilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Login extends WebSettings {

    @FindBy(id = "id_username")
    WebElement loginId;
    @FindBy(id = "id_password")
    WebElement loginPswd;
    @FindBy(xpath = "//*[contains(text(),'Welcome,')] | //*[contains(text(),'WELCOME,')] | //*[contains(text(),'welcome,')] | //a[contains(text(),'Home')]")
    WebElement welcome;
    @FindBy(xpath = "//*[@name='secret']")
    WebElement otp_box;

    private WebDriver driver;
    private AutomationReport report;
    private WebDriverWait wait;

    public Login(WebDriver driver, AutomationReport report) {
        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }




    /**
     * this function selects and enters login credentials depending upon the type of
     * user.
     *
     * @param user
     * @return
     */

    /**
     * This function takes username and password and performs admin login
     * @param username
     * @param password
     * @return
     */
    public HomePage doAdminLogin(String username, String password){
        report.log( "logging in to Admin", true);
        loginId.clear();
        loginId.sendKeys(username);
        loginPswd.clear();
        loginPswd.sendKeys(password);
        report.log( "Username entered:" + username, true);
        loginPswd.submit();
        wait.until(
                ExpectedConditions.visibilityOf(welcome));
        report.log( "logged in as "+username, true);
         return new HomePage(driver, report);
    }


}