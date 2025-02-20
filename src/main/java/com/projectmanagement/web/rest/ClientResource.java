package com.projectmanagement.web.rest;

import com.projectmanagement.domain.Client;
import com.projectmanagement.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * REST controller for managing {@link com.projectmanagement.domain.Client}.
 */
@RestController
@RequestMapping("/api/clients")
public class ClientResource {

    private final Logger log = LoggerFactory.getLogger(ClientResource.class);

    private final ClientService clientService;

    public ClientResource(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Create a new client.
     *
     * @param client the client to create.
     * @return the ResponseEntity with status 201 (Created) and with body the new client,
     * or with status 400 (Bad Request) if the client already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) throws URISyntaxException {
        log.debug("Request to save Client : {}", client);
        if (client.getId() != null) {
            return ResponseEntity.badRequest().body(null);
        }
        Client result = clientService.save(client);
        return ResponseEntity.created(new URI("/api/clients/" + result.getId())).body(result);
    }

    /**
     * Update an existing client.
     *
     * @param id the id of the client to update.
     * @param client the client to update.
     * @return the ResponseEntity with status 200 (OK) and with body the updated client,
     * or with status 404 (Not Found) if the client is not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client client) {
        log.debug("Request to update Client : {}", client);
        if (!clientService.exists(id)) {
            return ResponseEntity.notFound().build();
        }
        client.setId(id);
        Client result = clientService.save(client);
        return ResponseEntity.ok().body(result);
    }

    /**
     * Get all clients.
     *
     * @param pageable pagination information.
     * @return a page of clients.
     */
    @GetMapping
    public ResponseEntity<Page<Client>> getAllClients(Pageable pageable) {
        log.debug("Request to get all Clients");
        Page<Client> page = clientService.findAll(pageable);
        return ResponseEntity.ok().body(page);
    }

    /**
     * Get a client by id.
     *
     * @param id the id of the client.
     * @return the ResponseEntity with status 200 (OK) and with body the client,
     * or with status 404 (Not Found) if the client is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClient(@PathVariable Long id) {
        log.debug("Request to get Client : {}", id);
        Optional<Client> client = clientService.findOne(id);
        return client.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Delete the client by id.
     *
     * @param id the id of the client to delete.
     * @return the ResponseEntity with status 204 (NO_CONTENT).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        log.debug("Request to delete Client : {}", id);
        if (!clientService.exists(id)) {
            return ResponseEntity.notFound().build();
        }
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
