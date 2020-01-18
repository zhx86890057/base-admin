package com.zteng.moraleducation.service;

import com.zteng.moraleducation.pojo.entity.SysDepartment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zteng.moraleducation.pojo.vo.DepartVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author 
 * @since 2020-01-17
 */
public interface ISysDepartmentService extends IService<SysDepartment> {
    List<DepartVO> buildTree(List<DepartVO> list);

    boolean deleteById(Long id);

    Map<Long, SysDepartment> getMapByIds(List<Long> ids);
}
