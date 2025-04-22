package com.divum.reimbursement_platform.projects.service.impl;

import com.divum.reimbursement_platform.commons.exception.entity.EntityNotFoundException;
import com.divum.reimbursement_platform.employee.entity.Employee;
import com.divum.reimbursement_platform.employee.repo.EmployeeRepo;
import com.divum.reimbursement_platform.projects.dao.AddOrUpdateProjectRequest;
import com.divum.reimbursement_platform.projects.dao.GetProjectResponse;
import com.divum.reimbursement_platform.projects.entity.Project;
import com.divum.reimbursement_platform.projects.entity.ProjectStatus;
import com.divum.reimbursement_platform.projects.repo.ProjectRepo;
import com.divum.reimbursement_platform.projects.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepo projectRepo;

//    private final EmployeeRepo employeeRepo;


    @Override
    public GetProjectResponse getProjectById(final Long projectId) {
        log.info("Fetching project with id: {}", projectId);

        final Project project = projectRepo.findById(projectId).
                orElseThrow(() -> new EntityNotFoundException("Project", projectId.toString()));

        return transformProjectToResponse(project);
    }

    @Override
    public List<GetProjectResponse> getAllProjects(final ProjectStatus filter) {
        log.info("Fetching all projects with status: {}", filter);

        if(Objects.isNull(filter)) {
            return projectRepo.findAll().stream().map(ProjectServiceImpl::transformProjectToResponse).toList();
        }
            return projectRepo.findAllByProjectStatus(filter).stream().map(ProjectServiceImpl::transformProjectToResponse).toList();
    }

    @Override
    public void createProject(final AddOrUpdateProjectRequest addProjectRequest) {
        log.info("Creating project wit request: {}", addProjectRequest);

//        final List<Employee> employees = employeeRepo.findAllById(addProjectRequest.getEmployees());

        final Project project = Project.builder()
                .name(addProjectRequest.getProjectName())
                .description(addProjectRequest.getDescription())
                .projectStatus(LocalDate.now().isBefore(addProjectRequest.getStartDate()) ?
                        ProjectStatus.IN_PIPELINE : ProjectStatus.ONGOING)
                .managerId(addProjectRequest.getManagerId())
//                .employees(employees)
                .startDate(addProjectRequest.getStartDate())
                .build();

        log.info("Creating project: {}", project);
        projectRepo.save(project);
    }


    @Override
    public Long updateProject(final Long projectId, final AddOrUpdateProjectRequest updateProjectRequest) {
        Project projectToUpdate = projectRepo.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project", projectId.toString()));
        projectToUpdate.setName(updateProjectRequest.getProjectName());
        projectToUpdate.setDescription(updateProjectRequest.getDescription());
        projectToUpdate.setProjectStatus(updateProjectRequest.getProjectStatus());
        projectToUpdate.setManagerId(updateProjectRequest.getManagerId());
//        projectToUpdate.setEmployees(employeeRepo.findAllById(updateProjectRequest.getEmployees()));
        projectToUpdate.setStartDate(updateProjectRequest.getStartDate());

        log.info("Updating project: {}", projectToUpdate);
        projectRepo.save(projectToUpdate);

        return projectToUpdate.getProjectId();
    }

    private static GetProjectResponse transformProjectToResponse(final Project project) {
        return GetProjectResponse.builder()
                .projectId(project.getProjectId())
                .name(project.getName())
                .description(project.getDescription())
                .status(project.getProjectStatus())
                .managerId(project.getManagerId())
//                .employees(project.getEmployees().stream().map(Employee::getEmployeeId).toList())
                .startDate(project.getStartDate())
                .build();
    }
}
