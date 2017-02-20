package com.tts.codelab.oauth2;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@SpringBootApplication
@Configuration
public class Oauth2DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(Oauth2DemoApplication.class, args);
	}
	
	@Value("${mySecurity.keyStoreFileName}")
    private String keyStoreFileName;
    
    @Value("${mySecurity.keyStoreAlias}")
    private String keyStoreAlias;
    
    @Value("${mySecurity.secret}")
    private String secret;
    
    @Autowired
    private JwtAccessTokenConverter jwtTokenEnhancer;
    
    @Autowired
    private TokenStore jwtTokenStore;
    
    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtTokenEnhancer);
    }

    @Bean
    protected JwtAccessTokenConverter jwtTokenEnhancer() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(keyStoreFileName),
                secret.toCharArray());
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair(keyStoreAlias));
        return converter;
    }
    
	@Configuration
	@EnableAuthorizationServer
	public class OAuth2Configuration extends AuthorizationServerConfigurerAdapter {
	    @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;
	    
	    @Override
	    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
	        // @formatter:off
	        clients
	            .inMemory()
	                .withClient("web_app")
	                .scopes("FOO")
	                .autoApprove(true)
	                .authorities("FOO_READ", "FOO_WRITE")
	                .authorizedGrantTypes("implicit","refresh_token", "password", "authorization_code")
                .and()
                    .withClient("client")
                    .secret("codelab")
                    .authorities("FOO_READ", "FOO_WRITE")
                    .authorizedGrantTypes("client_credentials", "refresh_token")
                    .scopes("server");
	        // @formatter:on
	    }
	    
	    @Override
	    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
	        // @formatter:off
	        endpoints
	            .tokenStore(jwtTokenStore)
	            .tokenEnhancer(jwtTokenEnhancer)
	            .authenticationManager(authenticationManager);
	        // @formatter:on
	    }
	}
	
	@Configuration
	class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	    @Autowired
	    private UserDetailsService userDetailsService;

        @Override
	    @Bean
	    public AuthenticationManager authenticationManagerBean() throws Exception {
	        return super.authenticationManagerBean();
	    }

	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        // @formatter:off
	        http
	            .csrf().disable()
	                .exceptionHandling()
	                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
	            .and()
	                .authorizeRequests()
	                .antMatchers("/**").authenticated()
	            .and()
	                .httpBasic();
	        // @formatter:on
	    }

	    @Override
	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	        // @formatter:off
	        auth
	            .userDetailsService(userDetailsService)
	            .passwordEncoder(new BCryptPasswordEncoder());
	        // @formatter:on
	    }
	}
}
