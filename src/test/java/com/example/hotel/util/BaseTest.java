package com.example.hotel.util;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)//SpringRunner是SpringJUnit4ClassRunner的别名，二者没区别
@SpringBootTest
//@Transactional//有回滚数据清理现场的作用，但如果使用mysql，则默认引擎不为InnoDB时此回滚无效
//@Rollback//当@Rollback(false)时回滚无效
public class BaseTest {
    //为什么这里不通过继承BaseTest来获得@Transational带来的数据回滚了呢？因为这个该死的@Transactional
    //对外宣称可以清空现场数据，实际上根本就没有把实体类乖乖存到数据库去，自己搞了一个缓存藏起来，这叫瞒天过海
    //我在三小时的彷徨、焦虑与崩溃的debug后发现测试突然错误的原因是出现了一种类似于“修改取出的数据对象会修改数据库的对象”的事情
    //然后我开始觉得三小时前兴奋地在确认回滚可以使用后为所有类加上@Transational的我是个傻逼
    //by tc
}
