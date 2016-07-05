logLevel := Level.Warn

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.beowulfe.play" % "ebean-jdk8-fix" % "3.3.1-SNAPSHOT")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.8")
