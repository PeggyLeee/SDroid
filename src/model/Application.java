package model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;



@Entity
public class Application {

	@Column(name="id")
	private int id;
	
	@Column(name="asset_id")
	private String assetId;
	
	@Column(name="app_label")
	private String appLabel;
	
	@Column(name="pk_name")
	private String pkName;
	
	@Column(name="version")
	private String version;
	
	@Column(name="apk_file")
	private String apkFile;
	
	@Column(name="install_size")
	private Integer installSize;
	
	@Column(name="creator")
	private String creator;
	
	@Column(name="contact_email")
	private String contactEmail;
	
	@Column(name="contact_website")
	private String contactWebsite;
	
	@Column(name="modify_time")
	private Date modifyTime;
	
	@Column(name="create_time")
	private Date createTime;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAssetId() {
		return assetId;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	public String getAppLabel() {
		return appLabel;
	}
	public void setAppLabel(String appLabel) {
		this.appLabel = appLabel;
	}
	public String getPkName() {
		return pkName;
	}
	public void setPkName(String pkName) {
		this.pkName = pkName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getApkFile() {
		return apkFile;
	}
	public void setApkFile(String apkFile) {
		this.apkFile = apkFile;
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
	public Integer getInstallSize() {
		return installSize;
	}
	public void setInstallSize(Integer installSize) {
		this.installSize = installSize;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	public String getContactWebsite() {
		return contactWebsite;
	}
	public void setContactWebsite(String contactWebsite) {
		this.contactWebsite = contactWebsite;
	}
	
}
