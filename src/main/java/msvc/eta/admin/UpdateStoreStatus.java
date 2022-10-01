package msvc.eta.admin;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.joda.time.DateTime;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class UpdateStoreStatus extends WebSettings implements Endpoints {


    IReport report;
    private String xCaller;
    private String xEntryContextId;
    private String xEntryContext;
    private  String bb_decoded_uid;
    private RequestSpecification requestSpecification;

    public UpdateStoreStatus(String xCaller, String xEntryContextId, String xEntryContext, String bb_decoded_uid, IReport report) {
        this.report = report;
        this.bb_decoded_uid = bb_decoded_uid;
        this.xCaller = xCaller;
        this.xEntryContextId = xEntryContextId;
        this.xEntryContext = xEntryContext;


        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes Post Request to update store eta
     * sa ID is set during object creation.
     * This will validate response schema with the given schema file.
     *
     * @return response
     */
    public Response getStoreStatus(String expectedResponseSchemaPath) {
        String endpoint = String.format(Endpoints.STORE_STATUS);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller",xCaller);
        requestHeader.put("X-TimeStamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("X-Entry-Context",xEntryContext);
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("bb-decoded-uid",bb_decoded_uid);


        JSONObject requestBody = new JSONObject();
        requestBody.put("eta_in_mins" , "30");
        requestBody.put("sa_id","10001");
        requestBody.put("current_threshold_name","T1");
        requestBody.put("new_threshold_name","T2");
        requestBody.put("current_store_status","1");
        requestBody.put("new_store_status","0");



        report.log("Calling Get Store Status  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(requestBody.toString())
                .post(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n Get Store Status response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }



    public Response getStoreStatus() {
        String endpoint = String.format(Endpoints.STORE_STATUS);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-Entry-Context-Id", xEntryContextId);
        requestHeader.put("X-Entry-Context", xEntryContext);
        requestHeader.put("X-TimeStamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("bb-decoded-uid",bb_decoded_uid);


        JSONObject requestBody = new JSONObject();
        requestBody.put("eta_in_mins" , "30");
        requestBody.put("sa_id","10001");
        requestBody.put("current_threshold_name","T1");
        requestBody.put("new_threshold_name","T2");
        requestBody.put("current_store_status","1");
        requestBody.put("new_store_status","0");



        report.log("Calling Get Store Status  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);


        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(requestBody.toString())
                .post(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();
        report.log("Store Status  response: " + response.prettyPrint(), true);
        return  response;
    }

    public Response getStoreStatusWithIncompleteRequest() {
        String endpoint = String.format(Endpoints.STORE_STATUS);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-Entry-Context-Id", xEntryContextId);
     //   requestHeader.put("X-Entry-Context", xEntryContext);
        requestHeader.put("X-TimeStamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("bb-decoded-uid",bb_decoded_uid);


        JSONObject requestBody = new JSONObject();
        requestBody.put("eta_in_mins" , "30");
        requestBody.put("sa_id","10001");
        requestBody.put("current_threshold_name","T1");
        requestBody.put("new_threshold_name","T2");
        requestBody.put("current_store_status","1");
        requestBody.put("new_store_status","0");



        report.log("Calling Get Store Status  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);


        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(requestBody.toString())
                .post(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();
        report.log("Store Status  response: " + response.prettyPrint(), true);
        return  response;
    }
}
