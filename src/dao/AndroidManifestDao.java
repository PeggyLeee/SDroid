package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.AndroidManifest;
import util.JDBCmysql;
import util.LogInfo;
import util.ResultSetMapper;

public class AndroidManifestDao {

	private Connection con = null;
	private Statement stat = null;
	private ResultSet rs = null;
	private PreparedStatement pst = null;
	private String sqlStr = "";
	private LogInfo logInfo = null;

	private String tableName = "androidManifest";

	public AndroidManifestDao() {
		con = new JDBCmysql().getConnection();
		logInfo = new LogInfo();
	}

	/**
	 * 功能: 新增一筆 AndroidManifest 資料 
	 * aId: Application Id
	 * pIds: Permission Id 清單
	 * */
	public void insert(int aId , List<Integer> pIds) {
		sqlStr = "INSERT INTO " + tableName 
				+ " (app_id,permission_id)"
				+ " VALUES (?,?)";
		try {
			pst = con.prepareStatement(sqlStr);
			for(Integer pId: pIds){
				pst.setInt(1, aId);
				pst.setInt(2, pId);
				pst.addBatch();
			}
			pst.executeBatch();
			
			System.out.println("insertSQL:" + sqlStr);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			Close();
		}

	}

	public AndroidManifest findByIds(int aId,int pId){
		List<AndroidManifest> pojoList = new ArrayList<AndroidManifest>();
		ResultSetMapper<AndroidManifest> resultSetMapper = new ResultSetMapper<AndroidManifest>();
		sqlStr = "SELECT * FROM "+tableName+" WHERE app_id="+aId+" AND permission_id="+pId;
		
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sqlStr);
			logInfo.info("findByIds: %s", sqlStr);

			pojoList = resultSetMapper.mapRersultSetToObject(rs,AndroidManifest.class);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			Close();
		}
		if (pojoList == null) {
			return null;
		}
		return pojoList.get(0);
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
