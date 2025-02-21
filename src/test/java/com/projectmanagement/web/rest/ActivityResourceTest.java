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
import com.projectmanagement.domain.Activity;
import com.projectmanagement.domain.enumeration.Status;
import com.projectmanagement.service.ActivityService;

class ActivityResourceTest {

    private MockMvc mockMvc;

    @Mock
    private ActivityService activityService;

    @InjectMocks
    private ActivityResource activityResource;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(activityResource)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    private Activity createTestActivityWithoutId(Long id) {
        Activity activity = new Activity();

        if (id != null) {
            activity.setId(id);
        }

        activity.setDescription("Test Activity");
        activity.setStatus(Status.PENDING);
        activity.setStartDate(Instant.now());
        activity.setEndDate(Instant.now().plus(1, ChronoUnit.DAYS));
        return activity;
    }

    Activity activityWithId = createTestActivityWithoutId(1L);

    Activity activityWithoutId = createTestActivityWithoutId(null);

    private String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    @Test
    void testCreateActivity() throws Exception {
        when(activityService.save(any(Activity.class))).thenReturn(activityWithoutId);

        String response = mockMvc.perform(post("/api/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(activityWithoutId)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Activity responseActivity = objectMapper.readValue(response, Activity.class);

        assertEquals(activityWithoutId.getDescription(), responseActivity.getDescription());
        assertEquals(activityWithoutId.getStatus(), responseActivity.getStatus());
        assertEquals(activityWithoutId.getStartDate(), responseActivity.getStartDate());
        assertEquals(activityWithoutId.getEndDate(), responseActivity.getEndDate());
    }

    @Test
    void testUpdateActivity() throws Exception {
        when(activityService.exists(1L)).thenReturn(true);
        when(activityService.save(any(Activity.class))).thenReturn(activityWithId);

        String response = mockMvc.perform(put("/api/activities/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(activityWithId)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Activity responseActivity = objectMapper.readValue(response, Activity.class);

        assertEquals(activityWithId.getId(), responseActivity.getId());
        assertEquals(activityWithId.getDescription(), responseActivity.getDescription());
        assertEquals(activityWithId.getStatus(), responseActivity.getStatus());
        assertEquals(activityWithId.getStartDate(), responseActivity.getStartDate());
        assertEquals(activityWithId.getEndDate(), responseActivity.getEndDate());
    }

    @Test
    void testGetAllActivities() throws Exception {
        Page<Activity> page = new PageImpl<>(Collections.singletonList(activityWithId));
        when(activityService.findAll(any(Pageable.class))).thenReturn(page);

        String response = mockMvc.perform(get("/api/activities")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Activity> responseActivities = objectMapper.readValue(response, new TypeReference<List<Activity>>() {
        });

        assertEquals(1, responseActivities.size());
        assertEquals(activityWithId.getId(), responseActivities.get(0).getId());
        assertEquals(activityWithId.getDescription(), responseActivities.get(0).getDescription());
        assertEquals(activityWithId.getStatus(), responseActivities.get(0).getStatus());
        assertEquals(activityWithId.getStartDate(), responseActivities.get(0).getStartDate());
        assertEquals(activityWithId.getEndDate(), responseActivities.get(0).getEndDate());
    }

    @Test
    void testGetActivityById() throws Exception {
        when(activityService.findOne(1L)).thenReturn(Optional.of(activityWithId));

        String response = mockMvc.perform(get("/api/activities/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Activity responseActivity = objectMapper.readValue(response, Activity.class);

        assertEquals(activityWithId.getId(), responseActivity.getId());
        assertEquals(activityWithId.getDescription(), responseActivity.getDescription());
        assertEquals(activityWithId.getStatus(), responseActivity.getStatus());
        assertEquals(activityWithId.getStartDate(), responseActivity.getStartDate());
        assertEquals(activityWithId.getEndDate(), responseActivity.getEndDate());
    }

    @Test
    void testDeleteActivity() throws Exception {
        when(activityService.exists(1L)).thenReturn(true);
        doNothing().when(activityService).delete(1L);

        mockMvc.perform(delete("/api/activities/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetActivityNotFound() throws Exception {
        when(activityService.findOne(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/activities/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateActivityNotFound() throws Exception {
        when(activityService.exists(1L)).thenReturn(false);

        mockMvc.perform(put("/api/activities/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(activityWithId)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteActivityNotFound() throws Exception {
        when(activityService.exists(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/activities/1"))
                .andExpect(status().isNotFound());
    }
}