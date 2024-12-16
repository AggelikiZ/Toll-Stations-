#!/usr/bin/env python3

import argparse  
import requests  

# Healthcheck function to check system health
def healthcheck():
    url = "http://localhost:9115/api/admin/healthcheck"
    
    try:
        response = requests.get(url, timeout=5)
        response.raise_for_status()  # Raise HTTPError for bad responses (4xx or 5xx)
        print("Healthcheck successful:", response.json())
    except requests.exceptions.RequestException as e:
        print("Error performing healthcheck:", e)

# Tollstationpasses function to fetch toll station passes
def tollstationpasses(args):
    url = f"http://localhost:9115/api/tollStationPasses/{args.station}/{args.from_date}/{args.to_date}"
    
    try:
        # Adding format to the request (default to JSON)
        response = requests.get(url, params={"format": "json"}, timeout=5)
        response.raise_for_status()
        print("Toll station passes retrieved:", response.json())
    except requests.exceptions.RequestException as e:
        print("Error fetching toll station passes:", e)

# Main function to parse command-line arguments and dispatch commands
def main():
    # Create the argument parser
    parser = argparse.ArgumentParser(description="CLI for toll station system")
    
    # Define subcommands (e.g., "healthcheck") and their arguments
    subparsers = parser.add_subparsers(dest="command", required=True)  # `required=True` for Python 3.7+

    # Add a subcommand for "healthcheck"
    subparsers.add_parser("healthcheck", help="Check system health") 

    # Add a subcommand for "tollstationpasses"
    toll_parser = subparsers.add_parser("tollstationpasses", help="Retrieve toll station passes")
    toll_parser.add_argument("--station", required=True, help="Toll station ID")
    toll_parser.add_argument("--from_date", required=True, help="Start date (YYYYMMDD)")
    toll_parser.add_argument("--to_date", required=True, help="End date (YYYYMMDD)")
    toll_parser.set_defaults(func=tollstationpasses)

    # Parse the arguments provided by the user
    args = parser.parse_args()

    # Dispatch the appropriate function based on the command
    if args.command == "healthcheck":
        healthcheck()
    elif args.command == "tollstationpasses":
        args.func(args)

if __name__ == "__main__":
    main()