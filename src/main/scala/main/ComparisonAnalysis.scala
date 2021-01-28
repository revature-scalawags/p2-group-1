package main

import org.apache.spark.SparkContext
import org.apache.spark.sql._
import org.apache.spark.sql.functions.explode

import tweet.Tweets
// import util.S3Client
// import util.KeyPhraseExtractor
import java.io.File

import org.apache.http.impl.client.HttpClients
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.config.CookieSpecs
import org.apache.http.client.utils.URIBuilder
import org.apache.http.client.methods.HttpGet
import org.apache.http.util.EntityUtils

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

        // Build URI and set headers
        logger.info("Building URI and setting headers.")
        val uriBuilder = new URIBuilder(
        s"https://api.twitter.com/2/tweets/search/recent?query=${keywords._1}&max_results=100&start_time=2021-01-22T05:00:00Z"
        )

        val httpGet = new HttpGet(uriBuilder.build)
        httpGet.setHeader("Authorization", s"Bearer ${twitBearerToken}")
        logger.info("Headers set.")

        // Get response
        logger.info("Attempting response from the HTTP client.")
        val response = httpClient.execute(httpGet)
        val entity = response.getEntity()

        // Build SparkSession and Context
        val spark = SparkSession
            .builder()
            .appName("Twitter-Batch-Analysis")
            .master("local[4]")
            .getOrCreate()
        val sc = spark.sparkContext
        sc.setLogLevel("WARN")


        
        if (entity != null) {
            // Convert response to string
            val responseString = EntityUtils.toString(entity)
            //println(responseString)

            // Converts the json data into Tweet objects and flattens them into an array of words
            import spark.implicits._
            val tweetSet = spark.read.json(Seq(responseString).toDS).as[Tweets]
            val flattened = tweetSet.select(explode($"data"))
            val tweetArray = flattened
                .select("col.text")
                .collect
                .map(_.toSeq)
                .flatten
                .map(str => str.toString().filter(_ >= ' '))
            
            tweetArray.foreach(println)
        }

        sc.stop()
    }
  
}
