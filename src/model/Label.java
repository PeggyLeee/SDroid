package model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Label {

	@Column(name="id")
	private int id;
	
	@Column(name="label_Id")
	private String labelId;
	
	@Column(name="label")
	private String label;
	
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getLabelId() {
		return labelId;
	}
	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}
	
}
