package com.zteng.moraleducation;

import com.alibaba.fastjson.JSON;
import com.zteng.moraleducation.pojo.vo.DepartVO;
import com.zteng.moraleducation.service.impl.SysDepartmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = MoralEducationApplication.class)
@RunWith(SpringRunner.class)
class MoralEducationApplicationTests {


    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SysDepartmentServiceImpl departmentService;
    @Test
    public void test(){
        List<DepartVO> departVOS = departmentService.buildAllTree();
        List deptChildren = departmentService.getDeptChildren(10L, departVOS);
        System.out.println(JSON.toJSONString(deptChildren));
    }


}
