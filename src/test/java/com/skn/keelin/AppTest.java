package com.skn.keelin;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.skn.keelin.async.AsyncService;


/**
 * spring-boot-starter-test 基于junit
* 类名称：AppTest   
* 类描述：   
* 创建人：skn   
* 创建时间：2019年8月24日 下午7:53:32   
* @version
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {
   
	MockMvc mvc;

	@Autowired
	AsyncService asyncService;
	
	@Test
    public void testShow() throws Exception {
        String expectedResult = "hello world!";
        String uri = "/hello";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        System.out.println(content);/*
        Assert.assertTrue("错误，正确的返回值为200", status == 200);
        Assert.assertFalse("错误，正确的返回值为200", status != 200);
        Assert.assertTrue("数据一致", expectedResult.equals(content));
        Assert.assertFalse("数据不一致", !expectedResult.equals(content));*/
    }
	
	
	@Test
	
}
