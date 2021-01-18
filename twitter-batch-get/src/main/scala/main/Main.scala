package main

import org.apache.http.impl.client.HttpClients
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.config.CookieSpecs
import org.apache.http.client.utils.URIBuilder
import org.apache.http.client.methods.HttpGet
import org.apache.http.util.EntityUtils

import util.S3Client

/**
  * twitter-batch-get is the program that is intended to gather histroical data from the Twitter API
  * and store it on the S3 bucket s3://cjohn281-twit-lake/batch/data.json
  */
object Main {
  def main(args: Array[String]): Unit = {

    // Twitter key
    val twitBearerToken = System.getenv("BEARER_TOKEN")

    // S3 keys
    val key = System.getenv("AWS_ACCESS_KEY_ID")
    val secret = System.getenv("AWS_SECRET_ACCESS_KEY")
    val client = S3Client.buildS3Client(key, secret)      // Build the S3 client with access keys

    // Build HttpClient
    val httpClient = HttpClients.custom.setDefaultRequestConfig(
      RequestConfig.custom.setCookieSpec(CookieSpecs.STANDARD).build
    ).build

    // Build URI and set headers
    val uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/recent?query=insurrection&max_results=10") // Replace this after determining project goals
    val httpGet = new HttpGet(uriBuilder.build)
    httpGet.setHeader("Authorization", s"Bearer ${twitBearerToken}")

    // Get response
    val response = httpClient.execute(httpGet)
    val entity = response.getEntity()
    if (entity != null) {
      // Convert response to string and put in S3 bucket
      val responseString = EntityUtils.toString(entity, "UTF-8")                    // Replace this after determining project goals
      client.putObject("cjohn281-twit-lake/batch", "data.json", responseString)     // Replace this after determining project goals
    }
  }
}