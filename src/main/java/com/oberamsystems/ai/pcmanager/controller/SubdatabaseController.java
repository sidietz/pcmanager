package com.oberamsystems.ai.pcmanager.controller;

import com.oberamsystems.ai.pcmanager.model.Subdatabase;
import com.oberamsystems.ai.pcmanager.repository.SubdatabaseRepository;
import com.oberamsystems.ai.pcmanager.repository.DatabaseInstanceRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/subdatabases")
public class SubdatabaseController {

    private final SubdatabaseRepository subdatabaseRepository;
    private final DatabaseInstanceRepository instanceRepository;

    public SubdatabaseController(SubdatabaseRepository subdatabaseRepository,
                                 DatabaseInstanceRepository instanceRepository) {
        this.subdatabaseRepository = subdatabaseRepository;
        this.instanceRepository = instanceRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("subdatabases", subdatabaseRepository.findAll());
        return "subdatabases/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("subdatabase", new Subdatabase());
        model.addAttribute("instances", instanceRepository.findAll());
        return "subdatabases/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Subdatabase subdatabase = subdatabaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Subdatabase Id:" + id));
        model.addAttribute("subdatabase", subdatabase);
        model.addAttribute("instances", instanceRepository.findAll());
        return "subdatabases/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Subdatabase subdatabase) {
        subdatabaseRepository.save(subdatabase);
        return "redirect:/subdatabases";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Subdatabase subdatabase = subdatabaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Subdatabase Id:" + id));
        subdatabaseRepository.delete(subdatabase);
        return "redirect:/subdatabases";
    }
}
