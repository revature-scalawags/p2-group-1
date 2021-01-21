package main

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Duration, StreamingContext}

import com.danielasfregola.twitter4s.TwitterRestClient
import com.danielasfregola.twitter4s.TwitterStreamingClient
import com.danielasfregola.twitter4s.entities.Tweet
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}

import com.typesafe.scalalogging.LazyLogging


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

    val streamingClient = TwitterStreamingClient()

    val trackedWords = Seq("#fortnite", "#glitch")

    streamingClient.filterStatuses(tracks = trackedWords) { case tweet: Tweet =>
      println(tweet.text)
    }

    //test
  }
}
