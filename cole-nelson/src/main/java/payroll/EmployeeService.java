package payroll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class EmployeeService implements InterfaceEmployeeService{

    @Autowired
    private final EmployeeRepository employeeRepository;

    @Autowired
    private final EmployeeModelAssembler assembler;

    EmployeeService(EmployeeRepository employeeRepository, EmployeeModelAssembler assembler){
        this.employeeRepository = employeeRepository;
        this.assembler = assembler;
    }

    public CollectionModel<EntityModel<Employee>> getAllEmployees() {
        List<EntityModel<Employee>> employees = employeeRepository.findAll().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());

        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }

    public ResponseEntity<EntityModel<Employee>> makeNewEmployee(Employee newEmployee) {
        EntityModel<Employee> entityModel = assembler.toModel(employeeRepository.save(newEmployee));
        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    public EntityModel<Employee> getOneEmployee(Long id){
        Employee employee = employeeRepository.findById(id) //
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return assembler.toModel(employee);
    }

    public ResponseEntity<EntityModel<Employee>> updateOneEmployee(Employee newEmployee, Long id){
        Employee updatedEmployee = employeeRepository.findById(id) //
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return employeeRepository.save(employee);
                }) //
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return employeeRepository.save(newEmployee);
                });

        EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    public ResponseEntity<EntityModel<Employee>> deleteOneEmployee(Long id){
        employeeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
