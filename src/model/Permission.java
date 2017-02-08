package model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Permission {

	@Column(name="id")
	private int id;
	@Column(name="permission")
	private String permission;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	
}
