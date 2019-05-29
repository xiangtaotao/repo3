package com.project.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.project.enums.ResultEnum;
import com.project.exception.UserLoginException;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class HttpAspect {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Pointcut("execution(public * com.project.controller..*.*(..))")
	public void webLog() {
	}

	/**
	 * @Before 在方法执行之前执行
	 */
	@Before("webLog()")
	public void doBefore(JoinPoint joinPoint) {
		log.info("========Join Interface========");
		// 记录http请求
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();

		// 从request中获取http请求的url/请求的方法类型／响应该http请求的类方法／IP地址／请求中的参数
		String requestURI = request.getRequestURI();
		String token = request.getParameter("token");
		log.info("url={}", requestURI);
		if (!requestURI.contains("/login") && !requestURI.contains("/regist") && !requestURI.contains("/fileUpload")) {
			if (StringUtils.isEmpty(token)) {
				throw new UserLoginException(ResultEnum.UN_LOGIN.getCode(), ResultEnum.UN_LOGIN.getMessage());
			}
			String userInfor = stringRedisTemplate.opsForValue().get(token);
			if (StringUtils.isEmpty(userInfor)) {
				throw new UserLoginException(ResultEnum.LOGOUT_TIMEOUT.getCode(),
						ResultEnum.LOGOUT_TIMEOUT.getMessage());
			}
		}
		/*
		 * 退出登录
		 */
		if (requestURI.contains("/logout")) {
			// 清除登陆信息
			stringRedisTemplate.delete(token);
			throw new UserLoginException(ResultEnum.LOGOUT_SUCCESS.getCode(), ResultEnum.LOGOUT_SUCCESS.getMessage());
		}
		// method
		log.info("method={}", request.getMethod());

		// ip
		log.info("ip={}", request.getRemoteAddr());

		// 类方法
		log.info("class_method={}",
				joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());

		// 参数
		log.info("args={}", joinPoint.getArgs());

	}

	@AfterReturning(returning = "object", pointcut = "webLog()")
	public void doAfterReturning(Object object) {
		log.info("========Leave Interface========");
		log.info("result={}", object);
	}
}
