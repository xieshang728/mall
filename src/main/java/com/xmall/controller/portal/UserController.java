package com.xmall.controller.portal;

import com.alibaba.fastjson.JSON;
import com.xmall.common.Const;
import com.xmall.common.RedisShardedPool;
import com.xmall.common.ResponseCode;
import com.xmall.common.RestResponse;
import com.xmall.entity.User;
import com.xmall.service.IUserService;
import com.xmall.util.CookieUtil;
import com.xmall.util.JsonUtil;
import com.xmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author xies
 * @date 2018/1/21
 */
@Controller
@RequestMapping(value = "/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private RedisTemplate redisTemplate ;

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<User> login(String username, String password, HttpSession session,HttpServletResponse httpServletResponse) {
        RestResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            CookieUtil.writeLoginToken(httpServletResponse,session.getId());
            RedisShardedPoolUtil.setEx(session.getId(), JsonUtil.objToString(response.getData()),Const.RedisCacheExtTime.REDIS_SESSION_EXTIME);
        }
        return response;
    }


    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<User> logout(HttpSession session,HttpServletRequest request,HttpServletResponse response) {
        String loginToken = CookieUtil.readLoginToken(request);
        CookieUtil.delLoginToken(request,response);
        RedisShardedPoolUtil.del(loginToken);
        return RestResponse.success();
    }


    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<String> register(User user) {
        return iUserService.register(user);
    }


    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }


    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<User> getUserInfo(HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(loginToken)){
            return RestResponse.error("用户未登录无法获取用户信息");
        }
        User user = JsonUtil.stringToObj(RedisShardedPoolUtil.get(loginToken),User.class);
        //User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return RestResponse.success(user);
        }
        return RestResponse.error("用户未登录，无法获取用户的信息");
    }


    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<String> forgetGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }


    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<String> forgetGetAnswer(String username, String question, String answer) {
        return iUserService.selectCheckAnswer(username, question, answer);
    }


    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken) {
        return iUserService.forgetResetPassword(username, newPassword, forgetToken);
    }


    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<String> resetPassword(String oldPassword, String newPassword, HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(loginToken)){
            return RestResponse.error("用户未登录无法获取用户信息");
        }
        User user = JsonUtil.stringToObj(RedisShardedPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return RestResponse.error("用户未登录");
        }
        return iUserService.resetPassword(oldPassword, newPassword, user);
    }

    @RequestMapping(value = "update_information.do", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<User> updateInformation(HttpServletRequest request, User user) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(loginToken)){
            return RestResponse.error("用户未登录无法获取用户信息");
        }
        User currentUser = JsonUtil.stringToObj(RedisShardedPoolUtil.get(loginToken),User.class);
        if (currentUser == null) {
            RestResponse.error("用户未登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        RestResponse<User> response = iUserService.updateInformation(user);

        if (response.isSuccess()) {
            response.getData().setUsername(currentUser.getUsername());
            RedisShardedPoolUtil.set(loginToken,JsonUtil.objToString(user));
        }
        return response;
    }

    @RequestMapping(value = "get_information.do", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<User> get_information(HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(loginToken)){
            return RestResponse.error("用户未登录无法获取用户信息");
        }
        User currentUser = JsonUtil.stringToObj(RedisShardedPoolUtil.get(loginToken),User.class);
        if (currentUser == null) {
            return RestResponse.error(ResponseCode.NEED_LOGIN.getCode(), "未登录,需要强制登录status=10");
        }
        return iUserService.getInformation(currentUser.getId());
    }
}
