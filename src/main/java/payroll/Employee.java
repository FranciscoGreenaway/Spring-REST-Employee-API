package payroll;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
class Employee {
	
	private @Id @GeneratedValue Long id;
	private String firstName;
	private String lastName;
	private String role;
	
	public Employee (){}
	
	public Employee (String firstName, String lastName, String role){
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {	// Virtual Name Getter
		return firstName + " " + lastName;
	}
	
	public void setName(String name) {	// Virtual Name Setter
		String[] part = name.split(" ");
		this.firstName = part[0];
		this.lastName = part[1];
	}

	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, firstName, lastName, role);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		return Objects.equals(id, other.id) && Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName) && Objects.equals(role, other.role);
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", firstName= " + firstName + ", lastName= " + lastName + ", role=" + role + "]";
	}
	
	
	
	
	
}
