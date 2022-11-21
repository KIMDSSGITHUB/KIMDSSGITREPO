package com.dss.controller;

import com.dss.dto.*;
import com.dss.entity.Permission;
import com.dss.entity.Resource;
import com.dss.entity.Role;
import com.dss.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/role")
    public ResponseEntity<PermissionRoleResponseDTO> createRole(@Valid @RequestBody PermissionRoleRequestDTO request) {
        return new ResponseEntity<>(authService.createRole(request), HttpStatus.CREATED);
    }
    @PostMapping("/permission")
    public ResponseEntity<PermissionRoleResponseDTO> createPermission(@Valid @RequestBody PermissionRoleRequestDTO request) {
        return new ResponseEntity<>(authService.createPermission(request), HttpStatus.CREATED);
    }
    @GetMapping("/permissions")
    public ResponseEntity<List<Permission>> getPermissions() {
        return new ResponseEntity<>(authService.getPermissions(), HttpStatus.OK);
    }
    @PostMapping("/role/permission")
    public ResponseEntity<Role> addPermissionToRole(@Valid @RequestParam UUID permissionId, @RequestParam UUID roleId) {
        return new ResponseEntity<>(authService.addPermissionsToRole(permissionId, roleId), HttpStatus.CREATED);
    }
    @PostMapping("/resource")
    public ResponseEntity<ResourceResponseDTO> createResource(@Valid @RequestBody ResourceRequestDTO request) {
        return new ResponseEntity<>(authService.createResource(request), HttpStatus.CREATED);
    }
    @PostMapping("/permission/resource")
    public ResponseEntity<Permission> addResourceToPermission(@Valid @RequestParam UUID resourceId, @RequestParam UUID permissionId) {
        return new ResponseEntity<>(authService.addResourceToPermission(resourceId, permissionId), HttpStatus.CREATED);
    }
    @PostMapping("/action")
    public ResponseEntity<ActionResponseDTO> createAction(@Valid @RequestBody ActionRequestDTO request) {
        return new ResponseEntity<>(authService.creAction(request), HttpStatus.CREATED);
    }
    @PostMapping("/resource/action")
    public ResponseEntity<Resource> addActionToResource(@Valid @RequestParam UUID actionId, @RequestParam UUID resourceId) {
        return new ResponseEntity<>(authService.addActionToResource(actionId, resourceId), HttpStatus.CREATED);
    }
}
