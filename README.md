# p2-group-1
Project 2 for Group 1 (Cameron, Carrel, Chris, Tristan). Spark storage and manipulation of Twitter datasets

## Technologies
- Apache Spark
- Spark SQL
- YARN
- HDFS and/or S3
- Scala
- Git + GitHub

## Objectives
- Analyze both streamed and historical data from Twitter using its respective API's
- Construct .jar packages from a scala project that executes the necessary analyses
- Store and access the API data in an AWS cloud service EMR
- Produce a simple set of slides that displays our findings in the analyses
- Any additional mentions in the respective [project requirements page](https://github.com/revature-scalawags/notes/blob/main/Project%202.md)

## Project Setup and Execution
### Setup
 1. clone/pull repo files into a project folder
 2. acquire the appropriate keys from AWS, Twitter, and the Azure TextAnalytics servers:
      AWS_ACCESS_KEY_ID=
      AWS_SECRET_ACCESS_KEY=
      BEARER_TOKEN=
      ANALYTICS_TOKEN=
      ANALYTICS_ENDPOINT=
      TWITTER_CONSUMER_TOKEN_KEY=
      TWITTER_CONSUMER_TOKEN_SECRET=
      TWITTER_ACCESS_TOKEN_KEY=
      TWITTER_ACCESS_TOKEN_SECRET=
 3. Place the keys into a .env file
 4. proceed to execution
 
### Execution
#### Get:
 1. change the active dates on line 53 to the day before the current one
 1. console input: sbt "run ARG1"
 2. console input: 3
 3. resume to analysis
#### Analysis:
 1. console input: sbt run
 2. results output to console
#### Comparison Analysis:
 1. change the active dates on lines 52 and 55 to the day before the current one
 2. console input: sbt "run ARG1 ARG2"
 3. results output to console
#### Streaming:
 1. console input: sbt run
 2. let run for 1 minute
 3. output will be located in a file called 'tweetText.txt'
