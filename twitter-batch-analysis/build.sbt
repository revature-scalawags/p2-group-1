scalaVersion := "2.12.10"

name := "twitter-batch-analysis"
version := "1.0"
organization := "com.revature"

libraryDependencies += "org.apache.spark" %% "spark-core" % "3.0.1"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.0.1"
libraryDependencies += "com.github.seratch" %% "awscala-s3" % "0.8.+"
libraryDependencies += "org.apache.hadoop" % "hadoop-aws" % "2.8.3"

assemblyMergeStrategy in assembly := {
 case PathList("META-INF", xs @ _*) => MergeStrategy.discard
 case x => MergeStrategy.first
}