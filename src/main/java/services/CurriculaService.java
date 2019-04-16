
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CurriculaRepository;
import domain.Actor;
import domain.Company;
import domain.Curricula;
import domain.EducationData;
import domain.Hacker;
import domain.MiscellaneousData;
import domain.PersonalData;
import domain.PositionData;

@Service
@Transactional
public class CurriculaService {

	// Managed repository
	@Autowired
	private CurriculaRepository			curriculaRepository;

	// Supporting services
	@Autowired
	private PersonalDataService			personalDataService;

	@Autowired
	private PositionDataService			positionDataService;

	@Autowired
	private EducationDataService		educationDataService;

	@Autowired
	private MiscellaneousDataService	miscellaneousDataService;

	@Autowired
	private ActorService				actorService;

	@Autowired
	private HackerService				hackerService;

	@Autowired
	private CompanyService				companyService;


	// Simple CRUD methods
	// R17.1
	public Curricula create() {

		Curricula result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginHacker(actorLogged);

		result = new Curricula();
		final PersonalData personalData = this.personalDataService.create();
		final Collection<PositionData> positionDatas = new HashSet<>();
		final Collection<EducationData> educationDatas = new HashSet<>();
		final Collection<MiscellaneousData> miscellaneousDatas = new HashSet<>();

		result.setIsCopy(false);
		result.setHacker((Hacker) actorLogged);
		result.setPersonalData(personalData);
		result.setPositionDatas(positionDatas);
		result.setEducationDatas(educationDatas);
		result.setMiscellaneousDatas(miscellaneousDatas);

		return result;
	}

