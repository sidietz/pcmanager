package com.oberamsystems.ai.pcmanager.controller;

import com.oberamsystems.ai.pcmanager.model.SoftwareStack;
import com.oberamsystems.ai.pcmanager.repository.ServiceRepository;
import com.oberamsystems.ai.pcmanager.repository.SoftwareRepository;
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

@WebMvcTest(SoftwareStackController.class)
public class SoftwareStackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SoftwareStackRepository stackRepository;

    @MockitoBean
    private SoftwareRepository softwareRepository;

    @MockitoBean
    private ServiceRepository serviceRepository;

    @Test
    public void list_WithoutParams_ShouldReturnStacksView() throws Exception {
        Mockito.when(stackRepository.findAll()).thenReturn(Arrays.asList(new SoftwareStack()));

        mockMvc.perform(get("/software-stacks"))
                .andExpect(status().isOk())
                .andExpect(view().name("software-stacks/list"))
                .andExpect(model().attributeExists("stacks"));
    }
    
    @Test
    public void list_WithStackId_ShouldReturnStacksView() throws Exception {
        SoftwareStack stack = new SoftwareStack();
        stack.setSoftwareList(Arrays.asList());
        Mockito.when(stackRepository.findAll()).thenReturn(Arrays.asList(stack));
        Mockito.when(stackRepository.findById(1L)).thenReturn(Optional.of(stack));

        mockMvc.perform(get("/software-stacks").param("stackId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("software-stacks/list"))
                .andExpect(model().attributeExists("stacks"))
                .andExpect(model().attributeExists("selectedStack"))
                .andExpect(model().attributeExists("stackSoftware"));
    }
    
    @Test
    public void list_WithServiceStackId_ShouldReturnStacksView() throws Exception {
        SoftwareStack stack = new SoftwareStack();
        stack.setSoftwareList(Arrays.asList());
        Mockito.when(stackRepository.findAll()).thenReturn(Arrays.asList(stack));
        Mockito.when(stackRepository.findById(1L)).thenReturn(Optional.of(stack));
        Mockito.when(serviceRepository.findBySoftwareStackId(1L)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/software-stacks").param("serviceStackId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("software-stacks/list"))
                .andExpect(model().attributeExists("stacks"))
                .andExpect(model().attributeExists("selectedServiceStack"))
                .andExpect(model().attributeExists("stackServices"));
    }

    @Test
    public void showAddForm_ShouldReturnFormView() throws Exception {
        mockMvc.perform(get("/software-stacks/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("software-stacks/form"))
                .andExpect(model().attributeExists("stack"))
                .andExpect(model().attributeExists("allSoftware"));
    }

    @Test
    public void showEditForm_WithValidId_ShouldReturnFormView() throws Exception {
        SoftwareStack stack = new SoftwareStack();
        Mockito.when(stackRepository.findById(anyLong())).thenReturn(Optional.of(stack));

        mockMvc.perform(get("/software-stacks/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("software-stacks/form"))
                .andExpect(model().attributeExists("stack"));
    }

    @Test
    public void showEditForm_WithInvalidId_ShouldThrowException() throws Exception {
        Mockito.when(stackRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception e = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> mockMvc.perform(get("/software-stacks/edit/1")));
        org.junit.jupiter.api.Assertions.assertTrue(e.getCause() instanceof IllegalArgumentException);
    }

    @Test
    public void save_ShouldRedirectToStacks() throws Exception {
        mockMvc.perform(post("/software-stacks/save")
                .param("name", "Test Stack"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/software-stacks"));

        Mockito.verify(stackRepository).save(any(SoftwareStack.class));
    }

    @Test
    public void delete_WithValidId_ShouldRedirect() throws Exception {
        SoftwareStack stack = new SoftwareStack();
        Mockito.when(stackRepository.findById(anyLong())).thenReturn(Optional.of(stack));

        mockMvc.perform(get("/software-stacks/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/software-stacks"));

        Mockito.verify(stackRepository).delete(stack);
    }
    
    @Test
    public void delete_WithInvalidId_ShouldThrowException() throws Exception {
        Mockito.when(stackRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception e = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> mockMvc.perform(get("/software-stacks/delete/1")));
        org.junit.jupiter.api.Assertions.assertTrue(e.getCause() instanceof IllegalArgumentException);
    }
}
