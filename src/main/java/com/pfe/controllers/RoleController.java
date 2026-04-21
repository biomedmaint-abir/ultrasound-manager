package com.pfe.controllers;

import com.pfe.entities.Role;
import com.pfe.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public List<Role> getAll() { return roleService.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getById(@PathVariable Long id) {
        return roleService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Role create(@RequestBody Role role) { return roleService.save(role); }

    @PutMapping("/{id}")
    public ResponseEntity<Role> update(@PathVariable Long id, @RequestBody Role role) {
        return roleService.findById(id).map(r -> { role.setId(id); return ResponseEntity.ok(roleService.save(role)); }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roleService.deleteById(id); return ResponseEntity.noContent().build();
    }
}