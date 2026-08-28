package com.oberamsystems.ai.pcmanager.controller;

import com.oberamsystems.ai.pcmanager.model.Software;
import com.oberamsystems.ai.pcmanager.repository.SoftwareRepository;
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

@WebMvcTest(SoftwareController.class)
public class SoftwareControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SoftwareRepository softwareRepository;

    @Test
    public void list_ShouldReturnSoftwareView() throws Exception {
        Mockito.when(softwareRepository.findAll()).thenReturn(Arrays.asList(new Software()));

        mockMvc.perform(get("/software"))
                .andExpect(status().isOk())
                .andExpect(view().name("software/list"))
                .andExpect(model().attributeExists("softwareList"));
    }

    @Test
    public void showAddForm_ShouldReturnFormView() throws Exception {
        mockMvc.perform(get("/software/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("software/form"))
                .andExpect(model().attributeExists("software"))
                .andExpect(model().attributeExists("origins"))
                .andExpect(model().attributeExists("installationTypes"));
    }

    @Test
    public void showEditForm_WithValidId_ShouldReturnFormView() throws Exception {
        Software software = new Software();
        Mockito.when(softwareRepository.findById(anyLong())).thenReturn(Optional.of(software));

        mockMvc.perform(get("/software/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("software/form"))
                .andExpect(model().attributeExists("software"));
    }

    @Test
    public void showEditForm_WithInvalidId_ShouldThrowException() throws Exception {
        Mockito.when(softwareRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception e = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> mockMvc.perform(get("/software/edit/1")));
        org.junit.jupiter.api.Assertions.assertTrue(e.getCause() instanceof IllegalArgumentException);
    }

    @Test
    public void save_ShouldRedirectToSoftware() throws Exception {
        mockMvc.perform(post("/software/save")
                .param("name", "Test Software"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/software"));

        Mockito.verify(softwareRepository).save(any(Software.class));
    }

    @Test
    public void delete_WithValidId_ShouldRedirect() throws Exception {
        Software software = new Software();
        Mockito.when(softwareRepository.findById(anyLong())).thenReturn(Optional.of(software));

        mockMvc.perform(get("/software/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/software"));

        Mockito.verify(softwareRepository).delete(software);
    }
    
    @Test
    public void delete_WithInvalidId_ShouldThrowException() throws Exception {
        Mockito.when(softwareRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception e = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> mockMvc.perform(get("/software/delete/1")));
        org.junit.jupiter.api.Assertions.assertTrue(e.getCause() instanceof IllegalArgumentException);
    }
}
