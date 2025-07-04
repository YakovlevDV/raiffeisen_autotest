package tests;

import apis.MenuAPI;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class MenuTest {

    @DataProvider
    public Object[][] menuData() {
        return new Object[][] {
                {"retail", "Всё для частных лиц"},
                {"business", "Всё для малого бизнеса"},
                {"corporate", "Всё для корпоративного бизнеса"},
                {"premium", "Всё для премиум-клиентов"},
                {"private", ""},
                {"financial", "Всё для финансовых институтов"},
                {"about", "Всё о банке"}
        };
    }

    @Test(description = "GET /oapi/menu/public-view/", dataProvider = "menuData")
    public void menuTest(String section, String description) {
        MenuAPI.perform(section, "ru")
                .statusCode(200)
                .body("success", equalTo(true))
                .body("data.description", equalTo(description));
    }
}
