name := "autocourseassembly"

version := "1.0"

lazy val `autocourseassembly` = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(javaJdbc,
"com.beowulfe.play" % "ebean-jdk8-fix" % "3.3.1-SNAPSHOT",
  javaEbean,
  cache,
  javaWs,
  "org.apache.httpcomponents" % "httpcore" % "4.4.1",
  "org.postgresql" % "postgresql" % "9.4-1200-jdbc41",
  "com.typesafe.akka" % "akka-testkit_2.11" % "2.3.9",
  "commons-codec" % "commons-codec" % "1.10",
  "org.slf4j" % "slf4j-api" % "1.7.12",
  "com.typesafe.akka" % "akka-actor_2.11" % "2.3.4",
  "com.typesafe.akka" % "akka-stream-experimental_2.11" % "1.0-M5",
  "org.jsoup" % "jsoup" % "1.8.3",
  "org.apache.httpcomponents" % "httpclient" % "4.5.2")

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")
