package model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Condition")
public class Condition {
	
	private String name;
	private String value;
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
