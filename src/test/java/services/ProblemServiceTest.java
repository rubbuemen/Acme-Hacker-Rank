
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
import domain.Problem;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ProblemServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private ProblemService	problemService;

	@PersistenceContext
	EntityManager			entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 9.2
	 *         Caso de uso: listar "Problems"
	 *         Tests positivos: 1
	 *         *** 1. Listar "Problems" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de listar "Problems" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 100% 20/20 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListProblemsCompany() {
		final Object testingData[][] = {
			{
				"company1", null
			}, {
				"hacker1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listProblemsCompanyTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 9.2
	 *         Caso de uso: crear un "Problem"
	 *         Tests positivos: 1
	 *         *** 1. Crear un "Problem" correctamente
	 *         Tests negativos: 4
	 *         *** 1. Intento de creación de un "Problem" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "Problem" con título vacío
	 *         *** 3. Intento de creación de un "Problem" con estado vacío
	 *         *** 4. Intento de creación de un "Problem" que está en final mode
	 *         Analisis de cobertura de sentencias: 100% 75/75 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCreateProblem() {
		final Object testingData[][] = {
			{
				"company1", "titleTest", "statementTest", false, null
			}, {
				"hacker1", "titleTest", "statementTest", false, IllegalArgumentException.class
			}, {
				"company1", "", "statementTest", false, ConstraintViolationException.class
			}, {
				"company1", "titleTest", "", false, ConstraintViolationException.class
			}, {
				"company1", "titleTest", "statementTest", true, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createProblemTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 9.2
	 *         Caso de uso: editar un "Problem"
	 *         Tests positivos: 1
	 *         *** 1. Editar un "Problem" correctamente
	 *         Tests negativos: 5
	 *         *** 1. Intento de edición de un "Problem" con una autoridad no permitida
	 *         *** 2. Intento de edición de un "Problem" que no es de la "Company" logeada
	 *         *** 3. Intento de edición de un "Problem" con título vacío
	 *         *** 4. Intento de edición de un "Problem" con estado vacío
	 *         *** 5. Intento de edición de un "Problem" que está en final mode
	 *         Analisis de cobertura de sentencias: 100% 75/75 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverEditProblem() {
		final Object testingData[][] = {
			{
				"company1", "problem1", "titleTest", "statementTest", false, null
			}, {
				"hacker1", "problem1", "titleTest", "statementTest", false, IllegalArgumentException.class
			}, {
				"company2", "problem1", "titleTest", "statementTest", false, IllegalArgumentException.class
			}, {
				"company1", "problem1", "", "statementTest", false, ConstraintViolationException.class
			}, {
				"company1", "problem1", "titleTest", "", false, ConstraintViolationException.class
			}, {
				"company1", "problem1", "titleTest", "statementTest", true, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editProblemTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Boolean) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 9.2
	 *         Caso de uso: eliminar un "Problem"
	 *         Tests positivos: 1
	 *         *** 1. Eliminar un "Problem" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de eliminación de un "Problem" con una autoridad no permitida
	 *         *** 2. Intento de eliminación de un "Problem" que no es de la "Company" logeada
	 *         *** 3. Intento de eliminación de un "Problem" que está en modo final
	 *         Analisis de cobertura de sentencias: 99% 98/99 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeleteProblem() {
		final Object testingData[][] = {
			{
				"company4", "problem8", null
			}, {
				"hacker1", "problem8", IllegalArgumentException.class
			}, {
				"company1", "problem8", IllegalArgumentException.class
			}, {
				"company1", "problem1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteProblemTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 9.2
	 *         Caso de uso: cambiar a modo final un "Problem"
	 *         Tests positivos: 1
	 *         *** 1. Cambiar a modo final un "Problem" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de cambiar a modo final un "Problem" con una autoridad no permitida
	 *         *** 2. Intento de cambiar a modo final un "Problem" que no es de la "Company" logeada
	 *         *** 3. Intento de cambiar a modo final un "Problem" que ya está en modo final
	 *         Analisis de cobertura de sentencias: 97,5% 39/40 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverChangeFinalModeProblem() {
		final Object testingData[][] = {
			{
				"company4", "problem8", null
			}, {
				"hacker1", "problem8", IllegalArgumentException.class
			}, {
				"company1", "problem8", IllegalArgumentException.class
			}, {
				"company1", "problem1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.changeFinalModeProblemTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void listProblemsCompanyTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Problem> problems;

		super.startTransaction();

		try {
			super.authenticate(username);
			problems = this.problemService.findProblemsByCompanyLogged();
			Assert.notNull(problems);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void createProblemTemplate(final String username, final String title, final String statement, final Boolean isFinalMode, final Class<?> expected) {
		Class<?> caught = null;
		Problem problemEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			problemEntity = this.problemService.create();
			problemEntity.setTitle(title);
			problemEntity.setStatement(statement);
			problemEntity.setIsFinalMode(isFinalMode);
			this.problemService.save(problemEntity);
			this.problemService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void editProblemTemplate(final String username, final String problem, final String title, final String statement, final Boolean isFinalMode, final Class<?> expected) {
		Class<?> caught = null;
		Problem problemEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			problemEntity = this.problemService.findProblemCompanyLogged(super.getEntityId(problem));
			problemEntity.setTitle(title);
			problemEntity.setStatement(statement);
			problemEntity.setIsFinalMode(isFinalMode);
			this.problemService.save(problemEntity);
			this.problemService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void deleteProblemTemplate(final String username, final String problem, final Class<?> expected) {
		Class<?> caught = null;
		Problem problemEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			problemEntity = this.problemService.findProblemCompanyLogged(super.getEntityId(problem));
			this.problemService.delete(problemEntity);
			this.problemService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void changeFinalModeProblemTemplate(final String username, final String problem, final Class<?> expected) {
		Class<?> caught = null;
		Problem problemEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			problemEntity = this.problemService.findProblemCompanyLogged(super.getEntityId(problem));
			this.problemService.changeFinalMode(problemEntity);
			this.problemService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
}
