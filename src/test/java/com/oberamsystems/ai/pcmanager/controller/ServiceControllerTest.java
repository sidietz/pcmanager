package com.oberamsystems.ai.pcmanager.controller;

import com.oberamsystems.ai.pcmanager.model.Service;
import com.oberamsystems.ai.pcmanager.repository.ServiceRepository;
import com.oberamsystems.ai.pcmanager.repository.SoftwareRepository;
import com.oberamsystems.ai.pcmanager.repository.SoftwareStackRepository;
import com.oberamsystems.ai.pcmanager.repository.SubdatabaseRepository;
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

@WebMvcTest(ServiceController.class)
public class ServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServiceRepository serviceRepository;

    @MockitoBean
    private SoftwareStackRepository stackRepository;

    @MockitoBean
    private SoftwareRepository softwareRepository;

    @MockitoBean
    private SubdatabaseRepository subdatabaseRepository;

    @Test
    public void list_ShouldReturnServicesView() throws Exception {
        Mockito.when(serviceRepository.findAll()).thenReturn(Arrays.asList(new Service()));

        mockMvc.perform(get("/services"))
                .andExpect(status().isOk())
                .andExpect(view().name("services/list"))
                .andExpect(model().attributeExists("services"));
    }

    @Test
    public void showAddForm_ShouldReturnFormView() throws Exception {
        mockMvc.perform(get("/services/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("services/form"))
                .andExpect(model().attributeExists("service"))
                .andExpect(model().attributeExists("stacks"))
                .andExpect(model().attributeExists("allSoftware"))
                .andExpect(model().attributeExists("allSubdatabases"));
    }

    @Test
    public void showEditForm_WithValidId_ShouldReturnFormView() throws Exception {
        Service service = new Service();
        Mockito.when(serviceRepository.findById(anyLong())).thenReturn(Optional.of(service));

        mockMvc.perform(get("/services/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("services/form"))
                .andExpect(model().attributeExists("service"));
    }

    @Test
    public void showEditForm_WithInvalidId_ShouldThrowException() throws Exception {
        Mockito.when(serviceRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception e = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> mockMvc.perform(get("/services/edit/1")));
        org.junit.jupiter.api.Assertions.assertTrue(e.getCause() instanceof IllegalArgumentException);
    }

    @Test
    public void save_ShouldRedirectToServices() throws Exception {
        mockMvc.perform(post("/services/save")
                .param("name", "Test Service"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/services"));

        Mockito.verify(serviceRepository).save(any(Service.class));
    }

    @Test
    public void delete_WithValidId_ShouldRedirect() throws Exception {
        Service service = new Service();
        Mockito.when(serviceRepository.findById(anyLong())).thenReturn(Optional.of(service));

        mockMvc.perform(get("/services/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/services"));

        Mockito.verify(serviceRepository).delete(service);
    }
    
    @Test
    public void delete_WithInvalidId_ShouldThrowException() throws Exception {
        Mockito.when(serviceRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception e = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> mockMvc.perform(get("/services/delete/1")));
        org.junit.jupiter.api.Assertions.assertTrue(e.getCause() instanceof IllegalArgumentException);
    }
}
