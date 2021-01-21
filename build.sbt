scalaVersion := "2.12.10"

name := "project-2"
version := "1.0"
organization := "com.revature"

libraryDependencies += "com.azure" % "azure-ai-textanalytics" % "5.0.0"
libraryDependencies += "com.github.seratch" %% "awscala-s3" % "0.8.+"
libraryDependencies += "org.apache.hadoop" % "hadoop-aws" % "2.8.3"
libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5.12"
libraryDependencies += "org.apache.spark" %% "spark-core" % "3.0.1"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.0.1"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"

libraryDependencies += "org.apache.spark" %% "spark-streaming" % "3.0.1"
libraryDependencies += "org.apache.bahir" %% "spark-streaming-twitter" % "2.4.0"
libraryDependencies += "org.apache.spark" %% "spark-sql-kafka-0-10" % "3.0.1"
libraryDependencies += "com.danielasfregola" %% "twitter4s" % "7.0"

assemblyMergeStrategy in assembly := {
 case PathList("META-INF", xs @ _*) => MergeStrategy.discard
 case x => MergeStrategy.first
}