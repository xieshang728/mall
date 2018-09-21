package com.xmall.controller.backend;

import com.xmall.common.Const;
import com.xmall.common.ResponseCode;
import com.xmall.common.RestResponse;
import com.xmall.entity.Category;
import com.xmall.entity.User;
import com.xmall.service.ICategoryService;
import com.xmall.service.IUserService;
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
import java.util.List;

/**
 * @author xies
 * @date 2018/1/23
 */
@Controller
@RequestMapping("/manage/category/")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping(value = "add_category.do")
    @ResponseBody
    public RestResponse<String> addCategory(HttpServletRequest request, String categoryName,
                                            @RequestParam(value = "categoryId", defaultValue = Const.DEFAULT_PARENT_ID) int parentId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(loginToken)){
            return RestResponse.error("用户未登录无法获取用户信息");
        }
        User user = JsonUtil.stringToObj(RedisShardedPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return RestResponse.error(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (!iUserService.checkAdminRole(user).isSuccess()) {
            return RestResponse.error("用户权限不够，不能添加分类");
        }
        return iCategoryService.addCategory(categoryName, parentId);
    }

    @RequestMapping(value = "set_category_name.do")
    @ResponseBody
    public RestResponse<String> setCategoryName(HttpServletRequest request, String categoryName, int categoryId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(loginToken)){
            return RestResponse.error("用户未登录无法获取用户信息");
        }
        User user = JsonUtil.stringToObj(RedisShardedPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return RestResponse.error(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (!iUserService.checkAdminRole(user).isSuccess()) {
            return RestResponse.error("用户权限不够，不能修改分类");
        }
        return iCategoryService.setCategoryName(categoryName, categoryId);
    }

    @RequestMapping(value = "get_category.do")
    @ResponseBody
    public RestResponse<List<Category>> getChildrenParallelCategory(HttpServletRequest request,
                                                                    @RequestParam(value = "categoryId", defaultValue = Const.DEFAULT_PARENT_ID) Integer categoryId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(loginToken)){
            return RestResponse.error("用户未登录无法获取用户信息");
        }
        User user = JsonUtil.stringToObj(RedisShardedPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return RestResponse.error(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (!iUserService.checkAdminRole(user).isSuccess()) {
            return RestResponse.error("用户权限不够，不能修改分类");
        }
        return iCategoryService.getChildrenParallelCategory(categoryId);
    }

    @RequestMapping(value = "get_deep_category.do")
    @ResponseBody
    public RestResponse<List<Integer>> selectCategoryAndChildreanById(HttpServletRequest request,
                                                                      @RequestParam(value = "categoryId", defaultValue = Const.DEFAULT_PARENT_ID) Integer categoryId
    ) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(loginToken)){
            return RestResponse.error("用户未登录无法获取用户信息");
        }
        User user = JsonUtil.stringToObj(RedisShardedPoolUtil.get(loginToken),User.class);
        if (user == null) {
            return RestResponse.error(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        if (!iUserService.checkAdminRole(user).isSuccess()) {
            return RestResponse.error("用户权限不够，不能修改分类");
        }
        return iCategoryService.selectCategoryAndChildrenById(categoryId);
    }
}
