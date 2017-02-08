package model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class DataLabel {

	@Column(name="id")
	private int id;
	
	@Column(name="label")
	private String label;
	
	@Column(name="file_name")
	private String fileName;
	
	@Column(name="create_time")
	private Date createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
