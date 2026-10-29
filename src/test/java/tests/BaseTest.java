package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = System.getenv()
                .getOrDefault("BASE_URL", "http://localhost:9966/petclinic");
        RestAssured.filters(new AllureRestAssured());
    }
}
