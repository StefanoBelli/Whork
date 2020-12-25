package logic.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import logic.model.StatoOccupazionaleModel;

public class StatoOccupazionaleDao {
	
	private static String USER = "whork";
	private static String PASS = "whork123";
	private static String DB_URL = "jdbc:mysql://localhost:3306/createWhorkDbSchema.sql";
	private static String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
	
	public static List<StatoOccupazionaleModel> getElements() throws Exception{
		Statement statmt = null;
		Connection connect = null;
		String query = "SELECT Posizione FROM StatoOccupazionale";
		List<StatoOccupazionaleModel> listOfPosition = new ArrayList<StatoOccupazionaleModel>();
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
				String position = rs.getString("Posizione");
				StatoOccupazionaleModel ent = new StatoOccupazionaleModel();
				ent.setPosizione(position);
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




