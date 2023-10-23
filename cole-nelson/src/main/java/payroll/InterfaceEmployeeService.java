package payroll;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

public interface InterfaceEmployeeService {

    CollectionModel<EntityModel<Employee>> getAllEmployees();

    ResponseEntity<EntityModel<Employee>> makeNewEmployee(Employee newEmployee);

    EntityModel<Employee> getOneEmployee(Long id);

    ResponseEntity<EntityModel<Employee>> updateOneEmployee(Employee newEmployee, Long id);

    ResponseEntity<EntityModel<Employee>> deleteOneEmployee(Long id);
}
