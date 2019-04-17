
package services;

import java.util.Calendar;
import java.util.Date;

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

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class FinderServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private FinderService	finderService;

	@PersistenceContext
	EntityManager			entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 17.2
	 *         Caso de uso: usar "Finder"
	 *         Tests positivos: 1
	 *         *** 1. Usar "Finder" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de uso de un "Finder" con una autoridad no permitida
	 *         *** 2. Intento de uso de un "Finder" con salario menor a 0
	 *         Analisis de cobertura de sentencias: 66% 101/153 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverManageFinder() {
		final Calendar cal = Calendar.getInstance();
		cal.set(2019, 10, 30);
		final Date deadline = cal.getTime();
		cal.set(2029, 10, 30);
		final Date maxDeadline = cal.getTime();
		final Object testingData[][] = {
			{
				"hacker1", "keyWordTest", deadline, 20.0, maxDeadline, null
			}, {
				"company1", "keyWordTest", deadline, 20.0, maxDeadline, IllegalArgumentException.class
			}, {
				"hacker1", "keyWordTest", deadline, -10.0, maxDeadline, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.manageFinderTemplate((String) testingData[i][0], (String) testingData[i][1], (Date) testingData[i][2], (Double) testingData[i][3], (Date) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	// Template methods ------------------------------------------------------

	protected void manageFinderTemplate(final String username, final String keyWord, final Date deadline, final Double minSalary, final Date maxDeadline, final Class<?> expected) {
		Class<?> caught = null;

		super.startTransaction();

		try {
			super.authenticate(username);
			this.finderService.updateCriteria(keyWord, deadline, minSalary, maxDeadline);
			this.finderService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
}
