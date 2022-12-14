package bbnow.testcases.eta.order_eta;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.eta.internal.GetOrderEta;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetOrderEtaWithIncompleteRequest extends BaseTest {
    @DescriptionProvider(author = "tushar", description = "This TestCase verifies, api is throwing 401 when request has incomplete headers", slug = "Order Eta with incomplete fields")
    @Test(groups = { "bbnow" , "regression", "regression"})
    public void getOrderEtaWithIncompleteField()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String bb_decoded_mid="2";//todo
        String xcaller="bb-now";
        String entrycontextid="10";
        String entrycontext="bbnow";

        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name"));
        String searchTerm1 = Config.bbnowConfig.getString("bbnow_stores[1].search_term1");
        String searchTerm2 = Config.bbnowConfig.getString("bbnow_stores[1].search_term2");
        String[] searchTerms = {searchTerm1,searchTerm2};
        String areaName = Config.bbnowConfig.getString("bbnow_stores[1].area");

        String orderId = OrderPlacement.placeBBNowOrder("bbnow" , 10 , creds[0], areaName, 2,"ps", searchTerms, true, false, report);


        GetOrderEta getOrder =new GetOrderEta(orderId,xcaller,entrycontext,entrycontextid,bb_decoded_mid,report);

        Response response = getOrder.getOrderEtaWithInvalidHeaders();
        Assert.assertEquals(response.getStatusCode(), 401);
        report.log("Verify Status code: " +  response.getStatusCode(),true);


    }

}
