package com.xingling.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * <p>Title:	  spring-cloud-koala <br/> </p>
 * <p>Description TODO <br/> </p>
 * <p>Company:    http://www.xinglinglove.com  <br/> </p>
 *
 * @Author <a href="190332447@qq.com"/>杨文生</a>  <br/>
 * @Date 2017/8/18 17:57
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	public void configure(WebSecurity web) throws Exception {
		//忽略css.jq.img等文件
		web.ignoring().antMatchers("/**.html", "/**.css", "/img/**", "/**.js", "/third-party/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
			.csrf().disable() //HTTP with Disable CSRF
			.authorizeRequests() //Authorize Request Configuration
			.antMatchers("/login",
					"/api/**",
					"/**/heapdump",
					"/**/loggers",
					"/**/liquibase",
					"/**/logfile",
					"/**/flyway",
					"/**/auditevents",
					"/**/jolokia").permitAll() //放开"/api/**"：为了给被监控端免登录注册并解决Log与Logger冲突
			.and()
			.authorizeRequests()
			.antMatchers("/**").hasRole("USER")
			.antMatchers("/**").authenticated()
			.and() //Login Form configuration for all others
			.formLogin()
			.loginPage("/login.html")
			.loginProcessingUrl("/login").permitAll()
			.defaultSuccessUrl("/")
			.and() //Logout Form configuration
			.logout()
			.deleteCookies("remove")
			.logoutSuccessUrl("/login.html").permitAll()
			.and()
			.httpBasic();

	}
}