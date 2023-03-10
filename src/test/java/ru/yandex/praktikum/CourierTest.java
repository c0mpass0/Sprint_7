package ru.yandex.praktikum;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.yandex.praktikum.client.CourierClient;
import ru.yandex.praktikum.model.Courier;
import ru.yandex.praktikum.model.CourierCredentials;
import ru.yandex.praktikum.model.CourierGenerator;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CourierTest {
    private CourierClient courierClient;
    private int courierId;

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void clearData() {
        courierClient.delete(courierId);
    }

    @Test
    public void courierCanBeCreatedWithValidData1() {
        Courier courier = CourierGenerator.getRandom();

        ValidatableResponse createResponse = courierClient.create(courier);
        int statusCode = createResponse.extract().statusCode();
        boolean isCourierCreated = createResponse.extract().path("ok");

        assertEquals("Status code is incorrect", HTTP_CREATED, statusCode);
        assertTrue("Courier is not created", isCourierCreated);

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");

        assertTrue("Couried ID is not created", courierId != 0);
    }

    @Test
    public void courierCanBeCreatedWithValidData2() {
        Courier courier = CourierGenerator.getRandom();

        courierClient.create(courier)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .assertThat()
                .body("ok", is(true));

        courierId = courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .body("id", notNullValue())
                .extract().path("id");

    }
}
