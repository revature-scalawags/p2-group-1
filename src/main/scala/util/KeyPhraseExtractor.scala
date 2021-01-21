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
		return new TextAnalyticsClientBuilder().credential(new AzureKeyCredential(key)).endpoint(endpoint).buildClient()
	}

	def extractPhrases(inputText: String) {
		val keyWords = client.extractKeyPhrases(inputText)
		
	}
}