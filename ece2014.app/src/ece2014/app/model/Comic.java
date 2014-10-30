package ece2014.app.model;

import javafx.scene.image.Image;

public class Comic {

	private String title;
	private Integer issueNumber;
	private Image cover;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
		System.out.println(title);
	}
	public Integer getIssueNumber() {
		return issueNumber;
	}
	public void setIssueNumber(Integer issueNumber) {
		this.issueNumber = issueNumber;
		System.out.println(issueNumber);
	}
	public Image getCover() {
		return cover;
	}
	public void setCover(Image cover) {
		this.cover = cover;
		System.out.println(cover);
	}
	
	
	
}
