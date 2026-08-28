package com.oberamsystems.ai.pcmanager.controller;

import com.oberamsystems.ai.pcmanager.model.Subdatabase;
import com.oberamsystems.ai.pcmanager.repository.DatabaseInstanceRepository;
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

@WebMvcTest(SubdatabaseController.class)
public class SubdatabaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SubdatabaseRepository subdatabaseRepository;

    @MockitoBean
    private DatabaseInstanceRepository instanceRepository;

    @Test
    public void list_ShouldReturnSubdatabasesView() throws Exception {
        Mockito.when(subdatabaseRepository.findAll()).thenReturn(Arrays.asList(new Subdatabase()));

        mockMvc.perform(get("/subdatabases"))
                .andExpect(status().isOk())
                .andExpect(view().name("subdatabases/list"))
                .andExpect(model().attributeExists("subdatabases"));
    }

    @Test
    public void showAddForm_ShouldReturnFormView() throws Exception {
        mockMvc.perform(get("/subdatabases/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("subdatabases/form"))
                .andExpect(model().attributeExists("subdatabase"))
                .andExpect(model().attributeExists("instances"));
    }

    @Test
    public void showEditForm_WithValidId_ShouldReturnFormView() throws Exception {
        Subdatabase subdatabase = new Subdatabase();
        Mockito.when(subdatabaseRepository.findById(anyLong())).thenReturn(Optional.of(subdatabase));

        mockMvc.perform(get("/subdatabases/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("subdatabases/form"))
                .andExpect(model().attributeExists("subdatabase"));
    }

    @Test
    public void showEditForm_WithInvalidId_ShouldThrowException() throws Exception {
        Mockito.when(subdatabaseRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception e = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> mockMvc.perform(get("/subdatabases/edit/1")));
        org.junit.jupiter.api.Assertions.assertTrue(e.getCause() instanceof IllegalArgumentException);
    }

    @Test
    public void save_ShouldRedirectToSubdatabases() throws Exception {
        mockMvc.perform(post("/subdatabases/save")
                .param("name", "Test Subdb"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/subdatabases"));

        Mockito.verify(subdatabaseRepository).save(any(Subdatabase.class));
    }

    @Test
    public void delete_WithValidId_ShouldRedirect() throws Exception {
        Subdatabase subdatabase = new Subdatabase();
        Mockito.when(subdatabaseRepository.findById(anyLong())).thenReturn(Optional.of(subdatabase));

        mockMvc.perform(get("/subdatabases/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/subdatabases"));

        Mockito.verify(subdatabaseRepository).delete(subdatabase);
    }
    
    @Test
    public void delete_WithInvalidId_ShouldThrowException() throws Exception {
        Mockito.when(subdatabaseRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception e = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> mockMvc.perform(get("/subdatabases/delete/1")));
        org.junit.jupiter.api.Assertions.assertTrue(e.getCause() instanceof IllegalArgumentException);
    }
}