	public Collection<Curricula> findAll() {
		Collection<Curricula> result;

		result = this.curriculaRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Curricula findOne(final int curriculaId) {
		Assert.isTrue(curriculaId != 0);
		Curricula result;

		result = this.curriculaRepository.findOne(curriculaId);
		Assert.notNull(result);

		return result;
	}

	// R17.1
	public Curricula save(final Curricula curricula) {
		Assert.notNull(curricula);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginHacker(actorLogged);

		Curricula result;

		PersonalData personalData = curricula.getPersonalData();
		personalData = this.personalDataService.save(personalData);

		curricula.setPersonalData(personalData);

		result = this.curriculaRepository.save(curricula);

		return result;
	}

	public Curricula saveAuxiliar(final Curricula curricula) {
		Assert.notNull(curricula);

		Curricula result;

		result = this.curriculaRepository.save(curricula);

		return result;
	}

	// R17.1
	public void delete(final Curricula curricula) {
		Assert.notNull(curricula);
		Assert.isTrue(curricula.getId() != 0);
		Assert.isTrue(this.curriculaRepository.exists(curricula.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginHacker(actorLogged);

		this.curriculaRepository.delete(curricula);
	}

	public void deleteAuxiliar(final Curricula curricula) {
		Assert.notNull(curricula);
		Assert.isTrue(curricula.getId() != 0);
		Assert.isTrue(this.curriculaRepository.exists(curricula.getId()));

		this.curriculaRepository.delete(curricula);
	}

	// Other business methods
	// R17.1
	public Curricula copyCurricula(final Curricula curricula) {
		Curricula result = new Curricula();
		result.setIsCopy(true);
		result.setHacker(curricula.getHacker());
		PersonalData personalData = this.personalDataService.create();
		personalData.setName(curricula.getPersonalData().getName());
		personalData.setStatement(curricula.getPersonalData().getStatement());
		personalData.setPhoneNumber(curricula.getPersonalData().getPhoneNumber());
		personalData.setGitHubProfile(curricula.getPersonalData().getGitHubProfile());
		personalData.setLinkedInProfile(curricula.getPersonalData().getLinkedInProfile());
		personalData = this.personalDataService.save(personalData);
		final Collection<PositionData> positionDatas = new HashSet<>();
		final Collection<EducationData> educationDatas = new HashSet<>();
		final Collection<MiscellaneousData> miscellaneousDatas = new HashSet<>();
		for (final PositionData posData : curricula.getPositionDatas()) {
			PositionData positionData = this.positionDataService.create();
			positionData.setTitle(posData.getTitle());
			positionData.setDescription(posData.getDescription());
			positionData.setStartDate(posData.getStartDate());
			positionData.setEndDate(posData.getEndDate());
			positionData = this.positionDataService.saveAuxiliar(positionData);
			positionDatas.add(positionData);
		}
		for (final EducationData edData : curricula.getEducationDatas()) {
			EducationData educationData = this.educationDataService.create();
			educationData.setDegree(edData.getDegree());
			educationData.setInstitution(edData.getInstitution());
			educationData.setMark(edData.getMark());
			educationData.setStartDate(edData.getStartDate());
			educationData.setEndDate(edData.getEndDate());
			educationData = this.educationDataService.saveAuxiliar(educationData);
			educationDatas.add(educationData);
		}
		for (final MiscellaneousData misData : curricula.getMiscellaneousDatas()) {
			MiscellaneousData miscellaneousData = this.miscellaneousDataService.create();
			miscellaneousData.setText(misData.getText());
			miscellaneousData.setAttachments(misData.getAttachments());
			miscellaneousData = this.miscellaneousDataService.saveAuxiliar(miscellaneousData);
			miscellaneousDatas.add(miscellaneousData);
		}

		result.setPersonalData(personalData);
		result.setPositionDatas(positionDatas);
		result.setEducationDatas(educationDatas);
		result.setMiscellaneousDatas(miscellaneousDatas);

		result = this.curriculaRepository.save(result);

		return result;
	}

	// R17.1
	public Collection<Curricula> findCurriculasByHackerLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginHacker(actorLogged);

		Collection<Curricula> result;

		final Hacker hackerLogged = (Hacker) actorLogged;

		result = this.curriculaRepository.findCurriculasByHackerId(hackerLogged.getId());
		Assert.notNull(result);

		return result;
	}

	public Curricula findCurriculaByPersonalDataId(final int personalDataId) {
		Curricula result;

		result = this.curriculaRepository.findCurriculaByPersonalDataId(personalDataId);

		return result;
	}

	public Curricula findCurriculaByPositionDataId(final int positionDataId) {
		Curricula result;

		result = this.curriculaRepository.findCurriculaByPositionDataId(positionDataId);

		return result;
	}

	public Curricula findCurriculaByEducationDataId(final int educationDataId) {
		Curricula result;

		result = this.curriculaRepository.findCurriculaByEducationDataId(educationDataId);

		return result;
	}

	public Curricula findCurriculaByMiscellaneousDataId(final int miscellaneousDataId) {
		Curricula result;

		result = this.curriculaRepository.findCurriculaByMiscellaneousDataId(miscellaneousDataId);

		return result;
	}

	public Curricula findCurriculaHackerLogged(final int curriculaId) {
		Assert.isTrue(curriculaId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginHacker(actorLogged);

		final Hacker hackerOwner = this.hackerService.findHackerByCurriculaId(curriculaId);
		Assert.isTrue(actorLogged.equals(hackerOwner), "The logged actor is not the owner of this entity");

		Curricula result;

		result = this.curriculaRepository.findOne(curriculaId);
		Assert.notNull(result);

		return result;
	}

	public Curricula findCurriculaCompanyLogged(final int curriculaId) {
		Assert.isTrue(curriculaId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginCompany(actorLogged);

		final Collection<Company> companiesOwner = this.companyService.findCompaniesByCurriculaId(curriculaId);
		Assert.isTrue(companiesOwner.contains(actorLogged), "The logged actor is not the owner of this entity");

		Curricula result;

		result = this.curriculaRepository.findOne(curriculaId);
		Assert.notNull(result);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Curricula reconstruct(final Curricula curricula, final BindingResult binding) {
		Curricula result;

		//Nunca se va a editar un curriculum como tal, si no sus datas
		final Collection<PositionData> positionDatas = new HashSet<>();
		final Collection<EducationData> educationDatas = new HashSet<>();
		final Collection<MiscellaneousData> miscellaneousDatas = new HashSet<>();

		final Actor actorLogged = this.actorService.findActorLogged();

		curricula.setIsCopy(false);
		curricula.setHacker((Hacker) actorLogged);
		curricula.setPositionDatas(positionDatas);
		curricula.setEducationDatas(educationDatas);
		curricula.setMiscellaneousDatas(miscellaneousDatas);
		result = curricula;

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.curriculaRepository.flush();
	}

}
