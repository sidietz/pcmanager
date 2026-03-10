package com.oberamsystems.ai.pcmanager.controller;

import com.oberamsystems.ai.pcmanager.model.Appliance;
import com.oberamsystems.ai.pcmanager.repository.ApplianceRepository;
import com.oberamsystems.ai.pcmanager.repository.OperatingSystemRepository;
import com.oberamsystems.ai.pcmanager.repository.SoftwareStackRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/appliances")
public class ApplianceController {

    private final ApplianceRepository applianceRepository;
    private final OperatingSystemRepository osRepository;
    private final SoftwareStackRepository stackRepository;

    public ApplianceController(ApplianceRepository applianceRepository,
            OperatingSystemRepository osRepository,
            SoftwareStackRepository stackRepository) {
        this.applianceRepository = applianceRepository;
        this.osRepository = osRepository;
        this.stackRepository = stackRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("appliances", applianceRepository.findAll());
        return "appliances/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("appliance", new Appliance());
        model.addAttribute("osList", osRepository.findAll());
        model.addAttribute("stacks", stackRepository.findAll());
        return "appliances/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Appliance appliance = applianceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Appliance Id:" + id));
        model.addAttribute("appliance", appliance);
        model.addAttribute("osList", osRepository.findAll());
        model.addAttribute("stacks", stackRepository.findAll());
        return "appliances/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Appliance appliance) {
        applianceRepository.save(appliance);
        return "redirect:/appliances";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Appliance appliance = applianceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Appliance Id:" + id));
        applianceRepository.delete(appliance);
        return "redirect:/appliances";
    }
}
