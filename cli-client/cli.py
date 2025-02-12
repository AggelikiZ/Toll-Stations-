import argparse
import os
import requests
import mysql.connector
from mysql.connector import Error

# Function to check if the user is authenticated
def check_auth():
    token_path = "auth_token.txt"
    if not os.path.exists(token_path):
        print("Error: You must log in first. Use the 'login' command.")
        exit(1)
    with open(token_path, "r") as f:
        token = f.read().strip()
        if not token:
            print("Error: You must log in first. Use the 'login' command.")
            exit(1)
    return token


# Database connection setup
def get_db_connection():
    try:
        conn = mysql.connector.connect(
            host="localhost",  # Change if your database is hosted elsewhere
            user="root",       # Your database username
            password="",       # Your database password (leave empty if no password)
            database="paywaydb"  # Your database name
        )
        return conn
    except mysql.connector.Error as e:  # Catch database-specific errors
            print(f"Error connecting to the database: {e}")
            exit(1)  # **Ensure the function exits**


# Function for login (no token check needed here)
def login(args):
    url = "http://localhost:9115/api/login"
    data = {"username": args.username, "password": args.passw}
    try:
        response = requests.post(url, data=data, timeout=5)
        response.raise_for_status()
        token = response.json().get("token")

        if token:
            # Save token separately
            with open("auth_token.txt", "w") as f:
                f.write(token)

            # Save username separately
            with open("auth_user.txt", "w") as f:
                f.write(args.username)

            print(f"Login successful. User '{args.username}' logged in. Token saved.")
        else:
            print("Login failed. No token received.")
    except requests.exceptions.RequestException as e:
        print(f"Login failed: {e}")

def get_current_user():
    user_path = "auth_user.txt"
    
    if not os.path.exists(user_path):
        return None  # No user logged in

    with open(user_path, "r") as f:
        username = f.read().strip()  # Read username from file

    return username


# Function for logout (no token check needed here)
def logout(args):
    url = "http://localhost:9115/api/logout"
    try:
        with open("auth_token.txt", "r") as f:
            token = f.read().strip()
        headers = {"X-OBSERVATORY-AUTH": token}
        response = requests.post(url, headers=headers, timeout=5)
        response.raise_for_status()
        if response.status_code == 200:
            print("Logout successful.")
            # Remove the saved token
            with open("auth_token.txt", "w") as f:
                f.write("")
        else:
            print("Logout failed.")
    except FileNotFoundError:
        print("No authentication token found. Please login first.")
    except requests.exceptions.RequestException as e:
        print(f"Logout failed: {e}")

# Function to check if the logged-in user is an admin
def is_admin():
    username = get_current_user()
    if not username:
        print("Error: No user logged in.")
        return False

    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    try:
        # Check the role of the user in the database
        cursor.execute("SELECT user_role FROM user WHERE username = %s", (username,))
        user = cursor.fetchone()
        
        if user and user.get("user_role") == "admin":
            return True
        else:
            print(f"Access Denied: '{username}' is not an admin.")
            return False
    except Error as e:
        print(f"Database error while checking admin role: {e}")
        return False
    finally:
        cursor.close()
        conn.close()


def usermod(args):
    if not is_admin():
        print("Error: Only admin users can modify users.")
        return

    token = check_auth()
    conn = get_db_connection()
    cursor = conn.cursor()

    try:
        # Check if user exists
        cursor.execute("SELECT user_role FROM user WHERE username = %s", (args.username,))
        user = cursor.fetchone()

        if user:
            # Only update password, do NOT change role
            cursor.execute("UPDATE user SET password = %s WHERE username = %s",
                           (args.passw, args.username))
            print(f"Password updated successfully for user: {args.username}")
        else:
            # If user does not exist, create new user with default or provided role
            role = args.role if args.role else "operator"
            cursor.execute("INSERT INTO user (username, password, user_role) VALUES (%s, %s, %s)",
                           (args.username, args.passw, role))
            print(f"User created successfully: {args.username} with role: {role}")

        conn.commit()
    except Error as e:
        print(f"Error during user operation: {e}")
    finally:
        cursor.close()
        conn.close()


