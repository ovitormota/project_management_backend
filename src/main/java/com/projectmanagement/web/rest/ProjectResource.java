package com.projectmanagement.web.rest;

import com.projectmanagement.domain.Project;
import com.projectmanagement.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
public class ProjectResource {

    private final Logger log = LoggerFactory.getLogger(ProjectResource.class);

    private final ProjectService projectService;

    public ProjectResource(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Create a new project.
     *
     * @param project the project to create.
     * @return the ResponseEntity with status 201 (Created) and with body the new project,
     * or with status 400 (Bad Request) if the project has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) throws URISyntaxException {
        log.debug("Request to save Project : {}", project);
        if (project.getId() != null) {
            return ResponseEntity.badRequest().body(null);
        }
        Project result = projectService.save(project);
        return ResponseEntity.created(new URI("/api/projects/" + result.getId())).body(result);
    }

    /**
     * Updates an existing project.
     *
     * @param id the id of the project to update.
     * @param project the project to update.
     * @return the ResponseEntity with status 200 (OK) and with body the updated project,
     * or with status 404 (Not Found) if the project is not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project project) {
        log.debug("Request to update Project : {}", project);
        if (!projectService.exists(id)) {
            return ResponseEntity.notFound().build();
        }
        project.setId(id);
        Project result = projectService.save(project);
        return ResponseEntity.ok().body(result);
    }

    /**
     * Get all projects.
     *
     * @param pageable pagination information.
     * @return a page of projects.
     */
    @GetMapping
    public ResponseEntity<Page<Project>> getAllProjects(Pageable pageable) {
        log.debug("Request to get all Projects");
        Page<Project> page = projectService.findAll(pageable);
        return ResponseEntity.ok().body(page);
    }

    /**
     * Get a project by id.
     *
     * @param id the id of the project.
     * @return the ResponseEntity with status 200 (OK) and with body the project,
     * or with status 404 (Not Found) if the project is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        log.debug("Request to get Project : {}", id);
        Optional<Project> project = projectService.findOne(id);
        return project.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Delete the project by id.
     *
     * @param id the id of the project to delete.
     * @return the ResponseEntity with status 204 (NO_CONTENT).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        log.debug("Request to delete Project : {}", id);
        if (!projectService.exists(id)) {
            return ResponseEntity.notFound().build();
        }
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
