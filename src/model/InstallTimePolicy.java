package model;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("InstallTimePolicy")
public class InstallTimePolicy {

	private Integer id;
	private String access;
	private String application;
	private String permission;
	private List<Condition> conditions;
	private String conditionStr;

	private String createTime;
	
	private String result;
	
	public InstallTimePolicy(){
		conditions = new ArrayList();
	}
	

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

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}


	public List<Condition> getConditions() {
		return conditions;
	}


	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}


	public String getCreateTime() {
		return  createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}


	public String getConditionStr() {
		if(conditions.size()>0){
			conditionStr = "";
			for(int i =0; i<conditions.size(); i++){
				Condition condition = conditions.get(i);
				if(i == conditions.size()-1){
					conditionStr+=condition.getName()+":"+condition.getValue();
					break;
				}
				conditionStr+=condition.getName()+":"+condition.getValue()+" , ";
			}
		}
		return conditionStr;
	}


	public void setConditionStr(String conditionStr) {
		this.conditionStr = conditionStr;
	}


	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}
	
	
}

