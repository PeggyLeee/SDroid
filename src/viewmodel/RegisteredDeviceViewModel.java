package viewmodel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.ListModelList;

import util.ConfigFile;
import dao.GcmUsersDao;
import model.GcmUsers;

public class RegisteredDeviceViewModel {

	// Dao
	private GcmUsersDao guDao;
	// ListView
	private List<GcmUsers> userList;
	//Util
	private ConfigFile config;
	
	/**
	 * 功能: 初始化設定
	 * */
	@Init
	public void init() {
		
		config = new ConfigFile();
		guDao = new GcmUsersDao();
		userList = new ListModelList<GcmUsers>();
		
		callServlet();
		
		loadData();
	}
	
	/**
	 * 功能: 當頁面初始化後，載入清單資料
	 * */
	public void loadData() {

		List<GcmUsers> perList = guDao.getAllList();
		for(GcmUsers user: perList){
			userList.add(user);
		}
		
	}

	public void callServlet(){
		String serverURL = "http://"+config.getPropValue("ServerHost")+":"
				+config.getPropValue("ServerPort")+"/"+config.getPropValue("sendAllMsgServlet");
		
		
	    URL gwtServlet = null;
	    try {
	        gwtServlet = new URL(serverURL);
	        HttpURLConnection servletConnection = (HttpURLConnection) gwtServlet.openConnection();
	        servletConnection.setRequestMethod("POST");
	        servletConnection.setDoOutput(true);

	        InputStream response = servletConnection.getInputStream();
	        
	        BufferedReader in = new BufferedReader(
                    new InputStreamReader(response));
			String decodedString;
			while ((decodedString = in.readLine()) != null) {
				System.out.println("RegisteredDeviceViewModel:"+decodedString);
			}
			
			in.close();
	        response.close();
	        servletConnection.disconnect();
	        
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}
	
	
	public List<GcmUsers> getUserList() {
		return userList;
	}
	
	
	
}
