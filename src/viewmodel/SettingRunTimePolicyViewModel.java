package viewmodel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.AppPolicy;
import model.Application;
import model.Label;
import model.Policy;
import model.RunTimePolicy;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

import dao.ApplicationDao;
import dao.LabelDao;
import dao.PolicyDao;
import util.LogInfo;
import util.ParseXML;

public class SettingRunTimePolicyViewModel {
	// Model
	private Label label;
	private Application sourceApplication;
	private Application destinationApplication;
	private RunTimePolicy runTimePolicy;
	private AppPolicy appPolicy;
	// Dao
	private ApplicationDao aDao;
	private PolicyDao plDao;
	private LabelDao dlDao;
	// Util
	private ParseXML parseXML;
	// ListView
	private List<Application> applications;
	private List<RunTimePolicy> runTimePolicies;
	private List<AppPolicy> appPolicies;
	private List<Label> dataLabels;

	/**
	 * 功能: 初始化設定
	 * */
	@Init
	public void init() {
		label = new Label();
		sourceApplication = new Application();
		destinationApplication = new Application();
		runTimePolicy = new RunTimePolicy();
		appPolicy = new AppPolicy();

		aDao = new ApplicationDao();
		plDao = new PolicyDao();
		dlDao = new LabelDao();

		parseXML = new ParseXML();

		applications = new ArrayList<Application>();
		runTimePolicies = new ListModelList<RunTimePolicy>();
		appPolicies = new ListModelList<AppPolicy>();
		dataLabels = new ArrayList<Label>();

		loadData();
	}

	public void loadData() {

		applications = aDao.getAllList();
		dataLabels = dlDao.getAllList();

		getPolicyList("RunTimePolicy");
		getPolicyList("AppPolicy");

	}

	/**
	 * 功能: 取得目前最新的 Policies 清單 type: RunTimePolicy or AppPolicy
	 * */
	public void getPolicyList(String type) {
		List<Policy> pList = plDao.getAllList(type);
		switch (type) {
		case "RunTimePolicy":
			runTimePolicies.clear();
			if (pList != null) {
				for (Policy p : pList) {
					RunTimePolicy rtp = (RunTimePolicy) parseXML
							.XMLParseToRunTimeOrAppObj(p.getPolicy(), type);
					Application sourceApp = aDao.findByPkName(rtp.getSourceApplication());
					Application destApp = aDao.findByPkName(rtp.getDestinationApplication());
					Label label = dlDao.getByLabelId(rtp.getDataLabel());
					
					rtp.setId(p.getId());
					rtp.setDataLabel(label.getLabel());
					rtp.setSourceApplication(sourceApp.getAppLabel());
					rtp.setDestinationApplication(destApp.getAppLabel());
					rtp.setCreateTime(new SimpleDateFormat("yyyy/MM/dd")
							.format(p.getCreateTime()));
					runTimePolicies.add(rtp);
				}
			}
			break;
		case "AppPolicy":
			appPolicies.clear();
			if (pList != null) {
				for (Policy p : pList) {
					AppPolicy ap = (AppPolicy) parseXML
							.XMLParseToRunTimeOrAppObj(p.getPolicy(), type);
					Label label = dlDao.getByLabelId(ap.getDataLabel());
					Application app = aDao.findByPkName(ap.getApplication());
					
					ap.setId(p.getId());
					ap.setDataLabel(label.getLabel());
					ap.setApplication(app.getAppLabel());
					ap.setCreateTime(new SimpleDateFormat("yyyy/MM/dd")
							.format(p.getCreateTime()));
					appPolicies.add(ap);
				}
			}
			break;
		}
	}

	/**
	 * 功能: 新增安全政策 type: RunTimePolicy or AppPolicy
	 * */
	@Command
	public void insertPolicy(@BindingParam("mStr") String type) {
		Policy policy = new Policy();

		switch (type) {

		case "RunTimePolicy":
			if (validator(type)) {
				Messagebox.show("資料欄位不可為空！");
				return;
			}
			runTimePolicy.setDataLabel(label.getLabelId());
			runTimePolicy.setSourceApplication(sourceApplication.getPkName());
			runTimePolicy.setDestinationApplication(destinationApplication.getPkName());
			String runTimeXML = parseXML.RunTimeOrAppObjParseToXML(
					runTimePolicy, type);
			policy.setPolicy(runTimeXML);
			break;
		case "AppPolicy":
			if (validator(type)) {
				Messagebox.show("資料欄位不可為空！");
				return;
			}
			appPolicy.setDataLabel(label.getLabelId());
			appPolicy.setApplication(sourceApplication.getPkName());
			String appXML = parseXML.RunTimeOrAppObjParseToXML(appPolicy, type);
			policy.setPolicy(appXML);
			break;
		}

		policy.setType(type);
		plDao.insert(policy);

		getPolicyList(type);
	}

	/**
	 * 功能: 刪除安全政策
	 * */
	@Command
	public void removePolicy(@BindingParam("mStr") String id) {

		Messagebox.show("Policy刪除成功");
		plDao.removeById(id);

		getPolicyList("RunTimePolicy");
		getPolicyList("AppPolicy");
	}

	/**
	 * 功能: 驗證資料欄位
	 * */
	private boolean validator(String type) {
		boolean check = false;

		switch (type) {
		case "RunTimePolicy":
			if(StringUtils.isBlank(runTimePolicy.getAccess())
					|| StringUtils.isBlank(label.getLabelId())
					|| StringUtils.isBlank(sourceApplication.getPkName())
					|| StringUtils.isBlank(destinationApplication.getPkName())){
				check = true;
			}
			break;
		case "AppPolicy":
			if (StringUtils.isBlank(label.getLabelId())
					|| StringUtils.isBlank(sourceApplication.getPkName())) {
				check = true;
			}
			break;
		}

		return check;
	}

	/**
	 * Setter & Getter 物件
	 * */
	public RunTimePolicy getRunTimePolicy() {
		return runTimePolicy;
	}

	public AppPolicy getAppPolicy() {
		return appPolicy;
	}

	public List<Application> getApplications() {
		return applications;
	}

	public List<RunTimePolicy> getRunTimePolicies() {
		return runTimePolicies;
	}

	public List<AppPolicy> getAppPolicies() {
		return appPolicies;
	}

	public List<Label> getDataLabels() {
		return dataLabels;
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}

	public Application getSourceApplication() {
		return sourceApplication;
	}

	public void setSourceApplication(Application sourceApplication) {
		this.sourceApplication = sourceApplication;
	}

	public Application getDestinationApplication() {
		return destinationApplication;
	}

	public void setDestinationApplication(Application destinationApplication) {
		this.destinationApplication = destinationApplication;
	}

}
