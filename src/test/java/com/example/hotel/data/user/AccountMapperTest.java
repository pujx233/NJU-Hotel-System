package com.example.hotel.data.user;

import com.example.hotel.data.admin.AdminMapper;
import com.example.hotel.data.user.AccountMapper;
import com.example.hotel.enums.UserType;
import com.example.hotel.po.User;
import com.example.hotel.util.BaseTest;
import com.example.hotel.util.RunSql;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;


public class AccountMapperTest extends BaseTest {//主要是为了继承BaseTest的注释来注入依赖等等，详见该类

    @Autowired
    AdminMapper adminMapper;
    @Autowired
    AccountMapper accountMapper;

    private UserDao userDao;


    public void testMapper(int testNum){
        List<User> users = userDao.getUsers();
        int oldNum = users.size();
        List<User> userRecords = userDao.insertUsers(testNum);
        users = userDao.getUsers();
        assertEquals(oldNum+testNum,users.size());
        //用插入数量验证accountMapper.createNewAccount

        for(int i=0;i<userRecords.size();i++){
            User oldUser = userRecords.get(i);
            User newUser = accountMapper.getAccountById(oldUser.getId());
            userDao.equalAfterInsert(oldUser,newUser);
        }//用前后记录对比验证accountMapper.getAccountById

        for(int i=0;i<userRecords.size();i++){
            User oldUser = userRecords.get(i);
            User newUser = accountMapper.getAccountByName(oldUser.getEmail());
            userDao.equalAfterInsert(oldUser,newUser);
        }//用前后记录对比验证accountMapper.getAccountByName

        for(int i=0;i<userRecords.size();i++){
            User oldUser = userRecords.get(i);
            User newUser = userDao.generateUser(i);
            accountMapper.updateAccount(oldUser.getId(),newUser.getPassword(),newUser.getUserName(),newUser.getPhoneNumber());
            User changedUser = accountMapper.getAccountById(oldUser.getId());
            userDao.equalAfterUpdate(newUser,changedUser);
        }//用前后记录对比验证accountMapper.updateAccount

        for(int i=0;i<userRecords.size();i++){
            User oldUser = userRecords.get(i);
            double oldCredit = oldUser.getCredit();
//            System.out.println(i+" Uninitiallized Credit: "+oldCredit);
            double credit = (int)(new Random(i*i+400).nextInt())%100000.0;//将credit限制在10万以内
//            System.out.println(i+" Change Credit By: "+credit);
            accountMapper.updateAccountCredit(oldUser.getId(),credit);
            User changedUser = accountMapper.getAccountByName(oldUser.getEmail());
            assertEquals(oldCredit+credit,changedUser.getCredit(),0.0001);
        }//用前后记录对比验证accountMapper.updateAccountCredit

        for(int i=0;i<userRecords.size();i++){
            User oldUser = userRecords.get(i);
            accountMapper.deleteAccountById(oldUser.getId());
            assertNull(accountMapper.getAccountById(oldUser.getId()));
            assertNull(accountMapper.getAccountByName(oldUser.getEmail()));
        }//用前后记录对比验证accountMapper.deleteAccountById

        users = userDao.getUsers();
        assertEquals(oldNum,users.size());
        //至此，预期剩余数量与实际剩余数量相等
    }



    @BeforeClass//因为没能解决被删除的序号空悬的问题，故每次测试前都重新执行一次sql，防止序号增加至过大
    public static void testInit(){
        System.out.println("--init start--");
        RunSql.runsqlBySpringUtils("sql\\hotel.sql");
        System.out.println("--init end--");
    }

    @Test
    public void testWithUser(){
        this.userDao = new ManagerDao();
        testMapper(23);
        this.userDao = new MarketerDao();
        testMapper(23);
        this.userDao = new ClientDao();
        testMapper(23);
    }



    abstract class UserDao{

        public abstract UserType userType();
        public abstract int addUser(User user);
        public abstract List<User> getUsers();
        public User generateUser(int flag){
            User user = new User();
            user.setEmail(validEmail(flag,"999@qq.com"));
            user.setPassword(flag%10000+"9123456");//为了使密码不超过11位
            user.setUserType(userType());
            return user;
        }
        public List<User> insertUsers(int testNum){
            List<User> userRecords = new ArrayList<>();
            for(int i=0;i<testNum;i++){
                User user = generateUser(i);
                addUser(user);
                userRecords.add(user);
            }
            return userRecords;
        }
        public void equalAfterInsert(User user1,User user2){
            assertEquals(user1.getId(),user2.getId());
            assertEquals(user1.getEmail(),user2.getEmail());
            assertEquals(user1.getPassword(),user2.getPassword());
            assertEquals(user1.getUserType(),user2.getUserType());
        }
        public void equalAfterUpdate(User user1,User user2){
            assertEquals(user1.getUserName(),user2.getUserName());
            assertEquals(user1.getPassword(),user2.getPassword());
            assertEquals(user1.getPhoneNumber(),user2.getPhoneNumber());
        }
        public String validEmail(int flag, String emailFrame){
            Random r = new Random(flag);
            while(true){
                String tmpEmail = flag+(Math.abs(r.nextInt())+emailFrame);
                if(null==accountMapper.getAccountByName(tmpEmail)){
                    return tmpEmail;
                }
            }
        }
    }

    class ClientDao extends UserDao{

        @Override
        public UserType userType(){return UserType.Client;}
        @Override
        public int addUser(User user){return accountMapper.createNewAccount(user);}
        @Override
        public List<User> getUsers(){return adminMapper.getAllClients();}
        @Override
        public User generateUser(int flag){
            User user = super.generateUser(flag);
            user.setUserName(flag%10000+"小明");
            user.setCredit(400%(flag*flag+7)+flag%10000);
            user.setPhoneNumber(flag%10000+"0001");
            return user;
        }
        @Override
        public void equalAfterInsert(User user1,User user2){
            super.equalAfterInsert(user1,user2);
            assertEquals(user1.getUserName(),user2.getUserName());
            assertEquals(user1.getCredit(),user2.getCredit(),0.0001);
            assertEquals(user1.getPhoneNumber(),user2.getPhoneNumber());
        }
    }

    class ManagerDao extends UserDao{

        @Override
        public UserType userType(){return UserType.HotelManager;}
        @Override
        public int addUser(User user){return adminMapper.addManager(user);}
        @Override
        public List<User> getUsers(){return adminMapper.getAllManagers();}
    }

    class MarketerDao extends UserDao{

        @Override
        public UserType userType(){return UserType.Marketer;}
        @Override
        public int addUser(User user){return adminMapper.addMarketer(user);}
        @Override
        public List<User> getUsers(){return adminMapper.getAllMarketers();}
    }

}

