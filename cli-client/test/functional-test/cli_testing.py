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
        {"username": "ypourgeio", "password": "default_password"},
        {"username": "invalid", "password": "invalid"}
    ]
    
    for user in users:
        print(f"\n--- Testing login with {user['username']} ---")
        run_command(f"python cli.py login --username {user['username']} --passw {user['password']}")
        
        # Run a healthcheck if login was successful
        run_command("python cli.py healthcheck")

        # Test adding passes (ensure passes-sample.csv exists)
        run_command("python cli.py admin --addpasses --source passes25.csv")
        
        
        # Test listing users
        run_command("python cli.py admin --users")
        
        # Modify user (create/update)
        run_command("python cli.py admin --usermod --username testuser --passw newpass")

        # Modify user (create/update)
        run_command("python cli.py admin --usermod --username testuser2 --passw newpass --role ministry")
        
        # Logout
        run_command("python cli.py logout")
        
        # Healthcheck without token (should fail)
        run_command("python cli.py healthcheck")
        
        # Missing arguments for login
        run_command("python cli.py login --username admin")

        run_command(f"python cli.py login --username {user['username']} --passw {user['password']}")

        # Test toll station passes retrieval
        run_command("python cli.py tollstationpasses --station AM08 --from 20220127 --to 20220210 --format json")

        # Test toll station passes retrieval
        run_command("python cli.py tollstationpasses --station NAO01 --from 20220101 --to 20221212 --format json")
        
        # Test pass analysis
        run_command("python cli.py passanalysis --stationop AM --tagop NAO --from 20220127 --to 20220210 --format json")

        # Test pass analysis
        run_command("python cli.py passanalysis --stationop NAO --tagop NO --from 20220101 --to 20221212 --format csv")
        
        # Test passes cost
        run_command("python cli.py passescost --stationop AM --tagop NAO --from 20220101 --to 20221212 --format json")

        # Test passes cost
        run_command("python cli.py passescost --stationop AM --tagop NO --from 20220101 --to 20221212 --format csv")
        
        # Test charges by operator
        run_command("python cli.py chargesby --opid NAO --from 20220127 --to 20220210 --format json")
        
        # Charges by operator(wrong)
        run_command("python cli.py chargesby --opid OP1 --from 20220101 --to 20221231 --format json")

        # Charges by operator
        run_command("python cli.py chargesby --opid AM --from 20220101 --to 20221212 --format csv")

        # Logout
        run_command("python cli.py logout")
        time.sleep(1)
        
    print("\n=== CLI Tests Completed ===")

if __name__ == "__main__":
    test_cli()
