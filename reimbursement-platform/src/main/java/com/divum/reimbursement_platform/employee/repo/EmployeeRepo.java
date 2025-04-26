package com.divum.reimbursement_platform.employee.repo;

import com.divum.reimbursement_platform.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, UUID> {

    Optional<Employee> findByEmail(String email);
}
