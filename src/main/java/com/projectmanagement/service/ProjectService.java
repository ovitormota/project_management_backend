package com.projectmanagement.service;

import com.projectmanagement.domain.Project;
import com.projectmanagement.repository.ProjectRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.projectmanagement.domain.Project}.
 */
@Service
@Transactional
public class ProjectService {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectService.class);

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * Save a project.
     *
     * @param project the entity to save.
     * @return the persisted entity.
     */
    public Project save(Project project) {
        LOG.debug("Request to save Project : {}", project);
        return projectRepository.save(project);
    }

    /**
     * Update a project.
     *
     * @param project the entity to save.
     * @return the persisted entity.
     */
    public Project update(Project project) {
        LOG.debug("Request to update Project : {}", project);
        return projectRepository.save(project);
    }

    /**
     * Get all the projects.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Project> findAll(Pageable pageable) {
        LOG.debug("Request to get all Projects");
        return projectRepository.findAll(pageable);
    }

    /**
     * Get one project by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Project> findOne(Long id) {
        LOG.debug("Request to get Project : {}", id);
        return projectRepository.findById(id);
    }

    /**
     * Delete the project by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Project : {}", id);
        projectRepository.deleteById(id);
    }

    /**
     * Check if a project exists by id.
     *
     * @param id the id of the project.
     * @return true if the project exists, false otherwise.
     */
    public boolean exists(Long id) {
        return projectRepository.existsById(id);
    }
}
