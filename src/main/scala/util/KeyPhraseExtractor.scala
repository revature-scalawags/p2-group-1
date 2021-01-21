package util

import com.azure.ai.textanalytics.models._
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder
import com.azure.ai.textanalytics.TextAnalyticsClient
import com.azure.core.credential.AzureKeyCredential
import com.typesafe.scalalogging.LazyLogging

object KeyPhraseExtractor extends LazyLogging{
	val key = System.getenv("ANALYTICS_TOKEN")
	val endpoint = System.getenv("ANALYTICS_ENDPOINT")
	val client = authenticateClient(key,endpoint)

	def authenticateClient (key: String, endpoint: String): TextAnalyticsClient = {
		logger.info(s"authenticating client on $endpoint with analytics token $key")
		return new TextAnalyticsClientBuilder().credential(new AzureKeyCredential(key)).endpoint(endpoint).buildClient()
	}

	def extractPhrases(inputText: String): Array[String] = {
		logger.info(s"extracting key phrases from string: $inputText")
		var wordList = Array.empty[String]
		val keyWords = client.extractKeyPhrases(inputText)
		keyWords.forEach(wordList:+_)
		logger.info(s"returning list of keywords: $wordList")
		return wordList
	}
}