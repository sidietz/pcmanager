package com.oberamsystems.ai.pcmanager.controller;

import com.oberamsystems.ai.pcmanager.model.SoftwareStack;
import com.oberamsystems.ai.pcmanager.repository.SoftwareStackRepository;
import com.oberamsystems.ai.pcmanager.repository.SoftwareRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/software-stacks")
public class SoftwareStackController {

    private final SoftwareStackRepository stackRepository;
    private final SoftwareRepository softwareRepository;

    public SoftwareStackController(SoftwareStackRepository stackRepository, SoftwareRepository softwareRepository) {
        this.stackRepository = stackRepository;
        this.softwareRepository = softwareRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("stacks", stackRepository.findAll());
        return "software-stacks/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("stack", new SoftwareStack());
        model.addAttribute("allSoftware", softwareRepository.findAll());
        return "software-stacks/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        SoftwareStack stack = stackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Stack Id:" + id));
        model.addAttribute("stack", stack);
        model.addAttribute("allSoftware", softwareRepository.findAll());
        return "software-stacks/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute SoftwareStack stack) {
        stackRepository.save(stack);
        return "redirect:/software-stacks";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        SoftwareStack stack = stackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Stack Id:" + id));
        stackRepository.delete(stack);
        return "redirect:/software-stacks";
    }
}
