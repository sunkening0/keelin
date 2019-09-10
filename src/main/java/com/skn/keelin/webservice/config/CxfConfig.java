package com.skn.keelin.webservice.config;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.skn.keelin.webservice.AppService;
import com.skn.keelin.webservice.AppServiceImpl;

/**
* 类名称：CxfConfig   
* 类描述：  基于cxf实现webservice接口
* 创建人：skn   
* 创建时间：2019年9月10日 上午9:28:33   
* @version
 */
@Configuration
public class CxfConfig {
    //默认servlet路径/*,如果覆写则按照自己定义的来
    @Bean
    public ServletRegistrationBean dispatcherServlet1() {
        return new ServletRegistrationBean(new CXFServlet(), "/services/*");
    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    //把实现类交给spring管理
    @Bean
    public AppService appService() {
        return new AppServiceImpl();
    }

    //终端路径
    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), appService());
        //endpoint.getInInterceptors().add(new AuthInterceptor());//添加校验拦截器
        endpoint.publish("/user");
        return endpoint;
    }
}
