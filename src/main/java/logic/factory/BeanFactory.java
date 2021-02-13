package logic.factory;

import java.io.File;

import logic.exception.SyntaxException;
import logic.model.UserModel;
import logic.model.CompanyModel;
import logic.model.ComuneModel;
import logic.model.EmployeeUserModel;
import logic.model.EmploymentStatusModel;
import logic.model.JobSeekerUserModel;
import logic.model.ProvinciaModel;
import logic.model.RegioneModel;
import logic.bean.UserBean;
import logic.bean.CompanyBean;
import logic.bean.ComuneBean;
import logic.bean.EmploymentStatusBean;
import logic.bean.ProvinciaBean;
import logic.bean.RegioneBean;
import logic.bean.UserAuthBean;

public final class BeanFactory {
	private BeanFactory() {}
	
	public static CompanyBean buildCompanyBean(CompanyModel companyModel) 
			throws SyntaxException {
		CompanyBean companyBean = new CompanyBean();
		companyBean.setCf(companyModel.getCf());
		companyBean.setLogo(new File(companyModel.getLogo()));
		companyBean.setSocialReason(companyModel.getSocialReason());
		companyBean.setVat(companyModel.getVat());

		return companyBean;
	}

	public static RegioneBean buildRegioneBean(RegioneModel regioneModel) {
		RegioneBean regioneBean = new RegioneBean();
		regioneBean.setNome(regioneModel.getNome());

		return regioneBean;
	}

	public static ProvinciaBean buildProvinciaBean(ProvinciaModel provinciaModel) {
		ProvinciaBean provinciaBean = new ProvinciaBean();
		provinciaBean.setSigla(provinciaModel.getSigla());
		provinciaBean.setRegione(BeanFactory.buildRegioneBean(provinciaModel.getRegione()));

		return provinciaBean;
	}

	public static ComuneBean buildComuneBean(ComuneModel comuneModel) {
		ComuneBean comuneBean = new ComuneBean();
		comuneBean.setCap(comuneModel.getCap());
		comuneBean.setNome(comuneModel.getNome());
		comuneBean.setProvincia(BeanFactory.buildProvinciaBean(comuneModel.getProvincia()));

		return comuneBean;
	}
	
	public static EmploymentStatusBean buildEmploymentStatusBean(
			EmploymentStatusModel employmentStatusModel) {
		EmploymentStatusBean employmentStatusBean = new EmploymentStatusBean();
		employmentStatusBean.setStatus(employmentStatusModel.getStatus());

		return employmentStatusBean;
	}

	public static UserBean buildUserBean(UserModel userModel) throws SyntaxException {
		UserBean userBean = new UserBean();

		userBean.setName(userModel.getName());
		userBean.setSurname(userModel.getSurname());
		userBean.setPhoneNumber(userModel.getPhoneNumber());
		userBean.setEmployee(userModel.isEmployee());
		if (userBean.isEmployee()) {
			EmployeeUserModel m = (EmployeeUserModel) userModel;
			userBean.setRecruiter(m.isRecruiter());
			userBean.setAdmin(m.isAdmin());
			userBean.setNote(m.getNote());
			userBean.setPhoto(new File(m.getPhoto()));
			userBean.setCf(m.getCf());
			userBean.setCompany(BeanFactory.buildCompanyBean(m.getCompany()));
		} else {
			JobSeekerUserModel m = (JobSeekerUserModel) userModel;
			userBean.setHomeAddress(m.getHomeAddress());
			userBean.setPhoneNumber(m.getPhoneNumber());
			userBean.setCv(new File(m.getCv()));
			userBean.setCf(m.getCf());
			userBean.setEmploymentStatus(BeanFactory.buildEmploymentStatusBean(m.getEmploymentStatus()));
			userBean.setComune(BeanFactory.buildComuneBean(m.getComune()));
			userBean.setBiography(m.getBiography());
			userBean.setBirthday(m.getBirthday());
		}

		return userBean;
	}

	public static UserAuthBean buildUserAuthBean(String email, String password) 
			throws SyntaxException {
		UserAuthBean userAuthBean = new UserAuthBean();
		userAuthBean.setEmail(email);
		userAuthBean.setPassword(password);

		return userAuthBean;
	}
}
