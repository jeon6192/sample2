package com.taekwang.tcast.config.security;

import com.taekwang.tcast.config.security.admin.AdminAuthenticationFailureHandler;
import com.taekwang.tcast.config.security.admin.AdminAuthenticationProvider;
import com.taekwang.tcast.config.security.admin.AdminAuthenticationSuccessHandler;
import com.taekwang.tcast.config.security.admin.CustomAdminUserDetailsService;
import com.taekwang.tcast.config.security.auth.AuthFailureHandler;
import com.taekwang.tcast.config.security.auth.AuthService;
import com.taekwang.tcast.config.security.auth.AuthSuccessHandler;
import com.taekwang.tcast.config.security.user.*;
import com.taekwang.tcast.service.AdminService;
import com.taekwang.tcast.service.MailService;
import com.taekwang.tcast.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
			"/", "/signup", "/login/**", "/admin/login", "/admin/register"
	};

	private final CustomUserDetailsService customUserDetailsService;
	private final UserService userService;
	private final CustomAdminUserDetailsService customAdminUserDetailsService;
	private final AdminService adminService;
	private final CustomAuthenticationEntryPoint authenticationEntryPoint;
	private final CustomAccessDeniedHandler accessDeniedHandler;
	private final AuthService authService;
	private final MailService mailService;

	public SecurityConfig(CustomUserDetailsService customUserDetailsService, UserService userService, CustomAdminUserDetailsService customAdminUserDetailsService,
                          AdminService adminService, CustomAuthenticationEntryPoint authenticationEntryPoint, CustomAccessDeniedHandler accessDeniedHandler, AuthService authService, MailService mailService) {
		this.customUserDetailsService = customUserDetailsService;
		this.userService = userService;
		this.customAdminUserDetailsService = customAdminUserDetailsService;
		this.adminService = adminService;
		this.authenticationEntryPoint = authenticationEntryPoint;
		this.accessDeniedHandler = accessDeniedHandler;
		this.authService = authService;
        this.mailService = mailService;
    }

	@Configuration
	@Order(1)
	public static class AdminConfigurationAdapter {

		private final SecurityConfig securityConfig;

        public AdminConfigurationAdapter(SecurityConfig securityConfig) {
            this.securityConfig = securityConfig;
        }

		@Bean
		public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
			http
					.antMatcher("/admin/**")
					.authorizeRequests(authRequest ->
							authRequest
									.antMatchers(PERMIT_ALL).permitAll()
									.anyRequest().authenticated())
					.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
							.authenticationEntryPoint(securityConfig.authenticationEntryPoint)
							.accessDeniedHandler(securityConfig.accessDeniedHandler))
					.csrf(AbstractHttpConfigurer::disable)
					.formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
							.usernameParameter("username")
							.passwordParameter("password")
							.loginProcessingUrl("/admin/login")
							.successHandler(securityConfig.adminAuthenticationSuccessHandler())
							.failureHandler(securityConfig.adminAuthenticationFailureHandler())
							.and().authenticationManager(new ProviderManager(securityConfig.adminAuthenticationProvider())))
					.logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
							.logoutUrl("/admin/logout")
							.logoutSuccessUrl("/admin/login")
							.invalidateHttpSession(true))
					.sessionManagement()
							.maximumSessions(1)
							.maxSessionsPreventsLogin(false)
			;

			return http.build();
		}
	}

	@Configuration
	@Order(2)
	public static class UserConfigurationAdapter {

		private final SecurityConfig securityConfig;

        public UserConfigurationAdapter(SecurityConfig securityConfig) {
            this.securityConfig = securityConfig;
        }

        @Bean
		public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
			http
					.authorizeRequests(authRequest ->
							authRequest
									.antMatchers(PERMIT_ALL).permitAll()
									.anyRequest().authenticated())
					.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
							.authenticationEntryPoint(securityConfig.authenticationEntryPoint)
							.accessDeniedHandler(securityConfig.accessDeniedHandler))
					.csrf(AbstractHttpConfigurer::disable)
					.formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
							.usernameParameter("username")
							.passwordParameter("password")
							.loginProcessingUrl("/login")
							.successHandler(securityConfig.userAuthenticationSuccessHandler())
							.failureHandler(securityConfig.userAuthenticationFailureHandler()))
					.oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer
							.loginPage("/login")
							.userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
									.userService(securityConfig.authService))
							.successHandler(securityConfig.authSuccessHandler())
							.failureHandler(securityConfig.authFailureHandler()))
					.logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
							.logoutUrl("/logout")
							.logoutSuccessUrl("/login")
							.invalidateHttpSession(true))
					.sessionManagement()
							.maximumSessions(1)
							.maxSessionsPreventsLogin(false)
			;

			return http.build();
		}

		@Bean
		public AuthenticationManager authenticationManager() {
			return new ProviderManager(securityConfig.userAuthenticationProvider());
		}
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

	// User Standard Login Provider
	@Bean
	public UserAuthenticationProvider userAuthenticationProvider() {
		return new UserAuthenticationProvider(customUserDetailsService, passwordEncoder());
	}

	// User Standard Login Success Handler
	@Bean
	public UserAuthenticationSuccessHandler userAuthenticationSuccessHandler() {
		return new UserAuthenticationSuccessHandler(userService);
	}

	// User Standard Login Failure Handler
	@Bean
	public UserAuthenticationFailureHandler userAuthenticationFailureHandler() {
		return new UserAuthenticationFailureHandler();
	}

	// User Social Login Success Handler
	@Bean
	public AuthSuccessHandler authSuccessHandler() {
		return new AuthSuccessHandler(userService);
	}

	// User Social Login Failure Handler
	@Bean
	public AuthFailureHandler authFailureHandler() {
		return new AuthFailureHandler();
	}

	// Admin Provider
	@Bean
	public AdminAuthenticationProvider adminAuthenticationProvider() {
		return new AdminAuthenticationProvider(customAdminUserDetailsService, passwordEncoder());
	}

	// Admin Success Handler
	@Bean
	public AdminAuthenticationSuccessHandler adminAuthenticationSuccessHandler() {
		return new AdminAuthenticationSuccessHandler(adminService);
	}

	// Admin Failure Handler
	@Bean
	public AdminAuthenticationFailureHandler adminAuthenticationFailureHandler() {
		return new AdminAuthenticationFailureHandler(adminService, mailService);
	}

}
