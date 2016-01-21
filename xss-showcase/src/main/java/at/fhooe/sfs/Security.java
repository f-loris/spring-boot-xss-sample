/**
 *
 */
package at.fhooe.sfs;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.Policy;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import at.fhooe.sfs.security.AntiSamyFilterService;
import at.fhooe.sfs.security.SimpleFilterService;
import at.fhooe.sfs.security.XssFilter;

/**
 *
 * @author f.loris
 *
 */
@Configuration
public class Security {

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
