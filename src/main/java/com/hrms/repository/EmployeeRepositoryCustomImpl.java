package com.hrms.repository;

import com.hrms.model.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EmployeeRepositoryCustomImpl implements EmployeeRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Employee> searchEmployees(String firstName, String lastName, String department, String email, Pageable pageable) {
        StringBuilder sb = new StringBuilder("SELECT e FROM Employee e WHERE 1=1 ");
        StringBuilder countSb = new StringBuilder("SELECT COUNT(e) FROM Employee e WHERE 1=1 ");
        Map<String, Object> params = new HashMap<>();

        if (firstName != null && !firstName.trim().isEmpty()) {
            String filter = "AND LOWER(e.firstName) LIKE LOWER(:firstName) ";
            sb.append(filter);
            countSb.append(filter);
            params.put("firstName", "%" + firstName.trim() + "%");
        }
        if (lastName != null && !lastName.trim().isEmpty()) {
            String filter = "AND LOWER(e.lastName) LIKE LOWER(:lastName) ";
            sb.append(filter);
            countSb.append(filter);
            params.put("lastName", "%" + lastName.trim() + "%");
        }
        if (department != null && !department.trim().isEmpty()) {
            String filter = "AND LOWER(e.department) LIKE LOWER(:department) ";
            sb.append(filter);
            countSb.append(filter);
            params.put("department", "%" + department.trim() + "%");
        }
        if (email != null && !email.trim().isEmpty()) {
            String filter = "AND LOWER(e.email) LIKE LOWER(:email) ";
            sb.append(filter);
            countSb.append(filter);
            params.put("email", "%" + email.trim() + "%");
        }

        // Add sorting if needed, but Pageable handling is standard
        // For simplicity, we assume default sorting from Pageable is handled by JPA if we use repo methods,
        // but here we are in a custom query.
        
        TypedQuery<Employee> query = entityManager.createQuery(sb.toString(), Employee.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(countSb.toString(), Long.class);

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }

        long total = countQuery.getSingleResult();

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Employee> resultList = query.getResultList();

        return new PageImpl<>(resultList, pageable, total);
    }
}
