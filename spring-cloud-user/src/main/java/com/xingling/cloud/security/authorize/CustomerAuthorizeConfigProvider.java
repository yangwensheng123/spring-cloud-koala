package com.xingling.cloud.security.authorize;

import com.xingling.cloud.constant.SecurityConstants;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * <p>Title:      CustomerAuthorizeConfigProvider. </p>
 * <p>Description 核心模块的授权配置提供器，安全模块涉及的url的授权配置在这里 </p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company:    xinglinglove </p>
 *
 * @author <a href="190332447@qq.com"/>杨文生</a>
 * @since 2018 /2/7 15:54
 */
@Component
@Order(Integer.MIN_VALUE)
public class CustomerAuthorizeConfigProvider implements AuthorizeConfigProvider {


	@Override
	public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
		config.antMatchers(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
				SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_MOBILE,
				SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*",
				SecurityConstants.DEFAULT_LOGOUT_URL,
				"/user/**",
				"/druid/**", "/oauth/**", "/swagger-ui.html", "/swagger-resources/**","/v2/api-docs").permitAll();
		return false;
	}

}
