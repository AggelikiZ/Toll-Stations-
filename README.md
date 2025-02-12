# PAYWAY

Repository, used for NTUA/ECE Software Engineering, 2024-2025, team softeng24-25

## Team 25 - Contributors

- Anastasia Askouni el20046@mail.ntua.gr
- Nikolaos Verras el21440@mail.ntua.gr
- Angeliki Zerva el21101@mail.ntua.gr
- Dimitrios Kampanakis el21012@mail.ntua.gr

## Description

Payway App provides a connection between owners of motorways to settle economical differences with ease. This App provides this service and also some data analysis on teh usage of motorways and the toll stations.

## Table of contents

- [Installation](#installation)
  - [Prerequisities](#Prerequisities)
  - [Installation Process](#Installation)
  - [CLI Installation](#cli-installation) 
- [Usage](#usage)
  - [Back-End](#back-end)
  - [API Documentation](#documentation)
  - [CLI Client](#cli-client)
  - [Front-End](#front-end)
  - [Documentation](#documentation)
  - [AI Assistance Log](#ai_assistance_log)


## Installation
### Prerequisites
Before running the application, ensure you have the met the following software requirements:

| **Tool**            | **Version** |    **Installation Link** | **Purpose**|
|------------------------|-------------------|-----------------|------------|
| Java Development Kit (JDK) | 17 or higher | [Download JDK](https://adoptium.net/) | Required to run the application |
| Apache Maven | 3.9 or higher | [Download Maven](https://maven.apache.org/download.cgi) | Used for building and managing dependencies |
| MariaDB Server | 10.6 or higher | [Download MariaDB](https://mariadb.org/download/) | Database management (MySQL can also be used) |
| Node.js | 20.9 or higher | [Download Node.js](https://nodejs.org/) | Required to run the frontend (React) |
| npm (included with Node.js) | Latest | Included with [Node.js](https://nodejs.org/) | Manages frontend dependencies |
| Git| Latest | [Download Git](https://git-scm.com/downloads) | Required for cloning and managing the repository |
| Python| 3.8 or higher | [Download Python](https://www.python.org/downloads/) | Required for CLI tools |
| pip (included with Python) | Latest | Included with [Python](https://www.python.org/downloads/) | Manages Python package dependencies |
| Postman|	Latest	| [Download Postman](https://www.postman.com/downloads/) | (Optional) Used for testing API endpoints |

It is advised that all software requirements are downloaded and installed by the latest version.



## Usage
### CLI Client

#### 1.Navigate to the CLI directory  
```bash
cd ../../cli-client
```
#### 2.Access the CLI

You can access the CLI through the terminal using the ./se2425 command followed by the action you want to perform.
Below is an example usage for retrieving toll station passes:
```bash
./se2425 tollstationpasses --station NAO01 --from 20220101 --to 20221212
```
#### 3.CLI Commands  

The CLI includes several commands that allow interaction with the backend. Below is the list of available commands:  

| **Command**            | **Description** |
|------------------------|------------------------------------|
| `login`               | Login to the system |
| `logout`              | Logout from the system |
| `admin`               | Admin commands |
| `healthcheck`         | Check system health |
| `resetstations`       | Reset toll stations |
| `resetpasses`         | Reset all toll passes |
| `tollstationpasses`   | Retrieve toll station passes |
| `passanalysis`        | Analyze passes between two operators |
| `passescost`          | Retrieve pass cost between two operators |
| `chargesby`           | Retrieve charges by operator |
| `help`                | Show all the possible arguments |

**Run any command using the following format:**  
```bash
./se2425 <command> [--options]
```
####  4.Viewing Available Commands  
You can use the following command to list all available CLI options:  
```bash
./se2425 --help
```
## AI_Assistance_Log




