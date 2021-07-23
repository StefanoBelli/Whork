package test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import logic.Database;
import logic.dao.AccountDao;
import logic.exception.DataAccessException;
import logic.model.CandidatureModel;

public class TemporaryTest {
	
	private static boolean attemptToEstablishDbConnection() {
		try {
			Connection conn = Database.getInstance("localhost:3306", "whork", "whork123").getConnection();
			conn.setCatalog("whorkdb");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();			return false;
		} catch (SQLException e) {
			e.printStackTrace();			return false;
		}

		return true;
	}

	public static void main(String[] args) {
		attemptToEstablishDbConnection();
		String cf = "MGLLEI00D08D612D";
		ArrayList<CandidatureModel> listCandidatureModel = new ArrayList<CandidatureModel>();
		try {
			listCandidatureModel = AccountDao.getSeekerCandidature(cf);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		
		if(listCandidatureModel == null) {
			System.out.println("listCandidatureModel is null"); 
			return;
		}
		
		System.out.println(listCandidatureModel); 
		
		for(int i=0; i<listCandidatureModel.size(); i++) {
			System.out.println("social reason: " + listCandidatureModel.get(i).getSocialReason());
			System.out.println("candidature date: " + listCandidatureModel.get(i).getCandidatureDate());
			System.out.println("type of contract: " + listCandidatureModel.get(i).getTypeOfContract());
			System.out.println("job occupation " + listCandidatureModel.get(i).getJobOccupation());
			System.out.println("email " + listCandidatureModel.get(i).getEmail());
		}

	}

}
