package model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class AndroidManifest {

	@Column(name="id")
	private int id;
	
	@Column(name="app_id")
	private int appId;
	
	@Column(name="permission_id")
	private int permissionId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public int getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(int permissionId) {
		this.permissionId = permissionId;
	}
	
	
}
