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

import model.DataLabel;
import model.Permission;
import model.Policy;
import util.JDBCmysql;
import util.LogInfo;
import util.ResultSetMapper;

public class DataLabelDao {

	private Connection con = null;
	private Statement stat = null;
	private ResultSet rs = null;
	private PreparedStatement pst = null;
	private String sqlStr = "";
	private LogInfo logInfo = null;
	private String tableName = "datalabel";

	/**
	 * 功能: 物件初始化
	 * */
	public DataLabelDao() {
		con = new JDBCmysql().getConnection();
		logInfo = new LogInfo();
	}
	
	/**
	 * 功能: 新增一筆 Label 資料 
	 * label: Label 的物件
	 * */
	public void insert(DataLabel datalabel) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		
		sqlStr = String.format("INSERT INTO " + tableName + " (label,file_name,create_time)" +
				 " VALUES ('%s','%s','%s')",datalabel.getLabel(),datalabel.getFileName(),sdf.format(new Date()));
		try {
			System.out.println("insertSQL:"+sqlStr);
			stat = con.createStatement();
			stat.executeUpdate(sqlStr);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
	
	public boolean removeByLabel(String labelId){
		boolean res = false;   
		
		String sqlStr = "DELETE FROM "+tableName+" WHERE label='"+labelId+"'";
		
	    try 
	    { 
	      stat = con.createStatement(); 
	      int check = stat.executeUpdate(sqlStr); 
	      System.out.println("removeByLabelSQL:"+sqlStr);	
	      
	      if(check !=0)
	      {
	    	  res = true;
	      }
	      
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("removeByLabel DropDB Exception :" + e.toString()); 
	    } 
	    finally 
	    { 
	      Close(); 
	    }		
		
		return res;	
	}
	
	public DataLabel getById(String id){
		List<DataLabel> pojoList = new ArrayList<DataLabel>();
		ResultSetMapper<DataLabel> resultSetMapper = new ResultSetMapper<DataLabel>();
		sqlStr = "select * from "+tableName+" where id="+id;
	    try 
	    { 
	      stat = con.createStatement(); 
	      rs = stat.executeQuery(sqlStr); 
	      logInfo.info("getById: %s", sqlStr);	
	      
	      pojoList= resultSetMapper.mapRersultSetToObject(rs, DataLabel.class);
	      
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
	
	public List<DataLabel> getByFileName(String fileName){
		List<DataLabel> pojoList = new ArrayList<DataLabel>();
		ResultSetMapper<DataLabel> resultSetMapper = new ResultSetMapper<DataLabel>();
		sqlStr = "select * from "+tableName+" where file_name='"+fileName+"'";
	    try 
	    { 
	      stat = con.createStatement(); 
	      rs = stat.executeQuery(sqlStr); 
	      logInfo.info("getByLabelAndFileName: %s", sqlStr);	
	      
	      pojoList= resultSetMapper.mapRersultSetToObject(rs, DataLabel.class);
	      
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("getById DropDB Exception :" + e.toString()); 
	    } 
	    finally 
	    { 
			if (pojoList == null) {
				pojoList = new ArrayList<DataLabel>();
			}
	      Close(); 
	    }
	    return pojoList;
	}
	
	
	/**
	 * 功能: 查詢全部的DataLabel
	 * */
	public List<DataLabel> getAllList() {
		List<DataLabel> pojoList = new ArrayList<DataLabel>();
		ResultSetMapper<DataLabel> resultSetMapper = new ResultSetMapper<DataLabel>();
		sqlStr = "select * from " + tableName+" order by create_time desc";

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sqlStr);
			logInfo.info("getAllList: %s", sqlStr);

			pojoList = resultSetMapper.mapRersultSetToObject(rs,DataLabel.class);

		} catch (SQLException e) {
			System.out.println("getAllList DropDB Exception :" + e.toString());
		} finally {
			if(pojoList == null){
				pojoList = new ArrayList<DataLabel>();
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
