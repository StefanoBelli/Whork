package logic.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import logic.bean.CandidatureBean;
import logic.bean.CompanyBean;
import logic.bean.UserAuthBean;
import logic.bean.UserBean;
import logic.dao.AccountDao;
import logic.dao.OfferDao;
import logic.dao.UserAuthDao;
import logic.dao.UserDao;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.exception.InternalException;
import logic.exception.InvalidPasswordException;
import logic.factory.BeanFactory;
import logic.factory.ModelFactory;
import logic.model.CandidatureModel;
import logic.model.EmploymentStatusModel;
import logic.model.JobSeekerUserModel;
import logic.model.UserAuthModel;
import logic.util.ServletUtil;
import logic.util.Util;
import logic.util.tuple.Pair;

public final class AccountController {
	
	private static final String DATA_ACCESS_ERROR =
			"Data access error";
	
	private AccountController() {}
	
	public static List<CandidatureBean> getSeekerCandidature(UserBean userBean) throws InternalException {		
		List<CandidatureBean> listCandidatureBean = new ArrayList<>();		
		List<CandidatureModel> listCandidatureModel;
		try {
			listCandidatureModel = AccountDao.getSeekerCandidature(ModelFactory.buildUserModel(userBean));
		} catch (DataAccessException | DataLogicException e) {
			throw new InternalException(DATA_ACCESS_ERROR);
		}
		
		if(listCandidatureModel == null) return listCandidatureBean;
		
		int i = 0;
		while (i<listCandidatureModel.size()) {
			listCandidatureBean.add(BeanFactory.buildCandidatureBean(listCandidatureModel.get(i)));
			i ++;
		}
		
		return listCandidatureBean;
		
	}	
	
	public static void editAccountController(String function, UserBean userBean, UserAuthBean userAuthBean, String newPassword) throws DataAccessException, InternalException, InvalidPasswordException, DataLogicException {
		JobSeekerUserModel userModel = (JobSeekerUserModel) ModelFactory.buildUserModel(userBean);
		UserAuthModel userAuthModel = null;
		
		if(userAuthBean != null) userAuthModel = ModelFactory.buildUserAuthModel(userAuthBean);
		if(function == null) throw new InternalException("Function value cannot be null");
		
		if (function.equals("SocialAccounts")) 
			AccountDao.editSocialAccount (userModel);
		
		else if (function.equals("JobSeekerInfoAccount")) {
			if(userAuthModel == null) 
				throw new InternalException("userAuthModel is null");			

			AccountDao.editJobSeekerInfoAccount (userModel, userAuthModel.getEmail());
		} else if (function.equals("JobSeekerBiography"))
			AccountDao.editJobSeekerBiography (userModel);
		
		else if (function.equals("ChangePasswordAccount")) {	
			if(userAuthModel == null) 
				throw new InternalException("userAuthModel is null");			
			
			Pair<String, ByteArrayInputStream> user = UserAuthDao.getUserCfAndBcryPwdByEmail(userAuthModel.getEmail());						
			if(!Util.Bcrypt.equals(userAuthBean.getPassword(), user.getSecond().readAllBytes())) { //oldPassword == passwordSavedInDB
				throw new InvalidPasswordException();							
			} else {
				userAuthBean.setPassword(newPassword);
				UserAuthModel newUserAuthModel = ModelFactory.buildUserAuthModel(userAuthBean);
				UserAuthDao.changeUserAuthPassword(newUserAuthModel);
			}
			
		}
	}
	
	public static UserBean changePictureAccountJobSeeker(String newPath, UserBean userBean) throws DataAccessException, IOException {
		JobSeekerUserModel userModel = (JobSeekerUserModel) ModelFactory.buildUserModel(userBean);
		AccountDao.editJobSeekerPicture (userModel, newPath);
		if(userBean.getPhoto() != null)	ServletUtil.deleteUserFile(userBean.getPhoto());
		userBean.setPhoto(newPath);
		return userBean;
	}
	
