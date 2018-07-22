package com.net128.app.wechatin.config;

//import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
////@Configuration
////@Order(1)
//public class SecuritiyConfig extends WebSecurityConfigurerAdapter {
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//            .authorizeRequests()
//                .requestMatchers(EndpointRequest.to("info")).permitAll()
//            //    .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
//                .requestMatchers(EndpointRequest.to("/**")).permitAll()
//             //   .antMatchers("/cgi-bin/message/custom/").permitAll()
//
//            .and()
//                .httpBasic();
//    }
//}
