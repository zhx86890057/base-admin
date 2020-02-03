package com.zteng.moraleducation;

import com.zteng.moraleducation.service.impl.SysDepartmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = MoralEducationApplication.class)
@RunWith(SpringRunner.class)
class MoralEducationApplicationTests {


    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SysDepartmentServiceImpl departmentService;



    @Test
    public void test(){

    }


}
