package com.oberamsystems.ai.pcmanager.controller;

import com.oberamsystems.ai.pcmanager.model.Appliance;
import com.oberamsystems.ai.pcmanager.repository.ApplianceRepository;
import com.oberamsystems.ai.pcmanager.repository.OperatingSystemRepository;
import com.oberamsystems.ai.pcmanager.repository.SoftwareStackRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApplianceController.class)
public class ApplianceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ApplianceRepository applianceRepository;

    @MockitoBean
    private OperatingSystemRepository osRepository;

    @MockitoBean
    private SoftwareStackRepository stackRepository;

    @Test
    public void list_ShouldReturnAppliancesView() throws Exception {
        Mockito.when(applianceRepository.findAll()).thenReturn(Arrays.asList(new Appliance()));

        mockMvc.perform(get("/appliances"))
                .andExpect(status().isOk())
                .andExpect(view().name("appliances/list"))
                .andExpect(model().attributeExists("appliances"));
    }

    @Test
    public void showAddForm_ShouldReturnFormView() throws Exception {
        mockMvc.perform(get("/appliances/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("appliances/form"))
                .andExpect(model().attributeExists("appliance"))
                .andExpect(model().attributeExists("osList"))
                .andExpect(model().attributeExists("stacks"));
    }

    @Test
    public void showEditForm_WithValidId_ShouldReturnFormView() throws Exception {
        Appliance appliance = new Appliance();
        Mockito.when(applianceRepository.findById(anyLong())).thenReturn(Optional.of(appliance));

        mockMvc.perform(get("/appliances/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("appliances/form"))
                .andExpect(model().attributeExists("appliance"));
    }

    @Test
    public void showEditForm_WithInvalidId_ShouldThrowException() throws Exception {
        Mockito.when(applianceRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception e = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> mockMvc.perform(get("/appliances/edit/1")));
        org.junit.jupiter.api.Assertions.assertTrue(e.getCause() instanceof IllegalArgumentException);
    }

    @Test
    public void save_ShouldRedirectToAppliances() throws Exception {
        mockMvc.perform(post("/appliances/save")
                .param("name", "Test Appliance"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/appliances"));

        Mockito.verify(applianceRepository).save(any(Appliance.class));
    }

    @Test
    public void delete_WithValidId_ShouldRedirect() throws Exception {
        Appliance appliance = new Appliance();
        Mockito.when(applianceRepository.findById(anyLong())).thenReturn(Optional.of(appliance));

        mockMvc.perform(get("/appliances/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/appliances"));

        Mockito.verify(applianceRepository).delete(appliance);
    }
    
    @Test
    public void delete_WithInvalidId_ShouldThrowException() throws Exception {
        Mockito.when(applianceRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception e = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> mockMvc.perform(get("/appliances/delete/1")));
        org.junit.jupiter.api.Assertions.assertTrue(e.getCause() instanceof IllegalArgumentException);
    }
}
