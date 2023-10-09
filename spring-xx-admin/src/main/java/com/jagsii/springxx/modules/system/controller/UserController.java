package com.jagsii.springxx.modules.system.controller;

import com.jagsii.springxx.common.base.BaseController;
import com.jagsii.springxx.common.pagination.Page;
import com.jagsii.springxx.common.pagination.PageRequest;
import com.jagsii.springxx.common.security.CurrentUser;
import com.jagsii.springxx.common.security.SecurityUtils;
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

    @GetMapping("/list")
    public R<List<User>> list(@RequestParam Map<String, Object> map) {
        return R.success(userService.findByMap(map));
    }

    @GetMapping("/page")
    public R<Page<User>> page(@RequestParam Map<String, Object> map, PageRequest page) {
        return R.success(userService.findPageByMap(map, page));
    }

    @GetMapping("/detail")
    public R<User> detail(Long id) {
        return R.success(userService.findById(id));
    }

    @PostMapping("/save")
    public R<Void> save(User entity) {
        userService.save(entity);
        return R.success();
    }

    @PostMapping("/update")
    public R<Void> update(User entity) {
        userService.updateById(entity);
        return R.success();
    }

    @PostMapping("/remove")
    public R<Void> remove(Long id) {
        userService.deleteById(id);
        return R.success();
    }

    @PreAuthorize("hasPermission('user:update')")
    @PostMapping("/reset_password")
    public R<String> resetPassword(Long id) {
        String newPassword = SecurityUtils.generateRandomPassword(10);
        String encodedPassword = new BCryptPasswordEncoder().encode(newPassword);
        userService.updatePassword(id, encodedPassword);
        return R.success();
    }

    @PostMapping("/change_password")
    public R<Void> changePassword(String oldPassword, String newPassword, CurrentUser user) {
        return R.success();
    }
}
