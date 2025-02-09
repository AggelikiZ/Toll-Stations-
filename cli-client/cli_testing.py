import subprocess
import os
import time

# Function to execute a CLI command and print the output
def run_command(command):
    input("Press Enter to execute the next command...")
    try:
        print(f"\nExecuting: {command}")
        result = subprocess.run(command, shell=True, text=True, capture_output=True)
        
        print(f"Exit Code: {result.returncode}")
        if result.stdout.strip():
            print("Output:")
            print(result.stdout.strip())
        
        if result.stderr.strip():
            print("Error:")
            print(result.stderr.strip())
    except Exception as e:
        print(f"Error executing command: {e}")

# Test cases
def test_cli():
    print("=== Starting CLI Tests ===")
    
    users = [
        {"username": "admin", "password": "1234"},
        {"username": "aegeanmotorway1", "password": "default_password"},
        {"username": "ypoyrgeio", "password": "default_password"},
        {"username": "invalid", "password": "invalid"}
    ]
    
    for user in users:
        print(f"\n--- Testing login with {user['username']} ---")
        run_command(f"python cli.py login --username {user['username']} --passw {user['password']}")
        
        # Run a healthcheck if login was successful
        run_command("python cli.py healthcheck")

        # Test adding passes (ensure passes-sample.csv exists)
        with open("passes-sample.csv", "w") as f:
            f.write("Sample,Data,For,Testing\n")
        run_command("python cli.py admin --addpasses --source passes-sample.csv")
        os.remove("passes-sample.csv")
        
        # Test listing users
        run_command("python cli.py admin --users")
        
        # Modify user (create/update)
        run_command("python cli.py admin --usermod --username testuser --passw newpass")
        
        # Logout
        run_command("python cli.py logout")
        
        # Healthcheck without token (should fail)
        run_command("python cli.py healthcheck")
        
        # Missing arguments for login
        run_command("python cli.py login --username admin")

        run_command(f"python cli.py login --username {user['username']} --passw {user['password']}")

        
        # Charges by operator
        run_command("python cli.py chargesby --opid OP1 --from 20230101 --to 20231231 --format json")

        # Logout
        run_command("python cli.py logout")
        time.sleep(1)
        
    print("\n=== CLI Tests Completed ===")

if __name__ == "__main__":
    test_cli()
