package util;

import model.AppPolicy;
import model.Condition;
import model.InstallTimePolicy;
import model.RunTimePolicy;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class ParseXML {

	private XStream xstream = null;
	
	
	public ParseXML(){
		xstream = new XStream(new StaxDriver());
	}
	
	/**
	 * obj: 轉換成XML的物件
	 * */
	public String InstallObjParseToXML(Object obj ){
		
		xstream.processAnnotations(InstallTimePolicy.class);
		xstream.processAnnotations(Condition.class);
		
		return xstream.toXML(obj);
	}
	
	/**
	 * obj: 轉換成XML的物件
	 * type: RunTimePolicy or AppPolicy
	 * */
	public String RunTimeOrAppObjParseToXML(Object obj ,String type){
		
		switch(type){
		case "RunTimePolicy":
			xstream.processAnnotations(RunTimePolicy.class);
			
			break;
		case "AppPolicy":
			xstream.processAnnotations(AppPolicy.class);
			
			break;
		}
		
		return xstream.toXML(obj);
	}
	
	/**
	 * xml: xml字串
	 * */
	public Object XMLParseToInstallObj(String xml){

		xstream.processAnnotations(InstallTimePolicy.class);
		xstream.processAnnotations(Condition.class);
		
		return xstream.fromXML(xml);
	}
	
	/**
	 * xml: xml字串
	 * type: RunTimePolicy or AppPolicy
	 * */
	public Object XMLParseToRunTimeOrAppObj(String xml ,String type){

		switch(type){
		case "RunTimePolicy":
			xstream.processAnnotations(RunTimePolicy.class);
			
			break;
		case "AppPolicy":
			xstream.processAnnotations(AppPolicy.class);
			
			break;
		}
		
		return xstream.fromXML(xml);
	}
	
}
