package com.projectmanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import com.projectmanagement.domain.Project;
import com.projectmanagement.domain.Client;
import com.projectmanagement.domain.enumeration.Status;

@DataJpaTest
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ClientRepository clientRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void shouldSaveAndRetrieveProject() {
        // Creating and saving a client (required for Project due to foreign key constraint)
        Client client = new Client();
        client.setName("Client A");
        client.setEmail("clienta@example.com");
        client.setPhone("987654321");
        clientRepository.save(client);
        entityManager.flush();

        // Creating and saving a project
        Project project = new Project();
        project.setName("Project Beta");
        project.setDescription("A new beta project");
        project.setStatus(Status.IN_PROGRESS);
        project.setStartDate(LocalDate.now());
        project.setEndDate(LocalDate.now().plusWeeks(4));
        project.setClient(client);

        projectRepository.save(project);
        entityManager.flush();

        // Retrieving all projects and validating the saved data
        List<Project> projects = projectRepository.findAll();
        assertThat(projects).hasSize(1);
        assertThat(projects.get(0).getName()).isEqualTo("Project Beta");
        assertThat(projects.get(0).getStatus()).isEqualTo(Status.IN_PROGRESS);
        assertThat(projects.get(0).getClient().getName()).isEqualTo("Client A");
    }
}
