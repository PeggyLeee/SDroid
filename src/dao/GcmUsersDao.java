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

import model.GcmUsers;
import util.JDBCmysql;
import util.LogInfo;
import util.ResultSetMapper;

public class GcmUsersDao {

	private Connection con = null;
	private Statement stat = null;
	private ResultSet rs = null;
	private PreparedStatement pst = null;
	private String sqlStr = "";
	private LogInfo logInfo = null;
	private String tableName = "gcmusers";

	/**
	 * 功能: 物件初始化
	 * */
	public GcmUsersDao() {
		con = new JDBCmysql().getConnection();
		logInfo = new LogInfo();
	}
	
	/**
	 * 功能: 新增一筆 Label 資料 
	 * label: Label 的物件
	 * */
	public void insert(GcmUsers gcmUsers) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		
		sqlStr = String.format("INSERT INTO " + tableName + " (gcm_regid,name,email,status,create_time)" +
				 " VALUES ('%s','%s','%s','%s','%s')",gcmUsers.getGcmId(),gcmUsers.getName(),gcmUsers.getEmail(),0,sdf.format(new Date()));
		try {
			System.out.println("insertSQL:"+sqlStr);
			stat = con.createStatement();
			stat.executeUpdate(sqlStr);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean updateById(String oldId, String newId){
		boolean res = false;  
		String sqlStr = "UPDATE "+tableName+" SET gcm_regid='"+ newId+"' WHERE gcm_regid='"+oldId+"'";
		
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
	
	public boolean updateStatusById(String id, int status){
		boolean res = false;  
		String sqlStr = "UPDATE "+tableName+" SET status="+ status+" WHERE gcm_regid='"+id+"'";
		
	    try 
	    { 
	      stat = con.createStatement(); 
	      int check = stat.executeUpdate(sqlStr); 
	      System.out.println("updateStatusById:"+sqlStr);	
	      
	      if(check !=0)
	      {
	    	  res = true;
	      }
	      
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("updateStatusById DropDB Exception :" + e.toString()); 
	    } 
	    finally 
	    { 
	      Close(); 
	    }		
		
		return res;	
	}
	
	public boolean updateByVo(GcmUsers user){
		boolean res = false;  
		String sqlStr = "UPDATE "+tableName+" SET status="+ user.getStatus()+", gcm_regid='"+user.getGcmId()+"'"+" WHERE email='"+user.getEmail()+"'";
		
	    try 
	    { 
	      stat = con.createStatement(); 
	      int check = stat.executeUpdate(sqlStr); 
	      System.out.println("updateByVo:"+sqlStr);	
	      
	      if(check !=0)
	      {
	    	  res = true;
	      }
	      
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("updateByVo DropDB Exception :" + e.toString()); 
	    } 
	    finally 
	    { 
	      Close(); 
	    }		
		
		return res;	
	}
	
	public boolean removeById(String id){
		boolean res = false;   
		
		String sqlStr = "DELETE FROM "+tableName+" WHERE gcm_regid="+id;
		
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
	
	public GcmUsers getById(String id){
		List<GcmUsers> pojoList = new ArrayList<GcmUsers>();
		ResultSetMapper<GcmUsers> resultSetMapper = new ResultSetMapper<GcmUsers>();
		sqlStr = "select * from "+tableName+" where id="+id;
	    try 
	    { 
	      stat = con.createStatement(); 
	      rs = stat.executeQuery(sqlStr); 
	      logInfo.info("getById: %s", sqlStr);	
	      
	      pojoList= resultSetMapper.mapRersultSetToObject(rs, GcmUsers.class);
	      
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
	
	public GcmUsers getByEmail(String email){
		List<GcmUsers> pojoList = new ArrayList<GcmUsers>();
		ResultSetMapper<GcmUsers> resultSetMapper = new ResultSetMapper<GcmUsers>();
		sqlStr = "select * from "+tableName+" where email='"+email+"'";
	    try 
	    { 
	      stat = con.createStatement(); 
	      rs = stat.executeQuery(sqlStr); 
	      logInfo.info("getByEmail: %s", sqlStr);	
	      
	      pojoList= resultSetMapper.mapRersultSetToObject(rs, GcmUsers.class);
	      
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("getByEmail DropDB Exception :" + e.toString()); 
	    } 
	    finally 
	    { 
	      Close(); 
	    }
	    if(pojoList == null ){
	    	return null;
	    }
	    return pojoList.get(0);
	}
	
	/**
	 * 功能: 查詢全部的DataLabel
	 * */
	public List<GcmUsers> getAllList() {
		List<GcmUsers> pojoList = new ArrayList<GcmUsers>();
		ResultSetMapper<GcmUsers> resultSetMapper = new ResultSetMapper<GcmUsers>();
		sqlStr = "select * from " + tableName+" order by create_time desc";

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sqlStr);
			logInfo.info("getAllList: %s", sqlStr);

			pojoList = resultSetMapper.mapRersultSetToObject(rs,
					GcmUsers.class);

		} catch (SQLException e) {
			System.out.println("getAllList DropDB Exception :" + e.toString());
		} finally {
			if(pojoList == null){
				pojoList = new ArrayList<GcmUsers>();
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
