package com.example.foo.modules.system.controller;

import com.example.common.BaseController;
import com.example.foo.modules.system.entity.User;
import com.example.foo.modules.system.service.UserService;
import com.jagsii.springxx.common.pagination.Page;
import com.jagsii.springxx.common.pagination.PageRequest;
import com.jagsii.springxx.common.web.R;
import lombok.RequiredArgsConstructor;
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
    public R<Void> remove(@RequestParam Long id) {
        userService.deleteById(id);
        return R.success();
    }
}
