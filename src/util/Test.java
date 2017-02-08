package util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
	public static void main(String args[]){
		APKDownload ad = new APKDownload();
//		ad.SearchAPKFromGooglePlay("net.slideshare.mobile");
//		ad.APKDownloadFromGooglePlay("net.slideshare.mobile","155");
		
//		File apkFile = new File("C:\\Users\\lemon\\Desktop\\net.slideshare.mobile.apk");
//		AXMLPrinter printer = new AXMLPrinter();
//		printer.getManifestXMLFromAPK(apkFile);
		
//		boolean result = compareMinVersion("1.4.0","1.3.9");
		
		PolicyMatchedUtil pm = new PolicyMatchedUtil();
		pm.analysisPolicyMatched("jp.naver.line.android");
		
	}
	
	public static Boolean compareMinVersion(String conVersion,String appVersion){
		
		int aValue = 0;
		boolean compare = false;
		List<String> cv =  new ArrayList<String>(Arrays.asList(conVersion.split("\\.")));
		List<String> av =  new ArrayList<String>(Arrays.asList(appVersion.split("\\.")));
		
		if(av.size() > cv.size()){
			int diff = av.size()-cv.size();
			for(int j=0 ; j<diff ; j++ ){
				cv.add("0");
			}
		}
		
		for(int i=0 ; i<cv.size() ; i++){
//			System.out.println(cv.get(i)+", "+av.get(aValue));
			if(Integer.parseInt(cv.get(i)) <= Integer.parseInt(av.get(aValue)) ){
				compare = true;
			}else{
				compare = false;
				break;
			}
			if(aValue == av.size()-1){break;}
			aValue++;
		}
		
		return compare;
	}
	
	
	// @Command
	// public void uploadAPKFile(@BindingParam("mStr")Media media){
	// try{
	// File apkFile = new
	// File("C:\\Users\\lemon\\Desktop\\jp.naver.line.android.apk");
	// Files.copy(apkFile, media.getStreamData());
	// AXMLPrinter printer = new AXMLPrinter();
	// List<String> list = printer.getManifestXMLFromAPK(apkFile);
	//
	// Messagebox.show("Upload");
	// }catch(Exception ex){
	// ex.printStackTrace();
	// }
	// }
}
