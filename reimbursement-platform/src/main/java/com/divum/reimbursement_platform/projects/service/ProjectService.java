package com.divum.reimbursement_platform.projects.service;

import com.divum.reimbursement_platform.projects.dao.AddOrUpdateProjectRequest;
import com.divum.reimbursement_platform.projects.dao.GetProjectResponse;
import com.divum.reimbursement_platform.projects.entity.ProjectStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ProjectService {

    GetProjectResponse getProjectById(final Long projectId);

    List<GetProjectResponse> getAllProjects(final ProjectStatus filter);

    void createProject(final AddOrUpdateProjectRequest addProjectRequest);

    Long updateProject(final Long projectId, final AddOrUpdateProjectRequest updateProjectRequest);

}
