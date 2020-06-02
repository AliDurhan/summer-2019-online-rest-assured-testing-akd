package com.automation.tests.day2;


import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class ORDSTests {

    private String baseURI = "http://ec2-34-201-69-55.compute-1.amazonaws.com:1000/ords/hr";

    @Test
    public void test1(){

        Response response = given().get(baseURI + "/employees");

        assertEquals(200, response.getStatusCode());

        response.prettyPrint();

    }
}
