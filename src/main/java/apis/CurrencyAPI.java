package apis;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CurrencyAPI {

    public static final String URL = "https://raiffeisen.ru/oapi/currency_rate/get/";

    public static ValidatableResponse perform(String source, String currencies) {
        return given()
                .when()
                    .log().all()
                    .queryParam("source", source)
                    .queryParam("currencies", currencies)
                    .get(URL)
                .then()
                    .log().all();
    }
}