package com.oberamsystems.ai.pcmanager.controller;

import com.oberamsystems.ai.pcmanager.model.DatabaseInstance;
import com.oberamsystems.ai.pcmanager.repository.DatabaseInstanceRepository;
import com.oberamsystems.ai.pcmanager.repository.DatabaseProductRepository;
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

@WebMvcTest(DatabaseInstanceController.class)
public class DatabaseInstanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DatabaseInstanceRepository instanceRepository;

    @MockitoBean
    private DatabaseProductRepository productRepository;

    @MockitoBean
    private SoftwareStackRepository stackRepository;

    @Test
    public void list_ShouldReturnInstancesView() throws Exception {
        Mockito.when(instanceRepository.findAll()).thenReturn(Arrays.asList(new DatabaseInstance()));

        mockMvc.perform(get("/database-instances"))
                .andExpect(status().isOk())
                .andExpect(view().name("database-instances/list"))
                .andExpect(model().attributeExists("instances"));
    }

    @Test
    public void showAddForm_ShouldReturnFormView() throws Exception {
        mockMvc.perform(get("/database-instances/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("database-instances/form"))
                .andExpect(model().attributeExists("instance"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("stacks"));
    }

    @Test
    public void showEditForm_WithValidId_ShouldReturnFormView() throws Exception {
        DatabaseInstance instance = new DatabaseInstance();
        Mockito.when(instanceRepository.findById(anyLong())).thenReturn(Optional.of(instance));

        mockMvc.perform(get("/database-instances/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("database-instances/form"))
                .andExpect(model().attributeExists("instance"));
    }

    @Test
    public void showEditForm_WithInvalidId_ShouldThrowException() throws Exception {
        Mockito.when(instanceRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception e = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> mockMvc.perform(get("/database-instances/edit/1")));
        org.junit.jupiter.api.Assertions.assertTrue(e.getCause() instanceof IllegalArgumentException);
    }

    @Test
    public void save_ShouldRedirectToInstances() throws Exception {
        mockMvc.perform(post("/database-instances/save")
                .param("name", "Test Instance"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/database-instances"));

        Mockito.verify(instanceRepository).save(any(DatabaseInstance.class));
    }

    @Test
    public void delete_WithValidId_ShouldRedirect() throws Exception {
        DatabaseInstance instance = new DatabaseInstance();
        Mockito.when(instanceRepository.findById(anyLong())).thenReturn(Optional.of(instance));

        mockMvc.perform(get("/database-instances/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/database-instances"));

        Mockito.verify(instanceRepository).delete(instance);
    }
    
    @Test
    public void delete_WithInvalidId_ShouldThrowException() throws Exception {
        Mockito.when(instanceRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception e = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> mockMvc.perform(get("/database-instances/delete/1")));
        org.junit.jupiter.api.Assertions.assertTrue(e.getCause() instanceof IllegalArgumentException);
    }
}
