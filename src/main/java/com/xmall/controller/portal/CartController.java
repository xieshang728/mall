package com.xmall.controller.portal;

import com.xmall.common.Const;
import com.xmall.common.ResponseCode;
import com.xmall.common.RestResponse;
import com.xmall.entity.User;
import com.xmall.service.ICartService;
import com.xmall.util.CookieUtil;
import com.xmall.util.JsonUtil;
import com.xmall.util.RedisShardedPoolUtil;
import com.xmall.vo.CartVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author xies
 * @date 2018/2/2.
 */
@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;

    @RequestMapping("list.do")
    @ResponseBody
    public RestResponse<CartVo> list(HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(loginToken)){
            return RestResponse.error("用户未登录无法获取用户信息");
        }
        User user = JsonUtil.stringToObj(RedisShardedPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return RestResponse.error(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.list(user.getId());
    }

    @RequestMapping("add.do")
    @ResponseBody
    public RestResponse<CartVo> add(HttpServletRequest request, int count, int productId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(loginToken)){
            return RestResponse.error("用户未登录无法获取用户信息");
        }
        User user = JsonUtil.stringToObj(RedisShardedPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return RestResponse.error(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.add(user.getId(), productId, count);
    }

    @RequestMapping("update.do")
    @ResponseBody
    public RestResponse<CartVo> update(HttpServletRequest request, int count, int productId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(loginToken)){
            return RestResponse.error("用户未登录无法获取用户信息");
        }
        User user = JsonUtil.stringToObj(RedisShardedPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return RestResponse.error(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.update(user.getId(), productId, count);
    }

    @RequestMapping("delete_product.do")
    @ResponseBody
    public RestResponse<CartVo> deleteProduct(HttpServletRequest request, String productIds) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(loginToken)){
            return RestResponse.error("用户未登录无法获取用户信息");
        }
        User user = JsonUtil.stringToObj(RedisShardedPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return RestResponse.error(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.deleteProduct(user.getId(), productIds);
    }

    @RequestMapping("select_all.do")
    @ResponseBody
    public RestResponse<CartVo> selectAll(HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(loginToken)){
            return RestResponse.error("用户未登录无法获取用户信息");
        }
        User user = JsonUtil.stringToObj(RedisShardedPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return RestResponse.error(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(), null, Const.Cart.CHECKED);
    }


    @RequestMapping("un_select_all.do")
    @ResponseBody
    public RestResponse<CartVo> unSelectAll(HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(loginToken)){
            return RestResponse.error("用户未登录无法获取用户信息");
        }
        User user = JsonUtil.stringToObj(RedisShardedPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return RestResponse.error(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(), null, Const.Cart.UN_CHECKED);
    }

    @RequestMapping("select.do")
    @ResponseBody
    public RestResponse<CartVo> select(HttpServletRequest request, Integer productId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(loginToken)){
            return RestResponse.error("用户未登录无法获取用户信息");
        }
        User user = JsonUtil.stringToObj(RedisShardedPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return RestResponse.error(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(), productId, Const.Cart.CHECKED);
    }

    @RequestMapping("get_cart_product_count")
    @ResponseBody
    public RestResponse<Integer> getCartProductCount(HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(loginToken)){
            return RestResponse.error("用户未登录无法获取用户信息");
        }
        User user = JsonUtil.stringToObj(RedisShardedPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return RestResponse.error(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.getCartProductCount(user.getId());
    }

}
