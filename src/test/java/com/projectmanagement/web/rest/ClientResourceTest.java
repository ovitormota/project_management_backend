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
import com.projectmanagement.domain.Client;
import com.projectmanagement.service.ClientService;

class ClientResourceTest {

    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientResource clientResource;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clientResource)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    private Client createTestClient(Long id) {
        Client client = new Client();
        if (id != null) {
            client.setId(id);
        }
        client.setName("Test Client");
        client.setEmail("test@example.com");
        client.setPhone("1234567890");
        return client;
    }

    Client clientWithoutId = createTestClient(null);

    Client clientWithId = createTestClient(1L);

    private String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    @Test
    void testCreateClient() throws Exception {
        when(clientService.save(any(Client.class))).thenReturn(clientWithoutId);

        String response = mockMvc.perform(post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(clientWithoutId)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Client responseClient = objectMapper.readValue(response, Client.class);

        assertEquals(clientWithoutId.getId(), responseClient.getId());
        assertEquals(clientWithoutId.getName(), responseClient.getName());
        assertEquals(clientWithoutId.getEmail(), responseClient.getEmail());
        assertEquals(clientWithoutId.getPhone(), responseClient.getPhone());
    }

    @Test
    void testUpdateClient() throws Exception {
        when(clientService.exists(1L)).thenReturn(true);
        when(clientService.save(any(Client.class))).thenReturn(clientWithId);

        String response = mockMvc.perform(put("/api/clients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(clientWithId))) // Usa o método toJson para gerar o JSON
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Client responseClient = objectMapper.readValue(response, Client.class);

        assertEquals(clientWithId.getId(), responseClient.getId());
        assertEquals(clientWithId.getName(), responseClient.getName());
        assertEquals(clientWithId.getEmail(), responseClient.getEmail());
        assertEquals(clientWithId.getPhone(), responseClient.getPhone());
    }

    @Test
    void testGetAllClients() throws Exception {
        Page<Client> page = new PageImpl<>(Collections.singletonList(clientWithId));
        when(clientService.findAll(any(Pageable.class))).thenReturn(page);

        String response = mockMvc.perform(get("/api/clients")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Client> responseClients = objectMapper.readValue(response, new TypeReference<List<Client>>() {
        });

        assertEquals(1, responseClients.size());
        assertEquals(clientWithId.getId(), responseClients.get(0).getId());
        assertEquals(clientWithId.getName(), responseClients.get(0).getName());
        assertEquals(clientWithId.getEmail(), responseClients.get(0).getEmail());
        assertEquals(clientWithId.getPhone(), responseClients.get(0).getPhone());
    }

    @Test
    void testGetClientById() throws Exception {
        when(clientService.findOne(1L)).thenReturn(Optional.of(clientWithId));

        String response = mockMvc.perform(get("/api/clients/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Client responseClient = objectMapper.readValue(response, Client.class);

        assertEquals(clientWithId.getId(), responseClient.getId());
        assertEquals(clientWithId.getName(), responseClient.getName());
        assertEquals(clientWithId.getEmail(), responseClient.getEmail());
        assertEquals(clientWithId.getPhone(), responseClient.getPhone());
    }

    @Test
    void testDeleteClient() throws Exception {
        when(clientService.exists(1L)).thenReturn(true);
        doNothing().when(clientService).delete(1L);

        mockMvc.perform(delete("/api/clients/1"))
                .andExpect(status().isNoContent());
    }
}