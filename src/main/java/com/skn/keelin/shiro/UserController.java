package com.skn.keelin.shiro;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.skn.keelin.shiro.entity.BackAdminResult;
import com.skn.keelin.shiro.entity.User;
import com.skn.keelin.shiro.oauth2.token.PhoneToken;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@RequestMapping("/user")
@Controller
public class UserController {

	// 用户名密码登录
	@PostMapping("/dologin")
	@ResponseBody
	public BackAdminResult dologin(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("rememberMe") boolean rememberMe,
			HttpSession session) throws AuthenticationException {

		//rememberMe 记住我
		UsernamePasswordToken token = new UsernamePasswordToken(username, password,rememberMe);

		Subject subject = SecurityUtils.getSubject();
		try {
			//subject.getSession().setTimeout(180000000);
			subject.login(token);
		} catch (IncorrectCredentialsException ice) {
			return BackAdminResult.build(1, "密码错误");
		}

		User user = (User) subject.getPrincipal();
		/*
		 * session.setAttribute(Constants.LOGIN_ADMIN_KEY, user);
		 * subject.getSession().setAttribute(Constants.LOGIN_ADMIN_KEY, user);
		 */
		return BackAdminResult.build(0, "登录成功！");

	}

	// 使用手机号和短信验证码登录
	@RequestMapping("/plogin")
	@ResponseBody
	public BackAdminResult pLogin(@RequestParam("phone") String phone, @RequestParam("code") String code,
			HttpSession session) {

		// 根据phone从session中取出发送的短信验证码，并与用户输入的验证码比较
		String messageCode = "123";// (String) session.getAttribute(phone);

		if (!StringUtils.isEmpty(messageCode) && messageCode.equals(code)) {

			PhoneToken token = new PhoneToken(phone);

			Subject subject = SecurityUtils.getSubject();

			subject.login(token);
			User user = (User) subject.getPrincipal();
			// session.setAttribute(Constants.LOGIN_ADMIN_KEY, user);
			return BackAdminResult.build(0, "登录成功！");

		} else {
			return BackAdminResult.build(2, "验证码错误！");
		}
	}
	
	
	@GetMapping("/403")
    public String page_403(){
		return "403";
    }
	
	@GetMapping("/logout")
	public void logout() {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			subject.logout(); // session 会销毁，在SessionListener监听session销毁，清理权限缓存
			if (log.isDebugEnabled()) {
				log.debug("退出登录成功");
			}
		}
	}

}
