package bbnow.testcases.order;

import com.bigbasket.automation.BaseSettings;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.CartMethods;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.mapi.mapi_4_1_0.ProductListing;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.BigBasketApp;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.Helper;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import com.sun.org.apache.xpath.internal.operations.Or;
import framework.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.vertx.core.json.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class OrderTest extends BaseTest {

    @DescriptionProvider(author = "Shruti", description = "This testcase validates response for bbnow order creation with different payment status", slug = "This testcase validates response for bbnow order")
    @Test(groups = {"bbnow"})
    public void OrderPlacement() {

        AutomationReport report = getInitializedReport(this.getClass(), false);
        BigBasketApp app = new BigBasketApp(report);
        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[0].member_sheet_name"));
        report.log("Starting testcase for order placement.", true);
        String searchTerm1 = Config.bbnowConfig.getString("bbnow_stores[0].search_term1");
        String searchTerm2 = Config.bbnowConfig.getString("bbnow_stores[0].search_term2");
        String[] searchTerms = {searchTerm1, searchTerm2};
        String areaName = Config.bbnowConfig.getString("bbnow_stores[0].area");
        String[] var8 = searchTerms;
        app.getAppDataDynamic().login(creds[0]).getAppDataDynamic().setEntryContext("bbnow", 10).setCustomDeliveryAddress(areaName).emptyCart();

        int var9 = searchTerms.length;
        String paymentMethodType;
        for (int var10 = 0; var10 < var9; ++var10) {
            String s = var8[var10];
            JsonObject result = app.listProduct(s);

            try {
                paymentMethodType = Helper.getAvailableProductFromListingApiResponse(result);
                app.addToCart(paymentMethodType, 1, s);
            } catch (AssertionError var16) {
                report.log("Error while adding to cart for search term: " + s + "<br/> " + var16.getMessage(), false);
            }
        }

        Assert.assertTrue(app.cartCount > 0, "No items could be added to cart. Stopping order replacement.");
        String memberEmail = (String) app.getMemberDetail().path("response.member_details.email", new String[0]);
        String poId = app.createPotentialOrderNew(false);
        app.potentialOrderSummary(poId, true);
        String bbTxnId = (String) app.getSdkParams(memberEmail, poId, "2.0").path("response.bb_txn_id", new String[0]);
        float total = (Float) app.getWalletDuringCheckout(poId, bbTxnId, false).path("order_details.final_total", new String[0]);
        String paymentMethod;

        paymentMethod = "NB_HDFC";
        paymentMethodType = "NB_HDFC";


        String orderPlaced = app.createOrderNew(poId, bbTxnId, paymentMethod, paymentMethodType, false, true);
        String status = String.valueOf((new JsonObject(orderPlaced)).getJsonArray("orders").getJsonObject(0).getString("status"));
        Assert.assertEquals(status, "payment_pending", "Order status is not matching");
        report.log("Order is created in payment pending status", true);
        String order_number = String.valueOf((new JsonObject(orderPlaced)).getJsonArray("orders").getJsonObject(0).getString("order_number"));
        Assert.assertTrue(order_number.startsWith("BN"), "Order number doesn't contains prefix BN");
        report.log("Order number contains prefix BN", true);
        Assert.assertFalse(order_number.contains("A"));
        report.log("Add-on order is not created",true);
        int order_type = (new JsonObject(orderPlaced)).getJsonArray("orders").getJsonObject(0).getInteger("order_type_id");
        Assert.assertTrue(order_type == 1, "Order type isn't normal");
        report.log("Order type is normal", true);
        double delivery_charge = (new JsonObject(orderPlaced)).getJsonArray("orders").getJsonObject(0).getDouble("delivery_charge");
        Assert.assertTrue(delivery_charge != 0.0, "Delivery charge isn' applied to order");
        report.log(delivery_charge + " delivery charge is applied to order", true);


        String orderId = String.valueOf((new JsonObject(orderPlaced)).getJsonArray("orders").getJsonObject(0).getLong("id"));
        String tagquery = "select tag_id from tag_order_mapping where order_detail_id=" + orderId + ";";
        JSONObject jsonObject = new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(AutomationUtilities.getOrderDBName(), tagquery, report));
        int tag = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("tag_id");
        Assert.assertTrue(tag != 2);
        report.log("Tags not generated for add-on order",true);
        app.validatePayment(orderId, paymentMethod, paymentMethodType, "CHARGED", total, bbTxnId);
        Response paymentresponse = app.response;
        int polling_interval = (new JsonObject(paymentresponse.asString())).getJsonObject("response").getInteger("polling_interval");
        int polling_timeout = (new JsonObject(paymentresponse.asString())).getJsonObject("response").getInteger("polling_timeout");
        Assert.assertTrue(polling_timeout != 0 && polling_interval != 0);
        report.log("Polling_interval is " + polling_interval + " Polling timeout is " + polling_timeout, true);

        String query = "select status from order_detail where po_id=" + poId + ";";
        JSONObject orderJson= new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(AutomationUtilities.getOrderDBName(), query, report));
        String order_status = orderJson.getJSONArray("rows").getJSONObject(0).getString("status");
        Assert.assertTrue(order_status != null, "Order status is wrong");
        report.log("Order status changed to: " + order_status + " when payment is successful ", true);
        String query2= "select slot_date,slot_from_time,slot_to_time,slot_template_id from order_detail where po_id="+poId+";";
        JSONObject slotJson = new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(AutomationUtilities.getOrderDBName(), query2, report));
        String slotdate= slotJson.getJSONArray("rows").getJSONObject(0).getString("slot_date");
        String slotfromtime= slotJson.getJSONArray("rows").getJSONObject(0).getString("slot_from_time");
        String slottotime= slotJson.getJSONArray("rows").getJSONObject(0).getString("slot_to_time");
        String slotid= String.valueOf(slotJson.getJSONArray("rows").getJSONObject(0).getInt("slot_template_id"));
        Assert.assertTrue(slotdate!=null && slotfromtime!=null && slottotime!=null && slotid!=null);
        {
            report.log("Slot info is updated after order creation",true);
        }

        Response paymentfail = OrderPlacement.placeBBNowOrder("bbnow" , 10 , creds[0], areaName, 1, searchTerms, "FAILED", false, false, report);
        String payment_status = String.valueOf((new JsonObject(paymentfail.asString())).getJsonObject("response").getString("payment_status"));
        Assert.assertEquals(payment_status, "failed", "Payment status is not correct");
        report.log("Payment is failed", true);
        polling_interval = (new JsonObject(paymentfail.asString())).getJsonObject("response").getInteger("polling_interval");
        polling_timeout = (new JsonObject(paymentfail.asString())).getJsonObject("response").getInteger("polling_timeout");
        Assert.assertTrue(polling_timeout != 0 && polling_interval != 0);
        report.log("Polling_interval is " + polling_interval + " Polling timeout is " + polling_timeout, true);
        String validate_pay_msg = String.valueOf((new JsonObject(paymentfail.asString())).getJsonObject("response").getString("validate_payment_message"));
        String validate_pay_title = String.valueOf((new JsonObject(paymentfail.asString())).getJsonObject("response").getString("validate_payment_title"));
        Assert.assertTrue(validate_pay_msg != null && validate_pay_title != null);
        report.log("Validate_payment_message and Validate_payment_title both are present when payment got failed", true);

        Response paymentpending = OrderPlacement.placeBBNowOrder("bbnow" , 10 , creds[0], areaName, 1, searchTerms, "PENDING", false, false, report);
        payment_status = String.valueOf((new JsonObject(paymentpending.asString())).getJsonObject("response").getString("payment_status"));
        Assert.assertEquals(payment_status, "incomplete", "Payment status is not correct");
        report.log("Payment is incomplete", true);
        validate_pay_msg = String.valueOf((new JsonObject(paymentpending.asString())).getJsonObject("response").getString("validate_payment_message"));
        validate_pay_title = String.valueOf((new JsonObject(paymentpending.asString())).getJsonObject("response").getString("validate_payment_title"));
        Assert.assertTrue(validate_pay_msg != null && validate_pay_title != null);
        report.log("Validate_payment_message is:" + validate_pay_msg + " + and Validate_payment_title is: " + validate_pay_title + " when payment is incomplete", true);

    }

}