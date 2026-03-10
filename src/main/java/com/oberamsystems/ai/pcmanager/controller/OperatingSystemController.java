package com.oberamsystems.ai.pcmanager.controller;

import com.oberamsystems.ai.pcmanager.model.OperatingSystem;
import com.oberamsystems.ai.pcmanager.model.OSFamily;
import com.oberamsystems.ai.pcmanager.model.ReleaseType;
import com.oberamsystems.ai.pcmanager.repository.OperatingSystemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/os")
public class OperatingSystemController {

    private final OperatingSystemRepository osRepository;

    public OperatingSystemController(OperatingSystemRepository osRepository) {
        this.osRepository = osRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("oss", osRepository.findAll());
        return "os/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("os", new OperatingSystem());
        model.addAttribute("families", OSFamily.values());
        model.addAttribute("releaseTypes", ReleaseType.values());
        return "os/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        OperatingSystem os = osRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid OS Id:" + id));
        model.addAttribute("os", os);
        model.addAttribute("families", OSFamily.values());
        model.addAttribute("releaseTypes", ReleaseType.values());
        return "os/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute OperatingSystem os) {
        osRepository.save(os);
        return "redirect:/os";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        OperatingSystem os = osRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid OS Id:" + id));
        osRepository.delete(os);
        return "redirect:/os";
    }
}
