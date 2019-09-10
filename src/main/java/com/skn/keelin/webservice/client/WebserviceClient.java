package com.skn.keelin.webservice.client;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

public class WebserviceClient {
	//webservice接口地址
    private static String address = "http://localhost:8082/keelin/services/user?wsdl";

    //测试
    public static void main(String[] args) {
        test();
    }
	
	public static void test() {
        // 创建动态客户端
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(address);
        // 需要密码的情况需要加上用户名和密码
        // client.getOutInterceptors().add(new LoginInterceptor("root","admin"));
        Object[] objects = new Object[0];
        try {
            // invoke("方法名",参数1,参数2,参数3....);
            System.out.println("======client"+client);
            objects = client.invoke("getUserName", "1");
            System.out.println("返回数据:" + objects[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
