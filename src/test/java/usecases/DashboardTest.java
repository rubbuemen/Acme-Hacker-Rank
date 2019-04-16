
package usecases;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import services.AdministratorService;
import services.CompanyService;
import services.HackerService;
import services.PositionService;
import utilities.AbstractTest;
import domain.Company;
import domain.Hacker;
import domain.Position;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DashboardTest extends AbstractTest {

	// SUT Services
	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private CompanyService			companyService;

	@Autowired
	private HackerService			hackerService;

	@Autowired
	private PositionService			positionService;

	@PersistenceContext
	EntityManager					entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisitos funcionales: 11.2, 18.1
	 *         Caso de uso: mostrar una "dashboard"
	 *         Tests positivos: 1
	 *         *** 1. Mostrar una "dashboard" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de muestra de una "dashboard" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 100% 180/180 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDashboard() {
		final Object testingData[][] = {
			{
				"admin", "1.2,0,3,1.1662", "1.4,0,4,1.4967", "company1,company2,company3", "hacker1,hacker2,hacker3", "482.41333333333336,142.62,800.38,251.05168462998924", "position5", "position3", "0.8,0,2,0.74833", "1.0,0,2,0.8944", "0.66667", null
			},
			{
				"company1", "1.2,0,3,1.1662", "1.4,0,4,1.4967", "company1,company2,company3", "hacker1,hacker2,hacker3", "482.41333333333336,142.62,800.38,251.05168462998924", "position5", "position3", "0.8,0,2,0.74833", "1.0,0,2,0.8944", "0.66667",
				IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.dashboardTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (String) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10], (Class<?>) testingData[i][11]);
	}

	// Template methods ------------------------------------------------------

	protected void dashboardTemplate(final String username, final String dashboardQueryC1, final String dashboardQueryC2, final String dashboardQueryC3, final String dashboardQueryC4, final String dashboardQueryC5, final String dashboardQueryC6_1,
		final String dashboardQueryC6_2, final String dashboardQueryB1, final String dashboardQueryB2, final String dashboardQueryB3, final Class<?> expected) {
		Class<?> caught = null;
		final Collection<Company> companies = new HashSet<>();
		final Collection<Hacker> hackers = new HashSet<>();

		super.startTransaction();

		try {
			super.authenticate(username);

			//Query C1
			Assert.isTrue(dashboardQueryC1.equals(this.administratorService.dashboardQueryC1()));

			//Query C2
			Assert.isTrue(dashboardQueryC2.equals(this.administratorService.dashboardQueryC2()));

			//Query C3
			final String[] queryC3Dashboard = dashboardQueryC3.split(",");
			for (final String company : queryC3Dashboard) {
				final Company c = this.companyService.findOne(super.getEntityId(company));
				companies.add(c);
			}

			Assert.isTrue(companies.containsAll(this.administratorService.dashboardQueryC3()));
			companies.clear();

			//Query C4
			final String[] queryC4Dashboard = dashboardQueryC4.split(",");
			for (final String hacker : queryC4Dashboard) {
				final Hacker h = this.hackerService.findOne(super.getEntityId(hacker));
				hackers.add(h);
			}

			Assert.isTrue(hackers.containsAll(this.administratorService.dashboardQueryC4()));
			companies.clear();

			//Query C5
			Assert.isTrue(dashboardQueryC5.equals(this.administratorService.dashboardQueryC5()));

			//Query C6_1
			final Position pos = this.positionService.findOne(super.getEntityId(dashboardQueryC6_1));
			Assert.isTrue(pos.equals(this.administratorService.dashboardQueryC6_1()));

			//Query C6_2
			final Position p = this.positionService.findOne(super.getEntityId(dashboardQueryC6_2));
			Assert.isTrue(p.equals(this.administratorService.dashboardQueryC6_2()));

			//Query B1
			Assert.isTrue(dashboardQueryB1.equals(this.administratorService.dashboardQueryB1()));

			//Query B2
			Assert.isTrue(dashboardQueryB2.equals(this.administratorService.dashboardQueryB2()));

			//Query B3
			Assert.isTrue(dashboardQueryB3.equals(this.administratorService.dashboardQueryB3()));

		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
}
