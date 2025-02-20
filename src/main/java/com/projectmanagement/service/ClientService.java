package com.projectmanagement.service;

import com.projectmanagement.domain.Client;
import com.projectmanagement.repository.ClientRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.projectmanagement.domain.Client}.
 */
@Service
@Transactional
public class ClientService {

    private static final Logger LOG = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Save a client.
     *
     * @param client the entity to save.
     * @return the persisted entity.
     */
    public Client save(Client client) {
        LOG.debug("Request to save Client : {}", client);
        return clientRepository.save(client);
    }

    /**
     * Update a client.
     *
     * @param client the entity to save.
     * @return the persisted entity.
     */
    public Client update(Client client) {
        LOG.debug("Request to update Client : {}", client);
        return clientRepository.save(client);
    }

    /**
     * Get all the clients.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Client> findAll(Pageable pageable) {
        LOG.debug("Request to get all Clients");
        return clientRepository.findAll(pageable);
    }

    /**
     * Get one client by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Client> findOne(Long id) {
        LOG.debug("Request to get Client : {}", id);
        return clientRepository.findById(id);
    }

    /**
     * Delete the client by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Client : {}", id);
        clientRepository.deleteById(id);
    }

     /**
     * Check if a client exists by id.
     *
     * @param id the id of the client.
     * @return true if the client exists, false otherwise.
     */
    public boolean exists(Long id) {
        return clientRepository.existsById(id);
    }
}
