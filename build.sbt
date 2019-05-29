organization := "com.example"
version := "0.1"

lazy val scalaLibraries = new {
    val catsVersion = "1.6.0"
    val catsEffectVersion = "1.2.0"
    val scalaTestVersion = "3.0.5"
    val specs2Version = "4.3.5"
    val logBackVersion = "1.2.3"
    val scalaLoggingVersion = "3.9.2"
    val catsTaglessVersion = "0.5"
    val akkaVersion = "2.5.23"

    // Test
    val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion % Test
    val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.14.0" % Test
    // Cats
    val catsCore = "org.typelevel" %% "cats-core" % catsVersion
    val catsFree = "org.typelevel" %% "cats-free" % catsVersion
    val catsEffect = "org.typelevel" %% "cats-effect" % catsEffectVersion
    val taglessMacros = "org.typelevel" %% "cats-tagless-macros" % catsTaglessVersion
    val taglessLegacy = "org.typelevel" %% "cats-tagless-legacy-macros" % catsTaglessVersion
    val mtlCore = "org.typelevel" %% "cats-mtl-core" % "0.2.1"
    // Other
    val typesafeConfig = "com.typesafe" % "config" % "1.3.1"
    // Logging
    val logback = "ch.qos.logback" % "logback-classic" % logBackVersion
    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
    // Akka
    val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
}

lazy val scalaDependencies = Seq(
    scalaLibraries.scalaTest,
    scalaLibraries.scalaCheck,
    scalaLibraries.catsCore,
    scalaLibraries.catsFree,
    scalaLibraries.catsEffect,
    scalaLibraries.taglessMacros,
    scalaLibraries.taglessLegacy,
    scalaLibraries.mtlCore,
    scalaLibraries.logback,
    scalaLibraries.scalaLogging,
    scalaLibraries.akkaStream
)
lazy val ScalaCaseStudy = project

        .settings(
            name := "ScalaCaseStudy",
            scalaVersion := "2.12.8",
            libraryDependencies ++= scalaDependencies,
            addCompilerPlugin("org.scalameta" %% "paradise" % "3.0.0-M11" cross CrossVersion.full),
            addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4"),
            scalacOptions ++= Seq(
                "-Xplugin-require:macroparadise",
                "-language:higherKinds",
                "-deprecation",
                "-encoding", "UTF-8",
                "-Ypartial-unification",
                "-feature",
                "-language:_"
            )
        )


lazy val javaLibraries = new {
    val junitVersion = "4.12"
    val log4jVersion = "1.2.17"
    val slf4jVersion = "1.7.25"

    val junit = "junit" % "junit" % junitVersion % Test
    val log4j = "log4j" % "log4j" % log4jVersion
    val slf4j = "org.slf4j" % "slf4j-api" % slf4jVersion
}

lazy val javaDependencies = Seq(
    javaLibraries.junit,
    javaLibraries.log4j,
    javaLibraries.slf4j
)

lazy val JavaCaseStudy = project
        .settings(
            name := "JavaCaseStudy",
            resolvers ++= Seq(
                "Maven central repo" at "https://repo1.maven.org/maven2/"
            ),
            libraryDependencies ++= javaDependencies
        )
