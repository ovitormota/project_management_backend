package com.projectmanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import com.projectmanagement.domain.Client;

@DataJpaTest
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void shouldSaveAndRetrieveClient() {
        Client client = new Client();
        client.setName("John Doe");
        client.setEmail("john@example.com");
        client.setPhone("123456789");

        clientRepository.save(client);

        entityManager.flush();

        List<Client> clients = clientRepository.findAll();
        assertThat(clients).hasSize(1);
        assertThat(clients.get(0).getName()).isEqualTo("John Doe");
        assertThat(clients.get(0).getEmail()).isEqualTo("john@example.com");
    }
}
