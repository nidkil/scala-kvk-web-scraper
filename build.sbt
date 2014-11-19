import com.typesafe.sbteclipse.plugin.EclipsePlugin._

name := "scala-kvk-web-scraper"

organization := "com.nidkil"

version := "0.3.3"

scalaVersion := "2.11.2"

EclipseKeys.withSource := true

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

resolvers += "Typesafe Maven Repository" at "http://repo.typesafe.com/typesafe/maven-releases/"

libraryDependencies ++= Seq(
	"org.scala-lang.modules"     %% "scala-xml"       % "1.0.2"          withSources() withJavadoc(),
	"org.apache.httpcomponents"   % "httpclient"      % "4.1.1"          withSources()              ,
	"com.github.scopt"           %% "scopt"           % "3.2.0"          withSources() withJavadoc(),
	"com.typesafe.scala-logging" %% "scala-logging"   % "3.1.0"          withSources() withJavadoc(),
	"ch.qos.logback"              % "logback-classic" % "1.1.2"          withSources() withJavadoc(),
	"com.typesafe.play"          %% "play-json"       % "2.3.0"          withSources()              ,
	"org.jsoup"                   % "jsoup"           % "1.6.3"          withSources() withJavadoc(),
	"org.scalatest"               % "scalatest_2.11"  % "2.2.1" % "test" withSources() withJavadoc()
)