package com.oberamsystems.ai.pcmanager.controller;

import com.oberamsystems.ai.pcmanager.model.DatabaseProduct;
import com.oberamsystems.ai.pcmanager.repository.DatabaseProductRepository;
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

@WebMvcTest(DatabaseProductController.class)
public class DatabaseProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DatabaseProductRepository productRepository;

    @Test
    public void list_ShouldReturnProductsView() throws Exception {
        Mockito.when(productRepository.findAll()).thenReturn(Arrays.asList(new DatabaseProduct()));

        mockMvc.perform(get("/database-products"))
                .andExpect(status().isOk())
                .andExpect(view().name("database-products/list"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    public void showAddForm_ShouldReturnFormView() throws Exception {
        mockMvc.perform(get("/database-products/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("database-products/form"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    public void showEditForm_WithValidId_ShouldReturnFormView() throws Exception {
        DatabaseProduct product = new DatabaseProduct();
        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        mockMvc.perform(get("/database-products/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("database-products/form"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    public void showEditForm_WithInvalidId_ShouldThrowException() throws Exception {
        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception e = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> mockMvc.perform(get("/database-products/edit/1")));
        org.junit.jupiter.api.Assertions.assertTrue(e.getCause() instanceof IllegalArgumentException);
    }

    @Test
    public void save_ShouldRedirectToProducts() throws Exception {
        mockMvc.perform(post("/database-products/save")
                .param("name", "PostgreSQL"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/database-products"));

        Mockito.verify(productRepository).save(any(DatabaseProduct.class));
    }

    @Test
    public void delete_WithValidId_ShouldRedirect() throws Exception {
        DatabaseProduct product = new DatabaseProduct();
        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        mockMvc.perform(get("/database-products/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/database-products"));

        Mockito.verify(productRepository).delete(product);
    }
    
    @Test
    public void delete_WithInvalidId_ShouldThrowException() throws Exception {
        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception e = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> mockMvc.perform(get("/database-products/delete/1")));
        org.junit.jupiter.api.Assertions.assertTrue(e.getCause() instanceof IllegalArgumentException);
    }
}
