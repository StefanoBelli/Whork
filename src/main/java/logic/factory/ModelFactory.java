package logic.factory;

import logic.model.CandidatureModel;
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
import logic.model.UserAuthModel;
import logic.model.UserModel;
import logic.util.Util;

import java.io.ByteArrayInputStream;

import logic.bean.CandidatureBean;
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

	public static EmploymentStatusModel buildEmploymentStatusModel(EmploymentStatusBean employmentStatusBean) {
		EmploymentStatusModel employmentStatusModel = new EmploymentStatusModel();
		employmentStatusModel.setStatus(employmentStatusBean.getStatus());

		return employmentStatusModel;
	}

	public static ComuneModel buildComuneModel(ComuneBean comuneBean) {
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
	
	public static TypeOfContractModel buildTypeOfContractModel(TypeOfContractBean typeOfContractBean) {
		TypeOfContractModel typeOfContractModel = new TypeOfContractModel();
		typeOfContractModel.setContract(typeOfContractBean.getContract());

		return typeOfContractModel;
	}
	
	public static JobCategoryModel buildJobCategoryModel(JobCategoryBean jobCategoryBean) {
		JobCategoryModel jobCategoryModel = new JobCategoryModel();
		jobCategoryModel.setCategory(jobCategoryBean.getCategory());

		return jobCategoryModel;
	}
	
	public static JobPositionModel buildJobPositionModel(JobPositionBean jobPositionBean) {
		JobPositionModel jobPositionModel = new JobPositionModel();
		jobPositionModel.setPosition(jobPositionBean.getPosition());

		return jobPositionModel;
	}
	
	public static QualificationModel buildQualificationModel(QualificationBean qualificationBean) {
		QualificationModel qualificationModel = new QualificationModel();
		qualificationModel.setQualify(qualificationBean.getQualify());

		return qualificationModel;
	}

	public static OfferModel buildOfferModel(OfferBean offerBean) {
		OfferModel offerModel = new OfferModel();
		offerModel.setId(offerBean.getId());
		offerModel.setOfferName(offerBean.getOfferName());
		offerModel.setDescription(offerBean.getDescription());
		offerModel.setJobPhysicalLocationFullAddress(offerBean.getJobPhysicalLocationFullAddress());
		offerModel.setCompany(ModelFactory.buildCompanyModel(offerBean.getCompany()));
		offerModel.setSalaryEUR(offerBean.getSalaryEUR());
		offerModel.setPhoto(offerBean.getPhoto());
		offerModel.setWorkShit(offerBean.getWorkShit());
		offerModel.setJobPosition(ModelFactory.buildJobPositionModel(offerBean.getJobPosition()));
		offerModel.setQualification(ModelFactory.buildQualificationModel(offerBean.getQualification()));
		offerModel.setTypeOfContract(ModelFactory.buildTypeOfContractModel(offerBean.getTypeOfContract()));
		offerModel.setPublishDate(offerBean.getPublishDate());
		offerModel.setClickStats(offerBean.getClickStats());
		offerModel.setNote(offerBean.getNote());
		offerModel.setVerifiedByWhork(offerBean.isVerifiedByWhork());
		offerModel.setJobCategory(ModelFactory.buildJobCategoryModel(offerBean.getJobCategory()));
		offerModel.setEmployee(ModelFactory.buildUserModel(offerBean.getEmployee()));
		return offerModel;
	}


	public static CandidatureModel buildCandidatureModel(CandidatureBean candidatureBean) {
		CandidatureModel candidatureModel=new CandidatureModel();
		candidatureModel.setCandidatureDate(candidatureBean.getCandidatureDate());
		candidatureModel.setJobSeeker(ModelFactory.buildUserModel(candidatureBean.getJobSeeker()));
		candidatureModel.setOffer(ModelFactory.buildOfferModel(candidatureBean.getOffer()));
		return candidatureModel;
	}
	

	
}
