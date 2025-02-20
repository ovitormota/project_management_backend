package com.projectmanagement.service;

import com.projectmanagement.domain.Activity;
import com.projectmanagement.repository.ActivityRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.projectmanagement.domain.Activity}.
 */
@Service
@Transactional
public class ActivityService {

    private static final Logger LOG = LoggerFactory.getLogger(ActivityService.class);

    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    /**
     * Save a activity.
     *
     * @param activity the entity to save.
     * @return the persisted entity.
     */
    public Activity save(Activity activity) {
        LOG.debug("Request to save Activity : {}", activity);
        return activityRepository.save(activity);
    }

    /**
     * Update a activity.
     *
     * @param activity the entity to save.
     * @return the persisted entity.
     */
    public Activity update(Activity activity) {
        LOG.debug("Request to update Activity : {}", activity);
        return activityRepository.save(activity);
    }

    /**
     * Get all the activities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Activity> findAll(Pageable pageable) {
        LOG.debug("Request to get all Activities");
        return activityRepository.findAll(pageable);
    }

    /**
     * Get one activity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Activity> findOne(Long id) {
        LOG.debug("Request to get Activity : {}", id);
        return activityRepository.findById(id);
    }

    /**
     * Delete the activity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Activity : {}", id);
        activityRepository.deleteById(id);
    }

    /**
     * Check if a project exists by id.
     *
     * @param id the id of the project.
     * @return true if the project exists, false otherwise.
     */
    public boolean exists(Long id) {
        return activityRepository.existsById(id);
    }
}
