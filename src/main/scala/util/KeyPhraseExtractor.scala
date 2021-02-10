package util

import com.azure.ai.textanalytics.models._
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder
import com.azure.ai.textanalytics.TextAnalyticsClient
import com.azure.core.credential.AzureKeyCredential
import com.typesafe.scalalogging.LazyLogging

/** This object contains the textAnalytics client which processes tweet text for keyword extraction.
  */
object KeyPhraseExtractor extends LazyLogging {
  val key = System.getenv("ANALYTICS_TOKEN")
  val endpoint = System.getenv("ANALYTICS_ENDPOINT")
  val client = authenticateClient(key, endpoint)

  /** authenticateClient
    * Authenticates and creates the client that interacts with the azure textanalytics server.
    *
    * @param key - the textAnalytics key obtained from the azure API
    * @param endpoint - the textAnalytics endpoint location in the azure API
    * @return - the created and authenticated client
    */
  def authenticateClient(key: String, endpoint: String): TextAnalyticsClient = {
    logger.info(s"authenticating client on $endpoint with analytics token $key")
    return new TextAnalyticsClientBuilder()
      .credential(new AzureKeyCredential(key))
      .endpoint(endpoint)
      .buildClient()
  }

  /** extractPhrases
    * takes in the input text and runs the text through the Azure API to extract
    * the key phrases from the text that esablishes the 'main idea' of the text.
    *
    * @param inputText - the text to analyze
    * @return - an array that lists the found keywords in the text
    */
  def extractPhrases(inputText: String): Array[String] = {
    logger.info(s"extracting key phrases from string: $inputText")
    var wordList = Array.empty[String]
    val keyWords = client.extractKeyPhrases(inputText)
    keyWords.forEach(str => wordList = wordList :+ str)
    logger.info(s"returning list of keywords: $wordList")
    return wordList
  }
}