# Function to list users (Admin Only)
def list_users(args):
    if not is_admin():
        print("Error: Only admin users can list users.")
        return

    token = check_auth()
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    try:
        cursor.execute("SELECT username FROM user")
        users = cursor.fetchall()
        print("User list:")
        for user in users:
            print(f"- {user['username']}")
    except Error as e:
        print(f"Error retrieving user list: {e}")
    finally:
        cursor.close()
        conn.close()


# Function for healthcheck
def healthcheck(args):
    token = check_auth()  # Ensure the user is logged in
    url = "http://localhost:9115/api/admin/healthcheck"
    try:
        headers = {"X-OBSERVATORY-AUTH": token}
        response = requests.get(url, headers=headers, timeout=5)
        response.raise_for_status()
        print("Healthcheck successful:", response.json())
    except requests.exceptions.RequestException as e:
        print(f"Healthcheck failed: {e}.  Status Code: {response.status_code}")


# Function to reset stations
def resetstations(args):
    token = check_auth()  # Ensure the user is logged in
    url = "http://localhost:9115/api/admin/resetstations"
    try:
        headers = {"X-OBSERVATORY-AUTH": token}
        response = requests.post(url, headers=headers, timeout=5)
        response.raise_for_status()
        print("Stations reset successfully.")
    except requests.exceptions.RequestException as e:
        print(f"Error resetting stations: {e}.  Status Code: {response.status_code}")


# Function to reset passes
def resetpasses(args):
    token = check_auth()  # Ensure the user is logged in
    url = "http://localhost:9115/api/admin/resetpasses"
    try:
        headers = {"X-OBSERVATORY-AUTH": token}
        response = requests.post(url, headers=headers, timeout=5)
        response.raise_for_status()
        print("Passes reset successfully.")
    except requests.exceptions.RequestException as e:
        print(f"Error resetting passes: {e}")

def addpasses(file_path):
    token = check_auth()  # Ensure the user is logged in and retrieve the token
    url = "http://localhost:9115/api/admin/addpasses"
    try:
        # Open the file in binary mode and include its MIME type
        with open(file_path, 'rb') as file:
            files = {'file': (file_path, file, 'text/csv')}
            headers = {"X-OBSERVATORY-AUTH": token}  # Add the token to the headers
            response = requests.post(url, headers=headers, files=files, timeout=20)
            response.raise_for_status()
            print("Passes added successfully.")
    except FileNotFoundError:
        print(f"Error: The file '{file_path}' was not found.")
    except requests.exceptions.Timeout:
        print("Error: The request timed out.")
    except requests.exceptions.RequestException as e:
        print(f"Error adding passes: {e}  Status Code: {response.status_code}")

# Function to retrieve toll station passes
def tollstationpasses(args):
    token = check_auth()  # Ensure the user is logged in
    url = f"http://localhost:9115/api/tollStationPasses/{args.station}/{args.from_date}/{args.to_date}"

    try:
        headers = {"X-OBSERVATORY-AUTH": token}
        response = requests.get(url, headers=headers, params={"format": args.format}, timeout=5)
        response.raise_for_status()

        # Get content type to determine response format
        content_type = response.headers.get("Content-Type", "")

        # Check if the response is empty (204 No Content)
        if response.status_code == 204 or not response.text.strip():
            print("No data available. Status Code: 204")
            return

        if args.format == "json":
                print("Toll station passes retrieved:", response.json())
                print("Response Content:", response.text)

        elif args.format == "csv":
                print("CSV data retrieved:")
                print(response.text)  # Print raw CSV content
        else:
            print("Unsupported format specified.")

    except requests.exceptions.HTTPError as e:
        print(f"HTTP Error: {e}. Status Code: {response.status_code}")
    except requests.exceptions.RequestException as e:
        print(f"Error retrieving toll station passes: {e}. Status Code: {response.status_code}")


