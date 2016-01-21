/**
 *
 */
package at.fhooe.sfs.security;

/**
 * @author f.loris
 *
 */
public interface IXssFilterService {
	public String filterString(final String potentiallyDirtyParameter);
}
