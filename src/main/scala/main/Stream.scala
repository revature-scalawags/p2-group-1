package main

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Duration, StreamingContext}

import com.danielasfregola.twitter4s.TwitterRestClient
import com.danielasfregola.twitter4s.TwitterStreamingClient
import com.danielasfregola.twitter4s.entities.Tweet
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}

import com.typesafe.scalalogging.LazyLogging

import java.io._

import scala.concurrent._

/** This is the program that is intended to retrieve raw Twitter data from
  * a twitter streaming client (from Twitter4s) and count the mentions of 'covid'
  * per minute in the stream. Future functionality will allow for custom hashtag searching
  * and mini-batch processing of the streaming data.
  */
object Stream extends LazyLogging {
  def main(args: Array[String]): Unit = {

    //TwitterStreamingClient will pull Twitter keys from .env variables.
    //.env MUST CONTAIN THE FOLLOWING, in this exact format.
    /*
        TWITTER_CONSUMER_TOKEN_KEY=API_key
        TWITTER_CONSUMER_TOKEN_SECRET=API_secret_key
        TWITTER_ACCESS_TOKEN_KEY=access_token
        TWITTER_ACCESS_TOKEN_SECRET=access_token_secret
     */

    val runtime = 3 //minutes
    val runtimeMS = runtime * 60000 //stores runtime in milliseconds
    val streamingClient = TwitterStreamingClient()
    val trackedWords = Seq("#covid")
    var tweetCounter = 0
    val t0 = System.currentTimeMillis()
    val pw = new PrintWriter(new File("tweetText.txt"))

    /** This function checks the stream for Tweet objects that contain the hashtag(E.G. #covid) that we are searching for.
      * Then performs several analysis functions on the Tweet object
      */
    streamingClient.filterStatuses(tracks = trackedWords) { case tweet: Tweet =>
      println(tweet.text)
      println("Tweet Counter: " + tweetCounter)
      pw.append(tweet.text)
      tweetCounter += 1
      if (System.currentTimeMillis() - t0 > runtimeMS) {
        val tweetsPerMinute = tweetCounter / runtime
        println(
          "Twitter Users are tweeting about Covid " + tweetsPerMinute + " per minute on average"
        )
        streamingClient.shutdown()
        pw.close
      }
    }
  }
}
