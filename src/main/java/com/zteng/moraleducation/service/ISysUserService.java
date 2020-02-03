package com.zteng.moraleducation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zteng.moraleducation.pojo.entity.SysUser;
import com.zteng.moraleducation.pojo.param.UserParam;
import com.zteng.moraleducation.pojo.vo.UserVO;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 
 * @since 2020-01-13
 */
public interface ISysUserService extends IService<SysUser> {
    /**
     * 根据username查询用户信息
     * @param username
     * @return
     */
    UserVO getUserInfo(String username);

    /**
     * 查询用户列表
     * @param username
     * @param userParam
     * @return
     */
    IPage<UserVO> pageList(String username, UserParam userParam);

    void logout(String username);
}
