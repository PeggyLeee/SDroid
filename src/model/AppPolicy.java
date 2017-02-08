package model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("AppPolicy")
public class AppPolicy {

	private Integer id;
	private String access;
	private String dataLabel;
	private String application;
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
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
		

}
