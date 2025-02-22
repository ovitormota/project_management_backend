package com.projectmanagement.web.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.projectmanagement.service.DashboardService;

class DashboardResourceTest {

    private MockMvc mockMvc;

    @Mock
    private DashboardService dashboardService;

    @InjectMocks
    private DashboardResource dashboardResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(dashboardResource).build();
    }

    @Test
    void testGetDashboardIndicators() throws Exception {
        Map<String, Object> indicators = new HashMap<>();
        indicators.put("totalProjects", 10);
        indicators.put("activeUsers", 5);

        when(dashboardService.getDashboardIndicators()).thenReturn(indicators);

        mockMvc.perform(get("/api/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalProjects").value(10))
                .andExpect(jsonPath("$.activeUsers").value(5));
    }
}
