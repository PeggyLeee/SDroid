package util;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.zkoss.json.JSONValue;

import com.google.gson.Gson;

import model.DataLabel;
import model.Label;
import model.Policy;
import dao.DataLabelDao;
import dao.LabelDao;
import dao.PolicyDao;

@WebServlet(name = "RunTimeHttpServer", urlPatterns = { "/RunTimeHttpServer.do" })
public class RunTimeHttpServer extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LogInfo log;
	private PolicyDao pDao;
	private DataLabelDao dlDao;
	private LabelDao lDao;

	public RunTimeHttpServer() {
		super();
		log = new LogInfo();
		pDao = new PolicyDao();
		dlDao = new DataLabelDao();
		lDao = new LabelDao();
		
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		String jsonStr = "";
		String funcName = request.getParameter("funcName");

		if (!StringUtils.isBlank(funcName)) {
			switch (funcName) {
			case "AppPolicy":
				List<Policy> apList = pDao.getAllList("AppPolicy");
				jsonStr = new Gson().toJson(apList);
				break;
			case "RunTimePolicy":
				List<Policy> rtpList = pDao.getAllList("RunTimePolicy");
				jsonStr = new Gson().toJson(rtpList);
				break;
			case "DataLabel":
				String fileName = request.getParameter("fileName");
				byte[] bytes = fileName.getBytes(StandardCharsets.ISO_8859_1);
				fileName = new String(bytes, StandardCharsets.UTF_8);
				List<DataLabel> dl = dlDao.getByFileName(fileName);
				jsonStr = new Gson().toJson(dl);
				break;
			}
		} else {
			jsonStr = "No funcName data, please check it again!";
		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter output = response.getWriter();
		output.print(jsonStr);
		output.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}

}
