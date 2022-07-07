package payroll;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.RepresentationModel;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
class EmployeeController {
	
	private final EmployeeRepository repository;
	
	private final EmployeeModelAssembler assembler;
	
	EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler){
		this.repository = repository;
		this.assembler = assembler;
	}
	
	// Root
	@GetMapping("/")
	public RepresentationModel root() {
		
		RepresentationModel rootResource = new RepresentationModel();
		
		rootResource.add(
				linkTo(methodOn(EmployeeController.class).root()).withSelfRel(),
						linkTo(methodOn(EmployeeController.class).all()).withRel("Get All Employees"));
		
		return rootResource;
	}
	

	@GetMapping("/employees")
	CollectionModel<EntityModel<Employee>> all(){
		
		List<EntityModel<Employee>> employees = repository.findAll().stream()
				.map(assembler::toModel) // Method referencing. Parameter is automatically passed to toModel method.
				.collect(Collectors.toList());
					
		
		return CollectionModel.of(employees,
				linkTo(methodOn(EmployeeController.class).all()).withSelfRel()
				.andAffordance(afford(methodOn(EmployeeController.class).newEmployee(null))));
	}
	
	
	@PostMapping("/employees")
	ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) {
		
		EntityModel<Employee> entityModel = assembler.toModel(repository.save(newEmployee));
		return ResponseEntity // ResponseEntity used to create an HTTP 201 created status messagae
				
				// Returns link of where new employee is located and the body of that model
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) 
				.body(entityModel);
	}
	
	//Single Item
	
	@GetMapping("/employees/{id}")
	EntityModel<Employee> one(@PathVariable Long id) {
		
		Employee employee = repository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException(id));
		
		return assembler.toModel(employee);
	}
	
	@PutMapping("/employees/{id}")
	ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
		
		Employee updatedEmployee = repository.findById(id)
				.map(employee -> {
					employee.setName(newEmployee.getName());
					employee.setRole(newEmployee.getRole());
					return repository.save(employee);
				})
				.orElseGet(() -> {
					newEmployee.setId(id);
					return repository.save(newEmployee);
				});
		
		EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);
		
		// Returns HTTP201 Created Response
		// Returns link of where new employee is located and the body of that model
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(entityModel);
	}
	
	 @DeleteMapping("/employees/{id}")
	 ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
		 
		 if(repository.findById(id).isPresent()) {
			 repository.deleteById(id);
			 return ResponseEntity.ok(null);
		 }
		 
	    // Returns HTTP 204 No Content Response
	    return ResponseEntity.noContent().build(); // Build the response Body with no Content
	  }
	
}
