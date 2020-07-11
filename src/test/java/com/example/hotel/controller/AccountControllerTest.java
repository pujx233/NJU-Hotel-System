package com.example.hotel.controller;

import com.example.hotel.bl.admin.AdminService;
import com.example.hotel.bl.hotel.HotelService;
import com.example.hotel.bl.user.AccountService;
import com.example.hotel.data.user.AccountMapper;
import com.example.hotel.enums.UserType;
import com.example.hotel.logic.user.ClientLogic;
import com.example.hotel.logic.user.ManagerLogic;
import com.example.hotel.logic.user.MarketerLogic;
import com.example.hotel.logic.user.UserLogic;
import com.example.hotel.po.User;
import com.example.hotel.util.BaseTest;
import com.example.hotel.util.RunSql;
import com.example.hotel.vo.ResponseVO;
import com.example.hotel.vo.UserForm;
import com.example.hotel.vo.UserInfoVO;
import com.example.hotel.vo.UserVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

//import static com.sun.org.apache.xerces.internal.util.PropertyState.is;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebAppConfiguration//表示测试环境使用的ApplicationContext是WebApplicationContext类型的
public class AccountControllerTest extends BaseTest {//主要是为了继承BaseTest的注释来注入依赖等等，详见该类
    private final static String PRE = "/api/user";

    @Autowired
    AccountService accountService;
    @Autowired
    AdminService adminService;
    @Autowired
    HotelService hotelService;
    @Autowired
    AccountMapper accountMapper;

    @Autowired
    WebApplicationContext wac;

    MockMvc mvc;

    @Before
    public void setUp() throws Exception{
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();//初始化MockMvc对象
    }

    public ResponseVO testLogin(User iniUser) throws Exception{
        UserForm userForm = new UserForm();
        userForm.setEmail(iniUser.getEmail());
        userForm.setPassword(iniUser.getPassword());

        String json = com.alibaba.fastjson.JSONObject.toJSONString(userForm);//把对象转换为json字符串
        String url = PRE+"/login";//准备请求路径

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(json.getBytes())
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
                .andReturn();

//        int status = result.getResponse().getStatus();
//        Assert.assertEquals(200,status);

        //取回返回值的json字符串
        String content = result.getResponse().getContentAsString();
        System.out.println("testLogin ResponseVO content: "+content);

        //将json字符串转换为java对象
        ObjectMapper mapper = new ObjectMapper();
        ResponseVO responseVO = mapper.readValue(content, ResponseVO.class);

        if(responseVO.getSuccess()){
            //ResponseVO中的content变成了LinkedHashMap对象，下面将LinkedHashMap对象强制转换为java对象
            User user = mapper.convertValue(responseVO.getContent(),User.class);
            return ResponseVO.buildSuccess(user);
        }
        return responseVO;
    }

    public ResponseVO testRegisterAccount(User iniUser) throws Exception{
        UserVO userVO = new UserVO();
        userVO.setEmail(iniUser.getEmail());
        userVO.setPassword(iniUser.getPassword());
        userVO.setUserName(iniUser.getUserName());
        userVO.setUserType(iniUser.getUserType());
        userVO.setPhoneNumber(iniUser.getPhoneNumber());

        String json = com.alibaba.fastjson.JSONObject.toJSONString(userVO);//把对象转换为json字符串
        String url = PRE+"/register";//准备请求路径

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(json.getBytes())
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
                .andReturn();

//        int status = result.getResponse().getStatus();
//        Assert.assertEquals(200,status);

        //取回返回值的json字符串
        String content = result.getResponse().getContentAsString();
        System.out.println("testRegisterAccount ResponseVO content: "+content);

        //将json字符串转换为java对象
        ObjectMapper mapper = new ObjectMapper();
        ResponseVO responseVO = mapper.readValue(content, ResponseVO.class);

        if(responseVO.getSuccess()){
            //ResponseVO中的content变成了LinkedHashMap对象，下面将LinkedHashMap对象强制转换为java对象
            Integer userId = mapper.convertValue(responseVO.getContent(),Integer.class);
            return ResponseVO.buildSuccess(userId);
        }
        return responseVO;
    }


