package com.zteng.moraleducation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zteng.moraleducation.pojo.entity.SysJob;
import com.zteng.moraleducation.pojo.vo.JobVO;

/**
 * <p>
 * 岗位表 服务类
 * </p>
 *
 * @author 
 * @since 2020-01-17
 */
public interface ISysJobService extends IService<SysJob> {
    IPage<JobVO> pageList(String name, Integer status, Integer pageNo, Integer pageSize);
}
