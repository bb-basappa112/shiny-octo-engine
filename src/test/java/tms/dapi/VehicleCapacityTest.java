package tms.dapi;


import tms.Hertz.FieldAssets.VehicleCapacity;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class VehicleCapacityTest extends BaseTest {

    @DescriptionProvider(slug = "Add Van Capacity Upload", description = "Upload Van Capacity can verify if its working ",author = "Pranay")

    @Test(groups = {"test_oms","TMS","OMS","change_slot","Vehicle_capacity"},enabled = false)
    public void verifyVehicleCapacityUpload() throws IOException {

        AutomationReport report = getInitializedReport(this.getClass(), false);

        String xcaller="test";
        String xEntryContext="bb-internal";
        String xEntryContextId="102";
        String xTenantId="2";
        String xProject="bb-tms";
        String xUserId="1";

        report.info("calling vechile capacity upload api");
        VehicleCapacity vehicleCapacity=new VehicleCapacity(report,xcaller,xEntryContext,xEntryContextId,xTenantId,xProject,xUserId);
        Response response=vehicleCapacity.vehiclecapacityupload(10,1,8,"2022-02-07","200KG-VH",100,0);
        report.log("respnse"+response.body().asString(),true);
        Assert.assertTrue(response.body().asString().contains("Vehicle Capacity"));

    }

}
