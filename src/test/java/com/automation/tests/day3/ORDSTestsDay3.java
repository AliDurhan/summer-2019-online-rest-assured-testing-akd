package com.automation.tests.day3;

import com.automation.utilities.ConfigurationReader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class ORDSTestsDay3 {

    @BeforeAll
    public static void setup(){
        baseURI = ConfigurationReader.getProperty("ords.uri");
    }

    @Test
    public void test1(){
        given()
                .accept("application/json")
                .get("/employees")
                .then()
                .assertThat().statusCode(200)
                .and()
                .assertThat().contentType("application/json")
                .log().ifError();
    }

    @Test
    public void test2(){
        given()
                .accept("application/json")
                .pathParam("id", 100)
                .when()
                    .get("/employees/{id}")
                .then()
                    .assertThat().statusCode(100)
                .and()
                    .assertThat().body("employee_id", is(100),
                                    "department_id", is(90),
                                            "last_name", is("King"))
                .log().ifError();
    }

    @Test
    public void test3(){
        given()
                .accept("application/json")
                .pathParam("id", 1)
        .when()
                .get("/regions/{id}")
        .then()
                .assertThat().statusCode(200)
                .assertThat().body("region_name", is("Europe"))
                .time(lessThan(10L), TimeUnit.SECONDS)
        .log().body(true);//log body in pretty format. all = header + body + status code

        //verify that response time is less than 10 seconds
    }

    @Test
    public void test4() {
        JsonPath json = given().
                accept("application/json").
                when().
                get("/employees").
                thenReturn().jsonPath();

        //items[employee1, employee2, employee3] | items[0] = employee1.first_name = Steven

        String nameOfFirstEmployee = json.getString("items[0].first_name");
        String nameOfLastEmployee = json.getString("items[-1].first_name"); //-1 - last index

        System.out.println("First employee name: " + nameOfFirstEmployee);
        System.out.println("Last employee name: " + nameOfLastEmployee);
        //in JSON, employee looks like object that consists of params and their values
        //we can parse that json object and store in the map.
        Map<String, ?> firstEmployee = json.get("items[0]"); // we put ? because it can be also not String
        System.out.println(firstEmployee);

        //since firstEmployee it's a map (key-value pair, we can iterate through it by using Entry. entry represent one key=value pair)
        // put ? as a value (Map<String, ?>), because there are values of different data type: string, integer, etc..
        //if you put String as value, you might get some casting exception that cannot convert from integer(or something else) to string
        for (Map.Entry<String, ?> entry : firstEmployee.entrySet()) {
            System.out.println("key: " + entry.getKey() + ", value: " + entry.getValue());
        }
//       get and print all last names
//        items it's an object. whenever you need to read some property from the object, you put object.property
//        but, if response has multiple objects, we can get property from every object
        List<String> lastNames = json.get("items.last_name");
        for (String str : lastNames) {
            System.out.println("last name: " + str);
        }
    }

    @Test
    public void test5(){
        JsonPath json = given()
                .accept("application/json")
                .when()
                    .get("/countries").prettyPeek().jsonPath();

        List<Map<String, ?>> allCountries = json.get("items");

        System.out.println(allCountries);

        for(Map<String, ?> map : allCountries){
            System.out.println(map);
        }
    }

    @Test
    public void test6(){
        List<Integer> salaries = given().
                accept("application/json").
                when().
                get("/employees").
                thenReturn().jsonPath().get("items.salary");
        Collections.sort(salaries, Collections.reverseOrder());//sort from a to z, 0-9
        System.out.println(salaries);
    }

    //get collection of phone numbers, from employees
    //and replace all dots "." in every phone number with dash "-"

    @Test
    public void test7(){
        List<Object> phoneNumbers=given().
                accept("application/json").
                when().get("/employees").
                thenReturn().jsonPath().get("items.phone_number"); //it calls Gpath (GroovyPath), like Xpath(XMLpath),

//        Replaces each element of this list with the result of applying the operator to that element.
//        replace '.' with '-' in every value
        phoneNumbers.replaceAll(phone -> phone.toString().replace(".", "-"));

        System.out.println(phoneNumbers);
    }


    /** ####TASK#####
     *  Given accept type as JSON
     *  And path parameter is id with value 1700
     *  When user sends get request to /locations
     *  Then user verifies that status code is 200
     *  And user verifies following json path information:
     *      |location_id|postal_code|city   |state_province|
     *      |1700       |98199      |Seattle|Washington    |
     *
     */

    @Test
    public void test8(){
        Response    response = given()
                                .accept(ContentType.JSON)
                                .pathParam("id", 1700)
                                .get("/locations/{id}");
                    response.then()
                                .assertThat().body("location_id", is(1700))
                                .assertThat().body("postal_code", is("98199"))
                                .assertThat().body("city", is("Seattle"))
                                .assertThat().body("state_province", is("Washington"))
                            .log().body();
    }

}