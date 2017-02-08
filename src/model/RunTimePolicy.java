package model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("RunTimePolicy")
public class RunTimePolicy {

	private Integer id;
	private String access;
	private String dataLabel;
	private String sourceApplication;
	private String destinationApplication;
	private String createTime;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAccess() {
		return access;
	}
	public void setAccess(String access) {
		this.access = access;
	}

	public String getDataLabel() {
		return dataLabel;
	}
	public void setDataLabel(String dataLabel) {
		this.dataLabel = dataLabel;
	}
	public String getSourceApplication() {
		return sourceApplication;
	}
	public void setSourceApplication(String sourceApplication) {
		this.sourceApplication = sourceApplication;
	}
	public String getDestinationApplication() {
		return destinationApplication;
	}
	public void setDestinationApplication(String destinationApplication) {
		this.destinationApplication = destinationApplication;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
	
}
