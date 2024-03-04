package com.example.foo.modules.system.controller;

import com.example.foo.modules.system.entity.User;
import com.example.foo.modules.system.service.UserService;
import com.jagsii.springxx.common.pagination.Page;
import com.jagsii.springxx.common.pagination.PageRequest;
import com.jagsii.springxx.test.SpringTests;
import com.jagsii.springxx.test.hamcrest.ExtMatchers;
import org.hamcrest.Matchers;
import org.instancio.Instancio;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = SpringTests.TestPropertiesConfig.class)
public class UserControllerTest extends SpringTests {
    @SuppressWarnings("unused")
    @WithSettings
    final Settings settings = Settings.create()
            .set(Keys.BEAN_VALIDATION_ENABLED, true);
    @Autowired
    MockMvc mvc;
    @MockBean
    UserService userService;

    @Test
    void list() throws Exception {
        List<User> items = Instancio.ofList(User.class).size(5).create();
        Mockito.when(userService.findByMap(Mockito.any())).thenReturn(items);
        mvc.perform(get("/system/user/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(isOk)
                .andExpect(jsonPath("$.data.length()", Matchers.equalTo(5)))
                .andExpect(jsonPath("$.data[0].name", Matchers.not(Matchers.emptyOrNullString())));
        Mockito.verify(userService).findByMap(Mockito.any());
    }

    @Test
    void page() throws Exception {
        List<User> items = Instancio.ofList(User.class).size(5).create();
        PageRequest pageRequest = new PageRequest(2, 5);
        Mockito.when(userService.findPageByMap(Mockito.any(), Mockito.any())).thenReturn(new Page<>(pageRequest, 20, items));
        mvc.perform(get("/system/user/page"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(isOk)
                .andExpect(jsonPath("$.data.page", Matchers.equalTo(2)))
                .andExpect(jsonPath("$.data.size", Matchers.equalTo(5)))
                .andExpect(jsonPath("$.data.totalCount", Matchers.equalTo(20)))
                .andExpect(jsonPath("$.data.items.length()", Matchers.equalTo(5)))
                .andExpect(jsonPath("$.data.items[0].name", Matchers.not(Matchers.emptyOrNullString())));
        Mockito.verify(userService).findPageByMap(Mockito.any(), Mockito.any());
    }

    @Test
    void detail() throws Exception {
        User item = Instancio.create(User.class);
        Mockito.when(userService.findById(item.getId())).thenReturn(item);
        mvc.perform(get("/system/user/detail").queryParam("id", item.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(isOk)
                .andExpect(jsonPath("$.data.id", ExtMatchers.numericalEqualTo(item.getId())));
        Mockito.verify(userService).findById(item.getId());
    }

    @Test
    void save() throws Exception {
        User item = Instancio.create(User.class);
        Mockito.doNothing().when(userService).save(Mockito.argThat(arg -> Objects.equals(arg.getName(), item.getName())));
        mvc.perform(post("/system/user/save").param("name", String.valueOf(item.getName())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(isOk);
        Mockito.verify(userService).save(Mockito.argThat(arg -> Objects.equals(arg.getName(), item.getName())));
    }

    @Test
    void update() throws Exception {
        User item = Instancio.create(User.class);
        Mockito.doNothing().when(userService).updateById(Mockito.argThat(arg -> Objects.equals(arg.getName(), item.getName())));
        mvc.perform(post("/system/user/update").param("name", String.valueOf(item.getName())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(isOk);
        Mockito.verify(userService).updateById(Mockito.argThat(arg -> Objects.equals(arg.getName(), item.getName())));
    }

    @Test
    void remove() throws Exception {
        Long id = 1L;
        Mockito.doNothing().when(userService).deleteById(id);
        mvc.perform(post("/system/user/remove").param("id", String.valueOf(id)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(isOk);
        Mockito.verify(userService).deleteById(id);
    }
}
