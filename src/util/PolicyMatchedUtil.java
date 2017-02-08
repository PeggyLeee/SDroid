package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.AndroidManifest;
import model.Application;
import model.InstallTimePolicy;
import model.Permission;
import model.Policy;
import model.PolicyMatched;
import dao.AndroidManifestDao;
import dao.ApplicationDao;
import dao.PermissionDao;
import dao.PolicyDao;
import dao.PolicyMatchedDao;

/**
 * SDroid政策比對機制(重要)
 * */
public class PolicyMatchedUtil {

	// Dao
	private PolicyDao plDao;
	private ApplicationDao aDao;
	private PermissionDao pDao;
	private PolicyMatchedDao pmDao;
	private AndroidManifestDao amfDao;
	// Util
	private ParseXML parseXML;
	private ConfigFile config;
	private APKDownload apkDownload;

	/**
	 * 功能: 初始化設定
	 * */
	public PolicyMatchedUtil() {

		plDao = new PolicyDao();
		aDao = new ApplicationDao();
		pDao = new PermissionDao();
		pmDao = new PolicyMatchedDao();
		amfDao = new AndroidManifestDao();

		parseXML = new ParseXML();
		config = new ConfigFile();
		apkDownload = new APKDownload();
	}

	/**
	 * 功能: 政策比對分析(主要執行程式)
	 * pkName：Android package name
	 * */
	public String analysisPolicyMatched(String pkName) {

		String status = config.getPropValue("allow");
		Application app = aDao.findByPkName(pkName);

		// Application Table 沒有該pkName資料
		if (app == null) {
			//使用APK Downloader 取的該pkName資料
			apkDownload.SearchAPKFromGooglePlay(pkName);
			app = apkDownload.getApplication();
		}
		// 搜尋Policies
		List<Policy> pList = plDao.getListByAppName(app.getAppLabel());
		if (pList.size() > 0) {
			// 有Policies
			pmDao.removeByAid(app.getId());
			for (Policy p : pList) {
				String result = null;
				InstallTimePolicy itp = (InstallTimePolicy) parseXML
						.XMLParseToInstallObj(p.getPolicy());
				result = matchedPolicy(itp, app);
				// 儲存至PolicyMatched Table
				savePolicyMatched(p.getId(), app.getId(), result);
				if (result == config.getPropValue("deny")) {
					status = result;
				}
			}
		} else {
			// 無Policies
			status = config.getPropValue("noData");
		}
		return status;

	}

	/**
	 * 功能: 政策比對 
	 * itp: 欲比對的policy 
	 * app: 欲比對的Application
	 * */
	private String matchedPolicy(InstallTimePolicy itp, Application app) {

		String status = null;
		switch (itp.getAccess()) {
		case "allow":
//			if (itp.getPermission().equals("any")) {
				if (itp.getConditions().size() > 0) {
					String cMinVersion = itp.getConditions().get(0).getValue(); // min-Version
					if (compareMinVersion(cMinVersion, app.getVersion())) {
						status = config.getPropValue("allow");
					} else {
						status = config.getPropValue("deny");
					}
				}else{
					status = config.getPropValue("allow");
				}
//			}
			break;
		case "deny":
			if(itp.getPermission() == "any"){
				status = config.getPropValue("deny");
			}else{
				Permission p = pDao.getByPermission(itp.getPermission());
				AndroidManifest amf = amfDao.findByIds(app.getId(), p.getId());
				if (amf != null) {
					status = config.getPropValue("deny");
				} else {
					status = config.getPropValue("allow");
				}
			}
			break;
		}
		return status;
	}

	/**
	 * 功能: App版本比對 
	 * conVersion： Policy規定的最小版本號 
	 * appVersion: Application目前的版本號
	 * */
	private boolean compareMinVersion(String conVersion, String appVersion) {

		int aValue = 0;
		boolean compare = false;
		List<String> cv = new ArrayList<String>(Arrays.asList(conVersion
				.split("\\.")));
		List<String> av = new ArrayList<String>(Arrays.asList(appVersion
				.split("\\.")));

		if (av.size() > cv.size()) {
			int diff = av.size() - cv.size();
			for (int j = 0; j < diff; j++) {
				cv.add("0");
			}
		}
		for (int i = 0; i < cv.size(); i++) {
			if (Integer.parseInt(cv.get(i)) <= Integer.parseInt(av.get(aValue))) {
				compare = true;
			} else {
				compare = false;
				break;
			}
			if (aValue == av.size() - 1) {
				break;
			}
			aValue++;
		}
		return compare;
	}

	/**
	 * 功能: 儲存PolicyMatched Table
	 * policy_id: Policy id 
	 * app_id: Application id
	 * result: 分析結果
	 * */
	private void savePolicyMatched(int pId, int aId, String result) {
		PolicyMatched pm = new PolicyMatched();
		pm.setPolicyId(pId);
		pm.setAppId(aId);
		pm.setResult(result);
		pmDao.insert(pm);
	}
}
