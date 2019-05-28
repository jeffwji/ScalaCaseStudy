package com.examples.akka.stream

import akka.NotUsed
import akka.stream.scaladsl.{Flow, GraphDSL, RunnableGraph, Sink, Source}
import GraphDSL.Implicits._
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import cats.effect.IO

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object MultipleStream extends App {
    trait blueprint {
        type FutureResult = Future[List[_]]

        def start_1: Source[Unit, NotUsed]
        def step1_1: Flow[Unit, Unit, NotUsed]
        def step1_2: Flow[Unit, Unit, NotUsed]
        def out_1: Sink[Unit, FutureResult]

        def stream1: RunnableGraph[FutureResult] = RunnableGraph.fromGraph(GraphDSL.create(out_1){ implicit builder: GraphDSL.Builder[FutureResult] => out_1 =>
            start_1 ~> step1_1 ~> step1_2 ~> out_1
            ClosedShape
        })

        def start_2: Source[Unit, NotUsed]
        def step2_1: Flow[Unit, Unit, NotUsed]
        def step2_2: Flow[Unit, Unit, NotUsed]
        def out_2: Sink[Unit, FutureResult]

        def stream2: RunnableGraph[FutureResult] = RunnableGraph.fromGraph(GraphDSL.create(out_2){ implicit builder: GraphDSL.Builder[FutureResult] => out_2 =>
            start_2 ~> step2_1 ~> step2_2 ~> out_2
            ClosedShape
        })

        def comp(implicit system:ActorSystem): Future[(List[_], List[_])] = {
            implicit val materializer = ActorMaterializer()
            val res = IO {
                val r = for {
                    s1 <- stream1.run()
                    s2 <- stream2.run()
                } yield (s1, s2)
                r.onComplete {
                    _ => {
                        system.terminate()
                        Await.result(system.whenTerminated, Duration.Inf)
                    }
                };r
            }
            res.unsafeRunSync()
        }
    }
}
