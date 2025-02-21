package stepDefinitions;

import io.cucumber.java.en.Given;
import manage.QueryManage;
import utilities.ConfigReader;
import utilities.JDBCReusableMethods;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertEqualsNoOrder;

public class stepDefinition {

	Connection connection;
	Statement statement;
	ResultSet resultSet;

	PreparedStatement preparedStatement;
	int etkilenenSatirSayisi;
	String QUERY;
	QueryManage queryManage = new QueryManage();

	@Given("Database baglantisi kurulur.")
	public void database_baglantisi_kurulur() throws SQLException {

	connection = DriverManager.getConnection(ConfigReader.getProperty("URL"),
									ConfigReader.getProperty("USERNAME"),
									ConfigReader.getProperty("PASSWORD"));

	// JDBCReusableMethods.createConnection();

	}
	@Given("SQL Query'si hazirla ve calistir.")
	public void sql_query_si_hazirla_ve_calistir() throws SQLException {

		String QUERY = "SELECT DISTINCT user_id FROM deposits WHERE amount BETWEEN 100 AND 500;";
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		resultSet = statement.executeQuery(QUERY);

		// resultSet = JDBCReusableMethods.executeQuery(QUERY);

	}
	@Given("Sonuclari dogrula.")
	public void sonuclari_dogrula() throws SQLException {

		/*
		    1
			9
			10
			12
			64
		*/
		List<Integer> expectedResult = new ArrayList<>();

			expectedResult.add(1);
			expectedResult.add(9);
			expectedResult.add(10);
			expectedResult.add(12); //12
		    expectedResult.add(64);

		List<Integer> actualResult = new ArrayList<>();

		while(resultSet.next()){
		actualResult.add(resultSet.getInt(1));

		}

		assertEqualsNoOrder(actualResult,expectedResult);


	}
	@Given("Database baglantisini sonlandir.")
	public void database_baglantisini_sonlandir() throws SQLException {
		/*
		resultSet.close();
		statement.close();
		connection.close();
       */

		JDBCReusableMethods.closeConnection();
	}

// ******************* cron_schedules query **********************

	@Given("\\(cron_schedules) SQL query'si  calistirilir")
	public void cron_schedules_sql_query_si_calistirilir() {

		QUERY = queryManage.getCroneSchedulesQuery();
		resultSet = JDBCReusableMethods.executeQuery(QUERY);

	}
	@Given("\\(cron_schedules) sonuclari  dogrulanir.")
	public void cron_schedules_sonuclari_dogrulanir() throws SQLException {

		List<String> expectedResultList = new ArrayList<>();
		expectedResultList.add("5 Minutes");
		expectedResultList.add("10 Minutes");

		List<String> actualResultList = new ArrayList<>();
		while(resultSet.next()){
			actualResultList.add(resultSet.getString(1));
		}

		assertEqualsNoOrder(actualResultList, expectedResultList);

	}



// ************** UPDATE **********************

	@Given("Database ile baglanti kurulur.")
	public void database_ile_baglanti_kurulur() {

		JDBCReusableMethods.createConnection();

	}
	@Given("\\(users) Update sorgusu hazirlanir ve calistirilir.")
	public void users_update_sorgusu_hazirlanir_ve_calistirilir() throws SQLException {

		QUERY = queryManage.getUsersMobileUpdateQuery();
		preparedStatement = JDBCReusableMethods.getConnection().prepareStatement(QUERY);

		// UPDATE users SET mobile = ? WHERE username LIKE ?;

		preparedStatement.setString(1, "555555");
		preparedStatement.setString(2, "%e_");
		etkilenenSatirSayisi =  preparedStatement.executeUpdate();


	}
	@Given("\\(users) sorgu sonuclari dogrulanir.")
	public void users_sorgu_sonuclari_dogrulanir() {

		int expectedResult = 5;
		System.out.println(etkilenenSatirSayisi);
		assertEquals( etkilenenSatirSayisi, expectedResult);

	}
	@Given("database baglantisi sonlandirilir.")
	public void database_baglantisi_sonlandirilir() {

		JDBCReusableMethods.closeConnection();

	}

// ********** INSERT **********

	@Given("\\(device_tokens) Insert sorgusu hazirlanir ve calistirilir.")
	public void device_tokens_ınsert_sorgusu_hazirlanir_ve_calistirilir() throws SQLException {

		QUERY = queryManage.getDeviceTokensInsertQuery();
		preparedStatement =JDBCReusableMethods.getConnection().prepareStatement(QUERY);

		// INSERT INTO device_tokens (id, user_id, is_app, token)VALUES(?,?,?,?);
		preparedStatement.setInt(1, 7);
		preparedStatement.setInt(2, 3);
		preparedStatement.setInt(3, 2);
		preparedStatement.setString(4,"NewToken");

		etkilenenSatirSayisi = preparedStatement.executeUpdate();

	}
	@Given("\\(device_tokens) sorgu sonuclari dogrulanir.")
	public void device_tokens_sorgu_sonuclari_dogrulanir() {

		int expectedResult = 1;
		System.out.println(etkilenenSatirSayisi);
		assertEquals(etkilenenSatirSayisi, expectedResult);

	}


}
