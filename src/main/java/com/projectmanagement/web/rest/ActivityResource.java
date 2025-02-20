package com.projectmanagement.web.rest;

import com.projectmanagement.domain.Activity;
import com.projectmanagement.service.ActivityService;
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
@RequestMapping("/api/activities")
public class ActivityResource {

    private final Logger log = LoggerFactory.getLogger(ActivityResource.class);

    private final ActivityService activityService;

    public ActivityResource(ActivityService activityService) {
        this.activityService = activityService;
    }

    /**
     * Create a new activity.
     *
     * @param activity the activity to create.
     * @return the ResponseEntity with status 201 (Created) and with body the new activity,
     * or with status 400 (Bad Request) if the activity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping
    public ResponseEntity<Activity> createActivity(@RequestBody Activity activity) throws URISyntaxException {
        log.debug("Request to save Activity : {}", activity);
        if (activity.getId() != null) {
            return ResponseEntity.badRequest().body(null);
        }
        Activity result = activityService.save(activity);
        return ResponseEntity.created(new URI("/api/activities/" + result.getId())).body(result);
    }

    /**
     * Updates an existing activity.
     *
     * @param id the id of the activity to update.
     * @param activity the activity to update.
     * @return the ResponseEntity with status 200 (OK) and with body the updated activity,
     * or with status 404 (Not Found) if the activity is not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Activity> updateActivity(@PathVariable Long id, @RequestBody Activity activity) {
        log.debug("Request to update Activity : {}", activity);
        if (!activityService.exists(id)) {
            return ResponseEntity.notFound().build();
        }
        activity.setId(id);
        Activity result = activityService.save(activity);
        return ResponseEntity.ok().body(result);
    }

    /**
     * Get all activities.
     *
     * @param pageable pagination information.
     * @return a page of activities.
     */
    @GetMapping
    public ResponseEntity<Page<Activity>> getAllActivities(Pageable pageable) {
        log.debug("Request to get all Activities");
        Page<Activity> page = activityService.findAll(pageable);
        return ResponseEntity.ok().body(page);
    }

    /**
     * Get an activity by id.
     *
     * @param id the id of the activity.
     * @return the ResponseEntity with status 200 (OK) and with body the activity,
     * or with status 404 (Not Found) if the activity is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivity(@PathVariable Long id) {
        log.debug("Request to get Activity : {}", id);
        Optional<Activity> activity = activityService.findOne(id);
        return activity.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Delete the activity by id.
     *
     * @param id the id of the activity to delete.
     * @return the ResponseEntity with status 204 (NO_CONTENT).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        log.debug("Request to delete Activity : {}", id);
        if (!activityService.exists(id)) {
            return ResponseEntity.notFound().build();
        }
        activityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
