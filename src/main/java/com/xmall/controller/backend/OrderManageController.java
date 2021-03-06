package com.xmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.xmall.common.Const;
import com.xmall.common.ResponseCode;
import com.xmall.common.RestResponse;
import com.xmall.entity.User;
import com.xmall.service.IOrderService;
import com.xmall.service.IUserService;
import com.xmall.util.CookieUtil;
import com.xmall.util.JsonUtil;
import com.xmall.util.RedisShardedPoolUtil;
import com.xmall.vo.OrderVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author xies
 * @date 2018/2/8.
 */
@Controller
@RequestMapping("/manage/order")
public class OrderManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("list.do")
    @ResponseBody
    public RestResponse<PageInfo> orderList(HttpServletRequest request,
                                            @RequestParam(value="pageNum",defaultValue = Const.PAGE_NUM) Integer pageNum,
                                            @RequestParam(value="pageSize",defaultValue = Const.PAGE_SIZE) Integer pageSize
                                            ){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(loginToken)){
            return RestResponse.error("用户未登录无法获取用户信息");
        }
        User user = JsonUtil.stringToObj(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user == null){
            return RestResponse.error(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if(!iUserService.checkAdminRole(user).isSuccess()){
            return RestResponse.error("无权限操作");
        }
        return iOrderService.manageList(pageNum,pageSize);
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public RestResponse<OrderVo> orderDetail(HttpServletRequest request, Long orderNo){

        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(loginToken)){
            return RestResponse.error("用户未登录无法获取用户信息");
        }
        User user = JsonUtil.stringToObj(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user == null){
            return RestResponse.error(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");

        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //填充我们增加产品的业务逻辑

            return iOrderService.manageDetail(orderNo);
        }else{
            return RestResponse.error("无权限操作");
        }
    }

    @RequestMapping("search.do")
    @ResponseBody
    public RestResponse<PageInfo> orderSearch(HttpServletRequest request, Long orderNo,
                                                @RequestParam(value = "pageNum",defaultValue = Const.PAGE_NUM) int pageNum,
                                                @RequestParam(value = "pageSize",defaultValue = Const.PAGE_SIZE)int pageSize) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(loginToken)){
            return RestResponse.error("用户未登录无法获取用户信息");
        }
        User user = JsonUtil.stringToObj(RedisShardedPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return RestResponse.error(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录管理员");

        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //填充我们增加产品的业务逻辑
            return iOrderService.manageSearch(orderNo, pageNum, pageSize);
        } else {
            return RestResponse.error("无权限操作");
        }
    }

    @RequestMapping("send_goods.do")
    @ResponseBody
    public RestResponse<String> orderSendGoods(HttpServletRequest request, Long orderNo) {

        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(loginToken)){
            return RestResponse.error("用户未登录无法获取用户信息");
        }
        User user = JsonUtil.stringToObj(RedisShardedPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return RestResponse.error(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录管理员");

        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //填充我们增加产品的业务逻辑
            return iOrderService.manageSendGoods(orderNo);
        } else {
            return RestResponse.error("无权限操作");
        }
    }
}
