package com.tts.codelab.oauth2;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.FileCopyUtils;

@SpringBootApplication
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Oauth2ResourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(Oauth2ResourceApplication.class, args);
	}
	
	@Configuration
	public class JwtConfiguration {

	    @Autowired
	    private JwtAccessTokenConverter jwtAccessTokenConverter;
	    
	    @Bean
	    public TokenStore tokenStore() {
	        return new JwtTokenStore(jwtAccessTokenConverter);
	    }

	    @Bean
	    @Qualifier("publicTokenConverter")
	    protected JwtAccessTokenConverter jwtTokenEnhancer() {
	        JwtAccessTokenConverter converter =  new JwtAccessTokenConverter();
	        Resource resource = new ClassPathResource("public_key");
	        String publicKey = null;
	        try {
	            publicKey = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        }
	        converter.setVerifierKey(publicKey);
	        return converter;
	    }
	}
	
	@Configuration
	@EnableResourceServer
	public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter{

	    @Autowired
        TokenStore tokenStore;
	    
	    @Override
	    public void configure(HttpSecurity http) throws Exception {
	        http
	                .csrf().disable()
	                .authorizeRequests()
	                .antMatchers("/**").authenticated()
	                .antMatchers(HttpMethod.GET, "/foo").hasAuthority("FOO_READ");
	                //.antMatchers(HttpMethod.POST, "/foo").hasAuthority("FOO_WRITE");
	                //you can implement it like this, but I show method invocation security on write
	    }


	    @Override
	    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
	        resources.resourceId("foo").tokenStore(tokenStore);
	    }
	}
}
