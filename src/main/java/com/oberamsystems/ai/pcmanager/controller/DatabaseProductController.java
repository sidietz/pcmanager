package com.oberamsystems.ai.pcmanager.controller;

import com.oberamsystems.ai.pcmanager.model.DatabaseProduct;
import com.oberamsystems.ai.pcmanager.repository.DatabaseProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/database-products")
public class DatabaseProductController {

    private final DatabaseProductRepository productRepository;

    public DatabaseProductController(DatabaseProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "database-products/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new DatabaseProduct());
        return "database-products/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        DatabaseProduct product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Database Product Id:" + id));
        model.addAttribute("product", product);
        return "database-products/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute DatabaseProduct product) {
        productRepository.save(product);
        return "redirect:/database-products";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        DatabaseProduct product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Database Product Id:" + id));
        productRepository.delete(product);
        return "redirect:/database-products";
    }
}
