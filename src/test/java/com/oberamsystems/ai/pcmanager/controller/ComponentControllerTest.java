package com.oberamsystems.ai.pcmanager.controller;

import com.oberamsystems.ai.pcmanager.model.Component;
import com.oberamsystems.ai.pcmanager.repository.ComponentRepository;
import com.oberamsystems.ai.pcmanager.repository.ComponentTypeRepository;
import com.oberamsystems.ai.pcmanager.repository.PCRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ComponentController.class)
public class ComponentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ComponentRepository componentRepository;

    @MockitoBean
    private PCRepository pcRepository;

    @MockitoBean
    private ComponentTypeRepository componentTypeRepository;

    @Test
    public void list_ShouldReturnComponentsView() throws Exception {
        Component c1 = new Component();
        c1.setPrice(new BigDecimal("100.0"));
        Component c2 = new Component(); // no price

        Mockito.when(componentRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        mockMvc.perform(get("/components"))
                .andExpect(status().isOk())
                .andExpect(view().name("components/list"))
                .andExpect(model().attributeExists("components"))
                .andExpect(model().attributeExists("totalPrice"));
    }

    @Test
    public void showAddForm_ShouldReturnFormView() throws Exception {
        mockMvc.perform(get("/components/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("components/form"))
                .andExpect(model().attributeExists("component"))
                .andExpect(model().attributeExists("pcs"))
                .andExpect(model().attributeExists("componentTypes"));
    }

    @Test
    public void showEditForm_WithValidId_ShouldReturnFormView() throws Exception {
        Component c = new Component();
        Mockito.when(componentRepository.findById(anyLong())).thenReturn(Optional.of(c));

        mockMvc.perform(get("/components/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("components/form"))
                .andExpect(model().attributeExists("component"));
    }
    
    @Test
    public void showEditForm_WithInvalidId_ShouldThrowException() throws Exception {
        Mockito.when(componentRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception e = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> mockMvc.perform(get("/components/edit/1")));
        org.junit.jupiter.api.Assertions.assertTrue(e.getCause() instanceof IllegalArgumentException);
    }

    @Test
    public void save_ShouldRedirectToComponents() throws Exception {
        mockMvc.perform(post("/components/save")
                .param("model", "Test Model"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/components"));

        Mockito.verify(componentRepository).save(any(Component.class));
    }

    @Test
    public void delete_WithValidId_ShouldRedirect() throws Exception {
        Component c = new Component();
        Mockito.when(componentRepository.findById(anyLong())).thenReturn(Optional.of(c));

        mockMvc.perform(get("/components/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/components"));

        Mockito.verify(componentRepository).delete(c);
    }
    
    @Test
    public void delete_WithInvalidId_ShouldThrowException() throws Exception {
        Mockito.when(componentRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception e = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> mockMvc.perform(get("/components/delete/1")));
        org.junit.jupiter.api.Assertions.assertTrue(e.getCause() instanceof IllegalArgumentException);
    }

    @Test
    public void duplicate_WithValidId_ShouldRedirect() throws Exception {
        Component c = new Component();
        Mockito.when(componentRepository.findById(anyLong())).thenReturn(Optional.of(c));

        mockMvc.perform(get("/components/duplicate/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/components"));

        Mockito.verify(componentRepository).save(any(Component.class));
    }
    
    @Test
    public void duplicate_WithInvalidId_ShouldThrowException() throws Exception {
        Mockito.when(componentRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception e = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> mockMvc.perform(get("/components/duplicate/1")));
        org.junit.jupiter.api.Assertions.assertTrue(e.getCause() instanceof IllegalArgumentException);
    }
}
