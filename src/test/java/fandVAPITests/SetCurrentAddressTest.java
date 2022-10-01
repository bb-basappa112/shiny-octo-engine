package fandVAPITests;

import api.warehousecomposition.planogram_FC.AdminCookie;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.fandv.SetCurrentAddress;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class SetCurrentAddressTest extends BaseTest {
        @DescriptionProvider(author = "nijaguna", description = "This testcase validates response schema for SetCurrentAddress api.",slug = "Validate SetCurrentAddress api")
        @Test(groups = {"fnv_automation" , "regression",})
        public void validSetCurrentAddress() throws IOException {
            Properties prop=new Properties();
            prop.load(new FileInputStream("src//main//resources//fandvconfig.properties"));
            AutomationReport report = getInitializedReport(this.getClass(),false);
            String[] adminCred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
            String adminUserName = adminCred[0];
            String adminPassword = adminCred[1];
            Map<String, String> cookie = AdminCookie.getMemberCookie(adminUserName, adminPassword, report);
            String csurftoken = prop.getProperty("csurftoken");
            String channel =prop.getProperty("channel");
            String tracker = UUID.randomUUID().toString();
            String homeAddress = prop.getProperty("homeAddress");
            SetCurrentAddress setCurrentAddress = new SetCurrentAddress(cookie,csurftoken,channel,tracker);
            Response response = setCurrentAddress.validateSetCurrentAddress(serverName,"schema//fandv//setCurrentAddress-200.json",homeAddress,report);
            report.log("Status code: " +  response.getStatusCode(),true);
            report.log("Customer Config response: " + response.prettyPrint(),true);

        }

}
