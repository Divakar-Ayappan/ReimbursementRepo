package com.divum.reimbursement_platform.projects.repo;

import com.divum.reimbursement_platform.projects.entity.Project;
import com.divum.reimbursement_platform.projects.entity.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepo extends JpaRepository<Project, Long>{



    List<Project> findAllByProjectStatus(final ProjectStatus projectStatus);
}
