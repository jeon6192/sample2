package com.example.springbootsample.config.security;

import com.example.springbootsample.config.security.auth.AuthFailureHandler;
import com.example.springbootsample.config.security.auth.AuthSuccessHandler;
import com.example.springbootsample.service.AuthService;
import com.example.springbootsample.service.CustomUserDetailsService;
import com.example.springbootsample.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private static final String[] PERMIT_ALL = {
			"/", "/signup", "/login/**"
	};

	private final CustomUserDetailsService customUserDetailsService;
	private final UserService userService;
	private final CustomAuthenticationEntryPoint authenticationEntryPoint;
	private final CustomAccessDeniedHandler accessDeniedHandler;
	private final AuthService authService;

	public SecurityConfig(CustomUserDetailsService customUserDetailsService, UserService userService, CustomAuthenticationEntryPoint authenticationEntryPoint, CustomAccessDeniedHandler accessDeniedHandler, AuthService authService) {
		this.customUserDetailsService = customUserDetailsService;
		this.userService = userService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authService = authService;
    }

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.authorizeRequests(authRequest ->
						authRequest
								.antMatchers(PERMIT_ALL).permitAll()
								.anyRequest().authenticated())
				.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
						.authenticationEntryPoint(authenticationEntryPoint)
						.accessDeniedHandler(accessDeniedHandler))
				.csrf(AbstractHttpConfigurer::disable)
				.formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
						.usernameParameter("userId")
						.passwordParameter("password")
						.loginProcessingUrl("/login")
						.successHandler(customAuthenticationSuccessHandler())
						.failureHandler(customAuthenticationFailureHandler()))
				.logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
						.logoutUrl("/logout")
						.logoutSuccessUrl("/")
						.invalidateHttpSession(true))
				.oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer
                        .loginPage("/login")
						.userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
								.userService(authService))
						.successHandler(authSuccessHandler())
						.failureHandler(authFailureHandler()))
		;

		return http.build();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**");
			}
		};
	}

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CustomAuthenticationProvider customDaoAuthenticationProvider() {
		return new CustomAuthenticationProvider(customUserDetailsService, passwordEncoder());
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(customDaoAuthenticationProvider());
	}

	@Bean
	public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
		return new CustomAuthenticationSuccessHandler(userService);
	}

	@Bean
	public CustomAuthenticationFailureHandler customAuthenticationFailureHandler() {
		return new CustomAuthenticationFailureHandler();
	}

	@Bean
	public AuthSuccessHandler authSuccessHandler() {
		return new AuthSuccessHandler(userService);
	}

	@Bean
	public AuthFailureHandler authFailureHandler() {
        return new AuthFailureHandler();
    }
}
