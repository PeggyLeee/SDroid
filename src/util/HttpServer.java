package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.util.Log;

/**
 * 訊息通知，負責接收從App agent的訊息，並回傳分析結果
 * */
@WebServlet(name = "HttpServer", urlPatterns = { "/HttpServer.do" })
public class HttpServer extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private PolicyMatchedUtil pmUtil;
	private LogInfo log;
	
	public HttpServer() {
		super();
		pmUtil = new PolicyMatchedUtil();
		log = new LogInfo();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		double startTime,endTime,totTime;
		startTime = System.currentTimeMillis();
        try
        {
            Thread.sleep(1000);
        }catch(InterruptedException e)
        {
            System.out.println(e);
        }
		
		String pkname = request.getParameter("pkName");
		log.info("pkname: %s", pkname);
		String status = pmUtil.analysisPolicyMatched(pkname);
		
		PrintWriter output = response.getWriter();
		output.print(status);
		output.close();
		
		endTime = System.currentTimeMillis();
		totTime = endTime - startTime;
		
//		writerTxt(pkname,"Using Time: "+ totTime/1000 +"sec"); 
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void writerTxt(String pkname, String str){
		BufferedWriter fw = null;
		log.info("start writerTxt %s", pkname);
		
		try{
			File file = new File("E://"+pkname+".txt");
			fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8")); 
			fw.append(str);
			fw.flush(); 
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try {
				fw.close();
				log.info("end writerTxt %s", pkname);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
