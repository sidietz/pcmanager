package com.oberamsystems.ai.pcmanager.controller;

import com.oberamsystems.ai.pcmanager.model.Service;
import com.oberamsystems.ai.pcmanager.repository.ServiceRepository;
import com.oberamsystems.ai.pcmanager.repository.SoftwareStackRepository;
import com.oberamsystems.ai.pcmanager.repository.SoftwareRepository;
import com.oberamsystems.ai.pcmanager.repository.SubdatabaseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/services")
public class ServiceController {

    private final ServiceRepository serviceRepository;
    private final SoftwareStackRepository stackRepository;
    private final SoftwareRepository softwareRepository;
    private final SubdatabaseRepository subdatabaseRepository;

    public ServiceController(ServiceRepository serviceRepository,
                             SoftwareStackRepository stackRepository,
                             SoftwareRepository softwareRepository,
                             SubdatabaseRepository subdatabaseRepository) {
        this.serviceRepository = serviceRepository;
        this.stackRepository = stackRepository;
        this.softwareRepository = softwareRepository;
        this.subdatabaseRepository = subdatabaseRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("services", serviceRepository.findAll());
        return "services/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("service", new Service());
        model.addAttribute("stacks", stackRepository.findAll());
        model.addAttribute("allSoftware", softwareRepository.findAll());
        model.addAttribute("allSubdatabases", subdatabaseRepository.findAll());
        return "services/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Service Id:" + id));
        model.addAttribute("service", service);
        model.addAttribute("stacks", stackRepository.findAll());
        model.addAttribute("allSoftware", softwareRepository.findAll());
        model.addAttribute("allSubdatabases", subdatabaseRepository.findAll());
        return "services/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Service service) {
        serviceRepository.save(service);
        return "redirect:/services";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Service Id:" + id));
        serviceRepository.delete(service);
        return "redirect:/services";
    }
}
