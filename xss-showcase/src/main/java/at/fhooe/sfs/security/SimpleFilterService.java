/**
 *
 */
package at.fhooe.sfs.security;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Component;

/**
 * @author f.loris
 *
 */
@Component
public class SimpleFilterService implements IXssFilterService {

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
		return StringEscapeUtils.escapeHtml4(potentiallyDirtyParameter);
	}

}
