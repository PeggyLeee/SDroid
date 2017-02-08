package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Label;
import util.JDBCmysql;
import util.LogInfo;
import util.ResultSetMapper;

public class LabelDao {

	private Connection con = null;
	private Statement stat = null;
	private ResultSet rs = null;
	private PreparedStatement pst = null;
	private String sqlStr = "";
	private LogInfo logInfo = null;
	private String tableName = "label";

	/**
	 * 功能: 物件初始化
	 * */
	public LabelDao() {
		con = new JDBCmysql().getConnection();
		logInfo = new LogInfo();
	}
	
	/**
	 * 功能: 新增一筆 Label 資料 
	 * label: Label 的物件
	 * */
	public void insert(String label) {
		String labelId = "C" + String.format("%03d", getLastDataId());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		
		sqlStr = String.format("INSERT INTO " + tableName + " (label_Id,label,create_time)" +
				 " VALUES ('%s','%s','%s')",labelId,label,sdf.format(new Date()));
		try {
			System.out.println("insertSQL:"+sqlStr);
			stat = con.createStatement();
			stat.executeUpdate(sqlStr);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 功能: 依label Id刪除該筆資料
	 * */
	public boolean removeById(String id){
		boolean res = false;   
		
		String sqlStr = "DELETE FROM "+tableName+" WHERE id="+id;
		
	    try 
	    { 
	      stat = con.createStatement(); 
	      int check = stat.executeUpdate(sqlStr); 
	      System.out.println("removeSQL:"+sqlStr);	
	      
	      if(check !=0)
	      {
	    	  res = true;
	      }
	      
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("removeById DropDB Exception :" + e.toString()); 
	    } 
	    finally 
	    { 
	      Close(); 
	    }		
		
		return res;	
	}
	
	/**
	 * 功能: 依Id查詢Label物件
	 * */
	public Label getById(String id){
		List<Label> pojoList = new ArrayList<Label>();
		ResultSetMapper<Label> resultSetMapper = new ResultSetMapper<Label>();
		sqlStr = "select * from "+tableName+" where id="+id;
	    try 
	    { 
	      stat = con.createStatement(); 
	      rs = stat.executeQuery(sqlStr); 
	      logInfo.info("getById: %s", sqlStr);	
	      
	      pojoList= resultSetMapper.mapRersultSetToObject(rs, Label.class);
	      
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("getById DropDB Exception :" + e.toString()); 
	    } 
	    finally 
	    { 
	      Close(); 
	    }
	    return pojoList.get(0);
	}
	
	/**
	 * 功能: 依label名稱查詢Label物件
	 * */
	public Label getByLabelName(String name){
		List<Label> pojoList = new ArrayList<Label>();
		ResultSetMapper<Label> resultSetMapper = new ResultSetMapper<Label>();
		sqlStr = "select * from "+tableName+" where label='"+name+"'";
	    try 
	    { 
	      stat = con.createStatement(); 
	      rs = stat.executeQuery(sqlStr); 
	      logInfo.info("getByLabelName: %s", sqlStr);	
	      
	      pojoList= resultSetMapper.mapRersultSetToObject(rs, Label.class);
	      
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("getByLabelName DropDB Exception :" + e.toString()); 
	    } 
	    finally 
	    { 
	      Close(); 
	    }
	    return pojoList.get(0);
	}
	
	/**
	 * 功能: 依label Id查詢Label物件
	 * */
	public Label getByLabelId(String labelId){
		List<Label> pojoList = new ArrayList<Label>();
		ResultSetMapper<Label> resultSetMapper = new ResultSetMapper<Label>();
		sqlStr = "select * from "+tableName+" where label_Id='"+labelId+"'";
	    try 
	    { 
	      stat = con.createStatement(); 
	      rs = stat.executeQuery(sqlStr); 
	      logInfo.info("getByLabelId: %s", sqlStr);	
	      
	      pojoList= resultSetMapper.mapRersultSetToObject(rs, Label.class);
	      
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("getByLabelId DropDB Exception :" + e.toString()); 
	    } 
	    finally 
	    { 
	      Close(); 
	    }
	    return pojoList.get(0);
	}
	
	/**
	 * 功能: 取得最後一個Label Id
	 * */
	public Integer getLastDataId() {
		List<Label> pojoList = new ArrayList<Label>();
		ResultSetMapper<Label> resultSetMapper = new ResultSetMapper<Label>();
		sqlStr = "select * from " + tableName + " order by id desc limit 1";

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sqlStr);
			logInfo.info("getLastDataId: %s", sqlStr);

			pojoList = resultSetMapper.mapRersultSetToObject(rs, Label.class);

		} catch (SQLException e) {
			System.out.println("getLastDataId DropDB Exception :" + e.toString());
		} finally {
			Close();
		}
		if(pojoList == null || pojoList.size() == 0){
			return 1;
		}
		Label last = pojoList.get(0);
		
		return last.getId()+1;
	}
	
	/**
	 * 功能: 查詢全部的DataLabel
	 * */
	public List<Label> getAllList() {
		List<Label> pojoList = new ArrayList<Label>();
		ResultSetMapper<Label> resultSetMapper = new ResultSetMapper<Label>();
		sqlStr = "select * from " + tableName+" order by create_time desc";

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sqlStr);
			logInfo.info("getAllList: %s", sqlStr);

			pojoList = resultSetMapper.mapRersultSetToObject(rs,
					Label.class);

		} catch (SQLException e) {
			System.out.println("getAllList DropDB Exception :" + e.toString());
		} finally {
			if(pojoList == null){
				pojoList = new ArrayList<Label>();
			}
			Close();
		}
		return pojoList;
	}
	
	private void Close() {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (stat != null) {
				stat.close();
				stat = null;
			}
			if (pst != null) {
				pst.close();
				pst = null;
			}
		} catch (SQLException e) {
			logInfo.error("Close Exception : %s", e.toString());
		}
	}
}
