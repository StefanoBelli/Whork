package logic.factory;

import logic.model.UserModel;
import logic.model.CandidatureModel;
import logic.model.ChatLogEntryModel;
import logic.model.CompanyModel;
import logic.model.ComuneModel;
import logic.model.EmployeeUserModel;
import logic.model.EmploymentStatusModel;
import logic.model.JobCategoryModel;
import logic.model.JobPositionModel;
import logic.model.JobSeekerUserModel;
import logic.model.OfferModel;
import logic.model.ProvinciaModel;
import logic.model.QualificationModel;
import logic.model.RegioneModel;
import logic.model.TypeOfContractModel;
import logic.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

import logic.bean.CandidatureBean;
import logic.bean.ChatLogEntryBean;
import logic.bean.CompanyBean;
import logic.bean.ComuneBean;
import logic.bean.EmploymentStatusBean;
import logic.bean.JobCategoryBean;
import logic.bean.JobPositionBean;
import logic.bean.OfferBean;
import logic.bean.ProvinciaBean;
import logic.bean.QualificationBean;
import logic.bean.RegioneBean;
import logic.bean.TypeOfContractBean;
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
	
	public static List<OfferBean> buildOffersBean(List<OfferModel> offers) {
		List<OfferBean> offersBean = new ArrayList<>();
		for(OfferModel offerModel : offers) {
			OfferBean offerBean = new OfferBean();
			offerBean.setId(offerModel.getId());
			offerBean.setOfferName(offerModel.getOfferName());
			offerBean.setDescription(offerModel.getDescription());
			offerBean.setJobPhysicalLocationFullAddress(offerModel.getJobPhysicalLocationFullAddress());
			offerBean.setCompany(BeanFactory.buildCompanyBean(offerModel.getCompany()));
			offerBean.setSalaryEUR(offerModel.getSalaryEUR());
			offerBean.setPhoto(offerModel.getPhoto());
			offerBean.setWorkShift(offerModel.getWorkShift());
			offerBean.setJobPosition(BeanFactory.buildJobPositionBean(offerModel.getJobPosition()));
			offerBean.setQualification(BeanFactory.buildQualificationBean(offerModel.getQualification()));
			offerBean.setTypeOfContract(BeanFactory.buildTypeOfContractBean(offerModel.getTypeOfContract()));
			offerBean.setPublishDate(offerModel.getPublishDate());
			offerBean.setClickStats(offerModel.getClickStats());
			offerBean.setNote(offerModel.getNote());
			offerBean.setVerifiedByWhork(offerModel.isVerifiedByWhork());
			offerBean.setJobCategory(BeanFactory.buildJobCategoryBean(offerModel.getJobCategory()));
			offerBean.setEmployee(BeanFactory.buildUserBean(offerModel.getEmployee()));
			
			offersBean.add(offerBean);
		}
		return offersBean;
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
			userBean.setInstagram(m.getInstagram());
			userBean.setWebsite(m.getWebsite());
			userBean.setFacebook(m.getFacebook());
			userBean.setTwitter(m.getTwitter());
		}

		return userBean;
	}

	public static UserAuthBean buildUserAuthBean(String email, String password) {
		UserAuthBean userAuthBean = new UserAuthBean();
		userAuthBean.setEmail(email);
		userAuthBean.setPassword(password);

		return userAuthBean;
	}
	
	public static TypeOfContractBean buildTypeOfContractBean(TypeOfContractModel typeOfContractModel) {
		TypeOfContractBean typeOfContractBean = new TypeOfContractBean();
		typeOfContractBean.setContract(typeOfContractModel.getContract());

		return typeOfContractBean;
	}
	
	public static JobCategoryBean buildJobCategoryBean(JobCategoryModel jobCategoryModel) {
		JobCategoryBean jobCategoryBean = new JobCategoryBean();
		jobCategoryBean.setCategory(jobCategoryModel.getCategory());

		return jobCategoryBean;
	}
	
	public static JobPositionBean buildJobPositionBean(JobPositionModel jobPositionModel) {
		JobPositionBean jobPositionBean = new JobPositionBean();
		jobPositionBean.setPosition(jobPositionModel.getPosition());

		return jobPositionBean;
	}
	
	public static QualificationBean buildQualificationBean(QualificationModel qualificationModel) {
		QualificationBean qualificationBean = new QualificationBean();
		qualificationBean.setQualify(qualificationModel.getQualify());

		return qualificationBean;
	}

	public static CandidatureBean buildCandidatureBean(CandidatureModel candidatureModel) {
		CandidatureBean candidatureBean = new CandidatureBean();
		candidatureBean.setOffer(BeanFactory.buildOfferBean(candidatureModel.getOffer()));
		candidatureBean.setJobSeeker(BeanFactory.buildUserBean(candidatureModel.getJobSeeker()));
		candidatureBean.setCandidatureDate(candidatureModel.getCandidatureDate());
		
		return candidatureBean;
	}

	public static OfferBean buildOfferBean(OfferModel offerModel) {
		OfferBean offerBean = new OfferBean();
		offerBean.setOfferName(offerModel.getOfferName());
		offerBean.setVerifiedByWhork(offerModel.isVerifiedByWhork());
		offerBean.setDescription(offerModel.getDescription());
		offerBean.setCompany(BeanFactory.buildCompanyBean(offerModel.getCompany()));
		offerBean.setClickStats(offerModel.getClickStats());
		offerBean.setSalaryEUR(offerModel.getSalaryEUR());
		offerBean.setPublishDate(offerModel.getPublishDate());
		offerBean.setWorkShift(offerModel.getWorkShift());
		offerBean.setQualification(BeanFactory.buildQualificationBean(offerModel.getQualification()));
		offerBean.setTypeOfContract(BeanFactory.buildTypeOfContractBean(offerModel.getTypeOfContract()));
		offerBean.setPhoto(offerModel.getPhoto());
		offerBean.setNote(offerModel.getNote());
		offerBean.setJobPhysicalLocationFullAddress(offerModel.getJobPhysicalLocationFullAddress());
		offerBean.setJobCategory(BeanFactory.buildJobCategoryBean(offerModel.getJobCategory()));
		offerBean.setJobPosition(BeanFactory.buildJobPositionBean(offerModel.getJobPosition()));
		offerBean.setId(offerModel.getId());
		offerBean.setEmployee(BeanFactory.buildUserBean(offerModel.getEmployee()));
		return offerBean;
	}

	public static OfferBean buildOfferBean(String name, String category,
			String position, String qualification, String typeOfContract) {
		OfferBean offerBean = new OfferBean();
		offerBean.setOfferName(name);
		offerBean.setJobCategory(buildJobCategoryBean(category));
		offerBean.setJobPosition(buildJobPositionBean(position));
		offerBean.setQualification(buildQualificationBean(qualification));
		offerBean.setTypeOfContract(buildTypeOfContractBean(typeOfContract));
		return offerBean;
		
	}

	public static TypeOfContractBean buildTypeOfContractBean(String typeOfContract) {
		TypeOfContractBean typeOfContractBean = new TypeOfContractBean();
		typeOfContractBean.setContract(typeOfContract);
		return typeOfContractBean;
	}

	public static QualificationBean buildQualificationBean(String qualification) {
		QualificationBean qualificationBean = new QualificationBean();
		qualificationBean.setQualify(qualification);
		return qualificationBean;
	}

	public static JobPositionBean buildJobPositionBean(String position) {
		JobPositionBean jobPositionBean = new JobPositionBean();
		jobPositionBean.setPosition(position);
		return jobPositionBean;
	}

	public static JobCategoryBean buildJobCategoryBean(String category) {
		JobCategoryBean jobCategoryBean = new JobCategoryBean();
		jobCategoryBean.setCategory(category);
		return jobCategoryBean;
	}
	
	public static CandidatureBean buildCandidatureBean(OfferBean offer, UserBean jobSeeker) {
		CandidatureBean candidatureBean=new CandidatureBean();
		candidatureBean.setJobSeeker(jobSeeker);
		candidatureBean.setOffer(offer);
		return candidatureBean;
	}
	
	public static ChatLogEntryBean buildChatEntryLogBean(ChatLogEntryModel chatModel) {
		ChatLogEntryBean chatBean = new ChatLogEntryBean();
		chatBean.setLogEntryId(chatModel.getLogEntryId());
		chatBean.setSenderEmail(chatModel.getSenderEmail());
		chatBean.setReceiverEmail(chatModel.getReceiverEmail());
		chatBean.setText(chatModel.getText());
		chatBean.setDeliveryRequestTime(chatModel.getDeliveryRequestTime());
		return chatBean;
	}
	
}
