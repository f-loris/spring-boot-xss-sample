package at.fhooe.sfs.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Servlet filter that checks all request parameters for potential XSS attacks.
 * Based on https://github.com/barrypitman/antisamy-servlet-filter
 *
 * @author f.loris, barry pitman
 */
public class XssFilter implements Filter {

	private final IXssFilterService filterService;

	public XssFilter(final IXssFilterService filterService) {

		this.filterService = filterService;

	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			chain.doFilter(new CleanServletRequest((HttpServletRequest) request, this.filterService), response);
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		//empty
	}

	@Override
	public void destroy() {
		//empty
	}

	/**
	 * Wrapper for a {@link HttpServletRequest} that returns 'safe' parameter values by
	 * passing the raw request parameters through the anti-samy filter. Should be private
	 */
	public static class CleanServletRequest extends HttpServletRequestWrapper {

		private final IXssFilterService filterService;

		protected CleanServletRequest(final HttpServletRequest request, final IXssFilterService filterService) {
			super(request);
			this.filterService = filterService;

		}

		/**
		 * overriding getParameter functions in {@link ServletRequestWrapper}
		 */
		@Override
		public String[] getParameterValues(final String name) {
			final String[] originalValues = super.getParameterValues(name);
			if (originalValues == null) {
				return null;
			}
			final List<String> newValues = new ArrayList<String>(originalValues.length);
			for (final String value : originalValues) {
				newValues.add(this.filterService.filterString(value));
			}
			return newValues.toArray(new String[newValues.size()]);
		}

		@Override
		@SuppressWarnings("unchecked")
		public Map getParameterMap() {
			final Map<String, String[]> originalMap = super.getParameterMap();
			final Map<String, String[]> filteredMap = new ConcurrentHashMap<String, String[]>(originalMap.size());
			for (final String name : originalMap.keySet()) {
				filteredMap.put(name, this.getParameterValues(name));
			}
			return Collections.unmodifiableMap(filteredMap);
		}

		@Override
		public String getParameter(final String name) {
			final String potentiallyDirtyParameter = super.getParameter(name);
			return this.filterService.filterString(potentiallyDirtyParameter);
		}
	}

}