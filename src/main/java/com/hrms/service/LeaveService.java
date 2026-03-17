package com.hrms.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import com.hrms.model.Employee;
import com.hrms.model.Leaves;
import com.hrms.repository.LeaveRepository;

@Service
public class LeaveService {
	@Autowired
	private LeaveRepository repo;

	@Autowired
	private JdbcTemplate template;

	// add leave of employee
	public Leaves register(Leaves leaves) {
		return repo.save(leaves);
	}

	// fetch leave
	public List<Leaves> fetchLeaves() {
		return repo.findAll();
	}

	// get Leaves by ID
	public Optional<Leaves> getById(int id) {
		return repo.findById(id);
	}

	// update Leave
	public Leaves editLeaves(Leaves leaves) {
		return repo.save(leaves);
	}

	// delete Leave
	public String deleteleaves(int lid) {
		String result;
		try {
			repo.deleteById(lid);
			result = "Employee Leave Record Deleted";
		} catch (Exception e) {
			result = "Employee with LId is not Deleted";
			e.printStackTrace();

		}
		return result;

	}

	// get employee object by eid
	public Employee findEmailByEid(int tempId) {
		System.out.println("id at service: " + tempId);

		String url = "select email from employee where id='" + tempId + "'";
		return template.query(url, new ResultSetExtractor<Employee>() {
			public Employee extractData(ResultSet rs) throws SQLException, DataAccessException {
				Employee e = new Employee();
				while (rs.next()) {
					e.setEmail(rs.getString(1));
					System.out.println("Email id is: " + e.getEmail());
				}
				return e;
			}

		});

	}

}
