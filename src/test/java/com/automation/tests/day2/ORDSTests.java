package com.automation.tests.day2;


import io.restassured.http.Header;
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

    @Test
    public void test2() {
        //header stands for meta data
        //usually it's used to include cookies
        //in this example, we are specifying what kind of response type we need
        //because web service can return let's say json or xml
        //when we put header info "Accept", "application/json", we are saying that we need only json as response
        Response response = given().
                header("accept", "application/json").
                get(baseURI + "/employees/100");
        int actualStatusCode = response.getStatusCode();
        System.out.println(response.prettyPrint());
        assertEquals(200, actualStatusCode);

        //get information about response content type, you can retrieve from response object
        System.out.println("What kind of content server sends to you, in this response: "+response.getHeader("Content-Type"));
    }

    //    #Task: perform GET request to /regions, print body and all headers.
    @Test
    public void test3(){
        Response response = given().get(baseURI+"/regions");
        assertEquals(200, response.getStatusCode());
        //to get specific header
        Header header = response.getHeaders().get("Content-Type");
        //print all headers one by one
        for(Header h: response.getHeaders()){
            System.out.println(h);
        }
        System.out.println("##########################");
        System.out.println(response.prettyPrint());
    }
}
