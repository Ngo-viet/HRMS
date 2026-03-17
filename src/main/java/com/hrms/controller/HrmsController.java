package com.hrms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hrms.model.Employee;
import com.hrms.model.Leaves;
import com.hrms.model.Salary;
import com.hrms.model.User;
import com.hrms.service.EmployeeService;
import com.hrms.service.LeaveService;
import com.hrms.service.SalaryService;
import com.hrms.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class HrmsController {
	@Autowired
	private UserService uservice;

	@Autowired
	private EmployeeService eservice;

	@Autowired
	private LeaveService lservice;

	@Autowired
	private SalaryService sservice;

	// login HR User Only
	@PostMapping("/loginuser")
	public User loginUser(@RequestBody User user) throws Exception {
		String tempEmail = user.getEmail();
		String tempPassword = user.getPassword();
		User userObj = null;
		if (tempEmail != null && tempPassword != null) {
			userObj = uservice.fetchUserByEmailAndPassword(tempEmail, tempPassword);
		}
		if (userObj == null) {
			throw new Exception("Bad Crediential");
		}
		return userObj;
	}

	// add hr user
	@PostMapping("/addhr")
	public User addHr(@RequestBody User user) throws Exception {
		// to check whether email is already present or not
		String tempEmail = user.getEmail();
		System.out.println("Temp email is :"+tempEmail);
//			// fetch user email object
			User user1 = uservice.fetchUserByEmail(tempEmail);
			if (user1 != null) {
				throw new Exception("User with " + tempEmail + " allready exist ");
			}
			else
			{
				user1 = uservice.register(user);
				return user1;
			}
		
	}

//for employee CRUD  start
	// Get list of employee
	@GetMapping("/employeereport")
	public List<Employee> getemployees() {
		return eservice.fetchEmployees();
	}

	// Register Employee
	@PostMapping("/addemployee")
	public Employee registerEmployee(@RequestBody Employee employee) throws Exception {
		// to check whether email is already present or not
		String tempEmail = employee.getEmail();
		if (tempEmail != null && !"".equals(tempEmail)) {
			// fetch user email object
			Employee employeeObj = eservice.fetchEmployeeByEmail(tempEmail);
			if (employeeObj != null) {
				throw new Exception("User with " + tempEmail + " allready exist ");
			}
		}

		Employee employeeObj = null;
		employeeObj = eservice.register(employee);
		return employeeObj;
	}

	// get employee by id
	@GetMapping("/editemployee/{id}")
	public Employee getEmpById(@PathVariable int id) {
		return eservice.getById(id).get();
	}

	// edit employee
	@PostMapping("/editemployee")
	public Employee editEmployee(@RequestBody Employee employee) {
		return eservice.editEmployee(employee);
	}

	// delete employee by id
	@GetMapping("/deleteemployee/{id}")
	public void deleteEmployee(@PathVariable int id) {
		String result = eservice.deleteEmp(id);
	}

//for employee CRUD  End

//for Leave CRUD Start

	// get list of leaves
	@GetMapping("/leavesreport")
	public List<Leaves> getLeaves() {
		return lservice.fetchLeaves();
	}

	// add leaves
	@PostMapping("/addleaves")
	public Leaves addLeaves(@RequestBody Leaves leaves) throws Exception {
		// search employee email by eid
		int tempId = leaves.getEid();
		System.out.println("TempEid: " + tempId);
		Employee emp = lservice.findEmailByEid(tempId);

		if (emp.getEmail() != null) {
			leaves.setEmail(emp.getEmail());
			return lservice.register(leaves);
		} else {
			throw new Exception("Employee with " + tempId + " Not exists ");
		}

	}

	// get employee by id
	@GetMapping("/editleaves/{id}")
	public Leaves getLeavesById(@PathVariable int id) {
		return lservice.getById(id).get();
	}

	// edit Leaves
	@PostMapping("/editleaves")
	public Leaves editLeave(@RequestBody Leaves leaves) {
		return lservice.editLeaves(leaves);
	}

	// delete Leaves by id
	@GetMapping("/deleteleaves/{id}")
	public void deleteLeaves(@PathVariable int id) {
		String result = lservice.deleteleaves(id);
	}

//for Leave CRUD End

//for Salary CRUD Start			
	// get list of salary
	@GetMapping("/salaryreport")
	public List<Salary> getSalary() {
		return sservice.fetchSalary();
	}

	// add salary
	@PostMapping("/addsalary")
	public Salary addsalary(@RequestBody Salary salary) {
		return sservice.register(salary);
	}

	// get employee salary by id
	@GetMapping("/editsalary/{sid}")
	public Salary getSalaryById(@PathVariable int sid) {
		return sservice.getById(sid).get();
	}

	// Edit Salary
	@PostMapping("/editsalary")
	public Salary editSalary(@RequestBody Salary salary) {
		return sservice.editSalary(salary);
	}

	// delete Salary by id
	@GetMapping("/deletesalary/{sid}")
	public void deleteSalary(@PathVariable int sid) {
		String result = sservice.deleteSalary(sid);
	}

// Salary CRUD End		

	// search employee by email id
	@GetMapping("/searchemail/{email}")
	public Employee findEmployee(@PathVariable String email) {
		System.out.println(email);
		return eservice.fetchEmployeeByEmail(email);
	}
}
