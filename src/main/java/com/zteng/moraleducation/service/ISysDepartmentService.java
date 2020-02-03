package com.zteng.moraleducation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zteng.moraleducation.pojo.entity.SysDepartment;
import com.zteng.moraleducation.pojo.vo.DepartVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author 
 * @since 2020-01-17
 */
public interface ISysDepartmentService extends IService<SysDepartment> {
    /**
     * 组织成树结构
     * @param departments
     * @return
     */
    List<DepartVO> buildTree(List<SysDepartment> departments);

    /**
     * 根据id删除部门
     * @param id
     * @return
     */
    boolean deleteById(Long id);

    /**
     * 根据id查询返回map类型
     * @param ids
     * @return
     */
    Map<Long, SysDepartment> getMapByIds(List<Long> ids);

    /**
     * 根据角色查询关联部门id
     * @param roleId
     * @return
     */
    List<Long> getByRoleId(Long roleId);

    /**
     * 查询pid的子级
     * @param pid
     * @return
     */
    List<SysDepartment> getByPid(Long pid);

    /**
     * 查询用户的数据权限，即他拥有的部门
     * @param username
     * @return
     */
    Set<Long> getDeptIdsByUser(String username);
}
