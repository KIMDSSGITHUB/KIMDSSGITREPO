package com.dss.service;

import com.dss.dto.*;
import com.dss.entity.Action;
import com.dss.entity.Permission;
import com.dss.entity.Resource;
import com.dss.entity.Role;
import com.dss.repository.ActionRepository;
import com.dss.repository.PermissionRepository;
import com.dss.repository.ResourceRepository;
import com.dss.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ActionRepository actionRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    public List<Permission> getPermissions() {
        List<Permission> permissions;
        permissions = permissionRepository.findAll();
        return permissions;
    }

    @Override
    public PermissionRoleResponseDTO createRole(PermissionRoleRequestDTO request) {
        Role role = new Role();
        role.setName(request.getName());
        Role roleEntity = roleRepository.save(role);
        return roleEntityToDTO(new PermissionRoleResponseDTO(), roleEntity);
    }

    @Override
    public PermissionRoleResponseDTO createPermission(PermissionRoleRequestDTO request) {
        Permission permission = new Permission();
        permission.setName(request.getName());
        Permission permissionEntity = permissionRepository.save(permission);
        return permissionEntityToDTO(new PermissionRoleResponseDTO(), permissionEntity);
    }

    @Override
    public Role addPermissionsToRole(UUID permissionId, UUID roleId) {
        Role savedRole = roleRepository.findById(roleId).get();
        Permission savedPermission = permissionRepository.findById(permissionId).get();
        Set<Permission> rolePermissions = savedRole.getPermissions();
        rolePermissions.add(savedPermission);
        savedRole.setPermissions(rolePermissions);
        return roleRepository.save(savedRole);
    }

    @Override
    public Permission addResourceToPermission(UUID resourceId, UUID permissionId) {
        Permission savedPermission = permissionRepository.findById(permissionId).get();
        Resource savedResource = resourceRepository.findById(resourceId).get();
        Set<Resource> permissionResources = savedPermission.getResources();
        permissionResources.add(savedResource);
        savedPermission.setResources(permissionResources);
        return permissionRepository.save(savedPermission);
    }

    @Override
    public Resource addActionToResource(UUID actionId, UUID resourceId) {
        Resource savedResource = resourceRepository.findById(resourceId).get();
        Action savedAction = actionRepository.findById(actionId).get();
        Set<Action> resourceAction = savedResource.getActions();
        resourceAction.add(savedAction);
        savedResource.setActions(resourceAction);
        return resourceRepository.save(savedResource);
    }

    @Override
    public ResourceResponseDTO createResource(ResourceRequestDTO request) {
        Resource resource = new Resource();
        resource.setName(request.getName());
        resource.setPath(request.getPath());
        Resource resourceEntity = resourceRepository.save(resource);
        return resourceEntityToDTO(new ResourceResponseDTO(), resourceEntity);
    }

    @Override
    public ActionResponseDTO creAction(ActionRequestDTO request) {
        Action action = new Action();
        action.setValue(request.getValue());
        Action actionEntity = actionRepository.save(action);
        return actionEntityToDTO(new ActionResponseDTO(), actionEntity);
    }

    private PermissionRoleResponseDTO roleEntityToDTO(PermissionRoleResponseDTO dto, Role entity) {
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    private PermissionRoleResponseDTO permissionEntityToDTO(PermissionRoleResponseDTO dto, Permission entity) {
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    private ResourceResponseDTO resourceEntityToDTO(ResourceResponseDTO dto, Resource entity) {
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPath(entity.getPath());
        return dto;
    }

    private ActionResponseDTO actionEntityToDTO(ActionResponseDTO dto, Action entity) {
        dto.setId(entity.getId());
        dto.setValue(entity.getValue());
        return dto;
    }
}
