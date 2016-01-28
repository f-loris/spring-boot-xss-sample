/**
 *
 */
package at.fhooe.sfs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import at.fhooe.sfs.domain.BlogPost;

/**
 * @author f.loris
 *
 */
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

	List<BlogPost> findAllByOrderByIdDesc();
}