    public ResponseVO testGetUserInfo(int id) throws Exception{
        String url = PRE+"/{id}/getUserInfo";//准备请求路径

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(url,id)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
                .andReturn();

//        int status = result.getResponse().getStatus();
//        Assert.assertEquals(200,status);

        //取回返回值的json字符串
        String content = result.getResponse().getContentAsString();
        System.out.println("testGetUserInfo ResponseVO content: "+content);

        //将json字符串转换为java对象
        ObjectMapper mapper = new ObjectMapper();
        ResponseVO responseVO = mapper.readValue(content, ResponseVO.class);

        if(responseVO.getSuccess()){
            //ResponseVO中的content变成了LinkedHashMap对象，下面将LinkedHashMap对象强制转换为java对象
            User user = mapper.convertValue(responseVO.getContent(),User.class);
            return ResponseVO.buildSuccess(user);
        }
        return responseVO;

    }

    public ResponseVO testUpdateInfo(User iniUser) throws Exception{
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUserName(iniUser.getUserName());
        userInfoVO.setPassword(iniUser.getPassword());
        userInfoVO.setPhoneNumber(iniUser.getPhoneNumber());
        Integer id = iniUser.getId();

        String json = com.alibaba.fastjson.JSONObject.toJSONString(iniUser);//把对象转换为json字符串
        String url = PRE+"/{id}/userInfo/update";//准备请求路径

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(url,id)
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(json.getBytes())
                .accept(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(MockMvcResultMatchers.status().isOk())//在方法外部测试
//                .andDo(MockMvcResultHandlers.print())
                .andReturn();

//        int status = result.getResponse().getStatus();
//        Assert.assertEquals(200,status);

        //取回返回值的json字符串
        String content = result.getResponse().getContentAsString();
        System.out.println("testUpdateInfo ResponseVO content: "+content);

        //将json字符串转换为java对象
        ObjectMapper mapper = new ObjectMapper();
        ResponseVO responseVO = mapper.readValue(content, ResponseVO.class);

        return responseVO;
    }

    public void testController(UserLogic userLogic){
        try{
//            System.out.println();
//            System.out.println(" -- "+userLogic.userType().name()+" start -- ");

            ResponseVO responseVO = null;
            User user = userLogic.generateUser(0);
            assertFalse(testLogin(user).getSuccess());
            //生成新用户数据，无记录，登陆不成功，检验login

            if(userLogic.userType().equals(UserType.Client)){
                responseVO = testRegisterAccount(user);
//                    Assert.assertTrue(responseVO.getSuccess());//在调用的方法中已验证
                user.setId((Integer)responseVO.getContent());
            }else {
                responseVO = userLogic.addUser(user);
                Assert.assertTrue(responseVO.getSuccess());
                user.setId((Integer)responseVO.getContent());
            }
            assertTrue(testLogin(user).getSuccess());
            //注册用户后登陆成功，检验login

            responseVO = testGetUserInfo(user.getId());
//                Assert.assertTrue(responseVO.getSuccess());//在调用的方法中已验证
            user.setCredit(Math.floor(user.getCredit()));//信用值会被自动下取整
            userLogic.equalAfterInsert(user,(User)responseVO.getContent());
            //插入后的用户与原用户数据相符，检验getUserInfo

            User newUser = userLogic.generateUser(1);
            try{
                assertFalse(testUpdateInfo(newUser).getSuccess());
                fail();
            }catch (Exception e){
            }
            //生成无id新用户数据，用之更改原用户数据错误，检验updateInfo

            newUser.setId(user.getId());
            responseVO = testUpdateInfo(newUser);
            assertTrue(responseVO.getSuccess());
            responseVO = testGetUserInfo(user.getId());
//                assertTrue(responseVO.getSuccess());//在调用的方法中已验证
            userLogic.equalAfterUpdate(newUser,(User)responseVO.getContent());
            //为新用户数据添加id，用之更改原用户数据，并取出数据与新用户数据比对，检验updateInfo

//            System.out.println(" -- "+userLogic.userType().name()+" end -- ");
//            System.out.println();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @BeforeClass
    public static void testInit(){
        RunSql.runsqlBySpringUtils("sql\\hotel.sql");
    }

    @Test
    public void testWithClient(){
        testController(new ClientLogic(accountMapper,accountService,adminService));
    }

    @Test
    public void testWithManager(){
        testController(new ManagerLogic(accountMapper,adminService,hotelService));
    }

    @Test
    public void testWithMarketer(){
        testController(new MarketerLogic(accountMapper,adminService));
    }

    @After
    public void tearDown(){
        RunSql.runsqlBySpringUtils("sql\\hotel.sql");
    }

//    @Test
//    public void testTemplate1() throws Exception{
//        RequestBuilder requestBuilder = null;
//
//        String name = "controller测试添加";
//
//        //post提交
//        requestBuilder = post("/api/user").param("name",name);
//        MvcResult result = mvc.perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(name))
//                .andReturn();
//    }
}
