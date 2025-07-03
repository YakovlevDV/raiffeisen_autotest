package tests;

import apis.CurrencyAPI;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

public class CurrencyTest {

    @DataProvider
    public Object[][] currenciesData() {
        return new Object[][] {
                {"RCONNECT", "USD,EUR,GBP,CHF,JPY,CNY"}, // Интернет банк
                {"CASH", "USD,EUR"}, // Операционные кассы
                {"CARD", "USD,EUR"}, // Банкоматы и терминалы
                {"SME", "USD,EUR,GBP,CHF,JPY,AUD,CAD,SEK,NOK,DKK,SGD,PLN,CNY,KZT"}, // Для малого бизнеса
                {"CBR", "USD,EUR,GBP,CHF,SEK,JPY"} // ЦБ РФ
        };
    }

    @Test(description = "GET /oapi/currency_rate/get", dataProvider = "currenciesData")
    public void currencyTest(String source, String currencies) {
        String schemaPath = "json_schemas/currency_rate.json";
        String[] currenciesList = currencies.split(",");

        CurrencyAPI.perform(source, currencies)
                .statusCode(200)
                .body("success", equalTo(true))
                .body("data.rates[0].exchange.code.size()", equalTo(currenciesList.length))
                .body("data.rates[0].exchange.code", hasItems(currenciesList))
                .body(matchesJsonSchemaInClasspath(schemaPath));
    }
}
