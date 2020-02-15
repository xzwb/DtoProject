package com.xcphoenix.dto.controller;

import com.xcphoenix.dto.annotation.UserLoginToken;
import com.xcphoenix.dto.bean.dao.User;
import com.xcphoenix.dto.result.Result;
import com.xcphoenix.dto.service.UserService;
import com.xcphoenix.dto.utils.ContextHolderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author      xuanc
 * @date        2019/8/23 下午6:50
 * @version     1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @UserLoginToken
    @PutMapping("/username")
    public Result updateUserName(@Validated @RequestBody User user) {
        userService.updateName(user.getUserName());
        return new Result("更新成功");
    }

    @UserLoginToken
    @PutMapping("/avatar")
    public Result updateUserAvatar(@RequestBody User user) throws IOException {
        String avatarUrl = userService.updateAvatar(user.getUserAvatar());
        return new Result("更新成功").addMap("avatar", avatarUrl);
    }

    @UserLoginToken
    @GetMapping("/avatar")
    public Result getUserAvatar() {
        Long uid = ContextHolderUtils.getLoginUserId();
        String avatarUrl = userService.getUserDetail(uid).getUserAvatar();
        return new Result().addMap("avatar", avatarUrl);
    }

}
