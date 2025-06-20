import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.notNullValue;

import static io.restassured.RestAssured.given;

public class AuthIntegrationTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:4004";
    }

    @Test
    public void shouldReturnOKWithValidToken() {
        // arrange -> act -> assert
        String loginPayload = """
                    {
                        "email": "testuser@test.com",
                        "password": "password123"
                    }
                """;

        Response response = given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract()
                .response();

        System.out.println("TOKEN: " + response.jsonPath().getString("token"));
    }


    @Test
    public void shouldReturnUnauthorizedOnInvalidLogin() {
        // arrange -> act -> assert
        String loginPayload = """
                    {
                        "email": "invalidtuser@test.com",
                        "password": "wrongpassword123"
                    }
                """;

        given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);
    }


}
