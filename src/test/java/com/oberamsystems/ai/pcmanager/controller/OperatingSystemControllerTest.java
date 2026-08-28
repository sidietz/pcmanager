package com.oberamsystems.ai.pcmanager.controller;

import com.oberamsystems.ai.pcmanager.model.OperatingSystem;
import com.oberamsystems.ai.pcmanager.repository.OperatingSystemRepository;
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

@WebMvcTest(OperatingSystemController.class)
public class OperatingSystemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OperatingSystemRepository osRepository;

    @Test
    public void list_ShouldReturnOsView() throws Exception {
        Mockito.when(osRepository.findAll()).thenReturn(Arrays.asList(new OperatingSystem()));

        mockMvc.perform(get("/os"))
                .andExpect(status().isOk())
                .andExpect(view().name("os/list"))
                .andExpect(model().attributeExists("oss"));
    }

    @Test
    public void showAddForm_ShouldReturnFormView() throws Exception {
        mockMvc.perform(get("/os/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("os/form"))
                .andExpect(model().attributeExists("os"))
                .andExpect(model().attributeExists("families"))
                .andExpect(model().attributeExists("releaseTypes"));
    }

    @Test
    public void showEditForm_WithValidId_ShouldReturnFormView() throws Exception {
        OperatingSystem os = new OperatingSystem();
        Mockito.when(osRepository.findById(anyLong())).thenReturn(Optional.of(os));

        mockMvc.perform(get("/os/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("os/form"))
                .andExpect(model().attributeExists("os"));
    }

    @Test
    public void showEditForm_WithInvalidId_ShouldThrowException() throws Exception {
        Mockito.when(osRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception e = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> mockMvc.perform(get("/os/edit/1")));
        org.junit.jupiter.api.Assertions.assertTrue(e.getCause() instanceof IllegalArgumentException);
    }

    @Test
    public void save_ShouldRedirectToOs() throws Exception {
        mockMvc.perform(post("/os/save")
                .param("name", "Linux"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/os"));

        Mockito.verify(osRepository).save(any(OperatingSystem.class));
    }

    @Test
    public void delete_WithValidId_ShouldRedirect() throws Exception {
        OperatingSystem os = new OperatingSystem();
        Mockito.when(osRepository.findById(anyLong())).thenReturn(Optional.of(os));

        mockMvc.perform(get("/os/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/os"));

        Mockito.verify(osRepository).delete(os);
    }
    
    @Test
    public void delete_WithInvalidId_ShouldThrowException() throws Exception {
        Mockito.when(osRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception e = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> mockMvc.perform(get("/os/delete/1")));
        org.junit.jupiter.api.Assertions.assertTrue(e.getCause() instanceof IllegalArgumentException);
    }
}