# Function for pass analysis
def pass_analysis(args):
    token = check_auth()  # Ensure the user is logged in
    url = f"http://localhost:9115/api/passAnalysis/{args.stationop}/{args.tagop}/{args.from_date}/{args.to_date}"
    try:
        
        headers = {"X-OBSERVATORY-AUTH": token}
        response = requests.get(url, headers=headers, params={"format": args.format}, timeout=5)
        response.raise_for_status()

        # Check if the response is empty (204 No Content)
        if response.status_code == 204 or not response.text.strip():
            print("No data available. Status Code: 204")
            return
        
        if args.format == "json":
            print("Passes Cost Data:", response.json())
        elif args.format == "csv":
            print("CSV data retrieved:")
            print(response.text)  # Print raw CSV data
        else:
            print("Unsupported format specified.")

    except requests.exceptions.RequestException as e:
        print(f"Error retrieving pass analysis data: {e}.  Status Code: {response.status_code}")


#function for passes cost
def passes_cost(args):
    token = check_auth()  # Ensure the user is logged in
    url = f"http://localhost:9115/api/passesCost/{args.stationop}/{args.tagop}/{args.from_date}/{args.to_date}"
    
    try:
        headers = {"X-OBSERVATORY-AUTH": token}
        response = requests.get(url, headers=headers, params={"format": args.format}, timeout=5)
        response.raise_for_status()

        # Check if the response is empty (204 No Content)
        if response.status_code == 204 or not response.text.strip():
            print("No data available. Status Code: 204")
            return
        
        if args.format == "json":
            print("Passes Cost Data:", response.json())
        elif args.format == "csv":
            print("CSV data retrieved:")
            print(response.text)  # Print raw CSV data
        else:
            print("Unsupported format specified.")
    except requests.exceptions.RequestException as e:
        print(f"Error retrieving passes cost data: {e}.  Status Code: {response.status_code}")

# Function for charges by operator
def charges_by(args):
    token = check_auth()  # Ensure the user is logged in
    url = f"http://localhost:9115/api/chargesBy/{args.opid}/{args.from_date}/{args.to_date}"
    try:
        headers = {"X-OBSERVATORY-AUTH": token}
        response = requests.get(url, headers=headers, params={"format": args.format}, timeout=5)
        response.raise_for_status()

        # Check if the response is empty (204 No Content)
        if response.status_code == 204 or not response.text.strip():
            print("No data available. Status Code: 204")
            return
        
        if args.format == "json":
            print("Charges By Data:", response.json())
        elif args.format == "csv":
            print("CSV data retrieved:")
            print(response.text)  # Print raw CSV data
        else:
            print("Unsupported format specified.")
    except requests.exceptions.RequestException as e:
        print(f"Error retrieving charges by operator data: {e}.  Status Code: {response.status_code}")

# Function for admin operations
def handle_admin(args):
    if args.usermod:
        if not args.username or not args.passw:
            print("Error: Both --username and --passw are required for usermod.")
            return
        usermod(args)  # Pass the args to the usermod function
    elif args.users:
        list_users(args)  # Call the list_users function
    elif args.addpasses:
        if not args.source:
            print("Error: Please provide the file path using --source.")
            return
        addpasses(args.source)  # Call the addpasses function with the provided file path
    else:
        print("No valid admin operation provided. Use --usermod, --users, or --addpasses.")

