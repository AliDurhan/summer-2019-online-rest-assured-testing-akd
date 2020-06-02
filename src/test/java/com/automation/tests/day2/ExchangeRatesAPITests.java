package com.automation.tests.day2;

import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class ExchangeRatesAPITests {

    private String baseURI = "https://api.openrates.io/";


    @Test
    public void test1() {
        Response response = given().
                get(baseURI + "latest");
        //verify status code
        assertEquals(200, response.getStatusCode());
        System.out.println(response.prettyPrint());
    }

    @Test
    public void test2() {
        Response response = given().get(baseURI + "latest");
        //verify that content type is json
        assertEquals(200, response.getStatusCode());
        //verify that data is coming as json
        assertEquals("application/json", response.getHeader("Content-Type"));
        //or like this
        assertEquals("application/json", response.getContentType());
    }

    //GET https://api.exchangeratesapi.io/latest?base=USD HTTP/1.1
    //base it's a query parameter that will ask web service to change currency from eu to something else
    @Test
    public void test3() {
        //#TASK: get currency exchange rate for dollar. By default it's euro.
//        Response response = given().get(baseURI+"latest?base=USD");
        Response response = given().
                baseUri(baseURI + "latest").
                queryParam("base", "USD").
                get();
        assertEquals(200, response.getStatusCode());
        System.out.println(response.prettyPrint());
    }

}