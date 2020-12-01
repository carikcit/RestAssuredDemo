import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class PracticeTest1 {
    private ResponseSpecification responseSpec;
    private RequestSpecification requestSpec;

    @BeforeClass
    public void init() {
        RestAssured.baseURI = "https://zippopotam.us";
    }

    public void createResponseSpec() {
        responseSpec = new ResponseSpecBuilder().
                expectStatusCode(200).
                expectContentType(ContentType.JSON).
                build();
    }

    public void createRequestSpec() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("http://zippopotam.us")
                .setAccept(ContentType.JSON)
                .build();

    }
    
    @Test
    public void simpleTest() {
        given()
                .when()
                .get("us/90210")
                .then()
                .statusCode(200);
    }
    @Test
    public void simpleResponseTypeTest() {
        given()
                .when()
                .get("us/90210")
                .then()
                .contentType(ContentType.JSON);
    }
    @Test
    public void logRequestAndResponseDetails() {
        given()
                .log().all()
                .when()
                .get("/us/90210")
                .then()
                .log().body().notifyAll();
    }

    @Test
    public void checkRespondBody() {
        given()
                .when()
                .get("us/90210")
                .then()
                .body("places[0].state", equalTo("California"));
    }

    @Test
    public void checkRespondBodyPostCode() {
        given()
                .when()
                .get("us/90210")
                .then()
                .body("'post code'", equalTo("90210"));

    }

    @Test
    public void checklistHasItem() {
        given()
                .when()
                .get("tr/34295")
                .then()
                .body("places.'place name'", hasItem("Kartaltepe Mah."));
    }

    @Test
    public void checkListSize() {
        given()
                .when()
                .get("tr/34840")
                .then()
                .body("places ", hasSize(2));
    }

    @Test
    public void combiningTest() {
        given()
                .when()
                .get("us/90210")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("places[0].state", equalTo("California"));

    }

    @Test
    public void pathParameterTest() {
        given()
                .log().all()
                .pathParam("country", "us")
                .pathParam("zipcode", "90210")
                .when()
                .get("/{country}/{zipcode}")
                .then()
                .log().all()
                .statusCode(200);
    }

    //https://gorest.co.in/public-api/users?_format=json&access-token=j6XoJSutZrv-ikB-4X4_Zndi54_iqSZES-Ap
    @Test
    public void queryParamTest() {
        given()
                .log().uri()
                .param("_format", "json")
                .param("access-token", "j6XoJSutZrv-ikB-4X4_Zndi54_iqSZES-Ap")
                .when()
                .get("https://gorest.co.in/public-api/users")
                .then()
                .statusCode(200)
                .log().body()
                .log().status();
    }

    @Test
    public void extractValueFromResponseBody() {

        String placeName = given().
                when().
                get("us/90210").
                then().
                log().body().
                extract().
                path("places[0].'place name'");

        Assert.assertEquals(placeName, "Beverly Hills");
    }

}
