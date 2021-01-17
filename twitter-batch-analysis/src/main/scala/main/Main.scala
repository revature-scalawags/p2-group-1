package main

import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession

import tweet.Tweet
import util.S3Client


/**
  * twitter-batch-analysis is the program that is intended to retrieve raw Twitter data from 
  * s3://cjohn281-twit-lake/batch/data.json, process it in some way, and store the processed
  * data on s3://cjohn281-twit-warehouse/batch/data.json
  */
object Main {

  // Driver
  def main(args: Array[String]): Unit = {

    val key = System.getenv("AWS_ACCESS_KEY_ID")
    val secret = System.getenv("AWS_SECRET_ACCESS_KEY")
    val client = S3Client.buildS3Client(key, secret)      // Build the S3 client with access keys

    // Build SparkSession and Context
    val spark = SparkSession.builder().appName("Twitter-Batch-Analysis").master("local[4]").getOrCreate()
    val sc = spark.sparkContext
    sc.setLogLevel("WARN")

    // Reads in temp json data from S3 bucket and prints the schema; may be moved to separate method later
    val jsonFile = spark.read.option("multiline", "true").json("s3a://cjohn281-twit-lake/batch/TEST.json").cache()
    jsonFile.printSchema()

    // Converts the json data into Tweet objects and prints them to console; may be moved to a separate method later
    import spark.implicits._
    val userSet = jsonFile.as[Tweet]
    userSet.show()

    // TODO: process the twitter data in some way

    // TODO: push processed data to S3 /warehouse/batch/ bucket

    sc.stop()
  }
}