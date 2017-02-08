package viewmodel;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;

import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;

public class IndexViewModel{

	//UI component
	@Wire("#conHeader")
	Label title;
	@Wire("#crumbHeader")
	Label subTitle;
	@Wire("#pageContent")
	Include pageContent;
	
	/**
	 * 功能: 導入至指定的頁面
	 * header: 該指定頁面的Header
	 * */
	@Command
	public void goToIndexPage(@BindingParam("hStr") String header){
		title.setValue(header);
		subTitle.setValue(header);
		pageContent.setSrc("/template/IndexPage.zul");
	}
	
	@Command
	public void goToSettingInstallTimePolicy(@BindingParam("hStr") String header){
		title.setValue(header);
		subTitle.setValue(header);
		pageContent.setSrc("/template/SettingPolicy.zul");
		
		//showNotify("test", pageContent);
	}
	
	@Command
	public void goToSettingRunTimePolicy(@BindingParam("hStr") String header){
		title.setValue(header);
		subTitle.setValue(header);
		pageContent.setSrc("/template/SettingRunTimePolicy.zul");
	}
	
	@Command
	public void goToPolicyMatched(@BindingParam("hStr") String header){
		title.setValue(header);
		subTitle.setValue(header);
		pageContent.setSrc("/template/PolicyMatched.zul");
	}
	
	@Command
	public void goToSettingLabel(@BindingParam("hStr") String header){
		title.setValue(header);
		subTitle.setValue(header);
		pageContent.setSrc("/template/SettingDataLabel.zul");
	}

	@Command
	public void goToRegisteredDevice(@BindingParam("hStr") String header){
		title.setValue(header);
		subTitle.setValue(header);
		pageContent.setSrc("/template/RegisteredDevice.zul");
	}
	
	
		
	private void showNotify(String msg, Component ref) {
	   Clients.showNotification(msg, "info", ref, "end_center", 2000);
	}
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view){
	    Selectors.wireComponents(view, this, false);
	}
}
