package main

import org.apache.spark.SparkContext
import org.apache.spark.sql._
import org.apache.spark.sql.functions.explode
import org.apache.spark.sql.functions.split
import org.apache.spark.sql.functions.lower

import tweet.Tweets
import java.io.File
import scala.collection.mutable.ArrayBuffer

import org.apache.http.impl.client.HttpClients
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.config.CookieSpecs
import org.apache.http.client.utils.URIBuilder
import org.apache.http.client.methods.HttpGet
import org.apache.http.util.EntityUtils

//import org.apache.commons.lang3

import com.typesafe.scalalogging.LazyLogging


object ComparisonAnalysis extends LazyLogging {
    
    def main(args: Array[String]): Unit = {

        val keywords = (args(0), args(1))

        // Twitter key
        logger.info("Start of new execution.")

        // Twitter key
        logger.info("getting bearer token from environment variables")
        val twitBearerToken = System.getenv("BEARER_TOKEN")
        logger.info(s"bearer token acquired: $twitBearerToken")

            // Build HttpClient
        logger.info("Building HTTP client.")
        val httpClient = HttpClients.custom
        .setDefaultRequestConfig(
            RequestConfig.custom.setCookieSpec(CookieSpecs.STANDARD).build
        )
        .build
        logger.info("HTTP client build complete.")

        // Build URI and set headers for each query
        logger.info("Building URI and setting headers.")
        val uriBuilder1 = new URIBuilder(
        s"https://api.twitter.com/2/tweets/search/recent?query=${keywords._1}&max_results=10&start_time=2021-01-22T05:00:00Z"
        )
        val uriBuilder2 = new URIBuilder(
        s"https://api.twitter.com/2/tweets/search/recent?query=${keywords._2}&max_results=10&start_time=2021-01-22T05:00:00Z"
        )

        val httpGet1 = new HttpGet(uriBuilder1.build)
        httpGet1.setHeader("Authorization", s"Bearer ${twitBearerToken}")
        logger.info("Headers set.")
        val httpGet2 = new HttpGet(uriBuilder2.build)
        httpGet2.setHeader("Authorization", s"Bearer ${twitBearerToken}")
        logger.info("Headers set.")

        // Get response from each query
        logger.info("Attempting response from the HTTP client.")
        val response1 = httpClient.execute(httpGet1)
        val entity1 = response1.getEntity()
        val response2 = httpClient.execute(httpGet2)
        val entity2 = response2.getEntity()

        // Build SparkSession and Context
        val spark = SparkSession
            .builder()
            .appName("Twitter-Batch-Analysis")
            .master("local[4]")
            .getOrCreate()
        val sc = spark.sparkContext
        sc.setLogLevel("WARN")
        
        if (entity1 != null && entity2 != null) {
            // Convert response to string
            val responseString1 = EntityUtils.toString(entity1)
            //println(responseString)

            // Converts the json data into Tweet objects and flattens them into a dataframe of lowercase words
            import spark.implicits._
            val tweetSet1 = spark.read.json(Seq(responseString1).toDS).as[Tweets]
            val tweetsFrame1 = tweetSet1.select(explode($"data"))
            val wordsFrame1 = tweetsFrame1.select(explode(split($"col.text", " ")).as("words"))
            val loweredFrame1 = wordsFrame1.select(lower($"words").as("words"))//.filter(lang3.StringUtils.isAlpha(_.toSeq))
            
            loweredFrame1.createOrReplaceTempView("words1")
            
            // Creates a dataframe of each unique word and its count
            //filter @, numbers, commas and periods?????
            val wordCounts1: DataFrame = spark
                .sql(
                    "SELECT words, count(words) as count FROM words1 WHERE (words != \"rt\") GROUP BY words ORDER BY count DESC"
                )
                .cache()

            wordCounts1.show

            val responseString2 = EntityUtils.toString(entity2)
            val tweetSet2 = spark.read.json(Seq(responseString2).toDS).as[Tweets]
            val tweetsFrame2 = tweetSet2.select(explode($"data"))
            val wordsFrame2 = tweetsFrame2.select(explode(split($"col.text", " ")).as("words"))
            val loweredFrame2 = wordsFrame2.select(lower($"words").as("words"))//.filter(lang3.StringUtils.isAlpha(_.toSeq))
            
            loweredFrame2.createOrReplaceTempView("words2")
            val wordCounts2: DataFrame = spark
                .sql(
                    "SELECT words, count(words) as count FROM words2 WHERE (words != \"rt\") GROUP BY words ORDER BY count DESC"
                )
                .cache()

            wordCounts2.show
            
            







            // val tweetArray = flattened
            //     .select("col.text")
            //     .collect
            //     .map(_.toSeq)
            //     .flatten
            //     .map(str => str.toString().filter(_ >= ' '))

            //tweetArray.foreach(println)
            

            /*
            // Extract all the words from each tweet, creating a 2D matrix of tweets and their words
            var tweetsWords = ArrayBuffer(Array.empty[String])
            for (tweet <- tweetArray){
                //tweet.toLowerCase().split(" ").foreach(println)
                tweetsWords += tweet.toLowerCase().split(" ")//.filter(_ != "rt") also 
            }
            println("HERE!!!!!!!!!!")
            println(tweetsWords)
            tweetsWords.foreach(println)
            */


        }

        sc.stop()
    }
  
}
