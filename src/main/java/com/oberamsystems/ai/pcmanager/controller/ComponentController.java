package com.oberamsystems.ai.pcmanager.controller;

import com.oberamsystems.ai.pcmanager.model.Component;
import com.oberamsystems.ai.pcmanager.repository.ComponentRepository;
import com.oberamsystems.ai.pcmanager.repository.PCRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/components")
public class ComponentController {

    private final ComponentRepository componentRepository;
    private final PCRepository pcRepository;

    public ComponentController(ComponentRepository componentRepository, PCRepository pcRepository) {
        this.componentRepository = componentRepository;
        this.pcRepository = pcRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("components", componentRepository.findAll());
        return "components/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("component", new Component());
        model.addAttribute("pcs", pcRepository.findAll());
        return "components/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Component component = componentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Component Id:" + id));
        model.addAttribute("component", component);
        model.addAttribute("pcs", pcRepository.findAll());
        return "components/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Component component) {
        componentRepository.save(component);
        return "redirect:/components";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Component component = componentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Component Id:" + id));
        componentRepository.delete(component);
        return "redirect:/components";
    }
}
