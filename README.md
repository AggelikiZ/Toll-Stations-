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
  - [Prerequisites](#prerequisites)
- [Usage](#usage)
  - [Database](#database)
  - [Back-End](#back-end)
  - [Front-End](#front-end)
  - [CLI Client](#cli-client)
  - [Testing](#testing)

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

### Cloning the Repository

#### Clone the repository to your local machine:
```sh
git clone https://github.com/ntua/softeng24-25.git
```

### Database
#### Setting Up the Database
1. Ensure the database server is running by executing the command
```sh
net start MariaDB
```
2. Access the database shell with your credentials.
```sh
mysql -u root -p
```
3. Run the schema (DDL.sql) to create tables:
 ```sh
SOURCE /absolute/path/to/your/project/back-end/database/DDL.sql;
```

Make sure to replace /absolute/path/to/your/project/ with the actual path of the directory where you cloned the project.


4. Run the triggers.sql to ensure data integrity:
 ```sh
SOURCE /absolute/path/to/your/project/back-end/database/triggers.sql;
```
5. (Optional) :


The DML_test.sql contains data insertions into User and Operator tables for the basic toll station operators.


Run the script or manually add them later (through CLI).
 ```sh
SOURCE /absolute/path/to/your/project/back-end/database/DML_test.sql;

```
6. Exit the database shell
 ```sh
EXIT;
```

#### Configuring the Database Connection
1. Navigate to the application.properties of the project:
```sh
cd yourpath/back-end/rest_api/src/main/resources/application.properties;
```
2. Configure the database connection with your credentials.


The name of the database should remain 'paywaydb' or you should also change it in the sql scripts.
```sh
spring.datasource.url=jdbc:mariadb://localhost:3306/paywaydb
spring.datasource.username = your_username
spring.datasource.password = your password
```

### Back-end

1. Navigate to the rest_api directory of your project
 ```sh
cd /absolute/path/to/your/project/back-end/rest_api
```
2. Compile and build the project using Maven
 ```sh
mvn clean install
```


This will download dependencies and compile the project.
3. Run the Backend Server
 ```sh
mvn spring-boot:run
```
If everything is correct, the backend API will now be running at: http://localhost:9115/api


### Front-end
1. Navigate to the frot-end directory of your project.
 ```sh
cd /absolute/path/to/your/project/front-end
```
2. Run the following command to install all required Node.js packages.
 ```sh
npm install
```
3. Then start the frontend.
 ```sh
npm start
```

If everything is correct, the frontend will be available at: http://localhost:3000

### CLI Client

#### 1.Navigate to the CLI directory  
```bash
cd ../../cli-client
```
#### 2.Install Requirements
After installing Python from the requirements table, run the following command to install the necessary packages:
```bash
pip install -r requirements.txt
```
#### 3.Access the CLI

You can access the CLI through the terminal using the ./se2425 command followed by the action you want to perform.
Below is an example usage for retrieving toll station passes:
```bash
./se2425 tollstationpasses --station NAO01 --from 20220101 --to 20221212
```
#### 4.CLI Commands  

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
####  5.Viewing Available Commands  
You can use the following command to list all available CLI options:  
```bash
./se2425 --help
```
### Testing

#### Running API functional testing in Postman

1. Import the collection and environment from /backend/functional_testing in Postman.
2. Make sure the API is running.
3. Set the environment of the collection as the imported one.
4. Run the collection in Postman using 5 iterations for better results.


####  ClI Testing
##### Running CLI Functional Tests
1.Navigate to the CLI directory
```bash
cd ../../cli-client/test/functional-test
```
2.Run one of the scripts below
```bash
python cli_testing.py cli_testing.py
```
```bash
python cli_testingv2.py
```
Note: cli_testingv2.py requires the se2425.bat file on Windows.
##### Running CLI Unit Test
1.Navigate to the CLI directory
```bash
cd ../../cli-client/test/unit-test
```
2.Run the following script
```bash
python cli-unit-test.py
```



