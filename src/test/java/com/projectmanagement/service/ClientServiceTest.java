package com.projectmanagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.projectmanagement.domain.Client;
import com.projectmanagement.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setName("Test Client");
        client.setEmail("test@example.com");
        client.setPhone("123456789");
    }

    @Test
    void shouldSaveClient() {
        when(clientRepository.save(client)).thenReturn(client);

        Client savedClient = clientService.save(client);

        assertThat(savedClient).isNotNull();
        assertThat(savedClient.getName()).isEqualTo("Test Client");
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void shouldUpdateClient() {
        when(clientRepository.save(client)).thenReturn(client);

        Client updatedClient = clientService.update(client);

        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getEmail()).isEqualTo("test@example.com");
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void shouldFindClientById() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        Optional<Client> foundClient = clientService.findOne(1L);

        assertThat(foundClient).isPresent();
        assertThat(foundClient.get().getId()).isEqualTo(1L);
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    void shouldDeleteClientById() {
        doNothing().when(clientRepository).deleteById(1L);

        clientService.delete(1L);

        verify(clientRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldCheckIfClientExists() {
        when(clientRepository.existsById(1L)).thenReturn(true);

        boolean exists = clientService.exists(1L);

        assertThat(exists).isTrue();
        verify(clientRepository, times(1)).existsById(1L);
    }
}
