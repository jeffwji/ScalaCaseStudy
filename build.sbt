organization := "com.example"
version := "0.1"

lazy val logging = {
    val logBackVersion = "1.2.3"
    val scalaLoggingVersion = "3.9.2"
    Seq(
        "ch.qos.logback" % "logback-classic" % logBackVersion,
        "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
    )
}

lazy val chuusai = new {
    val shapelessVersion = "2.3.3"
    val shapeless = "com.chuusai" %% "shapeless" % shapelessVersion
    val scalacheck = "com.github.alexarchambault" %% "scalacheck-shapeless_1.14" % "1.2.3"
}

lazy val testing = {
    val scalaTestVersion = "3.0.8"
    val scalaMockVersion = "3.6.0"
    val scalaCheckVersion = "1.14.0"

    Seq(
        "org.scalacheck" %% "scalacheck"                  % scalaCheckVersion,
        "org.scalatest"  %% "scalatest"                   % scalaTestVersion,
        "org.scalamock"  %% "scalamock-scalatest-support" % scalaMockVersion,
    ).map(_ % Test)
}

lazy val benchmark = Seq(
    "com.storm-enroute" %% "scalameter" % "0.19"
).map(_ % Test)

lazy val typelevel = new {
    val catsVersion = "1.6.0"
    val catsEffectVersion = "1.3.1"
    val catsTaglessVersion = "0.9"
    val kittensVersion = "1.1.0"
    val spireVersion = "0.16.2"
    val mtlVersion = "0.5.0"

    val catsCore = "org.typelevel" %% "cats-core" % catsVersion
    val catsFree = "org.typelevel" %% "cats-free" % catsVersion
    val catsEffect = "org.typelevel" %% "cats-effect" % catsEffectVersion
    val taglessMacros = "org.typelevel" %% "cats-tagless-macros" % catsTaglessVersion
    val taglessCore = "org.typelevel" %% "cats-tagless-core" % catsTaglessVersion
    val kittens = "org.typelevel" %% "kittens" % kittensVersion
    val spire = "org.typelevel" %% "spire" % spireVersion
    val mtlCore = "org.typelevel" %% "cats-mtl-core" % mtlVersion
}

lazy val typesafe = new {
    val akkaVersion = "2.5.22"
    val akkaHttpVersion = "10.1.8"
    val configVersion = "1.3.1"

    val typesafeConfig = "com.typesafe" % "config" % configVersion
    val akkaActor = "com.typesafe.akka"   %% "akka-actor"              % akkaVersion
    val akkaStream = "com.typesafe.akka"   %% "akka-stream"             % akkaVersion
    val akkaClusterSharding = "com.typesafe.akka"   %% "akka-cluster-sharding"   % akkaVersion
    val akkaPersistence = "com.typesafe.akka"   %% "akka-persistence"        % akkaVersion excludeAll (ExclusionRule("io.netty"))
    val akkaPersistenceQuery = "com.typesafe.akka"   %% "akka-persistence-query"  % akkaVersion
    val akkaDistributedData = "com.typesafe.akka"   %% "akka-distributed-data"   % akkaVersion
    val akkaMultoNodeTestkit = "com.typesafe.akka"   %% "akka-multi-node-testkit" % akkaVersion
    val akkaTestkit = "com.typesafe.akka"   %% "akka-testkit"            % akkaVersion % Test
    val akkaStreamTestKit = "com.typesafe.akka"   %% "akka-stream-testkit"     % akkaVersion % Test
    val akkaHttp = "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion
    val akkaHttpSprayJson = "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion
    val akkaHttpTestkit = "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion
    val akkaHttpCirce = "de.heikoseeberger" %% "akka-http-circe"      % "1.21.0"
}

lazy val backuity = new {
    val version = "3.5.1"
    val clistCore = "org.backuity.clist" %% "clist-core"  % version
    val clistMacros = "org.backuity.clist" %% "clist-macros" % version % "provided"
}

lazy val beachape = new {
    val version = "1.5.15"
    val enumeratum = "com.beachape" %% "enumeratum" % version
    val enumeratumCats = "com.beachape" %% "enumeratum-cats" % version
}

lazy val scalaDependencies = testing ++ logging ++ Seq(
    typelevel.catsCore,
    typelevel.catsFree,
    typelevel.catsEffect,
    typelevel.taglessMacros,
    typelevel.taglessCore,
    typelevel.mtlCore,
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

lazy val NeoTemplate = project.settings(
    name := "NeoTemplate",
    scalaVersion := "2.12.8",
    libraryDependencies ++= (logging ++ Seq(
        typelevel.catsCore,
        typelevel.catsEffect,
        backuity.clistCore,
        backuity.clistMacros,
        beachape.enumeratumCats,
        typesafe.akkaActor,
        typesafe.akkaStream
    )),
    scalacOptions ++= Seq(
        "-Ypartial-unification"
    )
)

lazy val TaglessFinal = project.settings(
    name := "TaglessFinal",
    scalaVersion := "2.12.8",
    libraryDependencies ++= (logging ++ testing ++ Seq(
        typelevel.catsCore,
        typelevel.catsEffect,
        typelevel.taglessMacros
    )),
    scalacOptions ++= Seq(
        "-Ypartial-unification"
    ),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
)

lazy val BenchmarkTest = project.settings(
    name:="BenchmarkTest",
    scalaVersion := "2.13.0",
    libraryDependencies ++= (logging ++ benchmark)
)