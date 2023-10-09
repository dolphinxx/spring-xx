package com.jagsii.springxx.common.security;

import com.jagsii.springxx.modules.system.entity.User;
import com.jagsii.springxx.modules.system.service.RoleService;
import com.jagsii.springxx.modules.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
@RequiredArgsConstructor
public class MyUserDetailsServiceImpl implements MyUserDetailsService {
    private final UserService userService;
    private final RoleService roleService;
    private Map<Long, RoleInfo> roleCache;

    @PostConstruct
    private void init() {
        reloadAuthorities();
    }

    @Override
    public void reloadAuthorities() {
        List<Map<String, Object>> list = roleService.findAllRolesAndPerms();
        Map<Long, RoleInfo> cache = new HashMap<>();
        for (Map<String, Object> m : list) {
            Long id = (Long) m.get("id");
            RoleInfo role = cache.computeIfAbsent(id, key -> new RoleInfo(id, (String) m.get("role_value"), (Long) m.get("parent_id")));
            if (m.containsKey("perm_value")) {
                role.addPerm((String) m.get("perm_value"));
            }
        }
        for (RoleInfo role : cache.values()) {
            if (role.getParentId() != null) {
                cache.get(role.getParentId()).addChild(role);
            }
        }
        roleCache = cache;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        List<Long> roleIds = roleService.findRoleIdsByUserId(user.getId());
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Long roleId : roleIds) {
            RoleInfo role = roleCache.get(roleId);
            if (role == null) {
                continue;
            }
            collectAuthorities(authorities, role);
        }
        return new Principal(user.getName(), user.getUsername(), user.getPassword(), authorities, Objects.equals(user.getStatus(), 1), true, true, true);
    }

    private void collectAuthorities(List<GrantedAuthority> authorities, RoleInfo role) {
        authorities.add(new RoleGrantedAuthority(role.getValue()));
        if (role.getPerms().size() > 0) {
            for (String perm : role.getPerms()) {
                authorities.add(new PermGrantedAuthority(perm));
            }
        }
        if (role.getChildren().size() > 0) {
            for (RoleInfo child : role.getChildren()) {
                collectAuthorities(authorities, child);
            }
        }
    }
}
