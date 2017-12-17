name := "GoogleSheetsAPISample"

version := "1.0"

scalaVersion := "2.12.0"


lazy val root = (project in file("."))
  .enablePlugins(
    PlayScala
  ).settings(
  libraryDependencies ++= Seq(
    guice,
    "com.typesafe" % "config" % "1.3.2",
    "com.google.api-client" % "google-api-client" % "1.23.0",
    "com.google.oauth-client" % "google-oauth-client-jetty" % "1.23.0",
    "com.google.apis" % "google-api-services-sheets" % "v4-rev486-1.23.0"
  )
)