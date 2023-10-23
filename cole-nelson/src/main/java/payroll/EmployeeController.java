package payroll;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class EmployeeController {


    private final EmployeeService employeeService;


    public EmployeeController(EmployeeService employeeService) {this.employeeService = employeeService;}


    @GetMapping("/employees")
    public CollectionModel<EntityModel<Employee>> all() {
        return employeeService.getAllEmployees();
    }

    @PostMapping("/employees")
    public ResponseEntity<EntityModel<Employee>> newEmployee(@RequestBody Employee employee) {
        return employeeService.makeNewEmployee(employee);
    }

    @GetMapping("/employees/{id}")
    public EntityModel<Employee> one(@PathVariable Long id) {
        return employeeService.getOneEmployee(id);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
        return employeeService.updateOneEmployee(newEmployee, id);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        return employeeService.deleteOneEmployee(id);
    }
}