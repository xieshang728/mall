package com.xmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.xmall.common.Const;
import com.xmall.common.ResponseCode;
import com.xmall.common.RestResponse;
import com.xmall.entity.Shipping;
import com.xmall.entity.User;
import com.xmall.service.IShippingService;
import com.xmall.util.CookieUtil;
import com.xmall.util.JsonUtil;
import com.xmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Response;
import java.util.Map;

/**
 * @author xies
 * @date 2018/1/31
 */
@Controller
@RequestMapping(value = "/shipping/")
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;

    @RequestMapping(value = "add.do")
    @ResponseBody
    public RestResponse<Map<String, Object>> add(
            HttpServletRequest request, Shipping shipping
    ) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(loginToken)){
            return RestResponse.error("用户未登录无法获取用户信息");
        }
        User user = JsonUtil.stringToObj(RedisShardedPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return RestResponse.error(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.add(user.getId(), shipping);
    }

    @RequestMapping(value = "del.do")
    @ResponseBody
    public RestResponse<String> del(
            HttpServletRequest request, Integer shippingId
    ) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(loginToken)){
            return RestResponse.error("用户未登录无法获取用户信息");
        }
        User user = JsonUtil.stringToObj(RedisShardedPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return RestResponse.error(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.del(user.getId(), shippingId);
    }

    @RequestMapping("update.do")
    @ResponseBody
    public RestResponse update(HttpServletRequest request, Shipping shipping) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(loginToken)){
            return RestResponse.error("用户未登录无法获取用户信息");
        }
        User user = JsonUtil.stringToObj(RedisShardedPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return RestResponse.error(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.update(user.getId(), shipping);
    }

    @RequestMapping("select.do")
    @ResponseBody
    public RestResponse<Shipping> select(HttpServletRequest request, Integer shippingId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(loginToken)){
            return RestResponse.error("用户未登录无法获取用户信息");
        }
        User user = JsonUtil.stringToObj(RedisShardedPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return RestResponse.error(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.select(user.getId(), shippingId);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public RestResponse<PageInfo> list(@RequestParam(value = "pageNum", defaultValue = Const.PAGE_NUM) int pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = Const.PAGE_SIZE) int pageSize,
                                       HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(loginToken)){
            return RestResponse.error("用户未登录无法获取用户信息");
        }
        User user = JsonUtil.stringToObj(RedisShardedPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return RestResponse.error(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.list(user.getId(), pageNum, pageSize);
    }
}
