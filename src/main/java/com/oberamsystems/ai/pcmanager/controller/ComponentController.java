package com.oberamsystems.ai.pcmanager.controller;

import com.oberamsystems.ai.pcmanager.model.Component;
import com.oberamsystems.ai.pcmanager.repository.ComponentRepository;
import com.oberamsystems.ai.pcmanager.repository.ComponentTypeRepository;
import com.oberamsystems.ai.pcmanager.repository.PCRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/components")
public class ComponentController {

    private final ComponentRepository componentRepository;
    private final PCRepository pcRepository;
    private final ComponentTypeRepository componentTypeRepository;

    public ComponentController(ComponentRepository componentRepository, PCRepository pcRepository, ComponentTypeRepository componentTypeRepository) {
        this.componentRepository = componentRepository;
        this.pcRepository = pcRepository;
        this.componentTypeRepository = componentTypeRepository;
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
        model.addAttribute("componentTypes", componentTypeRepository.findAll());
        return "components/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Component component = componentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Component Id:" + id));
        model.addAttribute("component", component);
        model.addAttribute("pcs", pcRepository.findAll());
        model.addAttribute("componentTypes", componentTypeRepository.findAll());
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

    @GetMapping("/duplicate/{id}")
    public String duplicate(@PathVariable Long id) {
        Component existingComponent = componentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Component Id:" + id));
        
        Component newComponent = new Component();
        newComponent.setComponentType(existingComponent.getComponentType());
        newComponent.setModel(existingComponent.getModel());
        newComponent.setManufacturer(existingComponent.getManufacturer());
        newComponent.setVendor(existingComponent.getVendor());
        newComponent.setPrice(existingComponent.getPrice());
        newComponent.setBoughtAt(existingComponent.getBoughtAt());
        newComponent.setPc(existingComponent.getPc());
        
        componentRepository.save(newComponent);
        return "redirect:/components";
    }
}
