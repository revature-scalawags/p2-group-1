package main

import org.apache.http.impl.client.HttpClients
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.config.CookieSpecs
import org.apache.http.client.utils.URIBuilder
import org.apache.http.client.methods.HttpGet
import org.apache.http.util.EntityUtils

import util.S3Client
import com.typesafe.scalalogging.LazyLogging

/** twitter-batch-get is the program that is intended to gather histroical data from the Twitter API
  * and store it on the S3 bucket s3://cjohn281-twit-lake/batch/data.json
  */
object Get extends LazyLogging {
  def main(args: Array[String]): Unit = {

    val keyword = args(0)

    // Twitter key
    logger.info("Start of new execution.")

    // Twitter key
    logger.info("getting bearer token from environment variables")
    val twitBearerToken = System.getenv("BEARER_TOKEN")
    logger.info(s"bearer token acquired: $twitBearerToken")

    // S3 keys
    logger.info("Acquiring AWS access keys from environment variables")
    val key = System.getenv("AWS_ACCESS_KEY_ID")
    val secret = System.getenv("AWS_SECRET_ACCESS_KEY")

    logger.info("AWS Access keys acquired, building S3 client with keys.")
    val client = S3Client.buildS3Client(
      key,
      secret
    ) // Build the S3 client with access keys
    logger.info("S3 client build complete.")

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
      s"https://api.twitter.com/2/tweets/search/recent?query=${keyword}&max_results=100&start_time=2021-01-20T05:00:00Z"
    )

    val httpGet = new HttpGet(uriBuilder.build)
    httpGet.setHeader("Authorization", s"Bearer ${twitBearerToken}")
    logger.info("Headers set.")

    // Get response
    logger.info("Attempting response from the HTTP client.")
    val response = httpClient.execute(httpGet)
    val entity = response.getEntity()
    if (entity != null) {
      // Convert response to string and put in S3 bucket
      val responseString = EntityUtils.toString(
        entity,
        "UTF-8"
      )
      // Write response to json file, store it on S3 bucket
      client.putObject(
        "cjohn281-twit-lake/batch",
        "data.json",
        responseString
      )
    }
    logger.info("HTTP client response completed. Execution complete.")
  }
}
