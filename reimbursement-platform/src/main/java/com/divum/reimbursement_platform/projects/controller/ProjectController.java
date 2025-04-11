package com.divum.reimbursement_platform.projects.controller;

import com.divum.reimbursement_platform.commons.entity.StatusCode;
import com.divum.reimbursement_platform.commons.entity.SuccessResponse;
import com.divum.reimbursement_platform.projects.dao.AddOrUpdateProjectRequest;
import com.divum.reimbursement_platform.projects.dao.GetProjectResponse;
import com.divum.reimbursement_platform.projects.entity.ProjectStatus;
import com.divum.reimbursement_platform.projects.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<GetProjectResponse>> getAllProjects(@RequestParam("projectStatus") final ProjectStatus filter) {
        log.info("Received request to get all projects with filter: {}", filter);

        return ResponseEntity.ok(projectService.getAllProjects(filter));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<GetProjectResponse> getProjectById(@RequestParam("projectId") final Long projectId) {
        log.info("Received request to get project by id: {}", projectId);

        return ResponseEntity.ok(projectService.getProjectById(projectId));
    }

    @PostMapping
    public ResponseEntity<SuccessResponse> createProject(@RequestBody @Valid final AddOrUpdateProjectRequest addProjectRequest) {
        log.info("Received request to create project: {}", addProjectRequest);

        projectService.createProject(addProjectRequest);
        return ResponseEntity.ok(new SuccessResponse("Project created successfully", StatusCode.CREATED));
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<SuccessResponse> updateProject(@RequestParam("projectId") final Long projectId,
                                                         @RequestBody @Valid final AddOrUpdateProjectRequest updateProjectRequest) {
        log.info("Received request to update project with id: {} with data: {}", projectId, updateProjectRequest);

        return ResponseEntity.ok(new SuccessResponse("Project updated successfully", StatusCode.UPDATED));
    }

}
