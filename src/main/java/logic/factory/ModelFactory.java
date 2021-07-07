package logic.factory;

import logic.model.CompanyModel;
import logic.model.ComuneModel;
import logic.model.EmployeeUserModel;
import logic.model.EmploymentStatusModel;
import logic.model.JobSeekerUserModel;
import logic.model.ProvinciaModel;
import logic.model.RegioneModel;
import logic.model.UserAuthModel;
import logic.model.UserModel;
import logic.util.Util;

import java.io.ByteArrayInputStream;

import logic.bean.CompanyBean;
import logic.bean.ComuneBean;
import logic.bean.EmploymentStatusBean;
import logic.bean.ProvinciaBean;
import logic.bean.RegioneBean;
import logic.bean.UserAuthBean;
import logic.bean.UserBean;

public final class ModelFactory {
	private ModelFactory() {}

	public static UserAuthModel buildUserAuthModel(UserAuthBean userAuthBean) {
		UserAuthModel userAuthModel = new UserAuthModel();
		userAuthModel.setEmail(userAuthBean.getEmail());
		userAuthModel.setBcryptedPassword(
			new ByteArrayInputStream(
				Util.Bcrypt.hash(userAuthBean.getPassword())));

		return userAuthModel;
	}

	/**
	 * @description nullable values depends on usage
	 * @param userBean must never be null
	 * @param photoPath may be null - depends on usage
	 * @param logoPath may be null - depends on usage
	 * @param cvPath may be null if userBean.isEmployee() = true. Ignored anyway it that's the case
	 * @return newly-created UserModel object
	 */
	public static UserModel buildUserModel(UserBean userBean) {
		UserModel userModel;
		if(userBean.isEmployee()) {
			userModel = new EmployeeUserModel();
			EmployeeUserModel employeeUserModel = (EmployeeUserModel) userModel;
			employeeUserModel.setAdmin(userBean.isAdmin());
			employeeUserModel.setCf(userBean.getCf());
			employeeUserModel.setCompany(buildCompanyModel(userBean.getCompany()));
			employeeUserModel.setName(userBean.getName());
			employeeUserModel.setNote(userBean.getNote());
			employeeUserModel.setPhoneNumber(userBean.getPhoneNumber());
			employeeUserModel.setPhoto(userBean.getPhoto());
			employeeUserModel.setRecruiter(userBean.isRecruiter());
			employeeUserModel.setSurname(userBean.getSurname());
		} else {
			userModel = new JobSeekerUserModel();
			JobSeekerUserModel jobSeekerUserModel = (JobSeekerUserModel) userModel;
			jobSeekerUserModel.setBiography(userBean.getBiography());
			jobSeekerUserModel.setBirthday(userBean.getBirthday());
			jobSeekerUserModel.setCf(userBean.getCf());
			jobSeekerUserModel.setComune(buildComuneModel(userBean.getComune()));
			jobSeekerUserModel.setCv(userBean.getCv());
			jobSeekerUserModel.setEmploymentStatus(buildEmploymentStatusModel(userBean.getEmploymentStatus()));
			jobSeekerUserModel.setHomeAddress(userBean.getHomeAddress());
			jobSeekerUserModel.setName(userBean.getName());
			jobSeekerUserModel.setPhoneNumber(userBean.getPhoneNumber());
			jobSeekerUserModel.setPhoto(userBean.getPhoto());
			jobSeekerUserModel.setSurname(userBean.getSurname());
		}

		return userModel;
	}

	private static EmploymentStatusModel buildEmploymentStatusModel(EmploymentStatusBean employmentStatusBean) {
		EmploymentStatusModel employmentStatusModel = new EmploymentStatusModel();
		employmentStatusModel.setStatus(employmentStatusBean.getStatus());

		return employmentStatusModel;
	}

	private static ComuneModel buildComuneModel(ComuneBean comuneBean) {
		ComuneModel comuneModel = new ComuneModel();
		comuneModel.setCap(comuneBean.getCap());
		comuneModel.setNome(comuneBean.getNome());
		comuneModel.setProvincia(buildProvinciaModel(comuneBean.getProvincia()));

		return comuneModel;
	}

	private static ProvinciaModel buildProvinciaModel(ProvinciaBean provinciaBean) {
		ProvinciaModel provinciaModel = new ProvinciaModel();
		provinciaModel.setSigla(provinciaBean.getSigla());
		provinciaModel.setRegione(buildRegioneModel(provinciaBean.getRegione()));

		return provinciaModel;
	}

	private static RegioneModel buildRegioneModel(RegioneBean regioneBean) {
		RegioneModel regioneModel = new RegioneModel();
		regioneModel.setNome(regioneBean.getNome());

		return regioneModel;
	}

	public static CompanyModel buildCompanyModel(CompanyBean companyBean) {
		CompanyModel companyModel = new CompanyModel();
		companyModel.setCf(companyBean.getCf());
		companyModel.setLogo(companyBean.getLogo());
		companyModel.setSocialReason(companyBean.getSocialReason());
		companyModel.setVat(companyBean.getVat());

		return companyModel;
	}
}
