package com.jagsii.springxx.common.sys;

import com.jagsii.springxx.common.security.PermGrantedAuthority;
import com.jagsii.springxx.common.security.PermissionData;
import com.jagsii.springxx.modules.system.entity.Btn;
import com.jagsii.springxx.modules.system.service.BtnService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BtnManager {
    private List<Btn> cache;
    private final BtnService btnService;

    @PostConstruct
    public void init() {
        reload();
    }

    public void reload() {
        cache = btnService.findByMap(Collections.emptyMap());
    }

    public List<Btn> getAuthorized(Collection<GrantedAuthority> authorities) {
        List<Btn> result = new ArrayList<>();
        for (Btn btn : cache) {
            if (StringUtils.isEmpty(btn.getPerm())) {
                result.add(btn);
                continue;
            }
            PermissionData perm = new PermissionData(btn.getPerm());
            for (GrantedAuthority authority : authorities) {
                if (!(authority instanceof PermGrantedAuthority)) {
                    continue;
                }
                if (((PermGrantedAuthority) authority).getPerm().hasPermission(perm)) {
                    result.add(btn);
                    break;
                }
            }
        }
        return result;
    }
}