# Main function to parse arguments and dispatch commands
def main():
    # Create argument parser
    parser = argparse.ArgumentParser(description="CLI for toll station system")
    subparsers = parser.add_subparsers(dest="command", required=True)

    # Subcommand: login
    login_parser = subparsers.add_parser("login", help="Login to the system")
    login_parser.add_argument("--username", required=True, help="Username for login")
    login_parser.add_argument("--passw", required=True, help="Password for login")
    login_parser.set_defaults(func=login)

    # Subcommand: logout
    logout_parser = subparsers.add_parser("logout", help="Logout from the system")
    logout_parser.set_defaults(func=logout)

    # Subcommand: admin
    admin_parser = subparsers.add_parser("admin", help="Admin commands")
    admin_parser.add_argument("--usermod", action="store_true", help="Modify or create a user")
    admin_parser.add_argument("--username", help="Username of the user")
    admin_parser.add_argument("--passw", help="Password for the user")
    admin_parser.add_argument("--users", action="store_true", help="List all users")
    admin_parser.add_argument("--addpasses", action="store_true", help="Add passes from a CSV file")
    admin_parser.add_argument("--source", help="Path to the CSV file containing passes")
    admin_parser.add_argument("--role", help="Role to assign to the user (operator, admin, user)")
    admin_parser.set_defaults(func=handle_admin)

    # Subcommand: healthcheck
    health_parser = subparsers.add_parser("healthcheck", help="Check system health")
    health_parser.set_defaults(func=healthcheck)

    # Subcommand: resetstations
    resetstations_parser = subparsers.add_parser("resetstations", help="Reset toll stations")
    resetstations_parser.set_defaults(func=resetstations)

    # Subcommand: resetpasses
    resetpasses_parser = subparsers.add_parser("resetpasses", help="Reset all toll passes")
    resetpasses_parser.set_defaults(func=resetpasses)

    # Subcommand: tollstationpasses
    tollstationpasses_parser = subparsers.add_parser("tollstationpasses", help="Retrieve toll station passes")
    tollstationpasses_parser.add_argument("--station", required=True, help="Toll station ID")
    tollstationpasses_parser.add_argument("--from", dest="from_date", required=True, help="Start date (YYYYMMDD)")
    tollstationpasses_parser.add_argument("--to", dest="to_date", required=True, help="End date (YYYYMMDD)")
    tollstationpasses_parser.add_argument("--format", choices=["json", "csv"], default="json", help="Output format")
    tollstationpasses_parser.set_defaults(func=tollstationpasses)

    # Subcommand: passanalysis
    pass_analysis_parser = subparsers.add_parser("passanalysis", help="Analyze passes between two operators")
    pass_analysis_parser.add_argument("--stationop", required=True, help="Station operator ID")
    pass_analysis_parser.add_argument("--tagop", required=True, help="Tag operator ID")
    pass_analysis_parser.add_argument("--from", dest="from_date", required=True, help="Start date (YYYYMMDD)")
    pass_analysis_parser.add_argument("--to", dest="to_date", required=True, help="End date (YYYYMMDD)")
    pass_analysis_parser.add_argument("--format", default="json", choices=["json", "csv"], help="Output format")
    pass_analysis_parser.set_defaults(func=pass_analysis)

    # Subcommand: passescost
    passes_cost_parser = subparsers.add_parser("passescost", help="Retrieve passes cost between two operators")
    passes_cost_parser.add_argument("--stationop", required=True, help="Toll operator ID")
    passes_cost_parser.add_argument("--tagop", required=True, help="Tag operator ID")
    passes_cost_parser.add_argument("--from", dest="from_date", required=True, help="Start date (YYYYMMDD)")
    passes_cost_parser.add_argument("--to", dest="to_date", required=True, help="End date (YYYYMMDD)")
    passes_cost_parser.add_argument("--format", default="json", choices=["json", "csv"], help="Output format")
    passes_cost_parser.set_defaults(func=passes_cost)

    # Subcommand: chargesby
    charges_by_parser = subparsers.add_parser("chargesby", help="Retrieve charges by operator")
    charges_by_parser.add_argument("--opid", required=True, help="Operator ID")
    charges_by_parser.add_argument("--from", dest="from_date", required=True, help="Start date (YYYYMMDD)")
    charges_by_parser.add_argument("--to", dest="to_date", required=True, help="End date (YYYYMMDD)")
    charges_by_parser.add_argument("--format", default="json", choices=["json", "csv"], help="Output format")
    charges_by_parser.set_defaults(func=charges_by)

    # Parse arguments and dispatch to appropriate function
    args = parser.parse_args()
    if hasattr(args, "func"):
        args.func(args)
    else:
        parser.print_help()


if __name__ == "__main__":
    main()

