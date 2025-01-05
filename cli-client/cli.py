#!/usr/bin/env python3

import argparse
import requests


# Function for login
def login(args):
    url = "http://localhost:9115/api/login"
    data = {"username": args.username, "password": args.password}
    try:
        response = requests.post(url, data=data, timeout=5)
        response.raise_for_status()
        token = response.json().get("token")
        if token:
            with open("auth_token.txt", "w") as f:
                f.write(token)
            print("Login successful. Token saved.")
        else:
            print("Login failed. No token received.")
    except requests.exceptions.RequestException as e:
        print(f"Login failed: {e}")


# Function for logout
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

# Function to manage user creation or password update
def usermod(args):
    url = "http://localhost:9115/api/admin/usermod"
    try:
        with open("auth_token.txt", "r") as f:
            token = f.read().strip()
        headers = {"X-OBSERVATORY-AUTH": token}
        data = {"username": args.username, "password": args.passw}
        response = requests.post(url, headers=headers, data=data, timeout=5)
        response.raise_for_status()
        print("User operation successful: ", response.json())
    except FileNotFoundError:
        print("No authentication token found. Please login first.")
    except requests.exceptions.RequestException as e:
        print(f"Error during user operation: {e}")


# Function to list all users
def list_users(args):
    url = "http://localhost:9115/api/admin/users"
    try:
        with open("auth_token.txt", "r") as f:
            token = f.read().strip()
        headers = {"X-OBSERVATORY-AUTH": token}
        response = requests.get(url, headers=headers, timeout=5)
        response.raise_for_status()
        print("User list: ", response.json())
    except FileNotFoundError:
        print("No authentication token found. Please login first.")
    except requests.exceptions.RequestException as e:
        print(f"Error retrieving user list: {e}")


# Function for healthcheck
def healthcheck(args):
    url = "http://localhost:9115/api/admin/healthcheck"
    try:
        response = requests.get(url, timeout=5)
        response.raise_for_status()
        print("Healthcheck successful:", response.json())
    except requests.exceptions.RequestException as e:
        print(f"Healthcheck failed: {e}")


# Function to reset stations
def resetstations(args):
    url = "http://localhost:9115/api/admin/resetstations"
    try:
        response = requests.post(url, timeout=5)
        response.raise_for_status()
        print("Stations reset successfully.")
    except requests.exceptions.RequestException as e:
        print(f"Error resetting stations: {e}")


# Function to reset passes
def resetpasses(args):
    url = "http://localhost:9115/api/admin/resetpasses"
    try:
        response = requests.post(url, timeout=5)
        response.raise_for_status()
        print("Passes reset successfully.")
    except requests.exceptions.RequestException as e:
        print(f"Error resetting passes: {e}")

def addpasses(args):
    url = "http://localhost:9115/api/admin/addpasses"
    try:
        # Open the file in binary mode and include its MIME type
        with open(args.file, 'rb') as file:
            files = {'file': (args.file, file, 'text/csv')}
            response = requests.post(url, files=files, timeout=20)
            response.raise_for_status()
            print("Passes added successfully.")
    except FileNotFoundError:
        print(f"Error: The file '{args.file}' was not found.")
    except requests.exceptions.Timeout:
        print("Error: The request timed out.")
    except requests.exceptions.RequestException as e:
        print(f"Error adding passes: {e}")

# Function to retrieve toll station passes
def tollstationpasses(args):
    url = f"http://localhost:9115/api/tollStationPasses/{args.station}/{args.from_date}/{args.to_date}"
    try:
        response = requests.get(url, params={"format": args.format}, timeout=5)
        response.raise_for_status()
        print("Toll station passes retrieved:", response.json())
    except requests.exceptions.RequestException as e:
        print(f"Error retrieving toll station passes: {e}")


# Main function to parse arguments and dispatch commands
def main():
    # Create argument parser
    parser = argparse.ArgumentParser(description="CLI for toll station system")
    subparsers = parser.add_subparsers(dest="command", required=True)

    # Subcommand: login
    login_parser = subparsers.add_parser("login", help="Login to the system")
    login_parser.add_argument("--username", required=True, help="Username for login")
    login_parser.add_argument("--password", required=True, help="Password for login")
    login_parser.set_defaults(func=login)

    # Subcommand: logout
    logout_parser = subparsers.add_parser("logout", help="Logout from the system")
    logout_parser.set_defaults(func=logout)

    # Subcommand: usermod
    usermod_parser = subparsers.add_parser("usermod", help="Create or update a user")
    usermod_parser.add_argument("--username", required=True, help="Username of the user")
    usermod_parser.add_argument("--passw", required=True, help="Password for the user")
    usermod_parser.set_defaults(func=usermod)

    # Subcommand: list users
    users_parser = subparsers.add_parser("users", help="List all users")
    users_parser.set_defaults(func=list_users)

    # Subcommand: healthcheck
    health_parser = subparsers.add_parser("healthcheck", help="Check system health")
    health_parser.set_defaults(func=healthcheck)

    # Subcommand: resetstations
    resetstations_parser = subparsers.add_parser("resetstations", help="Reset toll stations")
    resetstations_parser.set_defaults(func=resetstations)

    # Subcommand: resetpasses
    resetpasses_parser = subparsers.add_parser("resetpasses", help="Reset all toll passes")
    resetpasses_parser.set_defaults(func=resetpasses)

    # Subcommand: addpasses
    addpasses_parser = subparsers.add_parser("addpasses", help="Add new passes from a CSV file")
    addpasses_parser.add_argument("--file", required=True, help="Path to CSV file with pass data")
    addpasses_parser.set_defaults(func=addpasses)

    # Subcommand: tollstationpasses
    tollstationpasses_parser = subparsers.add_parser("tollstationpasses", help="Retrieve toll station passes")
    tollstationpasses_parser.add_argument("--station", required=True, help="Toll station ID")
    tollstationpasses_parser.add_argument("--from_date", required=True, help="Start date (YYYYMMDD)")
    tollstationpasses_parser.add_argument("--to_date", required=True, help="End date (YYYYMMDD)")
    tollstationpasses_parser.add_argument("--format", choices=["json", "csv"], default="json", help="Output format")
    tollstationpasses_parser.set_defaults(func=tollstationpasses)

    # Parse arguments and dispatch to appropriate function
    args = parser.parse_args()
    args.func(args)


if __name__ == "__main__":
    main()
