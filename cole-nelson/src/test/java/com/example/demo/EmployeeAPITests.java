package com.example.demo;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import payroll.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PayrollApplication.class)
public class EmployeeAPITests {

    @LocalServerPort
    private Integer port;

    @MockBean
    private EmployeeRepository employeeRepository;



    @Test
    public void exampleTest1() throws URISyntaxException {

        // Make a string for post request
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("name", "Samwise Gamgee");
        jsonAsMap.put("role", "gardener");

        // Create the expected returned employee when saving to repository
        Employee returnedEmployee = new Employee("Samwise", "Gamgee", "gardener");
        returnedEmployee.setId(10L);
        when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(returnedEmployee);


        URI uri = new URI("http://localhost:" + port);
        System.out.println("\n" + uri + "\n");

        System.out.println("Repo Start: " + employeeRepository.findAll() + "\n");


        // Get Request
        System.out.println("\n \n   Rest Asssured Given_WhenGetEmployees_ThenLogAll: \n");
        given()
        .when()
                .get(uri + "/employees")
        .then()
                .log().all();
        System.out.println("\n \n \n");


        // Post Request
        System.out.println("\n \n   Rest Asssured GivenObject_WhenPostEmployees_ThenLogAll: \n");
        given()
                .contentType(ContentType.JSON)
                .body(jsonAsMap)
        .when()
                .post(uri + "/employees")
        .then()
                .log().all();
        System.out.println("\n \n \n");


        assert("123").equals("Hello World");
    }

//    @Test
//    public void exampleTest2() {
//        webClient
//                .get().uri("/")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(String.class).isEqualTo("Hello World");
//    }

//    @Test
//    public void getRequest(){
//
//        System.out.println(baseURI);
//        System.out.println(port);
//
//        // Store the response given when using the GET request without specified ID
//        Response response = given().when().get("/employees");
//
//        // Print the response body
//        response.prettyPrint();
//
//        // Test the status code to check that it is code 200 (Successful query)
//        response.then().assertThat().statusCode(200);
//
//        // Extract the response to an array list
//        ArrayList<LinkedHashMap<String, String>> employeeList1 = response.then().extract().path("_embedded.employeeList");
//        System.out.println("-------------------------1\n" + employeeList1 + "\n-------------------------\n");
//
//        ObjectMapper mapper = new ObjectMapper();
//
//        // Using the object mapper, you can convert the json string to the Employee class, so you can access the methods
//        // List<Employee> employeeList2 = mapper.convertValue(employeeList1, new TypeReference<List<Employee>>() { });
//        // This version is apparently way faster
//        List<Employee> employeeList2 = Arrays.asList(mapper.convertValue(employeeList1, Employee[].class));
//
//        System.out.println("EmployeeListIndex0: " + employeeList2.get(0));
//        System.out.println("EmployeeListIndex0Name: " + employeeList2.get(0).getName());
//        System.out.println("EmployeeListIndex0Role: " + employeeList2.get(0).getRole());
//        System.out.println();
//
//        System.out.println("EmployeeListIndex1: " + employeeList2.get(1));
//        System.out.println("EmployeeListIndex1Name: " + employeeList2.get(1).getName());
//        System.out.println("EmployeeListIndex1Role: " + employeeList2.get(1).getRole());
//
//        // Why do this instead of just using the hamcrest matchers directly to check the keys and values?
//        // No idea... I have forgotten the purpose of doing this... It deserializes the response but why did I need that?
//    }
//
//
//    // curl -X POST localhost:8080/employees -H 'Content-type:application/json' -d '{"name": "Samwise Gamgee", "role": "gardener"}'
//
//
//    @Test
//    public void postRequest() throws URISyntaxException {
//
//        // Given
////        EmployeeController mockedEmployeeController = Mockito.mock(EmployeeController.class);
//
//        Map<String, Object> jsonAsMap = new HashMap<>();
//        jsonAsMap.put("name", "Samwise Gamgee");
//        jsonAsMap.put("role", "gardener");
//
//        ObjectMapper mapper = new ObjectMapper();
////        MockRestServiceServer
//        Employee employee = mapper.convertValue(jsonAsMap, Employee.class);
//
////        given(mockedEmployeeController.newEmployee(any(Employee.class)))
////                .willAnswer((invocation)-> invocation.getArgument(0));
//
////        URI location = new URI("http://localhost:8080/employees/100");
////        HttpHeaders responseHeaders = new HttpHeaders();
////        responseHeaders.setLocation(location);
////        responseHeaders.set("MyResponseHeader", "MyValue");
////        ResponseEntity<String> entity = new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.CREATED);
////
////        Map<String, Object> expected = new HashMap<>();
//
//        //when(mockedEmployeeController.newEmployee(Mockito.any(Employee.class))).thenReturn(ResponseEntity .created(location) .body("Hello"));
//
////        Mockito.doReturn(entity).when(employeeController).newEmployee(Mockito.any(Employee.class));
//
//
////        jsonAsMap.put("firstName", "Samwise");
////        jsonAsMap.put("lastName", "Gamgee");
////        jsonAsMap.put("role", "gardener");
//
//        given()
//                .contentType(ContentType.JSON)
//                .body(jsonAsMap)
//        .when()
//                .post("/employees")
//        .then()
//                .statusCode(201);
//
//        Response response = given().when().get("/employees");
//
//        response.prettyPrint();
//
//        response.then().body("_embedded.employeeList[2].name", equalTo("Samwise Gamgee"));
//
////        System.out.println(employeeController.newEmployee(employee));
//
//    }

//    @Test
//    public void formParamAcceptsIntArguments() {
//        given().
//                formParam("firstName", 1234).
//                formParam("lastName", 5678).
//                expect().
//                body("greeting", equalTo("Greetings 1234 5678")).
//                when().
//                post("/greet");
//    }



    // curl -X PUT localhost:8080/employees/3 -H 'Content-type:application/json' -d '{"name": "Samwise Gamgee", "role": "ring bearer"}'


    // curl -X DELETE localhost:8080/employees/3
}
