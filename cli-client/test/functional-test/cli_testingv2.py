import subprocess
import os
import time

SE2425_PATH = "se2425.bat"  # Update this with your real path

# Function to execute a CLI command and print the output
def run_command(command):
    input("\nPress Enter to execute the next command...")
    try:
        full_command = f"{SE2425_PATH} {command}"
        print(f"\nExecuting: {"se2425 "+ command}")
        result = subprocess.run(full_command, shell=True, text=True, capture_output=True)
        
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
        run_command(f"login --username {user['username']} --passw {user['password']}")
        
        # Run a healthcheck if login was successful
        run_command("healthcheck")

        # Test adding passes (ensure passes-sample.csv exists)
        run_command("admin --addpasses --source passes25.csv")
        
        
        # Test listing users
        run_command("admin --users")
        
        # Modify user (create/update)
        run_command("admin --usermod --username testuser --passw newpass")

        # Modify user (create/update)
        run_command("admin --usermod --username testuser3 --passw newpass --role ministry")
        
        # Logout
        run_command("logout")
        
        # Healthcheck without token (should fail)
        run_command("healthcheck")
        
        # Missing arguments for login
        run_command("login --username admin")

        run_command(f"login --username {user['username']} --passw {user['password']}")

        # Test toll station passes retrieval
        run_command("tollstationpasses --station AM08 --from 20220127 --to 20220210 --format json")

        # Test toll station passes retrieval
        run_command("tollstationpasses --station NAO01 --from 20220101 --to 20221212 --format json")

        # Test toll station passes retrieval(wrong date)
        run_command("tollstationpasses --station NAO01 --from 2022010 --to 20221212 --format json")
        
        # Test pass analysis
        run_command("passanalysis --stationop AM --tagop NAO --from 20220127 --to 20220210 --format json")

        # Test pass analysis(missing operator)
        run_command("passanalysis --tagop NAO --from 20220127 --to 20220210 --format json")

        # Test pass analysis
        run_command("passanalysis --stationop NAO --tagop NO --from 20220101 --to 20221212 --format csv")
        
        # Test passes cost
        run_command("passescost --stationop AM --tagop NAO --from 20220101 --to 20221212 --format json")

        # Test passes cost
        run_command("passescost --stationop AM --tagop NO --from 20220101 --to 20221212 --format csv")

        # Test passes cost(invalid format)
        run_command("passescost --stationop AM --tagop NO --from 20220101 --to 20221212 --format txt")
        
        # Test charges by operator
        run_command("chargesby --opid NAO --from 20220127 --to 20220210 --format json")
        

        # Charges by operator
        run_command("chargesby --opid AM --from 20220101 --to 20221212 --format csv")

        # Logout
        run_command("logout")
        time.sleep(1)
        
    print("\n=== CLI Tests Completed ===")

if __name__ == "__main__":
    test_cli()
