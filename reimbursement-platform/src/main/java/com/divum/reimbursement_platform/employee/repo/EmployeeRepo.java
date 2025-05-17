package com.divum.reimbursement_platform.employee.repo;

import com.divum.reimbursement_platform.employee.entity.Employee;
import com.divum.reimbursement_platform.employee.entity.Role;
import com.divum.reimbursement_platform.employee.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, UUID> {

    Optional<Employee> findByEmail(String email);

    List<Employee> findAllByRoleAndStatus(Role role, Status status);
}
