package com.fighting.schoolo2o.util;

import javax.servlet.http.HttpServletRequest;

import com.fighting.schoolo2o.util.HttpServletRequestUtil;

public class CodeUtil {
	public static boolean checkVerifyCode(HttpServletRequest request) {
		String verifyCodeExpected = (String)request.getSession()
				.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		String verifyCodeActual = HttpServletRequestUtil.getString(request, "verifyCodeActual");
		if(verifyCodeActual != null && verifyCodeActual.equals(verifyCodeExpected))
			return true;
		return false;	
	}

}
