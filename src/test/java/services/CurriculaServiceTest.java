
package services;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Curricula;
import domain.PersonalData;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CurriculaServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private CurriculaService	curriculaService;

	@Autowired
	private PersonalDataService	personalDataService;

	@PersistenceContext
	EntityManager				entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 17.1
	 *         Caso de uso: listar "Curriculas" del "Hacker" logeado
	 *         Tests positivos: 1
	 *         *** 1. Listar "Curriculas" del "Hacker" logeado correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de listar "Curriculas" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 100% 18/18 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListCurriculaAuthenticated() {

		final Object testingData[][] = {
			{
				"hacker1", null
			}, {
				"company1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listCurriculaAuthenticatedTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 17.1
	 *         Caso de uso: crear un "Curricula"
	 *         Tests positivos: 1
	 *         *** 1. Creación de un "Curricula" correctamente
	 *         Tests negativos: 8
	 *         *** 1. Intento de creación de un "Curricula" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "Curricula" con nombre de "Personal Data" vacío
	 *         *** 3. Intento de creación de un "Curricula" con estado de "Personal Data" vacío
	 *         *** 4. Intento de creación de un "Curricula" con número de teléfono de "Personal Data" vacío
	 *         *** 5. Intento de creación de un "Curricula" con enlace de GitHub de "Personal Data" vacío
	 *         *** 6. Intento de creación de un "Curricula" con enlace de GitHub de "Personal Data" que no es URL
	 *         *** 7. Intento de creación de un "Curricula" con enlace de LinkedIn de "Personal Data" vacío
	 *         *** 8. Intento de creación de un "Curricula" con enlace de LinkedIn de "Personal Data" que no es URL
	 *         Analisis de cobertura de sentencias: 100% 86/86 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */

	@Test
	public void driverCreateCurricula() {
		final Object testingData[][] = {
			{
				"hacker1", "namePersonalData", "statementPersonalData", "phoneNumberPersonalData", "http://www.gitHubProfilePersonalData.com", "http://www.linkedInPersonalData.com", null
			}, {
				"company1", "namePersonalData", "statementPersonalData", "phoneNumberPersonalData", "http://www.gitHubProfilePersonalData.com", "http://www.linkedInPersonalData.com", IllegalArgumentException.class
			}, {
				"hacker1", "", "statementPersonalData", "phoneNumberPersonalData", "http://www.gitHubProfilePersonalData.com", "http://www.linkedInPersonalData.com", ConstraintViolationException.class
			}, {
				"hacker1", "namePersonalData", "", "phoneNumberPersonalData", "http://www.gitHubProfilePersonalData.com", "http://www.linkedInPersonalData.com", ConstraintViolationException.class
			}, {
				"hacker1", "namePersonalData", "", "phoneNumberPersonalData", "http://www.gitHubProfilePersonalData.com", "http://www.linkedInPersonalData.com", ConstraintViolationException.class
			}, {
				"hacker1", "namePersonalData", "statementPersonalData", "", "http://www.gitHubProfilePersonalData.com", "http://www.linkedInPersonalData.com", ConstraintViolationException.class
			}, {
				"hacker1", "namePersonalData", "statementPersonalData", "phoneNumberPersonalData", "", "http://www.linkedInPersonalData.com", ConstraintViolationException.class
			}, {
				"hacker1", "namePersonalData", "statementPersonalData", "phoneNumberPersonalData", "gitHubProfilePersonalData", "http://www.linkedInPersonalData.com", ConstraintViolationException.class
			}, {
				"hacker1", "namePersonalData", "statementPersonalData", "phoneNumberPersonalData", "http://www.gitHubProfilePersonalData.com", "", ConstraintViolationException.class
			}, {
				"hacker1", "namePersonalData", "statementPersonalData", "phoneNumberPersonalData", "http://www.gitHubProfilePersonalData.com", "linkedInPersonalData", ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.createCurriculaTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}
	// Template methods ------------------------------------------------------

	protected void listCurriculaAuthenticatedTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Curricula> curriculas;

		super.startTransaction();

		try {
			super.authenticate(username);
			curriculas = this.curriculaService.findCurriculasByHackerLogged();
			Assert.notNull(curriculas);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void createCurriculaTemplate(final String username, final String name, final String statement, final String phoneNumber, final String gitHubProfile, final String linkedInProfile, final Class<?> expected) {
		Class<?> caught = null;
		Curricula curricula;
		PersonalData personalData;

		super.startTransaction();

		try {
			super.authenticate(username);
			curricula = this.curriculaService.create();
			personalData = curricula.getPersonalData();
			personalData.setName(name);
			personalData.setStatement(statement);
			personalData.setPhoneNumber(phoneNumber);
			personalData.setGitHubProfile(gitHubProfile);
			personalData.setLinkedInProfile(linkedInProfile);
			personalData = this.personalDataService.save(personalData);
			this.personalDataService.flush();
			this.curriculaService.save(curricula);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
}
