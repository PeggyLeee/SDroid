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

import model.Permission;
import model.PolicyMatched;
import util.JDBCmysql;
import util.LogInfo;
import util.ResultSetMapper;

public class PolicyMatchedDao {

	private Connection con = null; 
	private Statement stat = null; 
	private ResultSet rs = null; 
	private PreparedStatement pst = null; 
	private LogInfo logInfo = null;
	
	private String tableName = "policymatched";

	public PolicyMatchedDao(){
		con = new JDBCmysql().getConnection();
		logInfo = new LogInfo();
	}
	
	public boolean insert(PolicyMatched pm){
		boolean res = false;
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");       
		
		String sqlStr = String.format("INSERT INTO "+tableName+
				" (policy_id,app_id,result,create_time) VALUES ('%s','%s','%s','%s')",
				pm.getPolicyId(),pm.getAppId(),pm.getResult(),bartDateFormat.format(new Date()));
	    try 
	    { 
	      stat = con.createStatement(); 
	      int check = stat.executeUpdate(sqlStr); 
	      System.out.println("insertSQL:"+sqlStr);	
	      
	      if(check !=0)
	      {
	    	  res = true;
	      }
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("insert DropDB Exception :" + e.toString()); 
	    } 
	    finally 
	    { 
	      Close(); 
	    }		
		return res;			
	}
	
	public boolean removeByAid(int aId){
		boolean res = false;   
		
		String sqlStr = "DELETE FROM "+tableName+" WHERE app_id="+aId;
		
	    try 
	    { 
	      stat = con.createStatement(); 
	      int check = stat.executeUpdate(sqlStr); 
	      System.out.println("removeByAid SQL:"+sqlStr);	
	      
	      if(check !=0)
	      {
	    	  res = true;
	      }
	      
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("removeByAid DropDB Exception :" + e.toString()); 
	    } 
	    finally 
	    { 
	      Close(); 
	    }		
		
		return res;	
	}
	
	public boolean removeByPid(String pId){
		boolean res = false;   
		
		String sqlStr = "DELETE FROM "+tableName+" WHERE policy_id="+pId;
		
	    try 
	    { 
	      stat = con.createStatement(); 
	      int check = stat.executeUpdate(sqlStr); 
	      System.out.println("removeByPid SQL:"+sqlStr);	
	      
	      if(check !=0)
	      {
	    	  res = true;
	      }
	      
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("removeByPid DropDB Exception :" + e.toString()); 
	    } 
	    finally 
	    { 
	      Close(); 
	    }		
		
		return res;	
	}
	
	public List<PolicyMatched> getListByPid(String pId){
		List<PolicyMatched> pojoList = new ArrayList<PolicyMatched>();
		ResultSetMapper<PolicyMatched> resultSetMapper = new ResultSetMapper<PolicyMatched>();
		String sqlStr = "select * from "+tableName+" ";
		sqlStr +="where policy_id="+pId;
	    try 
	    { 
	      stat = con.createStatement(); 
	      rs = stat.executeQuery(sqlStr); 
	      logInfo.info("getListByPid: %s", sqlStr);	
	      
	      pojoList= resultSetMapper.mapRersultSetToObject(rs, PolicyMatched.class);
	      
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("getAllList DropDB Exception :" + e.toString()); 
	    } 
	    finally 
	    { 
	      Close(); 
	    }	
	    return pojoList;
	}
	
	public List<PolicyMatched> getListByAid(int aId){
		List<PolicyMatched> pojoList = new ArrayList<PolicyMatched>();
		ResultSetMapper<PolicyMatched> resultSetMapper = new ResultSetMapper<PolicyMatched>();
		String sqlStr = "select * from "+tableName+" ";
		sqlStr +="where app_id="+aId;
	    try 
	    { 
	      stat = con.createStatement(); 
	      rs = stat.executeQuery(sqlStr); 
	      logInfo.info("getListByAid: %s", sqlStr);	
	      
	      pojoList= resultSetMapper.mapRersultSetToObject(rs, PolicyMatched.class);
	      
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("getAllList DropDB Exception :" + e.toString()); 
	    } 
	    finally 
	    { 
	      Close(); 
	    }	
	    return pojoList;
	}
	
	public List<PolicyMatched> getListByGroup(){
		List<PolicyMatched> pojoList = new ArrayList<PolicyMatched>();
		ResultSetMapper<PolicyMatched> resultSetMapper = new ResultSetMapper<PolicyMatched>();
		String sqlStr = "select * from "+tableName +" group by app_id order by create_time desc";
	    try 
	    { 
	      stat = con.createStatement(); 
	      rs = stat.executeQuery(sqlStr); 
	      logInfo.info("getListByGroup: %s", sqlStr);	
	      
	      pojoList= resultSetMapper.mapRersultSetToObject(rs, PolicyMatched.class);
	      
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("getAllList DropDB Exception :" + e.toString()); 
	    } 
	    finally 
	    { 
		  if(pojoList == null){
				pojoList = new ArrayList<PolicyMatched>();
		  }
	      Close(); 
	    }	
	    return pojoList;
	}
	
	private void Close() 
	{ 
	  try 
	  { 
	    if(rs!=null) 
	    { 
	      rs.close(); 
	      rs = null; 
	    } 
	    if(stat!=null) 
	    { 
	      stat.close(); 
	      stat = null; 
	    } 
	    if(pst!=null) 
	    { 
	      pst.close(); 
	      pst = null; 
	    } 
	  } 
	 catch(SQLException e) 
	  { 
		 logInfo.error("Close Exception : %s", e.toString());
	  } 
	}
}
