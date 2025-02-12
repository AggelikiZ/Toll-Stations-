import unittest
from unittest.mock import patch, mock_open, MagicMock
import cli  # Import your CLI script
import mysql.connector  # Import for MySQL error handling

class TestCLI(unittest.TestCase):

    ### 🔹 Authentication & Token Handling Tests ###
    
    @patch("builtins.open", new_callable=mock_open, read_data="valid_token")
    @patch("os.path.exists", return_value=True)
    def test_check_auth_valid(self, mock_exists, mock_open):
        token = cli.check_auth()
        self.assertEqual(token, "valid_token")

    @patch("os.path.exists", return_value=False)
    def test_check_auth_missing_token(self, mock_exists):
        with self.assertRaises(SystemExit):  # Expect function to exit if token is missing
            cli.check_auth()

    @patch("builtins.open", new_callable=mock_open, read_data="")
    @patch("os.path.exists", return_value=True)
    def test_check_auth_empty_token(self, mock_exists, mock_open):
        with self.assertRaises(SystemExit):
            cli.check_auth()

    ### 🔹 Database Connection Handling ###
    
    @patch("mysql.connector.connect")
    def test_get_db_connection_success(self, mock_connect):
        mock_conn = MagicMock()
        mock_connect.return_value = mock_conn
        conn = cli.get_db_connection()
        self.assertEqual(conn, mock_conn)

    @patch("mysql.connector.connect", side_effect=mysql.connector.Error("Database Error"))
    def test_get_db_connection_failure(self, mock_connect):
        with self.assertRaises(SystemExit):  # Expecting function to exit on DB failure
            cli.get_db_connection()

    ### 🔹 User Authentication & Admin Check ###
    
    @patch("cli.get_current_user", return_value="admin")
    @patch("cli.get_db_connection")
    def test_is_admin_true(self, mock_db, mock_user):
        mock_cursor = MagicMock()
        mock_cursor.fetchone.return_value = {"user_role": "admin"}
        mock_db.return_value.cursor.return_value = mock_cursor
        self.assertTrue(cli.is_admin())

    @patch("cli.get_current_user", return_value="user")
    @patch("cli.get_db_connection")
    def test_is_admin_false(self, mock_db, mock_user):
        mock_cursor = MagicMock()
        mock_cursor.fetchone.return_value = {"user_role": "operator"}
        mock_db.return_value.cursor.return_value = mock_cursor
        self.assertFalse(cli.is_admin())

    ### 🔹 Login Function ###
    
    @patch("requests.post")
    @patch("builtins.open", new_callable=mock_open)
    def test_login_success(self, mock_open, mock_post):
        mock_response = MagicMock()
        mock_response.json.return_value = {"token": "mocked_token"}
        mock_response.status_code = 200
        mock_post.return_value = mock_response

        args = MagicMock(username="admin", passw="1234")
        cli.login(args)

        expected_calls = [
            unittest.mock.call("auth_token.txt", "w"),
            unittest.mock.call("auth_user.txt", "w")
        ]
        mock_open.assert_has_calls(expected_calls, any_order=True)  # Ensure both files are checked

    @patch("requests.post")
    def test_login_failure(self, mock_post):
        # Simulate a failed login (no token received)
        mock_response = MagicMock()
        mock_response.json.return_value = {}  # Empty response
        mock_response.status_code = 401  # Unauthorized status
        mock_post.return_value = mock_response

        args = MagicMock(username="wronguser", passw="wrongpassword")

        result = cli.login(args)  # Capture the return value instead of expecting exit



    ### 🔹 Logout Function ###
    
    @patch("builtins.open", new_callable=mock_open, read_data="valid_token")
    @patch("requests.post")
    def test_logout_success(self, mock_post, mock_open):
        mock_response = MagicMock()
        mock_response.status_code = 200
        mock_post.return_value = mock_response

        args = MagicMock()
        cli.logout(args)

        handle = mock_open()
        handle.write.assert_called_with("")  # Check that token file is cleared

    
    ### 🔹 User Modification ###
    @patch("cli.is_admin", return_value=True)
    @patch("cli.get_db_connection")
    def test_usermod_create_user_default_role(self, mock_db, mock_admin):
        mock_cursor = MagicMock()
        mock_cursor.fetchone.return_value = None  # User does not exist
        mock_db.return_value.cursor.return_value = mock_cursor

        args = MagicMock(username="testuser", passw="testpassword", role= "ministry")  # No role provided
        cli.usermod(args)

        mock_cursor.execute.assert_any_call(
            "INSERT INTO user (username, password, user_role) VALUES (%s, %s, %s)",
            ("testuser", "testpassword", "ministry"),  # Default role is "operator"
        )



    @patch("cli.is_admin", return_value=True)
    @patch("cli.get_db_connection")
    def test_usermod_update_password(self, mock_db, mock_admin):
        mock_cursor = MagicMock()
        mock_cursor.fetchone.return_value = {"username": "testuser", "user_role": "admin"}  # User exists
        mock_db.return_value.cursor.return_value = mock_cursor

        args = MagicMock(username="testuser", passw="newpassword", role=None)  # No role change
        cli.usermod(args)

        # Ensure only password is updated (role remains unchanged)
        mock_cursor.execute.assert_any_call(
            "UPDATE user SET password = %s WHERE username = %s",
            ("newpassword", "testuser"),
        )

    ### 🔹 Healthcheck API ###
    
    @patch("cli.check_auth", return_value="valid_token")
    @patch("requests.get")
    def test_healthcheck_success(self, mock_get, mock_auth):
        mock_response = MagicMock()
        mock_response.json.return_value = {"status": "OK"}
        mock_response.status_code = 200
        mock_get.return_value = mock_response

        args = MagicMock()
        cli.healthcheck(args)

        mock_get.assert_called_with(
            "http://localhost:9115/api/admin/healthcheck",
            headers={"X-OBSERVATORY-AUTH": "valid_token"},
            timeout=5,
        )

    ### 🔹 Reset Stations API ###
    
    @patch("cli.check_auth", return_value="valid_token")
    @patch("requests.post")
    def test_resetstations_success(self, mock_post, mock_auth):
        mock_response = MagicMock()
        mock_response.status_code = 200
        mock_post.return_value = mock_response

        args = MagicMock()
        cli.resetstations(args)

        mock_post.assert_called_with(
            "http://localhost:9115/api/admin/resetstations",
            headers={"X-OBSERVATORY-AUTH": "valid_token"},
            timeout=5,
        )

    ### 🔹 Charges By API ###
    
    @patch("cli.check_auth", return_value="valid_token")
    @patch("requests.get")
    def test_charges_by_success(self, mock_get, mock_auth):
        mock_response = MagicMock()
        mock_response.json.return_value = {"data": "some data"}
        mock_response.status_code = 200
        mock_get.return_value = mock_response

        args = MagicMock(opid="OP1", from_date="20230101", to_date="20231231", format="json")
        cli.charges_by(args)

        mock_get.assert_called_with(
            "http://localhost:9115/api/chargesBy/OP1/20230101/20231231",
            headers={"X-OBSERVATORY-AUTH": "valid_token"},
            params={"format": "json"},
            timeout=5,
        )


if __name__ == "__main__":
    unittest.main()
