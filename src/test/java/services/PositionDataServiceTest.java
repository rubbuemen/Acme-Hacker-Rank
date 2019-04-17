
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
import domain.Curricula;
import domain.PositionData;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PositionDataServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private PositionDataService	positionDataService;

	@Autowired
	private CurriculaService	curriculaService;

	@PersistenceContext
	EntityManager				entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 17.1
	 *         Caso de uso: crear un "PositionData"
	 *         Tests positivos: 1
	 *         *** 1. Crear de un "PositionData" correctamente
	 *         Tests negativos: 7
	 *         *** 1. Intento de creación de un "PositionData" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "PositionData" con título vacío
	 *         *** 3. Intento de creación de un "PositionData" con descripción vacía
	 *         *** 4. Intento de creación de un "PositionData" con fecha de comienzo nulo
	 *         *** 5. Intento de creación de un "PositionData" con fecha de comienzo futuro
	 *         *** 6. Intento de creación de un "PositionData" con fecha de finalización futuro
	 *         *** 7. Intento de creación de un "PositionData" con fecha de comienzo posterior al de finalización
	 *         Analisis de cobertura de sentencias: 97,9% 111/113 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCreatePositionData() {
		final Calendar cal = Calendar.getInstance();
		cal.set(2000, 9, 30);
		final Date startDatePast = cal.getTime();
		cal.set(2200, 9, 30);
		final Date startDateFuture = cal.getTime();
		cal.set(2001, 9, 30);
		final Date endDatePast = cal.getTime();
		cal.set(2201, 9, 30);
		final Date endDateFuture = cal.getTime();
		final Object testingData[][] = {
			{
				"hacker1", "titleTest", "descriptionTest", startDatePast, endDatePast, null
			}, {
				"company1", "titleTest", "descriptionTest", startDatePast, endDatePast, IllegalArgumentException.class
			}, {
				"hacker1", "", "descriptionTest", startDatePast, endDatePast, ConstraintViolationException.class
			}, {
				"hacker1", "titleTest", "", startDatePast, endDatePast, ConstraintViolationException.class
			}, {
				"hacker1", "titleTest", "descriptionTest", null, endDatePast, ConstraintViolationException.class
			}, {
				"hacker1", "titleTest", "descriptionTest", startDateFuture, null, ConstraintViolationException.class
			}, {
				"hacker1", "titleTest", "descriptionTest", startDatePast, endDateFuture, ConstraintViolationException.class
			}, {
				"hacker1", "titleTest", "descriptionTest", endDatePast, startDatePast, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createPositionDataTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Date) testingData[i][3], (Date) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 17.1
	 *         Caso de uso: editar un "PositionData"
	 *         Tests positivos: 1
	 *         *** 1. Editar de un "PositionData" correctamente
	 *         Tests negativos: 9
	 *         *** 1. Intento de edición de un "PositionData" con una autoridad no permitida
	 *         *** 2. Intento de edición de un "PositionData" que no es del "Hacker" logeado
	 *         *** 3. Intento de edición de un "PositionData" con título vacío
	 *         *** 4. Intento de edición de un "PositionData" con descripción vacía
	 *         *** 5. Intento de edición de un "PositionData" con fecha de comienzo nulo
	 *         *** 6. Intento de edición de un "PositionData" con fecha de comienzo futuro
	 *         *** 7. Intento de edición de un "PositionData" con fecha de finalización futuro
	 *         *** 8. Intento de edición de un "PositionData" con fecha de comienzo posterior al de finalización
	 *         *** 9. Intento de edición de un "PositionData" de un "Curricula" que es una copia
	 *         Analisis de cobertura de sentencias: 97,9% 95/97 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverEditPositionData() {
		final Calendar cal = Calendar.getInstance();
		cal.set(2000, 9, 30);
		final Date startDatePast = cal.getTime();
		cal.set(2200, 9, 30);
		final Date startDateFuture = cal.getTime();
		cal.set(2001, 9, 30);
		final Date endDatePast = cal.getTime();
		cal.set(2201, 9, 30);
		final Date endDateFuture = cal.getTime();
		final Object testingData[][] = {
			{
				"hacker1", "positionData1", "titleTest", "descriptionTest", startDatePast, endDatePast, null
			}, {
				"company1", "positionData1", "titleTest", "descriptionTest", startDatePast, endDatePast, IllegalArgumentException.class
			}, {
				"hacker2", "positionData1", "titleTest", "descriptionTest", startDatePast, endDatePast, IllegalArgumentException.class
			}, {
				"hacker1", "positionData1", "", "descriptionTest", startDatePast, endDatePast, ConstraintViolationException.class
			}, {
				"hacker1", "positionData1", "titleTest", "", startDatePast, endDatePast, ConstraintViolationException.class
			}, {
				"hacker1", "positionData1", "titleTest", "descriptionTest", null, endDatePast, ConstraintViolationException.class
			}, {
				"hacker1", "positionData1", "titleTest", "descriptionTest", startDateFuture, null, ConstraintViolationException.class
			}, {
				"hacker1", "positionData1", "titleTest", "descriptionTest", startDatePast, endDateFuture, ConstraintViolationException.class
			}, {
				"hacker1", "positionData1", "titleTest", "descriptionTest", endDatePast, startDatePast, IllegalArgumentException.class
			}, {
				"hacker1", "positionData3", "titleTest", "descriptionTest", endDatePast, startDatePast, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editPositionDataTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Date) testingData[i][4], (Date) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 17.1
	 *         Caso de uso: eliminar un "PositionData"
	 *         Tests positivos: 1
	 *         *** 1. Eliminar un "PositionData" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de eliminación de un "PositionData" con una autoridad no permitida
	 *         *** 2. Intento de eliminación de un "PositionData" que no es del "Hacker" logeado
	 *         *** 3. Intento de eliminación de un "PositionData" de un "Curricula" que es una copia
	 *         Analisis de cobertura de sentencias: 98,3% 57/58 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeletePositionData() {
		final Object testingData[][] = {
			{
				"hacker1", "positionData1", null
			}, {
				"company1", "positionData1", IllegalArgumentException.class
			}, {
				"hacker2", "positionData1", IllegalArgumentException.class
			}, {
				"hacker1", "positionData3", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deletePositionDataTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void createPositionDataTemplate(final String username, final String title, final String description, final Date startDate, final Date endDate, final Class<?> expected) {
		Class<?> caught = null;
		PositionData positionData;
		Curricula curriculaEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			curriculaEntity = this.curriculaService.findCurriculaHackerLogged(super.getEntityId("curricula1"));
			positionData = this.positionDataService.create();
			positionData.setTitle(title);
			positionData.setDescription(description);
			positionData.setStartDate(startDate);
			positionData.setEndDate(endDate);
			this.positionDataService.save(positionData, curriculaEntity);
			this.positionDataService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void editPositionDataTemplate(final String username, final String positionData, final String title, final String description, final Date startDate, final Date endDate, final Class<?> expected) {
		Class<?> caught = null;
		PositionData positionDataEntity;
		Curricula curriculaEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			curriculaEntity = this.curriculaService.findCurriculaByPositionDataId(super.getEntityId(positionData));
			positionDataEntity = this.positionDataService.findPositionDataHackerLogged(super.getEntityId(positionData));
			positionDataEntity.setTitle(title);
			positionDataEntity.setDescription(description);
			positionDataEntity.setStartDate(startDate);
			positionDataEntity.setEndDate(endDate);
			this.positionDataService.save(positionDataEntity, curriculaEntity);
			this.positionDataService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void deletePositionDataTemplate(final String username, final String positionData, final Class<?> expected) {
		Class<?> caught = null;
		PositionData positionDataEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			positionDataEntity = this.positionDataService.findPositionDataHackerLogged(super.getEntityId(positionData));
			this.positionDataService.delete(positionDataEntity);
			this.positionDataService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

}
