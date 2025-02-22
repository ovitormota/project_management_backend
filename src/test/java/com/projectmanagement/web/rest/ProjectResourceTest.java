package com.projectmanagement.web.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.projectmanagement.domain.Project;
import com.projectmanagement.domain.enumeration.Status;
import com.projectmanagement.service.ProjectService;

class ProjectResourceTest {

    private MockMvc mockMvc;

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectResource projectResource;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(projectResource)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    private Project createTestProject(Long id) {
        Project project = new Project();
        if (id != null) {
            project.setId(id);
        }
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setStatus(Status.IN_PROGRESS);
        project.setStartDate(Instant.now());
        project.setEndDate(Instant.now().plus(1, ChronoUnit.DAYS));
        return project;
    }

    Project projectWithoutId = createTestProject(null);
    Project projectWithId = createTestProject(1L);

    private String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    @Test
    void testCreateProject() throws Exception {
        when(projectService.save(any(Project.class))).thenReturn(projectWithoutId);

        String response = mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(projectWithoutId)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Project responseProject = objectMapper.readValue(response, Project.class);

        assertEquals(projectWithoutId.getName(), responseProject.getName());
        assertEquals(projectWithoutId.getDescription(), responseProject.getDescription());
        assertEquals(projectWithoutId.getStatus(), responseProject.getStatus());
    }

    @Test
    void testUpdateProject() throws Exception {
        when(projectService.exists(1L)).thenReturn(true);
        when(projectService.save(any(Project.class))).thenReturn(projectWithId);

        String response = mockMvc.perform(put("/api/projects/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(projectWithId)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Project responseProject = objectMapper.readValue(response, Project.class);

        assertEquals(projectWithId.getId(), responseProject.getId());
        assertEquals(projectWithId.getName(), responseProject.getName());
        assertEquals(projectWithId.getDescription(), responseProject.getDescription());
        assertEquals(projectWithId.getStatus(), responseProject.getStatus());
    }

    @Test
    void testGetAllProjects() throws Exception {
        Page<Project> page = new PageImpl<>(Collections.singletonList(projectWithId));
        when(projectService.findAll(any(Pageable.class))).thenReturn(page);

        String response = mockMvc.perform(get("/api/projects")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Project> responseProjects = objectMapper.readValue(response, new TypeReference<List<Project>>() {
        });

        assertEquals(1, responseProjects.size());
        assertEquals(projectWithId.getId(), responseProjects.get(0).getId());
        assertEquals(projectWithId.getName(), responseProjects.get(0).getName());
        assertEquals(projectWithId.getDescription(), responseProjects.get(0).getDescription());
    }

    @Test
    void testGetProjectById() throws Exception {
        when(projectService.findOne(1L)).thenReturn(Optional.of(projectWithId));

        String response = mockMvc.perform(get("/api/projects/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Project responseProject = objectMapper.readValue(response, Project.class);

        assertEquals(projectWithId.getId(), responseProject.getId());
        assertEquals(projectWithId.getName(), responseProject.getName());
        assertEquals(projectWithId.getDescription(), responseProject.getDescription());
        assertEquals(projectWithId.getStatus(), responseProject.getStatus());
    }

    @Test
    void testDeleteProject() throws Exception {
        when(projectService.exists(1L)).thenReturn(true);
        doNothing().when(projectService).delete(1L);

        mockMvc.perform(delete("/api/projects/1"))
                .andExpect(status().isNoContent());
    }
}
