package com.example.demo;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import io.restassured.http.ContentType;
import org.junit.Test;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import payroll.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PayrollApplication.class)
public class EmployeeAPITests {

    @LocalServerPort
    private int port;

    @MockBean
    private EmployeeRepository employeeRepository;

//    private URI uri;


    @Test
    public void GetRequestAll() throws URISyntaxException {

        URI uri = new URI("http://localhost:" + this.port);
        System.out.println("\n" + uri + "\n");

        // Create the expected returned employee when saving to repository
        Employee returnedEmployee = new Employee("Samwise", "Gamgee", "gardener");
        returnedEmployee.setId(10L);

        when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(returnedEmployee);
        when(employeeRepository.findAll()).thenReturn(List.of(returnedEmployee));


        // Get Request
        given()
        .when()
                .get(uri + "/employees")
        .then()
                .log().all()
                .and().assertThat().statusCode(200)
                .and().assertThat().body("_links.self", hasKey("href"));
    }

    @Test
    public void GetRequestExistingId() throws URISyntaxException {

        URI uri = new URI("http://localhost:" + this.port);
        System.out.println("\n" + uri + "\n");

        Employee returnedEmployee = new Employee("Samwise", "Gamgee", "gardener");
        returnedEmployee.setId(10L);

        when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(returnedEmployee);
        when(employeeRepository.findById(Mockito.eq(10L))).thenReturn(Optional.of(returnedEmployee));

        given()
                .when()
                .get(uri + "/employees/10")
                .then()
                .log().all()
                .and().assertThat().statusCode(200)
                .and().assertThat().body("_links.self", hasKey("href"));
    }

    @Test
    public void GetRequestNonExistingId() throws URISyntaxException {

        URI uri = new URI("http://localhost:" + this.port);
        System.out.println("\n" + uri + "\n");

        given()
                .when()
                .get(uri + "/employees/1")
                .then()
                .log().all()
                .and().assertThat().statusCode(404)
                .and().assertThat().body(equalTo("Could not find employee 1"));
    }

    @Test
    public void PostRequest() throws URISyntaxException {

        URI uri = new URI("http://localhost:" + this.port);
        System.out.println("\n" + uri + "\n");

        // Create the expected returned employee when saving to repository
        Employee returnedEmployee = new Employee("Jeffery", "Jo", "janitor");
        returnedEmployee.setId(10L);
        when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(returnedEmployee);

        given()
                .contentType(ContentType.JSON)
                .body(returnedEmployee)
        .when()
                .post(uri + "/employees")
        .then()
                .log().all()
                .statusCode(201)
                .body("id", equalTo(10))
                .body("firstName", equalTo("Jeffery"))
                .body("lastName", equalTo("Jo"))
                .body("role", equalTo("janitor"));
    }

    @Test
    public void PutRequestExistingId() throws URISyntaxException {
        URI uri = new URI("http://localhost:" + this.port);
        System.out.println("\n" + uri + "\n");

        Employee existingEmployee = new Employee("Jeffery", "Jo", "janitor");
        existingEmployee.setId(1L);

        Employee givenEmployee = new Employee("Jeffery", "Bo", "janitor");

        when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(existingEmployee);
        when(employeeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(existingEmployee));

        System.out.println(employeeRepository.findById(1L) + "\n");

        given()
                .contentType(ContentType.JSON)
                .body(givenEmployee)
        .when()
                .put(uri + "/employees/1")
        .then()
                .log().all()
                .statusCode(201)
                .body("id", equalTo(1))
                .body("firstName", equalTo("Jeffery"))
                .body("lastName", equalTo("Bo"))
                .body("role", equalTo("janitor"));
    }

    @Test
    public void PutRequestNotExistingId() throws URISyntaxException {
        URI uri = new URI("http://localhost:" + this.port);
        System.out.println("\n" + uri + "\n");

//        Employee existingEmployee = new Employee("Jeffery", "Jo", "janitor");
//        existingEmployee.setId(1L);

        Employee givenEmployee = new Employee("Jeffery", "Bo", "janitor");
        givenEmployee.setId(2L);

//        when(employeeRepository.save(Mockito.eq(existingEmployee))).thenReturn(existingEmployee);
        when(employeeRepository.save(Mockito.eq(givenEmployee))).thenReturn(givenEmployee);
//        when(employeeRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.findById(Mockito.eq(2L))).thenReturn(Optional.empty());

        System.out.println(employeeRepository.findById(2L) + "\n");

        given()
                .contentType(ContentType.JSON)
                .body(givenEmployee)
        .when()
                .put(uri + "/employees/2")
        .then()
                .log().all()
                .statusCode(201)
                .body("id", equalTo(2))
                .body("firstName", equalTo("Jeffery"))
                .body("lastName", equalTo("Bo"))
                .body("role", equalTo("janitor"));
    }

    @Test
    public void DeleteRequest() throws URISyntaxException {
        URI uri = new URI("http://localhost:" + this.port);
        System.out.println("\n" + uri + "\n");

        Employee givenEmployee = new Employee("Jeffery", "Bo", "janitor");
        givenEmployee.setId(2L);

        when(employeeRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(givenEmployee));

        given()
        .when()
                .delete(uri + "/employees/1")
        .then()
                .log().all()
                .statusCode(204);
    }

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
