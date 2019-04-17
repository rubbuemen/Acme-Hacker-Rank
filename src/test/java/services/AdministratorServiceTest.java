
package services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Administrator;
import domain.CreditCard;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class AdministratorServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private AdministratorService	administratorService;

	@PersistenceContext
	EntityManager					entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 11.1
	 *         Caso de uso: registrar un "Administrator" en el sistema
	 *         Tests positivos: 1
	 *         *** 1. Registrar un "Administrator" correctamente
	 *         Tests negativos: 25
	 *         *** 1. Intento de registro de un "Administrator" con una autoridad no permitida
	 *         *** 2. Intento de registrar un "Administrator" con el nombre vacío
	 *         *** 3. Intento de registrar un "Administrator" con los apellidos vacíos
	 *         *** 4. Intento de registrar un "Administrator" con el número de VAT vacío
	 *         *** 5. Intento de registrar un "Administrator" cuya "CreditCard" tiene el propietario vacío
	 *         *** 6. Intento de registrar un "Administrator" cuya "CreditCard" tiene la marca vacío
	 *         *** 7. Intento de registrar un "Administrator" cuya "CreditCard" tiene el número vacío
	 *         *** 8. Intento de registrar un "Administrator" cuya "CreditCard" tiene un número que no es de tarjeta de crédito
	 *         *** 9. Intento de registrar un "Administrator" cuya "CreditCard" tiene el mes de caducidad nulo
	 *         *** 10. Intento de registrar un "Administrator" cuya "CreditCard" tiene un mes de caducidad menor a 1
	 *         *** 11. Intento de registrar un "Administrator" cuya "CreditCard" tiene un mes de caducidad mayor a 12
	 *         *** 12. Intento de registrar un "Administrator" cuya "CreditCard" tiene el año de caducidad nulo
	 *         *** 13. Intento de registrar un "Administrator" cuya "CreditCard" tiene un año de caducidad menor a 0
	 *         *** 14. Intento de registrar un "Administrator" cuya "CreditCard" tiene un año de caducidad mayor a 99
	 *         *** 15. Intento de registrar un "Administrator" cuya "CreditCard" tiene el CVV nulo
	 *         *** 16. Intento de registrar un "Administrator" cuya "CreditCard" tiene un CVV menor a 100
	 *         *** 17. Intento de registrar un "Administrator" cuya "CreditCard" tiene un CVV mayor a 999
	 *         *** 18. Intento de registrar un "Administrator" con el email vacío
	 *         *** 19. Intento de registrar un "Administrator" con el email sin cumplir el patrón adecuado
	 *         *** 20. Intento de registrar un "Administrator" con el usuario vacío
	 *         *** 21. Intento de registrar un "Administrator" con tamaño del usuario menor a 5 caracteres
	 *         *** 22. Intento de registrar un "Administrator" con tamaño del usuario mayor a 32 caracteres
	 *         *** 23. Intento de registrar un "Administrator" usuario ya usado
	 *         *** 24. Intento de registrar un "Administrator" cuya "CreditCard" tiene un número no numérico
	 *         *** 25. Intento de registrar un "Administrator" cuya "CreditCard" está caducada
	 *         Analisis de cobertura de sentencias: 100% 161/161 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverRegisterAdministrator() {

		final Object testingData[][] = {
			{
				"admin", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, "testEmail@testemail.com", "testUser", "testPass", null
			},
			{
				"company1", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, "testEmail@testemail.com", "testUser", "testPass", IllegalArgumentException.class
			},
			{
				"admin", "", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, "testEmail@testemail.com", "testUser", "testPass", ConstraintViolationException.class
			},
			{
				"admin", "testName", "", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, "testEmail@testemail.com", "testUser", "testPass", ConstraintViolationException.class
			},
			{
				"admin", "testName", "testSurnames", "", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, "testEmail@testemail.com", "testUser", "testPass", ConstraintViolationException.class
			},
			{
				"admin", "testName", "testSurnames", "testVatNumber", "", "VISA", "4739158676192764", 10, 25, 535, "testEmail@testemail.com", "testUser", "testPass", ConstraintViolationException.class
			},
			{
				"admin", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "", "4739158676192764", 10, 25, 535, "testEmail@testemail.com", "testUser", "testPass", ConstraintViolationException.class
			},
			{
				"admin", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "", 10, 25, 535, "testEmail@testemail.com", "testUser", "testPass", ConstraintViolationException.class
			},
			{
				"admin", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "8534634734746", 10, 25, 535, "testEmail@testemail.com", "testUser", "testPass", ConstraintViolationException.class
			},
			{
				"admin", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", null, 25, 535, "testEmail@testemail.com", "testUser", "testPass", ConstraintViolationException.class
			},
			{
				"admin", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 0, 25, 535, "testEmail@testemail.com", "testUser", "testPass", ConstraintViolationException.class
			},
			{
				"admin", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 13, 25, 535, "testEmail@testemail.com", "testUser", "testPass", ConstraintViolationException.class
			},
			{
				"admin", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, null, 535, "testEmail@testemail.com", "testUser", "testPass", ConstraintViolationException.class
			},
			{
				"admin", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, -1, 535, "testEmail@testemail.com", "testUser", "testPass", ConstraintViolationException.class
			},
			{
				"admin", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, 100, 535, "testEmail@testemail.com", "testUser", "testPass", ConstraintViolationException.class
			},
			{
				"admin", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, null, "testEmail@testemail.com", "testUser", "testPass", ConstraintViolationException.class
			},
			{
				"admin", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 99, "testEmail@testemail.com", "testUser", "testPass", ConstraintViolationException.class
			},
			{
				"admin", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 1000, "testEmail@testemail.com", "testUser", "testPass", ConstraintViolationException.class
			},
			{
				"admin", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, "", "testUser", "testPass", ConstraintViolationException.class
			},
			{
				"admin", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, "testEmail", "testUser", "testPass", ConstraintViolationException.class
			},
			{
				"admin", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, "testEmail@testemail.com", "", "testPass", ConstraintViolationException.class
			},
			{
				"admin", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, "testEmail@testemail.com", "test", "testPass", ConstraintViolationException.class
			},
			{
				"admin", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, "testEmail@testemail.com", "testUsertestUsertestUsertestUsertestUsertestUsertestUsertestUser", "testPass",
				ConstraintViolationException.class
			}, {
				"admin", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, "testEmail@testemail.com", "admin", "testPass", DataIntegrityViolationException.class
			}, {
				"admin", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "testCreditCardNumber", 10, 25, 535, "testEmail@testemail.com", "testUser", "testPass", IllegalArgumentException.class
			}, {
				"admin", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 2, 19, 535, "testEmail@testemail.com", "testUser", "testPass", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.registerAdministratorTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Integer) testingData[i][7], (Integer) testingData[i][8], (Integer) testingData[i][9], (String) testingData[i][10], (String) testingData[i][11], (String) testingData[i][12], (Class<?>) testingData[i][13]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 8.2
	 *         Caso de uso: editar sus datos estando logeado
	 *         Tests positivos: 1
	 *         *** 1. Editar sus datos correctamente
	 *         Tests negativos: 21
	 *         *** 1. Intento de edición de datos de un actor que no es el logeado
	 *         *** 2. Intento de edición como "Administrator" con el nombre vacío
	 *         *** 3. Intento de edición como "Administrator" con los apellidos vacíos
	 *         *** 4. Intento de edición como "Administrator" con el número de VAT vacío
	 *         *** 5. Intento de edición como "Administrator" cuya "CreditCard" tiene el propietario vacío
	 *         *** 6. Intento de edición como "Administrator" cuya "CreditCard" tiene la marca vacío
	 *         *** 7. Intento de edición como "Administrator" cuya "CreditCard" tiene el número vacío
	 *         *** 8. Intento de edición como "Administrator" cuya "CreditCard" tiene un número que no es de tarjeta de crédito
	 *         *** 9. Intento de edición como "Administrator" cuya "CreditCard" tiene el mes de caducidad nulo
	 *         *** 10. Intento de edición como "Administrator" cuya "CreditCard" tiene un mes de caducidad menor a 1
	 *         *** 11. Intento de edición como "Administrator" cuya "CreditCard" tiene un mes de caducidad mayor a 12
	 *         *** 12. Intento de edición como "Administrator" cuya "CreditCard" tiene el año de caducidad nulo
	 *         *** 13. Intento de edición como "Administrator" cuya "CreditCard" tiene un año de caducidad menor a 0
	 *         *** 14. Intento de edición como "Administrator" cuya "CreditCard" tiene un año de caducidad mayor a 99
	 *         *** 15. Intento de edición como "Administrator" cuya "CreditCard" tiene el CVV nulo
	 *         *** 16. Intento de edición como "Administrator" cuya "CreditCard" tiene un CVV menor a 100
	 *         *** 17. Intento de edición como "Administrator" cuya "CreditCard" tiene un CVV mayor a 999
	 *         *** 18. Intento de edición como "Administrator" con el email vacío
	 *         *** 19. Intento de edición como "Administrator" con el email sin cumplir el patrón adecuado
	 *         *** 20. Intento de edición como "Administrator" cuya "CreditCard" tiene un número no numérico
	 *         *** 21. Intento de edición como "Administrator" cuya "CreditCard" está caducada
	 *         Analisis de cobertura de sentencias: 100% 128/128 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverEditData() {
		final Object testingData[][] = {
			{
				"admin", "administrator1", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, "testEmail@testemail.com", null
			}, {
				"admin", "company1", "", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, "testEmail@testemail.com", IllegalArgumentException.class
			}, {
				"admin", "administrator1", "testName", "", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, "testEmail@testemail.com", ConstraintViolationException.class
			}, {
				"admin", "administrator1", "testName", "testSurnames", "", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, "testEmail@testemail.com", ConstraintViolationException.class
			}, {
				"admin", "administrator1", "testName", "testSurnames", "testVatNumber", "", "VISA", "4739158676192764", 10, 25, 535, "testEmail@testemail.com", ConstraintViolationException.class
			}, {
				"admin", "administrator1", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "", "4739158676192764", 10, 25, 535, "testEmail@testemail.com", ConstraintViolationException.class
			}, {
				"admin", "administrator1", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "", 10, 25, 535, "testEmail@testemail.com", ConstraintViolationException.class
			}, {
				"admin", "administrator1", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "8534634734746", 10, 25, 535, "testEmail@testemail.com", ConstraintViolationException.class
			}, {
				"admin", "administrator1", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", null, 25, 535, "testEmail@testemail.com", ConstraintViolationException.class
			}, {
				"admin", "administrator1", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 0, 25, 535, "testEmail@testemail.com", ConstraintViolationException.class
			}, {
				"admin", "administrator1", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 13, 25, 535, "testEmail@testemail.com", ConstraintViolationException.class
			}, {
				"admin", "administrator1", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, null, 535, "testEmail@testemail.com", ConstraintViolationException.class
			}, {
				"admin", "administrator1", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, -1, 535, "testEmail@testemail.com", ConstraintViolationException.class
			}, {
				"admin", "administrator1", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, 100, 535, "testEmail@testemail.com", ConstraintViolationException.class
			}, {
				"admin", "administrator1", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, null, "testEmail@testemail.com", ConstraintViolationException.class
			}, {
				"admin", "administrator1", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 99, "testEmail@testemail.com", ConstraintViolationException.class
			}, {
				"admin", "administrator1", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 1000, "testEmail@testemail.com", ConstraintViolationException.class
			}, {
				"admin", "administrator1", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, "", ConstraintViolationException.class
			}, {
				"admin", "administrator1", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, "testEmail", ConstraintViolationException.class
			}, {
				"admin", "administrator1", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "testCreditCardNumber", 10, 25, 535, "testEmail@testemail.com", IllegalArgumentException.class
			}, {
				"admin", "administrator1", "testName", "testSurnames", "testVatNumber", "testCreditCardHolder", "VISA", "4739158676192764", 2, 19, 535, "testEmail@testemail.com", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editDataTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (Integer) testingData[i][8], (Integer) testingData[i][9], (Integer) testingData[i][10], (String) testingData[i][11], (Class<?>) testingData[i][12]);
	}

	// Template methods ------------------------------------------------------

	protected void registerAdministratorTemplate(final String username, final String name, final String surnames, final String VATNumber, final String creditCardHolder, final String creditCardMake, final String creditCardNumber,
		final Integer creditCardExpirationMonth, final Integer creditCardExpirationYear, final Integer creditCardCVV, final String email, final String user, final String password, final Class<?> expected) {
		Class<?> caught = null;
		Administrator administrator;

		super.startTransaction();

		try {
			super.authenticate(username);
			administrator = this.administratorService.create();
			final CreditCard creditCard = new CreditCard();
			creditCard.setHolder(creditCardHolder);
			creditCard.setMake(creditCardMake);
			creditCard.setNumber(creditCardNumber);
			creditCard.setExpirationMonth(creditCardExpirationMonth);
			creditCard.setExpirationYear(creditCardExpirationYear);
			creditCard.setCvv(creditCardCVV);
			administrator.setName(name);
			administrator.setSurnames(surnames);
			administrator.setVATNumber(VATNumber);
			administrator.setCreditCard(creditCard);
			administrator.setEmail(email);
			administrator.getUserAccount().setUsername(user);
			administrator.getUserAccount().setPassword(password);
			this.administratorService.save(administrator);
			this.administratorService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		super.unauthenticate();
		this.checkExceptions(expected, caught);
		super.rollbackTransaction();
	}

	protected void editDataTemplate(final String username, final String actorData, final String name, final String surnames, final String VATNumber, final String creditCardHolder, final String creditCardMake, final String creditCardNumber,
		final Integer creditCardExpirationMonth, final Integer creditCardExpirationYear, final Integer creditCardCVV, final String email, final Class<?> expected) {
		Class<?> caught = null;
		Administrator administrator;

		super.startTransaction();

		try {
			super.authenticate(username);
			administrator = this.administratorService.findOne(super.getEntityId(actorData));
			final CreditCard creditCard = new CreditCard();
			creditCard.setHolder(creditCardHolder);
			creditCard.setMake(creditCardMake);
			creditCard.setNumber(creditCardNumber);
			creditCard.setExpirationMonth(creditCardExpirationMonth);
			creditCard.setExpirationYear(creditCardExpirationYear);
			creditCard.setCvv(creditCardCVV);
			administrator.setName(name);
			administrator.setSurnames(surnames);
			administrator.setVATNumber(VATNumber);
			administrator.setCreditCard(creditCard);
			administrator.setEmail(email);
			this.administratorService.save(administrator);
			this.administratorService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		super.unauthenticate();
		this.checkExceptions(expected, caught);
		super.rollbackTransaction();
	}
}
