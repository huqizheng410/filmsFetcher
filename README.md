
# Film Fetcher Assessment

This project aims to scrape, store, and process movie and TV series data 
from the external website www.elcinema.com. The collected data is stored 
in a MySQL database and AWS DynamoDB tables, categorized by type. In the 
event of an error, the data can also be automatically exported to an Excel file.

## Prerequisites

Before you begin, ensure you have met the following requirements:
- Java JDK 8 or later installed.
- Git installed.

## Table of Contents

- [Project Structure](#project-structure)
- [Tools and Technologies](#tools-and-technologies)
- [Design Architecture](#design-architecture)
- [Features](#features)
- [Setup and Installation](#setup-and-installation)
- [Usage](#usage)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

## Project Structure

```
project-root
│
├── src
│   ├── main
│   │   └── java
│   │       └── com
│   │           └── qizheng_filmsFetcher
│   │               ├── DatabaseHelper.java
│   │               ├── DynamoDBHelper.java
│   │               ├── ExcelHelper.java
│   │               ├── Movie.java
│   │               ├── MovieScraper.java
│   │               └── WebScraper.java
│   └── test
│       └── java
│           └── com
│               └── qizheng_filmsFetcher
│                   ├── DatabaseHelperTest.java
│                   ├── DynamoDBHelperTest.java
│                   ├── ExcelHelperTest.java
│                   ├── MovieScraperTest.java
│                   └── WebScraperTest.java
│
├── pom.xml
└── README.md
```

## Tools and Technologies

- **Java**: The primary programming language used for the project.
- **JUnit**: Used for writing and running tests.
- **Mockito**: Used for mocking objects in tests.
- **Apache POI**: Used for writing data to Excel files.
- **Jsoup**: Used for web scraping.
- **MySQL**: The relational database used to store the scraped data.
- **AWS DynamoDB**: The database used to store the other data.
- **Maven**: The build automation tool used for managing dependencies and building the project.

## Design Architecture

The project follows a modular design where each functionality is encapsulated within its own class:

- **`DatabaseHelper`**: Manages the connection to the MySQL database and handles operations like ensuring the table exists and batch inserting movies.
- **`DynamoDBHelper`**: Manages the connection to AWS DynamoDB and handles operations like ensuring the table exists and batch writing data.
- **`ExcelHelper`**: Handles writing movie data to an Excel file.
- **`Movie`**: Represents a movie entity with various attributes like title, genre, release date, etc.
- **`MovieScraper`**: The main class that orchestrates the scraping of movie data, storing it in the database, and exporting it to an Excel file.
- **`WebScraper`**: Handles the actual web scraping logic using Jsoup to fetch and parse movie data from the specified URLs.

### MySQL

The MySQL database is named `assessment` and contains two tables: `movies` and `tv_shows`.

#### `movies` Table

| Column        | Type         | Description                    |
|---------------|--------------|--------------------------------|
| id            | int          | Primary key, auto-increment    |
| title         | varchar(255) | Title of the movie             |
| rating        | varchar(100) | Rating of the movie            |
| created_at    | timestamp    | Timestamp of record creation   |
| release_year  | varchar(255) | Release year of the movie      |

#### `tv_shows` Table

| Column        | Type         | Description                    |
|---------------|--------------|--------------------------------|
| id            | int          | Primary key, auto-increment    |
| title         | varchar(255) | Title of the TV show           |
| rating        | varchar(100) | Rating of the TV show          |
| created_at    | timestamp    | Timestamp of record creation   |
| release_year  | varchar(255) | Release year of the TV show    |

### DynamoDB

The DynamoDB table is named `oliver_assessment` and contains the following attributes:

| Attribute     | Type   | Description                       |
|---------------|--------|-----------------------------------|
| elcinema_id   | String | Primary key, unique identifier    |
| title         | String | Title of the movie or TV show     |
| genre         | String | Genre of the movie or TV show     |
| releaseDate   | String | Release date                      |
| releaseYear   | String | Release year                      |
| duration      | String | Duration                          |
| language      | String | Language                          |
| country       | String | Country                           |
| director      | String | Director                          |
| rating        | String | Rating                            |
| description   | String | Description                       |

### Attributes Mapping Example

- `genre`
- `releaseDate`
- `releaseYear`
- `duration`
- `language`
- `country`
- `director`
- `rating`
- `description`

## Features

- **Web Scraping**: Scrapes movie and TV series data from the specified website using Jsoup.
- **Data Storage**: Stores the scraped data in both MySQL and AWS DynamoDB databases.
- **Data Export**: Exports the scraped data to an Excel file.
- **Concurrency**: Utilizes multithreading to scrape multiple pages concurrently.
- **Testing**: Includes comprehensive unit tests for each major component using JUnit and Mockito.

## Setup and Usage

1. **Clone the repository**:
    ```sh
    git clone https://github.com/huqizheng410/filmsFetcher.git
    cd filmsFetcher
    ```

2. **Install maven (Recommended)**:
   
   MacOS: 
    ```sh
    brew install maven
    ```
   
   Windows: 
   - Go to the [Apache Maven download page](https://maven.apache.org/download.cgi).


3. **Install the dependency**:
    ```sh
    mvn clean install
    ```

3. **Run the main class**:
    ```sh
    mvn exec:java -Dexec.mainClass="com.qizheng_filmsFetcher.MovieScraper"
    ```
   Or click the run button on [MovieScraper.](./src/main/java/com/qizheng_filmsFetcher/MovieScraper.java)

## Testing

Run the tests using Maven:

```sh
mvn test
```

This will execute all the unit tests in the `src/test/java/com/qizheng_filmsFetcher` directory.

## Contact

If you have any suggestions or questions, feel free to contact me at [huqizheng@outlook.com](mailto:huqizheng@outlook.com).