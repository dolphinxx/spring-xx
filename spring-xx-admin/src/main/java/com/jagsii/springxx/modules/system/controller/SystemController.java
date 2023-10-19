package com.jagsii.springxx.modules.system.controller;

import com.jagsii.springxx.common.security.MyUserDetailsService;
import com.jagsii.springxx.common.security.Principal;
import com.jagsii.springxx.common.security.SecurityUtils;
import com.jagsii.springxx.common.security.access.annotation.HasPerm;
import com.jagsii.springxx.common.sys.BtnManager;
import com.jagsii.springxx.common.web.R;
import com.jagsii.springxx.common.web.RR;
import com.jagsii.springxx.modules.system.entity.Btn;
import com.jagsii.springxx.modules.system.entity.Parameter;
import com.jagsii.springxx.modules.system.service.ParameterService;
import com.jagsii.springxx.mybatis.Criterion;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sys")
public class SystemController {
    private final ParameterService parameterService;
    private final BtnManager btnManager;
    private final MyUserDetailsService myUserDetailsService;

    /**
     * Get platform settings.
     * <pre>
     *     {
     *         "name": ""
     *     }
     * </pre>
     */
    @GetMapping("/settings")
    public RR settings() {
        List<Parameter> list = parameterService.findByCriteria(Criterion.lambdaQuery(Parameter.class).eq(Parameter::getKey, "settings").limit(1));
        return new RR(list.isEmpty() ? "{}" : list.get(0).getValue());
    }

    @GetMapping("/principal")
    public R<Principal> readPrincipal(Principal principal, HttpServletResponse response) {
        SecurityUtils.sendAuthCookie(response);
        return R.success(principal);
    }

    /**
     * Get authorized buttons.
     */
    @GetMapping("/buttons")
    public R<List<Btn>> readButtons(Principal principal) {
        return R.success(btnManager.getAuthorized(principal.getAuthorities()));
    }

    /**
     * Reload buttons
     */
    @HasPerm("sys")
    @PostMapping("/reload_buttons")
    public R<Void> reloadButtons() {
        btnManager.reload();
        return R.success();
    }

    /**
     * Reload authorities(roles and perms)
     */
    @HasPerm("sys")
    @PostMapping("/reload_authorities")
    public R<Void> reloadAuthorities() {
        myUserDetailsService.reloadAuthorities();
        return R.success();
    }
}
