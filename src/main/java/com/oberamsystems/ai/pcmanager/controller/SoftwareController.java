package com.oberamsystems.ai.pcmanager.controller;

import com.oberamsystems.ai.pcmanager.model.Software;
import com.oberamsystems.ai.pcmanager.model.SoftwareOrigin;
import com.oberamsystems.ai.pcmanager.model.InstallationType;
import com.oberamsystems.ai.pcmanager.repository.SoftwareRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/software")
public class SoftwareController {

    private final SoftwareRepository softwareRepository;

    public SoftwareController(SoftwareRepository softwareRepository) {
        this.softwareRepository = softwareRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("softwareList", softwareRepository.findAll());
        return "software/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("software", new Software());
        model.addAttribute("origins", SoftwareOrigin.values());
        model.addAttribute("installationTypes", InstallationType.values());
        return "software/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Software software = softwareRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Software Id:" + id));
        model.addAttribute("software", software);
        model.addAttribute("origins", SoftwareOrigin.values());
        model.addAttribute("installationTypes", InstallationType.values());
        return "software/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Software software) {
        softwareRepository.save(software);
        return "redirect:/software";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Software software = softwareRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Software Id:" + id));
        softwareRepository.delete(software);
        return "redirect:/software";
    }
}
