
package services;

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
import domain.MiscellaneousData;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MiscellaneousDataServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private MiscellaneousDataService	miscellaneousDataService;

	@Autowired
	private CurriculaService			curriculaService;

	@PersistenceContext
	EntityManager						entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 17.1
	 *         Caso de uso: crear un "MiscellaneousData"
	 *         Tests positivos: 1
	 *         *** 1. Crear de un "MiscellaneousData" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de creación de un "MiscellaneousData" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "MiscellaneousData" con texto vacío
	 *         Analisis de cobertura de sentencias: 100% 98/98 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCreateMiscellaneousData() {
		final Object testingData[][] = {
			{
				"hacker1", "textTest", null
			}, {
				"company1", "textTest", IllegalArgumentException.class
			}, {
				"hacker1", "", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createMiscellaneousDataTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 17.1
	 *         Caso de uso: editar un "MiscellaneousData"
	 *         Tests positivos: 1
	 *         *** 1. Editar de un "MiscellaneousData" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de edición de un "MiscellaneousData" con una autoridad no permitida
	 *         *** 2. Intento de edición de un "MiscellaneousData" que no es del "Hacker" logeado
	 *         *** 3. Intento de edición de un "MiscellaneousData" con texto vacío
	 *         *** 4. Intento de edición de un "MiscellaneousData" de un "Curricula" que es una copia
	 *         Analisis de cobertura de sentencias: 100% 82/82 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverEditMiscellaneousData() {
		final Object testingData[][] = {
			{
				"hacker1", "miscellaneousData1", "textTest", null
			}, {
				"company1", "miscellaneousData1", "textTest", IllegalArgumentException.class
			}, {
				"hacker2", "miscellaneousData1", "textTest", IllegalArgumentException.class
			}, {
				"hacker1", "miscellaneousData1", "", ConstraintViolationException.class
			}, {
				"hacker1", "miscellaneousData3", "textTest", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editMiscellaneousDataTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 17.1
	 *         Caso de uso: eliminar un "MiscellaneousData"
	 *         Tests positivos: 1
	 *         *** 1. Eliminar un "MiscellaneousData" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de eliminación de un "MiscellaneousData" con una autoridad no permitida
	 *         *** 2. Intento de eliminación de un "MiscellaneousData" que no es del "Hacker" logeado
	 *         *** 3. Intento de eliminación de un "MiscellaneousData" de un "Curricula" que es una copia
	 *         Analisis de cobertura de sentencias: 98.3% 57/58 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeleteMiscellaneousData() {
		final Object testingData[][] = {
			{
				"hacker1", "miscellaneousData1", null
			}, {
				"company1", "miscellaneousData1", IllegalArgumentException.class
			}, {
				"hacker2", "miscellaneousData1", IllegalArgumentException.class
			}, {
				"hacker1", "miscellaneousData3", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteMiscellaneousDataTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void createMiscellaneousDataTemplate(final String username, final String text, final Class<?> expected) {
		Class<?> caught = null;
		MiscellaneousData miscellaneousData;
		Curricula curriculaEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			curriculaEntity = this.curriculaService.findCurriculaHackerLogged(super.getEntityId("curricula1"));
			miscellaneousData = this.miscellaneousDataService.create();
			miscellaneousData.setText(text);
			this.miscellaneousDataService.save(miscellaneousData, curriculaEntity);
			this.miscellaneousDataService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void editMiscellaneousDataTemplate(final String username, final String miscellaneousData, final String text, final Class<?> expected) {
		Class<?> caught = null;
		MiscellaneousData miscellaneousDataEntity;
		Curricula curriculaEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			curriculaEntity = this.curriculaService.findCurriculaByMiscellaneousDataId(super.getEntityId(miscellaneousData));
			miscellaneousDataEntity = this.miscellaneousDataService.findMiscellaneousDataHackerLogged(super.getEntityId(miscellaneousData));
			miscellaneousDataEntity.setText(text);
			this.miscellaneousDataService.save(miscellaneousDataEntity, curriculaEntity);
			this.miscellaneousDataService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void deleteMiscellaneousDataTemplate(final String username, final String miscellaneousData, final Class<?> expected) {
		Class<?> caught = null;
		MiscellaneousData miscellaneousDataEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			miscellaneousDataEntity = this.miscellaneousDataService.findMiscellaneousDataHackerLogged(super.getEntityId(miscellaneousData));
			this.miscellaneousDataService.delete(miscellaneousDataEntity);
			this.miscellaneousDataService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

}
