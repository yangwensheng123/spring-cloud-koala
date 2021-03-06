package com.xingling.cloud.web.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xingling.cloud.model.domain.User;
import com.xingling.cloud.model.dto.CheckUserNameDto;
import com.xingling.cloud.security.SecurityUserUtils;
import com.xingling.cloud.service.UserService;
import com.xingling.controller.BaseController;
import com.xingling.dto.AuthUserDto;
import com.xingling.wrap.WrapMapper;
import com.xingling.wrap.Wrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>Title:	  UserController. </p>
 * <p>Description 用户controller </p>
 * <p>Company:    http://www.xinglinglove.com </p>
 *
 * @Author <a href="190332447@qq.com"/>杨文生</a>
 * @CreateDate 2017 /8/17 19:05
 */
@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(value = "UserController", tags = "UserController", description = "用户controller", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController extends BaseController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Resource
    private UserService userService;

    /**
     * <p>Title:	  showInfo. </p>
     * <p>Description 本地服务实例的信息</p>
     *
     * @return the service instance
     * @Author <a href="190332447@qq.com"/>杨文生</a>
     * @CreateDate 2017 /8/17 19:05
     */
    @GetMapping("/instance-info")
    @ApiOperation(notes = "本地服务实例的信息", httpMethod = "GET", value = "本地服务实例的信息")
    public ServiceInstance showInfo() {
        ServiceInstance localServiceInstance = this.discoveryClient.getLocalServiceInstance();
        return localServiceInstance;
    }


    /**
     * <p>Title:	  selectUserById. </p>
     * <p>Description 根据id查询用户信息</p>
     *
     * @param id String
     * @return the wrapper
     * @Author <a href="190332447@qq.com"/>杨文生</a>
     * @CreateDate 2017 /8/17 19:04
     */
    @GetMapping("/{id}")
    @ApiOperation(notes = "根据id查询用户信息", httpMethod = "GET", value = "根据id查询用户信息")
    public Wrapper<User> selectUserById(@PathVariable String id) {
        User user = this.userService.selectByKey(id);
        return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, user);
    }

    /**
     * <p>Title:	  listPage. </p>
     * <p>Description 分页查询用户列表</p>
     *
     * @param user the user
     * @return the wrapper
     * @Author <a href="190332447@qq.com"/>杨文生</a>
     * @CreateDate 2017 /8/17 19:04
     */
    @PostMapping(value = "/listPage")
    @ApiOperation(httpMethod = "POST", value = "分页查询用户列表")
    public Wrapper<PageInfo<User>> listPage(@ApiParam(name = "user", value = "用户信息") @RequestBody User user) {
        PageHelper.startPage(user.getPageNum(), user.getPageSize());
        List<User> listPage = userService.queryListPage(user);
        PageInfo<User> pageInfo = new PageInfo<>(listPage);
        return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, pageInfo);
    }

    /**
     * <p>Title:      currentUser. </p>
     * <p>Description 获取当前用户信息</p>
     *
     * @param
     * @return current user
     * @Author <a href="190332447@qq.com"/>杨文生</a>
     * @since 2018 /2/20 14:10
     */
    @GetMapping(value = "/currentUser")
    @ApiOperation(httpMethod = "GET", value = "获取当前用户信息")
    public Wrapper<AuthUserDto> getCurrentUser() {
        String loginName = SecurityUserUtils.getCurrentLoginName();
        AuthUserDto authUserDto = userService.getAuthUserInfo(loginName);
        return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, authUserDto);
    }

    /**
     * <p>Title:      delete. </p>
     * <p>Description 根据id删除用户</p>
     *
     * @param id the id
     * @return wrapper wrapper
     * @Author <a href="190332447@qq.com"/>杨文生</a>
     * @since 2018 /2/20 14:14
     */
    @PostMapping(value = "/delete/{id}")
    @ApiOperation(httpMethod = "POST", value = "根据id删除用户")
    public Wrapper<Integer> delete(@PathVariable String id) {
        AuthUserDto authUserDto = getLoginAuthDto();
        int result = userService.deleteUserById(id,authUserDto);
        return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, result);
    }

    /**
     * <p>Title:      enable. </p>
     * <p>Description 启用用户</p>
     *
     * @param id the id
     * @return wrapper wrapper
     * @Author <a href="190332447@qq.com"/>杨文生</a>
     * @since 2018 /2/20 14:14
     */
    @PostMapping(value = "/enable/{id}")
    @ApiOperation(httpMethod = "POST", value = "启用用户")
    public Wrapper<Integer> enable(@PathVariable String id) {
        AuthUserDto authUserDto = getLoginAuthDto();
        int result = userService.enableUserById(id,authUserDto);
        return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, result);
    }

    /**
     * <p>Title:      disable. </p>
     * <p>Description 禁用用户</p>
     *
     * @param id the id
     * @return wrapper wrapper
     * @Author <a href="190332447@qq.com"/>杨文生</a>
     * @since 2018 /2/20 14:14
     */
    @PostMapping(value = "/disable/{id}")
    @ApiOperation(httpMethod = "POST", value = "禁用用户")
    public Wrapper<Integer> disable(@PathVariable String id) {
        AuthUserDto authUserDto = getLoginAuthDto();
        int result = userService.disableUserById(id,authUserDto);
        return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, result);
    }

    /**
     * <p>Title:      modifyUser. </p>
     * <p>Description 修改用户信息</p>
     *
     * @param user the user
     * @return wrapper wrapper
     * @Author <a href="190332447@qq.com"/>杨文生</a>
     * @since 2018 /2/20 15:38
     */
    @PostMapping(value = "/modifyUser")
    @ApiOperation(httpMethod = "POST", value = "修改用户信息")
    public Wrapper<Integer> modifyUser(@ApiParam(name = "user", value = "用户信息") @RequestBody User user) {
        AuthUserDto authUserDto = getLoginAuthDto();
        int result = userService.modifyUser(user,authUserDto);
        return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, result);
    }

    /**
     * <p>Title:      saveUserInfo. </p>
     * <p>Description 保存用户信息</p>
     *
     * @param user the user
     * @return wrapper wrapper
     * @Author <a href="190332447@qq.com"/>杨文生</a>
     * @since 2018 /2/22 17:24
     */
    @PostMapping(value = "/saveUserInfo")
    @ApiOperation(httpMethod = "POST", value = "保存用户信息")
    public Wrapper<Integer> saveUserInfo(@ApiParam(name = "user", value = "用户信息") @RequestBody User user) {
        AuthUserDto authUserDto = getLoginAuthDto();
        int result = userService.saveUserInfo(user,authUserDto);
        return WrapMapper.wrap(Wrapper.SUCCESS_CODE, Wrapper.SUCCESS_MESSAGE, result);
    }

    /**
     * <p>Title:      checkUserName. </p>
     * <p>Description 校验用户名唯一性</p>
     *
     * @param checkUserNameDto the check user name dto
     * @return wrapper
     * @Author <a href="190332447@qq.com"/>杨文生</a>
     * @since 2018 /2/24 17:08
     */
    @PostMapping(value = "/checkUserName")
    @ApiOperation(httpMethod = "POST", value = "校验用户名唯一性")
    public Wrapper<Boolean> checkUserName(@ApiParam(name = "checkUserNameDto", value = "用户名dto") @RequestBody CheckUserNameDto checkUserNameDto) {
        boolean flag = false;
        String userId = checkUserNameDto.getUserId();
        String userName = checkUserNameDto.getUserName();
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userName", userName);
        if (StringUtils.isNotEmpty(userId)) {
            criteria.andNotEqualTo("id", userId);
        }
        int result = userService.selectCountByExample(example);
        if(result > 0) {
            flag = true;
        }
        return WrapMapper.ok(flag);
    }
}
