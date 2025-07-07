package tests;

import apis.CurrencyAPI;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

public class CurrencyTest {

    @DataProvider
    public Object[][] currenciesValidData() {
        return new Object[][] {
                {"RCONNECT", "USD,EUR,GBP,CHF,JPY,CNY"}, // Интернет банк
                {"CASH", "USD,EUR"}, // Операционные кассы
                {"CARD", "USD,EUR"}, // Банкоматы и терминалы
                {"SME", "USD,EUR,GBP,CHF,JPY,AUD,CAD,SEK,NOK,DKK,SGD,PLN,CNY,KZT"}, // Для малого бизнеса
                {"CBR", "USD,EUR,GBP,CHF,SEK,JPY"} // ЦБ РФ
        };
    }

    @Test(description = "GET /oapi/currency_rate/get", dataProvider = "currenciesValidData")
    public void currencyPositiveTest(String source, String currencies) {
        String schemaPath = "json_schemas/currency_rate.json";
        String[] currenciesList = currencies.split(",");

        CurrencyAPI.perform(source, currencies)
                .statusCode(200)
                .body("success", equalTo(true))
                .body("data.rates[0].exchange.code.size()", equalTo(currenciesList.length))
                .body("data.rates[0].exchange.code", hasItems(currenciesList))
                .body(matchesJsonSchemaInClasspath(schemaPath));
    }

    @DataProvider
    public Object[][] currenciesInvalidData() {
        return new Object[][] {
                {"", ""}, // Пустые значения source и currencies
                {"CASHH", "MNT"}, // Невалидные значения source и currencies
        };
    }

    @Test(description = "GET /oapi/currency_rate/get", dataProvider = "currenciesInvalidData")
    public void currencyNegativeTest(String source, String currencies) {
        String sourceErrorMessage = "The value you selected is not a valid choice.";
        String currenciesErrorMessage = "One or more of the given values is invalid.";

        CurrencyAPI.perform(source, currencies)
                .statusCode(400)
                .body("success", equalTo(false))
                .body("fields.source", equalTo(sourceErrorMessage))
                .body("fields.currencies", equalTo(currenciesErrorMessage));
    }
}