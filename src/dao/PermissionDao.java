package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Permission;
import util.JDBCmysql;
import util.LogInfo;
import util.ResultSetMapper;

public class PermissionDao {

	private Connection con = null;
	private Statement stat = null;
	private ResultSet rs = null;
	private PreparedStatement pst = null;
	private String sqlStr = "";
	private LogInfo logInfo = null;

	private String tableName = "permission";

	public PermissionDao() {
		con = new JDBCmysql().getConnection();
		logInfo = new LogInfo();
	}

	/**
	 * 功能: 新增一筆 Permission 資料 permission: Permission 的物件
	 * */
	public Integer insert(String permission) {
		sqlStr = "INSERT INTO " + tableName + " (permission)" +
				 " VALUES ('" + permission + "')";
		int key = 0;
		int gId = 0;
		try {
			System.out.println("insertSQL:"+sqlStr);
			stat = con.createStatement();
			key = stat.executeUpdate(sqlStr, Statement.RETURN_GENERATED_KEYS);
			if (key == 0) {
				System.out.println("App not found!");
			}
			ResultSet keys = stat.getGeneratedKeys();
			keys.next();
			gId = keys.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return gId;

	}

	/**
	 * 功能: 依 String 查詢 Permission pStr: permission
	 * */
	public Permission getByPermission(String pStr) {
		List<Permission> pojoList = new ArrayList<Permission>();
		ResultSetMapper<Permission> resultSetMapper = new ResultSetMapper<Permission>();
		sqlStr = "select * from " + tableName + " as p where p.permission = '"
				+ pStr + "' ";

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sqlStr);
			logInfo.info("getByPermission: %s", sqlStr);

			pojoList = resultSetMapper.mapRersultSetToObject(rs,
					Permission.class);

		} catch (SQLException e) {
			System.out.println("getListById DropDB Exception :" + e.toString());
		} finally {
			Close();
		}
		if (pojoList == null) {
			return null;
		}

		return pojoList.get(0);
	}

	/**
	 * 功能: 依 id 查詢 Permission pid: Permission id
	 * */
	public Permission getListById(int pid) {

		List<Permission> pojoList = new ArrayList<Permission>();
		ResultSetMapper<Permission> resultSetMapper = new ResultSetMapper<Permission>();
		sqlStr = "select * from " + tableName + " as p where p.id = '" + pid
				+ "' ";

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sqlStr);
			logInfo.info("getListById: %s", sqlStr);

			pojoList = resultSetMapper.mapRersultSetToObject(rs,
					Permission.class);

		} catch (SQLException e) {
			System.out.println("getListById DropDB Exception :" + e.toString());
		} finally {
			Close();
		}
		return pojoList.get(0);
	}

	/**
	 * 功能: 查詢全部的Permission
	 * */
	public List<Permission> getAllList() {
		List<Permission> pojoList = new ArrayList<Permission>();
		ResultSetMapper<Permission> resultSetMapper = new ResultSetMapper<Permission>();
		sqlStr = "select * from " + tableName;

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sqlStr);
			logInfo.info("getAllList: %s", sqlStr);

			pojoList = resultSetMapper.mapRersultSetToObject(rs,
					Permission.class);

		} catch (SQLException e) {
			System.out.println("getAllList DropDB Exception :" + e.toString());
		} finally {
			if(pojoList == null){
				pojoList = new ArrayList<Permission>();
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
