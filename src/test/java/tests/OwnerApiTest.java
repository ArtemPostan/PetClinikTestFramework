package tests;

import DTO.OwnerDTO;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import io.qameta.allure.Allure;

import org.junit.jupiter.api.Tag;

@Epic("PetClinic API")
@Feature("Управление владельцами")
public class OwnerApiTest extends BaseTest {

    @Tag("smoke")
    @Test
    @Story("Получение списка владельцев")
    @DisplayName("Успешное получение списка владельцев")
    @Severity(SeverityLevel.CRITICAL)
    void getAllOwnersTest(){
       Response response =  Allure.step("Получить список владельцев", () ->
               given()
               .when()
               .get("/api/owners")
       );

        Allure.step("Проверить HTTP-статус 200", () -> assertThat(response.statusCode()).isEqualTo(200));


       List<Map<String, Object>> owners =
               response.jsonPath().getList("$");

        Allure.step("Проверить что список хозяев не пустой", () -> assertThat(owners).isNotEmpty());

       var owner = owners.get(0);

       int id = (int)owner.get("id");
       String firstName = (String)owner.get("firstName");
       String lastName = (String)owner.get("lastName");

        Allure.step("Проверить что ID больше нуля", () -> assertThat(id).isGreaterThan(0));
        Allure.step("Проверить что имя не пустое", () -> assertThat(firstName).isNotBlank());
        Allure.step("Проверить что фамилия не пустая", () -> assertThat(lastName).isNotBlank());
    }

    @Tag("smoke")
    @Test
    @Story("Получение владельца через ID")
    @DisplayName("Успешное получение владельца по ID")
    @Severity(SeverityLevel.CRITICAL)
    void getOwnerByIdTest(){
        Response response = given()
                .when()
                .get("/api/owners");

        assertThat(response.statusCode()).isEqualTo(200);

        List<Map<String, Object>> owners =
                response.jsonPath().getList("$");

        assertThat(owners).isNotEmpty();

        var owner = owners.get(0);

        int id = (int)owner.get("id");
        String firstName = (String)owner.get("firstName");
        String lastName = (String)owner.get("lastName");

        Response response1 = given()
                .pathParam("ownerId", id)
                .when()
                .get("/api/owners/{ownerId}");

        assertThat(response1.statusCode()).isEqualTo(200);

        Map<String, Object> owner1 = response1.jsonPath().getMap("$");

        int id1 = (int)owner1.get("id");
        String firstName1 = (String)owner1.get("firstName");
        String lastName2 = (String)owner1.get("lastName");

        assertThat(id).isEqualTo(id1);
        assertThat(firstName).isEqualTo(firstName1);
        assertThat(lastName).isEqualTo(lastName2);
    }

    @Test
    @Story("Проверка несуществующео владельца")
    @DisplayName("Ошибка при получении несуществующего владельца по ID")
    @Severity(SeverityLevel.CRITICAL)
    void getOwnerByInvalidIdTest(){
        Response response = given()
                .pathParam("ownerId", 1234)
                .when()
                .get("/api/owners/{ownerId}");

        assertThat(response.statusCode()).isEqualTo(404);

    }

    @Test
    @Story("Проверка на наличие питомца у хозяина")
    @DisplayName("Не найден владелец с питомцами")
    @Severity(SeverityLevel.NORMAL)
    void getPetOfOwnerTest(){
        List<OwnerDTO> owners = given()
                .when()
                .get("/api/owners")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("$", OwnerDTO.class);

        Optional<OwnerDTO> owner = owners.stream().
                filter(p -> p.getPets() != null && !p.getPets().isEmpty())
                .findFirst();
        OwnerDTO result = owner.orElseThrow(() -> new AssertionError("Нет питомцев"));

        assertThat(result.getPets()).isNotEmpty();
        }
}

