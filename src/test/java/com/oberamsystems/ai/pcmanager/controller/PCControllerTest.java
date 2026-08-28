package com.oberamsystems.ai.pcmanager.controller;

import com.oberamsystems.ai.pcmanager.model.PC;
import com.oberamsystems.ai.pcmanager.model.Component;
import com.oberamsystems.ai.pcmanager.repository.ApplianceRepository;
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

@WebMvcTest(PCController.class)
public class PCControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PCRepository pcRepository;

    @MockitoBean
    private ApplianceRepository applianceRepository;

    @Test
    public void list_WithoutPcId_ShouldReturnPcsView() throws Exception {
        PC pc = new PC();
        Component c = new Component();
        c.setPrice(new BigDecimal("100.0"));
        pc.setComponents(Arrays.asList(c));
        
        PC pc2 = new PC(); // no components, no base price
        
        Mockito.when(pcRepository.findAll()).thenReturn(Arrays.asList(pc, pc2));

        mockMvc.perform(get("/pcs"))
                .andExpect(status().isOk())
                .andExpect(view().name("pcs/list"))
                .andExpect(model().attributeExists("pcs"))
                .andExpect(model().attributeExists("totalPrice"));
    }
    
    @Test
    public void list_WithValidPcId_ShouldAddSelectedPcToModel() throws Exception {
        PC pc = new PC();
        pc.setComponents(Arrays.asList());
        Mockito.when(pcRepository.findAll()).thenReturn(Arrays.asList(pc));
        Mockito.when(pcRepository.findById(1L)).thenReturn(Optional.of(pc));

        mockMvc.perform(get("/pcs").param("pcId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("pcs/list"))
                .andExpect(model().attributeExists("selectedPC"))
                .andExpect(model().attributeExists("pcComponents"));
    }
    
    @Test
    public void list_WithInvalidPcId_ShouldNotAddSelectedPcToModel() throws Exception {
        Mockito.when(pcRepository.findAll()).thenReturn(Arrays.asList());
        Mockito.when(pcRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/pcs").param("pcId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("pcs/list"))
                .andExpect(model().attributeDoesNotExist("selectedPC"));
    }

    @Test
    public void showAddForm_ShouldReturnFormView() throws Exception {
        mockMvc.perform(get("/pcs/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("pcs/form"))
                .andExpect(model().attributeExists("pc"))
                .andExpect(model().attributeExists("allAppliances"));
    }

    @Test
    public void showEditForm_WithValidId_ShouldReturnFormView() throws Exception {
        PC pc = new PC();
        Mockito.when(pcRepository.findById(anyLong())).thenReturn(Optional.of(pc));

        mockMvc.perform(get("/pcs/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("pcs/form"))
                .andExpect(model().attributeExists("pc"))
                .andExpect(model().attributeExists("allAppliances"));
    }
    
    @Test
    public void showEditForm_WithInvalidId_ShouldThrowException() throws Exception {
        Mockito.when(pcRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception e = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> mockMvc.perform(get("/pcs/edit/1")));
        org.junit.jupiter.api.Assertions.assertTrue(e.getCause() instanceof IllegalArgumentException);
    }

    @Test
    public void save_ShouldRedirectToPcs() throws Exception {
        mockMvc.perform(post("/pcs/save")
                .param("name", "Test PC"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pcs"));

        Mockito.verify(pcRepository).save(any(PC.class));
    }

    @Test
    public void delete_WithValidId_ShouldRedirect() throws Exception {
        PC pc = new PC();
        Mockito.when(pcRepository.findById(anyLong())).thenReturn(Optional.of(pc));

        mockMvc.perform(get("/pcs/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pcs"));

        Mockito.verify(pcRepository).delete(pc);
    }
    
    @Test
    public void delete_WithInvalidId_ShouldThrowException() throws Exception {
        Mockito.when(pcRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception e = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> mockMvc.perform(get("/pcs/delete/1")));
        org.junit.jupiter.api.Assertions.assertTrue(e.getCause() instanceof IllegalArgumentException);
    }
}
