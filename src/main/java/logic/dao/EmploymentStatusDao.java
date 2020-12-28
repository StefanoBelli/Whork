package logic.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import logic.model.EmploymentStatusModel;

public class EmploymentStatusDao {
	
	private static String USER = "whork";
	private static String PASS = "whork123";
	private static String DB_URL = "jdbc:mysql://localhost:3306/whorkdb?autoReconnect=true&useSSL=false";
	private static String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
	//private static istance = null;
	
	/*public static StatoOccupazionaleDao(){
		istance = getIstance();
	}*/
	
	public ArrayList<EmploymentStatusModel> getElements() throws Exception{
		Statement statmt = null;
		Connection connect = null;
		String query = "SELECT Position FROM EmploymentStatus";
		ArrayList<EmploymentStatusModel> listOfPosition = new ArrayList<EmploymentStatusModel>();
		ResultSet rs = null;
		
		try {
			Class.forName(DRIVER_CLASS_NAME);
			connect = DriverManager.getConnection(DB_URL, USER, PASS);
			statmt = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = statmt.executeQuery(query);
			
			if(!rs.first()) {
				Exception e = new Exception("Not Found");
				throw e;
			}
			rs.first();
			do {
				String position = rs.getString("Position");
				EmploymentStatusModel ent = new EmploymentStatusModel();
				ent.setPosition(position);
				listOfPosition.add(ent);
			}while(rs.next());
			
			rs.close();
		}finally {
			closeConnection(statmt, connect, rs);
			
		}
		
		return listOfPosition;
	}
	


	public static void closeConnection(Statement statmt, Connection connect, ResultSet rs) {
		try {
			if(statmt != null) {
				statmt.close();
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			if(connect != null) {
				connect.close();
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}




