package com.oberamsystems.ai.pcmanager.controller;

import com.oberamsystems.ai.pcmanager.model.DatabaseInstance;
import com.oberamsystems.ai.pcmanager.repository.DatabaseInstanceRepository;
import com.oberamsystems.ai.pcmanager.repository.DatabaseProductRepository;
import com.oberamsystems.ai.pcmanager.repository.SoftwareStackRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/database-instances")
public class DatabaseInstanceController {

    private final DatabaseInstanceRepository instanceRepository;
    private final DatabaseProductRepository productRepository;
    private final SoftwareStackRepository stackRepository;

    public DatabaseInstanceController(DatabaseInstanceRepository instanceRepository,
                                      DatabaseProductRepository productRepository,
                                      SoftwareStackRepository stackRepository) {
        this.instanceRepository = instanceRepository;
        this.productRepository = productRepository;
        this.stackRepository = stackRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("instances", instanceRepository.findAll());
        return "database-instances/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("instance", new DatabaseInstance());
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("stacks", stackRepository.findAll());
        return "database-instances/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        DatabaseInstance instance = instanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Database Instance Id:" + id));
        model.addAttribute("instance", instance);
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("stacks", stackRepository.findAll());
        return "database-instances/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute DatabaseInstance instance) {
        instanceRepository.save(instance);
        return "redirect:/database-instances";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        DatabaseInstance instance = instanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Database Instance Id:" + id));
        instanceRepository.delete(instance);
        return "redirect:/database-instances";
    }
}
