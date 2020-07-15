package com.ozymern.spring.microservices.zuul.oauth2;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@RefreshScope
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {


    //configuracion del token
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

        resources.tokenStore(tokenStore());
    }

    @Value("${config.security.oaut.jwt.key}")
    private String key;

    //configuracion de las rutas
    @Override
    public void configure(HttpSecurity http) throws Exception {
        //proteger endpoint
        //cualquier usuario se puede autenticar
        http.authorizeRequests().antMatchers("/api/v1/oauth2/oauth/**").permitAll()
            .antMatchers(HttpMethod.GET, "/api/v1/product/products", "/api/v1/item/items", "/api/v1/user/users").permitAll()
            //ROLE_ se coloca de forma automatica en hasAnyRole
            .antMatchers(HttpMethod.GET, "/api/v1/product/products/{id}", "/api/v1/item/items/{id}/count/{count", "/api/v1/user/users/{id}").hasAnyRole("ADMIN", "USER")
            .antMatchers(HttpMethod.POST, "/api/v1/product/products", "/api/v1/item/products", "/api/v1/user/users").hasAnyRole("ADMIN")
            .antMatchers(HttpMethod.PUT, "/api/v1/product/products", "/api/v1/item/products", "/api/v1/user/users").hasAnyRole("ADMIN")
            .antMatchers(HttpMethod.DELETE, "/api/v1/product/products", "/api/v1/item/products", "/api/v1/user/users").hasAnyRole("ADMIN")
            //cualquier otra ruta requiere autenticacion
            .anyRequest().authenticated()
            //agregar cors
            .and().cors().configurationSource(corsConfigurationSource());

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("POST", "GET", "OPTIONS", "DELETE", "PUT"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //QUE SE APLICA ESTA CONFIGURACION A TODAS LAS RUTAS
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    //filtro para que se quede registrado a nivel global los cors
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> corsFilterFilterRegistrationBean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        //asignamos prioridad alta
        corsFilterFilterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return corsFilterFilterRegistrationBean;
    }

    @Bean
    public JwtTokenStore tokenStore() {

        //  recibe el accessTokenConverter  es el componente que se encarga de convertir el token en jwt
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {

        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey(key);

        return accessTokenConverter;

    }
}
