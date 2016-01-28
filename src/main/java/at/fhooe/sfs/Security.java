/**
 *
 */
package at.fhooe.sfs;

import java.util.Arrays;

import org.apache.catalina.Context;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.Policy;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import at.fhooe.sfs.security.AntiSamyFilterService;
import at.fhooe.sfs.security.SimpleFilterService;
import at.fhooe.sfs.security.XssFilter;

/**
 *
 * @author f.loris
 *
 */
@Configuration
public class Security extends WebMvcConfigurerAdapter {

	@Override
	public void addViewControllers(final ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
	}

	@Bean
	public ApplicationSecurity applicationSecurity() {
		return new ApplicationSecurity();
	}

	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(final HttpSecurity http) throws Exception {
			http.csrf().disable()//
					.authorizeRequests().antMatchers("/css/**", "/webjars/**", "/").permitAll().anyRequest().fullyAuthenticated().and().formLogin().loginPage("/login")
					.failureUrl("/login?error").permitAll().and().logout().permitAll();
		}

		@Override
		public void configure(final AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication()//
					.withUser("alice").password("alice").roles("USER")//
					.and().withUser("bob").password("bob").roles("USER");
		}

	}

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
		factory.setTomcatContextCustomizers(Arrays.asList(new CustomCustomizer()));
		return factory;
	}

	/**
	 * dirty hack to remove httponly flag from cookie
	 *
	 */
	static class CustomCustomizer implements TomcatContextCustomizer {
		@Override
		public void customize(final Context context) {
			context.setUseHttpOnly(false);
		}
	}

	@Bean
	public AntiSamy antiSamy(final ApplicationContext ctx) {
		try {
			final Policy policy = Policy.getInstance(ctx.getResource("classpath:configuration/antisamy.xml").getFile());
			return new AntiSamy(policy);
		} catch (final Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	@Bean
	public FilterRegistrationBean antiSamyFilterRegistration(final AntiSamyFilterService filterService) {

		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new XssFilter(filterService));
		registration.addUrlPatterns("/antisamy/*");

		registration.setName("antiSamyFilter");
		return registration;
	}

	@Bean
	public FilterRegistrationBean simpleFilterRegistration(final SimpleFilterService filterService) {

		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new XssFilter(filterService));
		registration.addUrlPatterns("/secure/*");

		registration.setName("simpleFilter");
		return registration;
	}

}
