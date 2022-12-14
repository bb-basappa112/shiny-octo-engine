package bbnow.schema_validation.planogram.internal;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.planogram.internal.GetAllFCAlertsApi;
import org.testng.annotations.Test;

public class GetAllFCAlerts extends BaseTest {
    @DescriptionProvider(author = "Tushar", description = "This testcase validates response schema for alert api.",slug = "alert")
    @Test(groups = { "bbnow" , "regression","bbnow-payments","bbnow-schema-validation","unstable"},enabled = false)
    public void getAllFcAlertTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);
        String fc_id = Config.bbnowConfig.getString("bbnow_stores[1].fc_id");

        GetAllFCAlertsApi getAllFCAlertsApi = new GetAllFCAlertsApi(report);
        Response response = getAllFCAlertsApi.getAllFcAlerts("schema//planogram//get-all-fc-alerts-200.json", fc_id);
        report.log("Status code: " +  response.getStatusCode(),true);
    }

}
