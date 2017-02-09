package model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import jxl.write.DateTime;

@Entity
public class PolicyMatched {

	@Column(name="id")
	private int id;
	@Column(name="policy_id")
	private int policyId;
	@Column(name="app_id")
	private int appId;
	@Column(name="result")
	private String result;
	@Column(name="modify_time")
	private Date modifyTime;
	@Column(name="create_time")
	private Date createTime;
	
	private Application app;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPolicyId() {
		return policyId;
	}
	public void setPolicyId(int policyId) {
		this.policyId = policyId;
	}
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Application getApp() {
		return app;
	}
	public void setApp(Application app) {
		this.app = app;
	}
	
	
	
}
