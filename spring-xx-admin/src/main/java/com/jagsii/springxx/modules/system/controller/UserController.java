package com.jagsii.springxx.modules.system.controller;

import com.jagsii.springxx.common.base.BaseController;
import com.jagsii.springxx.common.pagination.Page;
import com.jagsii.springxx.common.pagination.PageRequest;
import com.jagsii.springxx.common.security.Principal;
import com.jagsii.springxx.common.security.SecurityUtils;
import com.jagsii.springxx.common.security.access.annotation.HasPerm;
import com.jagsii.springxx.common.web.R;
import com.jagsii.springxx.modules.system.entity.User;
import com.jagsii.springxx.modules.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/system/user")
public class UserController extends BaseController {
    private final UserService userService;

    @HasPerm("user:read")
    @GetMapping("/list")
    public R<List<User>> list(@RequestParam Map<String, Object> map) {
        return R.success(userService.findByMap(map));
    }

    @HasPerm("user:read")
    @GetMapping("/paginate")
    public R<Page<User>> paginate(@RequestParam Map<String, Object> map, PageRequest page) {
        return R.success(userService.findPageByMap(map, page));
    }

    @HasPerm("user:read")
    @GetMapping("/read")
    public R<User> read(Long id) {
        return R.success(userService.findById(id));
    }

    @HasPerm("user:create")
    @PostMapping("/create")
    public R<User> create(User entity) {
        userService.save(entity);
        return R.success(entity);
    }

    @HasPerm("user:update")
    @PostMapping("/update")
    public R<User> update(User entity) {
        userService.updateById(entity);
        return R.success(entity);
    }

    @HasPerm("user:delete")
    @PostMapping("/delete")
    public R<Void> delete(Long id) {
        userService.deleteById(id);
        return R.success();
    }

    @HasPerm("user:update")
    @PreAuthorize("hasPermission('user:update')")
    @PostMapping("/reset_password")
    public R<String> resetPassword(Long id) {
        String newPassword = SecurityUtils.generateRandomPassword(10);
        String encodedPassword = new BCryptPasswordEncoder().encode(newPassword);
        userService.updatePassword(id, encodedPassword);
        return R.success();
    }

    @PostMapping("/change_password")
    public R<Void> changePassword(String oldPassword, String newPassword, Principal principal) {
        // TODO
        return R.success();
    }
}
