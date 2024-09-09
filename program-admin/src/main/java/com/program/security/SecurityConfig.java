package com.program.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 *  权限配置
 */
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserLoadSecurityServiceImpl userLoadSecurityService;

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userLoadSecurityService);
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //这表示所有以/public/开头的URL路径，只有当用户具有student、teacher或admin任意一种角色时才能访问。
                .mvcMatchers("/public/**").hasAnyAuthority("student", "teacher", "admin","generalAdmin")
                .mvcMatchers("/student/**").hasAnyAuthority("student", "admin", "teacher","generalAdmin")
                .mvcMatchers("/teacher/**").hasAnyAuthority("teacher", "admin","generalAdmin")
                .mvcMatchers("/generalAdmin/**").hasAnyAuthority( "admin","generalAdmin")
                .mvcMatchers("/admin/**").hasAnyAuthority("admin")
                // 配合下面的web.ignore,将下面所有的路径的校验都过滤掉了
                .mvcMatchers("/util/**", "/common/**", "/actuator/**", "/api/**").permitAll()

                .anyRequest().authenticated()

                .and()

                // token认证
                .addFilterBefore(new TokenAuthFilter(authenticationManager()), BasicAuthenticationFilter.class)

                // 认证异常处理器
                .exceptionHandling()
                .accessDeniedHandler(new MyAccessDeniedHandler())
                .authenticationEntryPoint(new MyAuthenticationEntryPointHandler())

                .and().csrf().disable();// 默认csrf token 校验开启，针对 POST, PUT, PATCH

        http.formLogin().disable();
        // 前后端分离关闭配置登录
        http.logout().disable();

        // 所有的Rest服务一定要设置为无状态，以提升操作性能
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    // 公开资源路径
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/util/**", "/common/**")
                .antMatchers("/actuator/**")
                .antMatchers("/api/**")
                .antMatchers("/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/v2/**");
    }
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }
}
