#!/usr/bin/env python3

import argparse
import requests


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
        files = {'file': open(args.file, 'rb')}  # Upload the CSV file
        response = requests.post(url, files=files, timeout=5)
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


# Function to add passes
def addpasses(args):
    url = "http://localhost:9115/api/admin/addpasses"
    try:
        files = {'file': open(args.file, 'rb')}  # Upload the CSV file
        response = requests.post(url, files=files, timeout=5)
        response.raise_for_status()
        print("Passes added successfully.")
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

    # Subcommand: healthcheck
    health_parser = subparsers.add_parser("healthcheck", help="Check system health")
    health_parser.set_defaults(func=healthcheck)

    # Subcommand: resetstations
    resetstations_parser = subparsers.add_parser("resetstations", help="Reset toll stations")
    resetstations_parser.add_argument("--file", required=True, help="Path to CSV file with station data")
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
