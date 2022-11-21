package com.dss.service;

import com.dss.dto.*;
import com.dss.entity.Permission;
import com.dss.entity.Resource;
import com.dss.entity.Role;

import java.util.List;
import java.util.UUID;

public interface AuthService {
   List<Permission> getPermissions();
   PermissionRoleResponseDTO createRole(PermissionRoleRequestDTO request);
   PermissionRoleResponseDTO createPermission(PermissionRoleRequestDTO request);
   Role addPermissionsToRole(UUID permissionId, UUID roleId);
   Permission addResourceToPermission(UUID resourceId, UUID permissionId);
   Resource addActionToResource(UUID actionId, UUID resourceId);
   ResourceResponseDTO createResource(ResourceRequestDTO request);
   ActionResponseDTO creAction(ActionRequestDTO request);
}
