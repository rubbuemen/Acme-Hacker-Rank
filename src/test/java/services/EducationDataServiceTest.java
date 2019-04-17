
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
import domain.EducationData;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class EducationDataServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private EducationDataService	educationDataService;

	@Autowired
	private CurriculaService		curriculaService;

	@PersistenceContext
	EntityManager					entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 17.1
	 *         Caso de uso: crear un "EducationData"
	 *         Tests positivos: 1
	 *         *** 1. Crear de un "EducationData" correctamente
	 *         Tests negativos: 10
	 *         *** 1. Intento de creación de un "EducationData" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "EducationData" con grado vacío
	 *         *** 3. Intento de creación de un "EducationData" con institución vacía
	 *         *** 4. Intento de creación de un "EducationData" con nota nula
	 *         *** 5. Intento de creación de un "EducationData" con nota menor a 0
	 *         *** 6. Intento de creación de un "EducationData" con nota mayor a 10
	 *         *** 7. Intento de creación de un "EducationData" con fecha de comienzo nulo
	 *         *** 8. Intento de creación de un "EducationData" con fecha de comienzo futuro
	 *         *** 9. Intento de creación de un "EducationData" con fecha de finalización futuro
	 *         *** 10. Intento de creación de un "EducationData" con fecha de comienzo posterior al de finalización
	 *         Analisis de cobertura de sentencias: 97,9% 111/113 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCreateEducationData() {
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
				"hacker1", "degreeTest", "institutionTest", 10.0, startDatePast, endDatePast, null
			}, {
				"company1", "degreeTest", "institutionTest", 10.0, startDatePast, endDatePast, IllegalArgumentException.class
			}, {
				"hacker1", "", "institutionTest", 10.0, startDatePast, endDatePast, ConstraintViolationException.class
			}, {
				"hacker1", "degreeTest", "", 10.0, startDatePast, endDatePast, ConstraintViolationException.class
			}, {
				"hacker1", "degreeTest", "institutionTest", null, startDatePast, endDatePast, ConstraintViolationException.class
			}, {
				"hacker1", "degreeTest", "institutionTest", -1.0, startDatePast, endDatePast, ConstraintViolationException.class
			}, {
				"hacker1", "degreeTest", "institutionTest", 11.0, startDatePast, endDatePast, ConstraintViolationException.class
			}, {
				"hacker1", "degreeTest", "institutionTest", 10.0, null, endDatePast, ConstraintViolationException.class
			}, {
				"hacker1", "degreeTest", "institutionTest", 10.0, startDateFuture, null, ConstraintViolationException.class
			}, {
				"hacker1", "degreeTest", "institutionTest", 10.0, startDatePast, endDateFuture, ConstraintViolationException.class
			}, {
				"hacker1", "degreeTest", "institutionTest", 10.0, endDatePast, startDatePast, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createEducationDataTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Double) testingData[i][3], (Date) testingData[i][4], (Date) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 17.1
	 *         Caso de uso: editar un "EducationData"
	 *         Tests positivos: 1
	 *         *** 1. Editar de un "EducationData" correctamente
	 *         Tests negativos: 12
	 *         *** 1. Intento de edición de un "EducationData" con una autoridad no permitida
	 *         *** 2. Intento de edición de un "EducationData" que no es del "Hacker" logeado
	 *         *** 3. Intento de edición de un "EducationData" con grado vacío
	 *         *** 4. Intento de edición de un "EducationData" con institución vacía
	 *         *** 5. Intento de edición de un "EducationData" con nota nula
	 *         *** 6. Intento de edición de un "EducationData" con nota menor a 0
	 *         *** 7. Intento de edición de un "EducationData" con nota mayor a 10
	 *         *** 8. Intento de edición de un "EducationData" con fecha de comienzo nulo
	 *         *** 9. Intento de edición de un "EducationData" con fecha de comienzo futuro
	 *         *** 10. Intento de edición de un "EducationData" con fecha de finalización futuro
	 *         *** 11. Intento de edición de un "EducationData" con fecha de comienzo posterior al de finalización
	 *         *** 12. Intento de edición de un "EducationData" de un "Curricula" que es una copia
	 *         Analisis de cobertura de sentencias: 97,9% 95/97 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverEditEducationData() {
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
				"hacker1", "educationData1", "degreeTest", "institutionTest", 10.0, startDatePast, endDatePast, null
			}, {
				"company1", "educationData1", "degreeTest", "institutionTest", 10.0, startDatePast, endDatePast, IllegalArgumentException.class
			}, {
				"hacker2", "educationData1", "degreeTest", "institutionTest", 10.0, startDatePast, endDatePast, IllegalArgumentException.class
			}, {
				"hacker1", "educationData1", "", "institutionTest", 10.0, startDatePast, endDatePast, ConstraintViolationException.class
			}, {
				"hacker1", "educationData1", "degreeTest", "", 10.0, startDatePast, endDatePast, ConstraintViolationException.class
			}, {
				"hacker1", "educationData1", "degreeTest", "institutionTest", null, startDatePast, endDatePast, ConstraintViolationException.class
			}, {
				"hacker1", "educationData1", "degreeTest", "institutionTest", -1.0, startDatePast, endDatePast, ConstraintViolationException.class
			}, {
				"hacker1", "educationData1", "degreeTest", "institutionTest", 11.0, startDatePast, endDatePast, ConstraintViolationException.class
			}, {
				"hacker1", "educationData1", "degreeTest", "institutionTest", 10.0, null, endDatePast, ConstraintViolationException.class
			}, {
				"hacker1", "educationData1", "degreeTest", "institutionTest", 10.0, startDateFuture, null, ConstraintViolationException.class
			}, {
				"hacker1", "educationData1", "degreeTest", "institutionTest", 10.0, startDatePast, endDateFuture, ConstraintViolationException.class
			}, {
				"hacker1", "educationData1", "degreeTest", "institutionTest", 10.0, endDatePast, startDatePast, IllegalArgumentException.class
			}, {
				"hacker1", "educationData3", "degreeTest", "institutionTest", 10.0, endDatePast, startDatePast, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editEducationDataTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Double) testingData[i][4], (Date) testingData[i][5], (Date) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 17.1
	 *         Caso de uso: eliminar un "EducationData"
	 *         Tests positivos: 1
	 *         *** 1. Eliminar un "EducationData" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de eliminación de un "EducationData" con una autoridad no permitida
	 *         *** 2. Intento de eliminación de un "EducationData" que no es del "Hacker" logeado
	 *         *** 3. Intento de eliminación de un "EducationData" de un "Curricula" que es una copia
	 *         Analisis de cobertura de sentencias: 98,3% 57/58 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeleteEducationData() {
		final Object testingData[][] = {
			{
				"hacker1", "educationData1", null
			}, {
				"company1", "educationData1", IllegalArgumentException.class
			}, {
				"hacker2", "educationData1", IllegalArgumentException.class
			}, {
				"hacker1", "educationData3", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteEducationDataTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void createEducationDataTemplate(final String username, final String degree, final String institution, final Double mark, final Date startDate, final Date endDate, final Class<?> expected) {
		Class<?> caught = null;
		EducationData educationData;
		Curricula curriculaEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			curriculaEntity = this.curriculaService.findCurriculaHackerLogged(super.getEntityId("curricula1"));
			educationData = this.educationDataService.create();
			educationData.setDegree(degree);
			educationData.setInstitution(institution);
			educationData.setMark(mark);
			educationData.setStartDate(startDate);
			educationData.setEndDate(endDate);
			this.educationDataService.save(educationData, curriculaEntity);
			this.educationDataService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void editEducationDataTemplate(final String username, final String educationData, final String degree, final String institution, final Double mark, final Date startDate, final Date endDate, final Class<?> expected) {
		Class<?> caught = null;
		EducationData educationDataEntity;
		Curricula curriculaEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			curriculaEntity = this.curriculaService.findCurriculaByEducationDataId(super.getEntityId(educationData));
			educationDataEntity = this.educationDataService.findEducationDataHackerLogged(super.getEntityId(educationData));
			educationDataEntity.setDegree(degree);
			educationDataEntity.setInstitution(institution);
			educationDataEntity.setMark(mark);
			educationDataEntity.setStartDate(startDate);
			educationDataEntity.setEndDate(endDate);
			this.educationDataService.save(educationDataEntity, curriculaEntity);
			this.educationDataService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void deleteEducationDataTemplate(final String username, final String educationData, final Class<?> expected) {
		Class<?> caught = null;
		EducationData educationDataEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			educationDataEntity = this.educationDataService.findEducationDataHackerLogged(super.getEntityId(educationData));
			this.educationDataService.delete(educationDataEntity);
			this.educationDataService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

}
