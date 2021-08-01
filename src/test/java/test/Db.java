package test;

import java.sql.SQLException;

import logic.Database;
import logic.dao.ComuniDao;
import logic.dao.EmploymentStatusDao;
import logic.dao.JobCategoryDao;
import logic.dao.JobPositionDao;
import logic.dao.QualificationDao;
import logic.dao.TypeOfContractDao;
import logic.exception.DataAccessException;

public class Db {
	private Db(){}

	public static void init() throws ClassNotFoundException, SQLException, DataAccessException {
		Database.getInstance(DbmsConfig.DB_HOST + ":" + Integer.toString(DbmsConfig.DB_PORT),
				DbmsConfig.DB_USER, DbmsConfig.DB_PWD);
		
		Database.getInstance().getConnection().setCatalog(DbmsConfig.DB_NAME);

		ComuniDao.populatePool();
		EmploymentStatusDao.populatePool();
		JobCategoryDao.populatePool();
		JobPositionDao.populatePool();
		QualificationDao.populatePool();
		TypeOfContractDao.populatePool();
	}
}
