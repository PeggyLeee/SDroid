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

import model.Application;
import util.JDBCmysql;
import util.LogInfo;
import util.ResultSetMapper;

public class ApplicationDao {

	private Connection con = null;
	private Statement stat = null;
	private ResultSet rs = null;
	private PreparedStatement pst = null;
	private String sqlStr = "";
	private LogInfo logInfo = null;

	private String tableName = "application";

	/**
	 * 功能: 物件初始化
	 * */
	public ApplicationDao() {
		con = new JDBCmysql().getConnection();
		logInfo = new LogInfo();
	}

	/**
	 * 功能: 新增一筆 Application 資料 app: Application 的物件
	 * */
	public int insert(Application app) {

		int key = 0;
		int gId = 0;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String sqlStr = String
				.format("INSERT INTO "
						+ tableName
						+ " (asset_id,app_label,pk_name,version,install_size,creator,contact_email,contact_website,create_time)"
						+ " VALUES ('%s','%s','%s','%s','%d','%s','%s','%s','%s')",
						app.getAssetId(), app.getAppLabel(), app.getPkName(),
						app.getVersion(), app.getInstallSize(),
						app.getCreator(), app.getContactEmail(),
						app.getContactWebsite(), sdf.format(new Date()));

		try {
			System.out.println("insertSQL:" + sqlStr);
			stat = con.createStatement();
			key = stat.executeUpdate(sqlStr, Statement.RETURN_GENERATED_KEYS);

			if (key == 0) {
				System.out.println("App not found!");
			}
			ResultSet keys = stat.getGeneratedKeys();
			keys.next();
			gId = keys.getInt(1);
			
		} catch (SQLException e) {
			System.out.println("insertApplication DropDB Exception :"
					+ e.toString());
		} finally {
			Close();
		}

		return gId;
	}

	/**
	 * 功能: 依 pkName 查詢 Application pkName: Android package file name
	 * */
	public Application findByPkName(String pkName) {
		List<Application> pojoList = new ArrayList<Application>();
		ResultSetMapper<Application> resultSetMapper = new ResultSetMapper<Application>();
		sqlStr = "select * from " + tableName + " as a where a.pk_name = '"
				+ pkName + "' ";

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sqlStr);
			logInfo.info("findByPkName: %s", sqlStr);

			pojoList = resultSetMapper.mapRersultSetToObject(rs,
					Application.class);

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
	 * 功能: 依 id 查詢 Application id: Application id
	 * */
	public Application getListById(int id) {

		List<Application> pojoList = new ArrayList<Application>();
		ResultSetMapper<Application> resultSetMapper = new ResultSetMapper<Application>();
		sqlStr = "select * from " + tableName + " as a where a.id = '" + id
				+ "' ";

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sqlStr);
//			logInfo.info("getListById: %s", sqlStr);

			pojoList = resultSetMapper.mapRersultSetToObject(rs,
					Application.class);

		} catch (SQLException e) {
			System.out.println("getListById DropDB Exception :" + e.toString());
		} finally {
			Close();
		}
		return pojoList.get(0);
	}

	/**
	 * 功能: 查詢全部的Application
	 * */
	public List<Application> getAllList() {

		List<Application> pojoList = new ArrayList<Application>();
		ResultSetMapper<Application> resultSetMapper = new ResultSetMapper<Application>();
		sqlStr = "select * from " + tableName;

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sqlStr);
			logInfo.info("getAllList: %s", sqlStr);

			pojoList = resultSetMapper.mapRersultSetToObject(rs,
					Application.class);

		} catch (SQLException e) {
			System.out.println("getAllList DropDB Exception :" + e.toString());
		} finally {
			if(pojoList == null){
				pojoList = new ArrayList<Application>();
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