	public static int getNumberOfEmployees(UserBean userBean) throws DataAccessException, DataLogicException {
		return AccountDao.countOfEmployees(ModelFactory.buildCompanyModel(userBean.getCompany()));		
	}
	
	public static int getNumberOfOffers(UserBean userBean) throws DataAccessException, DataLogicException {
		return OfferDao.totalNumberOffers(ModelFactory.buildCompanyModel(userBean.getCompany()));		
	}
	
	public static int getNumberOfClick(UserBean userBean) throws DataAccessException, DataLogicException {
		return OfferDao.totalNumberOfClick(ModelFactory.buildCompanyModel(userBean.getCompany()));		
	}
	
	public static Map<String, Double> getEmploymentStatusBtCompanyVAT(CompanyBean company) throws DataAccessException, DataLogicException {
		List<EmploymentStatusModel> listEmployments =  UserDao.getEmploymentStatusByCompanyVat(ModelFactory.buildCompanyModel(company));
		Map<String, Double> map = new HashMap<>();
		double tot = 0;
			
		for(int i=0; i<listEmployments.size(); i++) {
			if(map.containsKey(listEmployments.get(i).getStatus())) {
				map.replace(listEmployments.get(i).getStatus(), map.get(listEmployments.get(i).getStatus())+1.0);				
			} else {
				map.put(listEmployments.get(i).getStatus(), 1.0);				
			}
			tot += 1.0;
		}		
		
		Iterator<String> keys = map.keySet().iterator();		
		
		if(keys.hasNext()) {
			String key = keys.next();
			map.replace(key, map.get(key)/tot);
		}		
		
		return map;
	}
}

