package com.skn.keelin.webservice;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface AppService {
    @WebMethod
    String getUserName(@WebParam(name = "id") String id) throws UnsupportedEncodingException;
    @WebMethod
    public Map<String, Object> getUser(String id) throws UnsupportedEncodingException;
}
