package viewmodel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.Application;
import model.InstallTimePolicy;
import model.Policy;
import model.PolicyMatched;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

import dao.ApplicationDao;
import dao.PolicyDao;
import dao.PolicyMatchedDao;
import util.APKDownload;
import util.ConfigFile;
import util.LogInfo;
import util.ParseXML;
import util.PolicyMatchedUtil;

public class PolicyMatchedViewModel {

	// Model
	private Application application;
	private PolicyMatched policyMatched;
	// Dao
	private PolicyDao plDao;
	private ApplicationDao aDao;
	private PolicyMatchedDao pmDao;
	// Util
	private LogInfo logInfo;
	private ConfigFile config;
	private ParseXML parseXML;
	private APKDownload apkDownload;
	// ListView
	private List<PolicyMatched> policyMatcheds;
	private List<InstallTimePolicy> installTimePolicies;
	// UI component
	@Wire("#appLabel")
	Label appLabel;
	@Wire("#pkName")
	Label pkName;
	@Wire("#version")
	Label version;
	@Wire("#size")
	Label size;
	@Wire("#creator")
	Label creator;
	@Wire("#cEmail")
	Label cEmail;
	@Wire("#website")
	Label website;
	@Wire("#result")
	Label result;

	/**
	 * 功能: 初始化設定
	 * */
	@Init
	public void init() {
		application = new Application();
		policyMatched = new PolicyMatched();

		plDao = new PolicyDao();
		aDao = new ApplicationDao();
		pmDao = new PolicyMatchedDao();

		logInfo = new LogInfo();
		config = new ConfigFile();
		parseXML = new ParseXML();
		apkDownload = new APKDownload();

		policyMatcheds = new ArrayList<PolicyMatched>();
		installTimePolicies = new ListModelList<InstallTimePolicy>();

		loadData();
	}

	/**
	 * 功能: 當頁面初始化後，載入清單資料
	 * */
	public void loadData() {
		policyMatcheds = pmDao.getListByGroup();
		for (PolicyMatched pm : policyMatcheds) {
			Application app = aDao.getListById(pm.getAppId());
			pm.setApp(app);
		}
	}

	@Command
	public void selected() {
		boolean check = false;
		List<PolicyMatched> list = pmDao.getListByAid(policyMatched.getAppId());
		// App
		application = policyMatched.getApp();
		appLabel.setValue(application.getAppLabel());
		pkName.setValue(application.getPkName());
		version.setValue(application.getVersion());
		if (application.getInstallSize() != null) {
			size.setValue(application.getInstallSize().toString());
		}
		creator.setValue(application.getCreator());
		cEmail.setValue(application.getContactEmail());
		website.setValue(application.getContactWebsite());
		// Policy
		installTimePolicies.clear();
		for (PolicyMatched pm : list) {
			Policy p = plDao.getById(pm.getPolicyId());
			InstallTimePolicy itp = (InstallTimePolicy) parseXML
					.XMLParseToInstallObj(p.getPolicy());
			itp.setId(p.getId());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			itp.setCreateTime(sdf.format(p.getCreateTime()));
			itp.setResult(pm.getResult());
			installTimePolicies.add(itp);
			if (pm.getResult().equals(config.getPropValue("deny"))) {
				check = true;
			}
		}
		if (check) {
			result.setStyle("color:red;font-weight:bolder;");
			result.setValue("Deny");
		} else {
			result.setStyle("color:blue;font-weight:bolder;");
			result.setValue("Allow");
		}

	}

	/**
	 * 功能: 搜尋App資訊，並將儲存至資料庫 pkName: Package name
	 * */
	@Command
	public void searchAPKInfo(@BindingParam("mStr") String pkName) {
		if (pkName.trim().isEmpty()) {
			Messagebox.show("此欄位不可為空");
			return;
		}
		boolean result = searchApkInfo(pkName);
		if (result) {
			Messagebox.show("成功新增 " + pkName + " 檔案資訊");
		} else {
			Messagebox.show("已存在 " + pkName + " 檔案資訊");
		}
	}

	public boolean searchApkInfo(String pkName) {
		boolean result = false;
		Application app = aDao.findByPkName(pkName);
		if (app == null) {
			apkDownload.SearchAPKFromGooglePlay(pkName);
			result = true;
		}
		return result;
	}

	private void showNotify(String msg, Component ref) {
		Clients.showNotification(msg, "error", ref, "middle_center", 10);
	}

	/**
	 * Setter & Getter 物件
	 * */
	public List<PolicyMatched> getPolicyMatcheds() {
		return policyMatcheds;
	}

	public List<InstallTimePolicy> getInstallTimePolicies() {
		return installTimePolicies;
	}

	public PolicyMatched getPolicyMatched() {
		return policyMatched;
	}

	public void setPolicyMatched(PolicyMatched policyMatched) {
		this.policyMatched = policyMatched;
	}

	public Application getApplication() {
		return application;
	}

	public ConfigFile getConfig() {
		return config;
	}

	public void setConfig(ConfigFile config) {
		this.config = config;
	}

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}
}
