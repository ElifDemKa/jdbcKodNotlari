package manage;

public class QueryManage {


	private String croneSchedulesQuery = "SELECT name FROM cron_schedules LIMIT 2;";
	private String usersMobileUpdateQuery = "UPDATE users SET mobile = ? WHERE username LIKE ?;";
	private String deviceTokensInsertQuery = "INSERT INTO device_tokens (id, user_id, is_app, token)VALUES(?,?,?,?);";






	// ************ GETTERS **************
	public String getCroneSchedulesQuery() {
		return croneSchedulesQuery;
	}

	public String getUsersMobileUpdateQuery() {
		return usersMobileUpdateQuery;
	}

	public String getDeviceTokensInsertQuery() {
		return deviceTokensInsertQuery;
	}
}
