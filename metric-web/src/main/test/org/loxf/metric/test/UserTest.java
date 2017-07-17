package org.loxf.metric.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.loxf.metric.api.IUserService;
import org.loxf.metric.common.dto.UserDto;
import org.loxf.metric.test.core.JUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by luohj on 2017/7/17.
 */
@RunWith(JUnit4ClassRunner.class)
@ActiveProfiles("rd")
@ContextConfiguration(locations = {"classpath*:root-test.xml"})
public class UserTest {
    private static Logger logger = LoggerFactory.getLogger(UserTest.class);
    @Autowired
    private IUserService userService;

    @Test
    public void register(){
        UserDto user = new UserDto();
        logger.debug(JSON.toJSONString(userService.register("18989999103", "123456", "测试")));
    }
    @Test
    public void createChild(){
        UserDto user = new UserDto();
        user.setRealName("子账号01");
        user.setEmail("loxf001@qilv.com");
        user.setUniqueCode("lfYeJKA0");
        user.setPhone("18989999103");
        logger.debug(JSON.toJSONString(userService.addChildUser(user)));
    }
}
