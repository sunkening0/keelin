package com.skn.keelin.async.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skn.keelin.async.AsyncService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
* 类名称：Hello   
* 类描述：   springboot中线程池的使用
* 创建人：skn   
* 创建时间：2019年8月21日 上午11:07:29   
* @version
 */

@Api(tags = "springboot中线程池的使用 ")
@RestController
public class Hello {
	private static final Logger logger = LoggerFactory.getLogger(Hello.class);

    @Autowired
    private AsyncService asyncService;

    @ApiOperation(value = "入口")
    @RequestMapping("/hello")
    public String submit(){
        logger.info("start submit");

        //调用service层的任务
        asyncService.executeAsync();

        logger.info("end submit");
        
        logger.error("error...");

        return "success";
    }
}
