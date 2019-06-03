package org.spuzi.domain;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "news")
public class News {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String title;

	// columnDefinition to specify that the field is a long text
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "publication_data")
	private Date publicationData;

	@Column(name = "image_url")
	private String imageUrl;

	public News(String title, String description, Date publicationData, String imageUrl) {
		this.title = title;
		this.description = description;
		this.publicationData = publicationData;
		this.imageUrl = imageUrl;
	}

	public News() {
	}

	public Integer getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getPublicationData() {
		return publicationData;
	}

	public void setPublicationData(Date publicationData) {
		this.publicationData = publicationData;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "News [id=" + id + ", title=" + title + ", description=" + description + ", publicationData="
				+ publicationData + ", imageUrl=" + imageUrl + "]";
	}

}
