package util;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Application;
import model.Permission;

import com.gc.android.market.api.LoginException;
import com.gc.android.market.api.MarketSession;
import com.gc.android.market.api.MarketSession.Callback;
import com.gc.android.market.api.model.Market.App;
import com.gc.android.market.api.model.Market.AppsRequest;
import com.gc.android.market.api.model.Market.AppsResponse;
import com.gc.android.market.api.model.Market.ResponseContext;
import com.gc.android.market.api.model.Market.GetAssetResponse.InstallAsset;

import dao.AndroidManifestDao;
import dao.ApplicationDao;
import dao.PermissionDao;


public class APKDownload {
	
	private ConfigFile config = null; 
	private Application application = null;
	private LogInfo logInfo = null;
	
	public APKDownload(){
		config = new ConfigFile();
		logInfo = new LogInfo();
	}
	
	/**
	 * 功能: 於Google Market上，搜尋該Application的相關資訊
	 * pkName：Android package name
	 * */
	public void SearchAPKFromGooglePlay(String pname){      
		MarketSession session = new MarketSession(false);
		session.login(config.getPropValue("email"), config.getPropValue("password"), config.getPropValue("deviceId"));
		
        AppsRequest appsRequest = AppsRequest.newBuilder().setStartIndex(0)
                        .setEntriesCount(10).setQuery("pname:"+pname)
                        .setWithExtendedInfo(true).build();
        
        session.append(appsRequest, new Callback<AppsResponse>() {
                @Override
                public void onResult(ResponseContext context, AppsResponse response) {
                        int count = response.getAppCount();
                        System.out.println("APP's count: " + count);
                        for (int i = 0; i < count; i++) {
                                App app = response.getApp(i); 
                                System.out.print(app.toString());
                                saveAppInfo(app);
                        }
                }
        });
        session.flush(); 
	}
	
	/**
	 * 功能: 將Android app資訊，儲存至application、permission、androidManifest DB Table
	 * app: Android app 相關資訊
	 * */
	private void saveAppInfo(App app){
		
		ApplicationDao aDao = new ApplicationDao();
		PermissionDao pDao = new PermissionDao();
		
		//新增Application
		application = new Application();
		
		application.setAssetId(app.getId());
		application.setAppLabel(app.getTitle());
		application.setPkName(app.getPackageName());
		application.setVersion(app.getVersion());
		application.setInstallSize(app.getExtendedInfoOrBuilder().getInstallSize());
		application.setCreator(app.getCreator());
		application.setContactEmail(app.getExtendedInfoOrBuilder().getContactEmail());
		application.setContactWebsite(app.getExtendedInfoOrBuilder().getContactWebsite());
		int aId =aDao.insert(application);
		application.setId(aId);
		
		//新增Permission
		List<Integer> pIdList = new ArrayList<Integer>();
        int pCount = app.getExtendedInfoOrBuilder().getPermissionIdCount();
        for (int i = 0; i < pCount; i++){
        	String perStr = app.getExtendedInfoOrBuilder().getPermissionId(i);
        	Permission permission = pDao.getByPermission(perStr);
        	if(permission != null){
        		pIdList.add(permission.getId());
        	}else{
        		pIdList.add(pDao.insert(perStr));
        	}
        }
        
		//新增 Application & Permission 關聯
        AndroidManifestDao amfDao = new AndroidManifestDao();
        amfDao.insert(aId, pIdList);
        
	}
	
	/**
	 * 功能: 於Google Market上，下載指定的Apk檔案
	 * assetId：為App UniqueID，可透過 SearchAPKFromGooglePlay 方法取得
	 * */
	public boolean APKDownloadFromGooglePlay(String appLabel,String assetId){  
		//ex: assetId = 8069246084405590500
		boolean result = false;
		try{
			logInfo.info("appLabel: %s, assetId: %s", appLabel,assetId);
			
			MarketSession session = new MarketSession(true);
			session.login(config.getPropValue("email"), config.getPropValue("password"), config.getPropValue("deviceId"));
			
			String fileToSave = appLabel + ".apk";
			
			System.out.println("Downloading.. " + fileToSave);
            InstallAsset ia = session.queryGetAssetRequest(assetId).getInstallAsset(0);
            String cookieName = ia.getDownloadAuthCookieName();
            String cookieValue = ia.getDownloadAuthCookieValue();	
            
            URL url = new URL(ia.getBlobUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Android-Market/2 (sapphire PLAT-RC33); gzip");
            conn.setRequestProperty("Cookie", cookieName + "=" + cookieValue);
            if (conn.getResponseCode() == 302) {
                String location = conn.getHeaderField("Location");
                url = new URL(location);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "Android-Market/2 (sapphire PLAT-RC33); gzip");
                conn.setRequestProperty("Cookie", cookieName + "=" + cookieValue);
            }
            
            InputStream inputstream = (InputStream) conn.getInputStream();
            
            BufferedOutputStream stream = new BufferedOutputStream(
            		new FileOutputStream(new File(config.getPropValue("fileToSavePath")+fileToSave)));
            byte[] buf = new byte[16536/*1024*/];
            int k = 0;
            int readed = 0;
            while ((k = inputstream.read(buf)) > 0) {
                stream.write(buf, 0, k);
                readed += k;
            }
            inputstream.close();
            stream.flush();
            stream.close();
            System.gc();
			
            result = true;
            System.out.println("Download finished!");
		}catch(LoginException le){
			le.printStackTrace();
			result = false;
		}catch(Exception ex){
			ex.printStackTrace();
			result = false;
		}
		return result;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

}
