//package com.jackdawapi.jackdawapiinterface;
//
//import com.jackdawapi.jackdawapisdk.client.YuApiClient;
//import com.jackdawapi.jackdawapisdk.model.User;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.web.context.annotation.RequestScope;
//
//import javax.annotation.Resource;
//
//@SpringBootTest
//class JackdawApiInterfaceApplicationTests {
//
//    @Resource
//    private YuApiClient yuApiClient;
//
//    @Test
//    void contextLoads(){
//        String result = yuApiClient.getNameByGet("yupi");
//        User user = new User();
//        user.setUsername("liyupi");
//        String usernameByPost = yuApiClient.getUsernameByPost(user);
//        System.out.println(result);
//        System.out.println(usernameByPost);
//    }
//
//}
