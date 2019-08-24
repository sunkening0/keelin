package com.skn.keelin.shiro.entity;

import io.swagger.annotations.ApiModelProperty;

public class BackAdminResult {
    private Integer code;
    private String msg;
    
    
    
    public Integer getCode() {
		return code;
	}



	public void setCode(Integer code) {
		this.code = code;
	}



	public String getMsg() {
		return msg;
	}



	public void setMsg(String msg) {
		this.msg = msg;
	}



	public static BackAdminResult build(Integer code, String msg) {
    	BackAdminResult result = new BackAdminResult();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
