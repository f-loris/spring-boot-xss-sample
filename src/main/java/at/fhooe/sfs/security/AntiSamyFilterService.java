/**
 *
 */
package at.fhooe.sfs.security;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author f.loris
 *
 */
@Component
public class AntiSamyFilterService implements IXssFilterService {
	private static final Logger LOG = Logger.getLogger(AntiSamyFilterService.class);
	@Autowired
	private AntiSamy antiSamy;

	/*
	 * (non-Javadoc)
	 *
	 * @see at.fhooe.sfs.security.IXssFilterService#filterString(java.lang.String)
	 */
	@Override
	public String filterString(final String potentiallyDirtyParameter) {
		if (potentiallyDirtyParameter == null) {
			return null;
		}
		try {
			final CleanResults cr = this.antiSamy.scan(potentiallyDirtyParameter);
			if (cr.getNumberOfErrors() > 0) {
				this.printDebug(cr, potentiallyDirtyParameter);
			}
			return cr.getCleanHTML();
		} catch (final Exception e) {

			LOG.error("Error while xss processing " + e.getMessage()); //do not write stacktrace
			return StringEscapeUtils.escapeHtml4(potentiallyDirtyParameter); // secure fallback
		}
	}

	private void printDebug(final CleanResults cr, final String potentiallyDirtyParameter) {
		final StringBuilder builder = new StringBuilder("antisamy encountered problem with input:");
		builder.append(StringEscapeUtils.escapeHtml4(potentiallyDirtyParameter));
		builder.append("\n error");
		builder.append(cr.getErrorMessages());

		LOG.info(builder.toString());
	}
}
