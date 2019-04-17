/*
 * SampleTest.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package services;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.SystemConfiguration;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SystemConfigurationServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	@Autowired
	private ActorService				actorService;

	@PersistenceContext
	EntityManager						entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisitos no funcionales sobre edición de la configuración del sistema
	 *         Caso de uso: editar un "SystemConfiguration"
	 *         Tests positivos: 1
	 *         *** 1. Editar de un "SystemConfiguration" correctamente
	 *         Tests negativos: 15
	 *         *** 1. Intento de edición de "SystemConfiguration" con una autoridad no permitida
	 *         *** 2. Intento de edición de "SystemConfiguration" con nombre vacío
	 *         *** 3. Intento de edición de "SystemConfiguration" con url de banner vacío
	 *         *** 4. Intento de edición de "SystemConfiguration" con url de banner que no es URL
	 *         *** 5. Intento de edición de "SystemConfiguration" con mensaje de bienvenida en inglés vacío
	 *         *** 6. Intento de edición de "SystemConfiguration" con mensaje de bienvenida en español vacío
	 *         *** 7. Intento de edición de "SystemConfiguration" con código telefónico del país vacío
	 *         *** 8. Intento de edición de "SystemConfiguration" con código telefónico del país que no cumple patrón
	 *         *** 9. Intento de edición de "SystemConfiguration" con periodo del finder nulo
	 *         *** 10. Intento de edición de "SystemConfiguration" con periodo del finder menor a 1
	 *         *** 11. Intento de edición de "SystemConfiguration" con periodo del finder mayor a 24
	 *         *** 12. Intento de edición de "SystemConfiguration" con máximo resultados del finder nulo
	 *         *** 13. Intento de edición de "SystemConfiguration" con máximo resultados del finder menor a 1
	 *         *** 14. Intento de edición de "SystemConfiguration" con máximo resultados del finder mayor a 100
	 *         *** 15. Intento de edición de "SystemConfiguration" con palabras de spam vacío
	 *         Analisis de cobertura de sentencias: 100% 20/20 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void driverEditSystemConfiguration() {
		final Collection<String> stringValues = this.stringValues();
		final Collection<String> emptyCollection = new HashSet<>();

		final Object testingData[][] = {
			{
				"admin", "testName", "http://www.testBannerUrl.com", "testWelcomeMessageEN", "testWelcomeMessageES", "+34", 4, 5, stringValues, null
			}, {
				"hacker1", "testName", "http://www.testBannerUrl.com", "testWelcomeMessageEN", "testWelcomeMessageES", "+34", 4, 5, stringValues, IllegalArgumentException.class
			}, {
				"admin", "", "http://www.testBannerUrl.com", "testWelcomeMessageEN", "testWelcomeMessageES", "+34", 4, 5, stringValues, ConstraintViolationException.class
			}, {
				"admin", "testName", "", "testWelcomeMessageEN", "testWelcomeMessageES", "+34", 4, 5, stringValues, ConstraintViolationException.class
			}, {
				"admin", "testName", "test", "testWelcomeMessageEN", "testWelcomeMessageES", "+34", 4, 5, stringValues, ConstraintViolationException.class
			}, {
				"admin", "testName", "http://www.testBannerUrl.com", "", "testWelcomeMessageES", "+34", 4, 5, stringValues, ConstraintViolationException.class
			}, {
				"admin", "testName", "http://www.testBannerUrl.com", "testWelcomeMessageEN", "", "+34", 4, 5, stringValues, ConstraintViolationException.class
			}, {
				"admin", "testName", "http://www.testBannerUrl.com", "testWelcomeMessageEN", "testWelcomeMessageES", "", 4, 5, stringValues, ConstraintViolationException.class
			}, {
				"admin", "testName", "http://www.testBannerUrl.com", "testWelcomeMessageEN", "testWelcomeMessageES", "test", 4, 5, stringValues, ConstraintViolationException.class
			}, {
				"admin", "testName", "http://www.testBannerUrl.com", "testWelcomeMessageEN", "testWelcomeMessageES", "+34", null, 5, stringValues, ConstraintViolationException.class
			}, {
				"admin", "testName", "http://www.testBannerUrl.com", "testWelcomeMessageEN", "testWelcomeMessageES", "+34", 0, 5, stringValues, ConstraintViolationException.class
			}, {
				"admin", "testName", "http://www.testBannerUrl.com", "testWelcomeMessageEN", "testWelcomeMessageES", "+34", 25, 5, stringValues, ConstraintViolationException.class
			}, {
				"admin", "testName", "http://www.testBannerUrl.com", "testWelcomeMessageEN", "testWelcomeMessageES", "+34", 4, null, stringValues, ConstraintViolationException.class
			}, {
				"admin", "testName", "http://www.testBannerUrl.com", "testWelcomeMessageEN", "testWelcomeMessageES", "+34", 4, 0, stringValues, ConstraintViolationException.class
			}, {
				"admin", "testName", "http://www.testBannerUrl.com", "testWelcomeMessageEN", "testWelcomeMessageES", "+34", 4, 101, stringValues, ConstraintViolationException.class
			}, {
				"admin", "testName", "http://www.testBannerUrl.com", "testWelcomeMessageEN", "testWelcomeMessageES", "+34", 4, 5, emptyCollection, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editSystemConfigurationTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Integer) testingData[i][6],
				(Integer) testingData[i][7], (Collection<String>) testingData[i][8], (Class<?>) testingData[i][9]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 24.2
	 *         Caso de uso: marcar "Actors" como spammers lanzando un proceso
	 *         Tests positivos: 1
	 *         *** 1. Marcar "Actors" como spammers correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de marcado de "Actors" como spammers con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 100% 50/50 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverFlagSpammersProcess() {
		final Object testingData[][] = {
			{
				"admin", null
			}, {
				"hacker1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.flagSpammersProcessTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 24.3 (Acme-Madrugá)
	 *         Caso de uso: banear un "Actor"
	 *         Tests positivos: 1
	 *         *** 1. Banear un "Actor" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de banear un "Actor" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 100% 21/21 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverBanActor() {
		final Object testingData[][] = {
			{
				"admin", "company2", null
			}, {
				"hacker1", "company2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.banActorTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 24.4
	 *         Caso de uso: desbanear un "Actor"
	 *         Tests positivos: 1
	 *         *** 1. Desbanear un "Actor" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de desbanear un "Actor" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 100% 21/21 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverUnbanActor() {
		final Object testingData[][] = {
			{
				"admin", "company2", null
			}, {
				"hacker1", "company2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.unbanActorTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void editSystemConfigurationTemplate(final String username, final String nameSystem, final String bannerUrl, final String welcomeMessageEnglish, final String welcomeMessageSpanish, final String phoneCountryCode, final Integer periodFinder,
		final Integer maxResultsFinder, final Collection<String> spamWords, final Class<?> expected) {
		Class<?> caught = null;
		SystemConfiguration systemConfiguration;

		super.startTransaction();

		try {
			super.authenticate(username);
			systemConfiguration = this.systemConfigurationService.getConfiguration();
			systemConfiguration.setNameSystem(nameSystem);
			systemConfiguration.setBannerUrl(bannerUrl);
			systemConfiguration.setWelcomeMessageEnglish(welcomeMessageEnglish);
			systemConfiguration.setWelcomeMessageSpanish(welcomeMessageSpanish);
			systemConfiguration.setPhoneCountryCode(phoneCountryCode);
			systemConfiguration.setPeriodFinder(periodFinder);
			systemConfiguration.setMaxResultsFinder(maxResultsFinder);
			systemConfiguration.setSpamWords(spamWords);
			this.systemConfigurationService.save(systemConfiguration);
			this.systemConfigurationService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void flagSpammersProcessTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;

		super.startTransaction();

		try {
			super.authenticate(username);
			this.systemConfigurationService.computeSpammers();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void banActorTemplate(final String username, final String actor, final Class<?> expected) {
		Class<?> caught = null;

		super.startTransaction();

		try {
			super.authenticate(username);
			this.actorService.banActor(this.actorService.findOne(super.getEntityId(actor)));
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void unbanActorTemplate(final String username, final String actor, final Class<?> expected) {
		Class<?> caught = null;

		super.startTransaction();

		try {
			super.authenticate(username);
			this.actorService.unbanActor(this.actorService.findOne(super.getEntityId(actor)));
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	// Auxiliar methods ------------------------------------------------------

	private Collection<String> stringValues() {
		final Collection<String> stringValues = new HashSet<>();
		stringValues.add("test1");
		stringValues.add("test2");
		stringValues.add("test3");
		stringValues.add("test4");
		stringValues.add("test5");

		return stringValues;
	}
}
