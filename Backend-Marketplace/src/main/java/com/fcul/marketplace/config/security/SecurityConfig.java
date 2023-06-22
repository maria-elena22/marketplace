package com.fcul.marketplace.config.security;


import com.fcul.marketplace.config.security.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    SecurityUtils securityUtils;

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.csrf().disable();
        http.cors().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //Allow unauthenticated users to login and register
        http.authorizeRequests().antMatchers("/api/utilizador/login/**", "/api/utilizador/register/**").permitAll();
        //Allow swagger to be public
        http.authorizeRequests().antMatchers("/swagger-ui/**", "/swagger-ui.html", "/configuration/ui",
                "/swagger-resources/**", "/configuration/security", "/webjars/**", "/v2/api-docs/**", "/v3/api-docs/**", "/v3/api-docs.yaml").permitAll();

        //Allow unauthenticated to have access to certain resources
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/categoria/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/produto/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "**").permitAll();

        http.authorizeRequests().anyRequest().authenticated();
        http.addFilterBefore(new CustomAuthorizationFilter(securityUtils), UsernamePasswordAuthenticationFilter.class);
    }
}
