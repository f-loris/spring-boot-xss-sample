/**
 *
 */
package at.fhooe.sfs.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author f.loris
 *
 */
@Entity
public class BlogPost {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Size(max = 255)
	@Column(name = "title", length = 255, nullable = false)
	private String title;

	@NotNull
	@Lob
	@Column(name = "text", nullable = false)
	private String text;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}
		BlogPost blogentry = (BlogPost) o;
		if (blogentry.id == null || this.id == null) {
			return false;
		}
		return Objects.equals(this.id, blogentry.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.id);
	}

	@Override
	public String toString() {
		return "Blogentry{" + "id=" + this.id + ", title='" + this.title + "'" + ", text='" + this.text + "'" + '}';
	}

}
