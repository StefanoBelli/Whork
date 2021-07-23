package logic.factory;

import logic.model.UserModel;
import logic.model.CompanyModel;
import logic.model.ComuneModel;
import logic.model.EmployeeUserModel;
import logic.model.EmploymentStatusModel;
import logic.model.JobSeekerUserModel;
import logic.model.ProvinciaModel;
import logic.model.RegioneModel;
import logic.bean.UserBean;

import java.util.Date;

import logic.bean.CompanyBean;
import logic.bean.ComuneBean;
import logic.bean.EmploymentStatusBean;
import logic.bean.ProvinciaBean;
import logic.bean.RegioneBean;
import logic.bean.UserAuthBean;

public final class BeanFactory {
	private BeanFactory() {}

	public static CompanyBean buildCompanyBean(String cf, String logo, String socialReason, String vat) {
		CompanyBean companyBean = new CompanyBean();
		companyBean.setCf(cf);
		companyBean.setLogo(logo);
		companyBean.setSocialReason(socialReason);
		companyBean.setVat(vat);

		return companyBean;
	}
	
	public static CompanyBean buildCompanyBean(CompanyModel companyModel) {
		CompanyBean companyBean = new CompanyBean();
		companyBean.setCf(companyModel.getCf());
		companyBean.setLogo(companyModel.getLogo());
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

	/**
	 * Builds a ComuneBean object from a correctly-formatted string "parsed"
	 * @param parsed - str like "Nome del comune Italiano PR - 00000, Regione"
	 * @return newly-created ComuneBean object
	 */
	public static ComuneBean buildComuneBean(String parsed) {
		int lastSpc = parsed.lastIndexOf(' ');
		String regioneName = parsed.substring(lastSpc + 1, parsed.length());
		String cap = parsed.substring(lastSpc - 6, lastSpc - 1);
		String provinciaSigla = parsed.substring(lastSpc - 11, lastSpc - 9);
		String comuneName = parsed.substring(0, lastSpc - 12);

		RegioneBean regioneBean = new RegioneBean();
		regioneBean.setNome(regioneName);

		ProvinciaBean provinciaBean = new ProvinciaBean();
		provinciaBean.setSigla(provinciaSigla);
		provinciaBean.setRegione(regioneBean);

		ComuneBean comuneBean = new ComuneBean();
		comuneBean.setNome(comuneName);
		comuneBean.setCap(cap);
		comuneBean.setProvincia(provinciaBean);

		return comuneBean;
	}
	
	public static EmploymentStatusBean buildEmploymentStatusBean(
			EmploymentStatusModel employmentStatusModel) {
		EmploymentStatusBean employmentStatusBean = new EmploymentStatusBean();
		employmentStatusBean.setStatus(employmentStatusModel.getStatus());

		return employmentStatusBean;
	}

	public static EmploymentStatusBean buildEmploymentStatusBean(String status) {
		EmploymentStatusBean employmentStatusBean = new EmploymentStatusBean();
		employmentStatusBean.setStatus(status);

		return employmentStatusBean;
	}

	public static UserBean buildUserBean(UserModel userModel) {
		UserBean userBean = new UserBean();
		userBean.setName(userModel.getName());
		userBean.setSurname(userModel.getSurname());
		userBean.setPhoneNumber(userModel.getPhoneNumber());
		userBean.setCf(userModel.getCf());
		userBean.setPhoto(userModel.getPhoto());
		userBean.setEmployee(userModel.isEmployee());
		
		if (userBean.isEmployee()) {
			EmployeeUserModel m = (EmployeeUserModel) userModel;
			userBean.setRecruiter(m.isRecruiter());
			userBean.setAdmin(m.isAdmin());
			userBean.setNote(m.getNote());
			userBean.setCompany(BeanFactory.buildCompanyBean(m.getCompany()));
		} else {
			JobSeekerUserModel m = (JobSeekerUserModel) userModel;
			userBean.setHomeAddress(m.getHomeAddress());
			userBean.setPhoneNumber(m.getPhoneNumber());
			userBean.setCv(m.getCv());
			userBean.setEmploymentStatus(BeanFactory.buildEmploymentStatusBean(m.getEmploymentStatus()));
			userBean.setComune(BeanFactory.buildComuneBean(m.getComune()));
			userBean.setBiography(m.getBiography());
			userBean.setBirthday(m.getBirthday());
		}

		return userBean;
	}

	public static UserAuthBean buildUserAuthBean(String email, String password) {
		UserAuthBean userAuthBean = new UserAuthBean();
		userAuthBean.setEmail(email);
		userAuthBean.setPassword(password);

		return userAuthBean;
	}
	
	public static void buildCandidatureBean(String socialReason, Date candidatureDate, String typeOfContract, String jobOccupation, String email) {
		/*CandidatureBean candidatureBean = new CandidatureBean();
		candidatureBean.setCandidatureDate(candidatureDate);
		candidatureBean.setJobOccupation(jobOccupation);
		candidatureBean.setSocialReason(socialReason);
		candidatureBean.setTypeOfContract(typeOfContract);
		candidatureBean.setEmail(email);
		
		return candidatureBean;*/
		return;
	}
}
