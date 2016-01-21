/**
 *
 */
package at.fhooe.sfs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import at.fhooe.sfs.domain.BlogPost;
import at.fhooe.sfs.repository.BlogPostRepository;

/**
 * @author f.loris
 *
 */
@Controller
public class BlogController {

	@Autowired
	private BlogPostRepository repository;

	@RequestMapping(value = "/")
	public ModelAndView index() {

		return new ModelAndView("index", "posts", this.repository.findAll());
	}

	@RequestMapping(value = { "/unsecure/addPost", "/secure/addPost", "/antisamy/addPost" }, method = RequestMethod.GET)
	public ModelAndView addPost() {

		return new ModelAndView("addPost", "post", new BlogPost());
	}

	@RequestMapping(value = { "/unsecure/addPost", "/secure/addPost", "/antisamy/addPost" }, method = RequestMethod.POST)
	public ModelAndView addPost(final BlogPost post) {

		this.repository.save(post);
		return this.index();
	}

}