Italia	n.d.
Albania	Z100
Andorra	Z101
Austria	Z102
Belgio	Z103
Bulgaria	Z104
Danimarca	Z107
Finlandia	Z109
Francia	Z110
Germania	Z112
Regno Unito	Z114
Grecia	Z115
Irlanda	Z116
Islanda	Z117
Liechtenstein	Z119
Lussemburgo	Z120
Malta	Z121
Monaco	Z123
Norvegia	Z125
Paesi Bassi	Z126
Polonia	Z127
Portogallo	Z128
Romania	Z129
San Marino	Z130
Spagna	Z131
Svezia	Z132
Svizzera	Z133
Ucraina	Z138
Ungheria	Z134
Federazione russa	Z154
Stato della Città del Vaticano	Z106
Estonia	Z144
Lettonia	Z145
Lituania	Z146
Croazia	Z149
Slovenia	Z150
Bosnia-Erzegovina	Z153
Macedonia del Nord	Z148
Moldova	Z140
Slovacchia	Z155
Bielorussia	Z139
Repubblica ceca	Z156
Montenegro	Z159
Serbia	Z158
Kosovo	Z160
Afghanistan	Z200
Arabia Saudita	Z203
Bahrein	Z204
Bangladesh	Z249
Bhutan	Z205
Myanmar/Birmania	Z206
Brunei Darussalam	Z207
Cambogia	Z208
Sri Lanka	Z209
Cina	Z210
Cipro	Z211
Corea del Nord	Z214
Corea del Sud	Z213
Emirati Arabi Uniti	Z215
Filippine	Z216
Palestina	Z161
Giappone	Z219
Giordania	Z220
India	Z222
Indonesia	Z223
Iran	Z224
Iraq	Z225
Israele	Z226
Kuwait	Z227
Laos	Z228
Libano	Z229
Timor Leste	Z242
Maldive	Z232
Malaysia	Z247
Mongolia	Z233
Nepal	Z234
Oman	Z235
Pakistan	Z236
Qatar	Z237
Singapore	Z248
Siria	Z240
Thailandia	Z241
Turchia	Z243
Vietnam	Z251
Yemen	Z246
Kazakhstan	Z255
Uzbekistan	Z259
Armenia	Z252
Azerbaigian	Z253
Georgia	Z254
Kirghizistan	Z256
Tagikistan	Z257
Taiwan	Z217
Turkmenistan	Z258
Algeria	Z301
Angola	Z302
Costa d'Avorio	Z313
Benin	Z314
Botswana	Z358
Burkina Faso	Z354
Burundi	Z305
Camerun	Z306
Capo Verde	Z307
Repubblica Centrafricana	Z308
Ciad	Z309
Comore	Z310
Congo	Z311
Egitto	Z336
Etiopia	Z315
Gabon	Z316
Gambia	Z317
Ghana	Z318
Gibuti	Z361
Guinea	Z319
Guinea-Bissau	Z320
Guinea equatoriale	Z321
Kenya	Z322
Lesotho	Z359
Liberia	Z325
Libia	Z326
Madagascar	Z327
Malawi	Z328
Mali	Z329
Marocco	Z330
Mauritania	Z331
Maurizio	Z332
Mozambico	Z333
Namibia	Z300
Niger	Z334
Nigeria	Z335
Ruanda	Z338
Sao Tomé e Principe	Z341
Seychelles	Z342
Senegal	Z343
Sierra Leone	Z344
Somalia	Z345
Sudafrica	Z347
Sudan	Z348
Eswatini	Z349
Tanzania	Z357
Togo	Z351
Tunisia	Z352
Uganda	Z353
Repubblica Democratica del Congo	Z312
Zambia	Z355
Zimbabwe	Z337
Eritrea	Z368
Sud Sudan	Z907
Antigua e Barbuda	Z532
Bahamas	Z502
Barbados	Z522
Belize	Z512
Canada	Z401
Costa Rica	Z503
Cuba	Z504
Dominica	Z526
Repubblica Dominicana	Z505
El Salvador	Z506
Giamaica	Z507
Grenada	Z524
Guatemala	Z509
Haiti	Z510
Honduras	Z511
Messico	Z514
Nicaragua	Z515
Panama	Z516
Santa Lucia	Z527
Saint Vincent e Grenadine	Z528
Saint Kitts e Nevis	Z533
Stati Uniti d'America	Z404
Argentina	Z600
Bolivia	Z601
Brasile	Z602
Cile	Z603
Colombia	Z604
Ecuador	Z605
Guyana	Z606
Paraguay	Z610
Perù	Z611
Suriname	Z608
Trinidad e Tobago	Z612
Uruguay	Z613
Venezuela	Z614
Australia	Z700
Figi	Z704
Kiribati	Z731
Isole Marshall	Z711
Stati Federati di Micronesia	Z735
Nauru	Z713
Nuova Zelanda	Z719
Palau	Z734
Papua Nuova Guinea	Z730
Isole Salomone	Z724
Samoa	Z726
Tonga	Z728
Tuvalu	Z732
Vanuatu	Z733
Nuova Caledonia	Z716
Saint-Martin (FR)	n.d.
Sahara occidentale	Z339
Saint-Barthélemy	n.d.
Bermuda	Z400
Isole Cook (NZ)	Z703
Gibilterra	Z113
Isole Cayman	Z530
Anguilla	Z529
Polinesia francese	Z723
Isole Fær Øer	Z108
Jersey	n.d.
Aruba	Z501
Sint Maarten (NL)	n.d.
Groenlandia	Z402
Sark	n.d.
Guernsey	n.d.
Isole Falkland (Malvine)	Z609
Isola di Man	Z122
Montserrat	Z531
Curaçao	n.d.
Isole Pitcairn	Z722
Saint Pierre e Miquelon	Z403
Sant'Elena	Z340
Terre australi e antartiche francesi	n.d.
Isole Turks e Caicos	Z519
Isole Vergini britanniche	Z525
Wallis e Futuna	Z729

