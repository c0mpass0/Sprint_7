package ru.yandex.praktikum;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.client.OrderClient;
import ru.yandex.praktikum.model.Order.Order;

import java.util.ArrayList;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderTest {
    private OrderClient orderClient;
    private int track;

    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;

    ArrayList<String> colors = new ArrayList<>();

    public OrderTest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
    }

    @Parameterized.Parameters(name = "Тестовые данные заказа: {0} {1}")
    public static Object[][] getSumData() {
        return new Object[][]{
                {"Самец", "Васильевич", "Небоусова дом 3 кв 15", "Черепановская", "984513513", 2, "2023-06-06", "Коммент!"},
                {"Самка", "Васильевна", "Небоусова дом 3 кв 15", "Черепановская", "984513513", 2, "2023-06-06", "Коммент!"},
        };
    }



    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @After
    public void clearData(){
        orderClient.delete(track);
    }

    @Test
    public void createOrderHappyPath(){
        colors.add("BLACK");
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, colors);

        track = orderClient.create(order)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .assertThat()
                .body("track", notNullValue())
                .extract().path("track");
    }
}
