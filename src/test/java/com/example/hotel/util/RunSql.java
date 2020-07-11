package com.example.hotel.util;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;

//@RunWith(SpringRunner.class)//SpringRunner是SpringJUnit4ClassRunner的别名，二者没区别
//@SpringBootTest
public class RunSql {
    //这里其实可以自动注入，不过因为采用了静态方法，所以放弃此方法
//    @Autowired
//    SqlSessionFactory sqlSessionFactory;

    //因为没能解决被删除的序号空悬的问题，故每次测试前都重新执行一次sql，防止序号增加至过大
    public static void runsqlBySpringUtils(String sqlPath){
        try {
            //以下被注释的方法失败了，因为我找不到mybatis的配置文件
//            //mybatis的配置文件
//            String resource = "conf.xml";
//            //使用类加载器加载mybatis的配置文件（它也加载关联的映射文件）
//            Reader reader = Resources.getResourceAsReader(resource);
//            //构建sqlSession的工厂
//            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
//            //创建能执行映射文件中sql的sqlSession

            //以下这坨代码经测试可用，其中PooledDataSource的参数是照抄application.yml的
            DataSource dataSource =new PooledDataSource("com.mysql.cj.jdbc.Driver",
                    "jdbc:mysql://localhost:3306/Hotel?serverTimezone=CTT&characterEncoding=UTF-8","root","mysql_123");
            TransactionFactory transactionFactory =  new JdbcTransactionFactory();
            Environment environment = new Environment("development", transactionFactory, dataSource);
            Configuration configuration = new Configuration(environment);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);


            SqlSession sqlSession = sqlSessionFactory.openSession();
            Connection conn = sqlSession.getConnection();
            FileSystemResource rc = new FileSystemResource(sqlPath);
            EncodedResource er = new EncodedResource(rc, "utf-8");
            ScriptUtils.executeSqlScript(conn, er);
        } catch (Exception e) {
            System.out.println("执行sql文件失败");
            e.printStackTrace();
            throw e;
        }
    }
}
