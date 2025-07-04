package apis;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class MenuAPI {

    public static final String URI = "https://raiffeisen.ru/oapi/menu/public-view/";

    public static ValidatableResponse perform(String section, String language) {
        return given()
                .when()
                    .log().all()
                    .queryParam("section", section)
                    .queryParam("language", language)
                    .get(URI)
                .then()
                    .log().all();
    }
}