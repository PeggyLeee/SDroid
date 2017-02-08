package util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.xmlpull.v1.XmlPullParser;

import android.content.res.AXmlResourceParser;
import android.util.TypedValue;

public class AXMLPrinter {

	final String DEFAULT_XML = "AndroidManifest.xml";
	private LogInfo logInfo;
	
	/**
	 * 功能: 初始化設定
	 * */
	public AXMLPrinter(){
		logInfo = new LogInfo();
	}
	
	/**
	 * 功能: 解壓縮.apk檔案和取得 AndroidManifest.xml
	 * apkFile: 上傳的檔案
	 * */
	public List<String> getManifestXMLFromAPK(File apkFile) {
		
		List<String> list = new ArrayList<String>();
		try {
			
			ZipFile file = new ZipFile(apkFile, ZipFile.OPEN_READ);
			ZipEntry entry = file.getEntry(DEFAULT_XML);

			AXmlResourceParser parser = new AXmlResourceParser();
			parser.open(file.getInputStream(entry));

			StringBuilder indent = new StringBuilder(10);
			final String indentStep = "	";

			while (true) {
				int type = parser.next();
				if (type == XmlPullParser.END_DOCUMENT) {
					break;
				}

				switch (type) {
				case XmlPullParser.START_TAG: {
					indent.append(indentStep);
					if (parser.getName().contains("uses-permission")) {
						for (int i = 0; i != parser.getAttributeCount(); ++i) {
							String permission = getAttributeValue(parser, i);
							list.add(permission);
							logInfo.info("%s", permission);
						}
					}
					break;
				}
				case XmlPullParser.END_TAG: {
					indent.setLength(indent.length() - indentStep.length());
					break;
				}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;

	}

	private static String getNamespacePrefix(String prefix) {
		if (prefix == null || prefix.length() == 0) {
			return "";
		}
		return prefix + ":";
	}

	private static String getAttributeValue(AXmlResourceParser parser, int index) {
		int type = parser.getAttributeValueType(index);
		int data = parser.getAttributeValueData(index);
		if (type == TypedValue.TYPE_STRING) {
			return parser.getAttributeValue(index);
		}
		if (type == TypedValue.TYPE_ATTRIBUTE) {
			return String.format("?%s%08X", getPackage(data), data);
		}
		if (type == TypedValue.TYPE_REFERENCE) {
			return String.format("@%s%08X", getPackage(data), data);
		}
		if (type == TypedValue.TYPE_FLOAT) {
			return String.valueOf(Float.intBitsToFloat(data));
		}
		if (type == TypedValue.TYPE_INT_HEX) {
			return String.format("0x%08X", data);
		}
		if (type == TypedValue.TYPE_INT_BOOLEAN) {
			return data != 0 ? "true" : "false";
		}
		if (type >= TypedValue.TYPE_FIRST_COLOR_INT
				&& type <= TypedValue.TYPE_LAST_COLOR_INT) {
			return String.format("#%08X", data);
		}
		if (type >= TypedValue.TYPE_FIRST_INT
				&& type <= TypedValue.TYPE_LAST_INT) {
			return String.valueOf(data);
		}
		return String.format("<0x%X, type 0x%02X>", data, type);
	}

	private static String getPackage(int id) {
		if (id >>> 24 == 1) {
			return "android:";
		}
		return "";
	}

}
