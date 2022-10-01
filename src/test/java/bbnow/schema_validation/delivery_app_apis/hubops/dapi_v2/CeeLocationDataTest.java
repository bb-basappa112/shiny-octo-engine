package bbnow.schema_validation.delivery_app_apis.hubops.dapi_v2;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.delivery_app_apis.hubops.dapi_v2.CeeLocationData;
import msvc.delivery_app_apis.hubops.dapi_v2.GetConfig;
import org.testng.annotations.Test;

public class CeeLocationDataTest extends BaseTest {

    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for CeeLocationDataTest api.",slug = "Validate CeeLocationDataTest api")
    @Test(groups = {"bbnow","dl2","eta","bbnow-schema-validation","dl2-schema-validation","api-schema-validation","unstable"})
    public void ceeLocationDataTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String cookie = "_bb_cid=1; _bb_vid=\"Mzk1NDA1MzM1Ng==\"; _bb_tc=0; _bb_rdt=\"MzE0NDc3NjE4NQ==.0\"; _bb_rd=6; _client_version=2748; sessionid=c7mtx66jcad0mfz5cl5z52yrz1mwd4lh; ts=\"2021-11-18 16:30:35.220\"";//todo

        String xRefreshtoken="refresh";//todo
        String authorization="Bearer auth_token";//todo
        String latitude="0";//todo
        String longitude="0";//todo


        CeeLocationData ceeLocationData=new CeeLocationData(cookie,xRefreshtoken,authorization,report);
        Response response = ceeLocationData.ceeLocationData("schema//delivery_app//hubops//dapi_v2//cee-location-data-200.json",latitude,longitude);
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("GetConfig  response: " + response.prettyPrint(),true);
    }
}
