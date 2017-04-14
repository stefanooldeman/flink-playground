package com.kpn.datalab.flink.playground

import org.apache.flink.streaming.api.scala._


/**
  *
  * Kastermans Teachings - Assignment 1:
  *
  * reading from a socket on which not only numbers are typed, output on every input the average
  * of the numbers entered so far.
  * Note: inputs can be text, on a text input still output the current average.
  * But these text inputs clearly don't change the average.
  */
object StatefullJob {

  def main(args: Array[String]) {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // env.setParallelism(1)

    val s1: DataStream[String] = env.socketTextStream("localhost", 9999)
    // val s1 = env.fromElements("1", "2", "foo", "4", "5")
    val s3: KeyedStream[String, Int] = s1.keyBy { _ =>  1 }
    val s4: DataStream[Int] = s3.mapWithState((in: String, state: Option[(Int, Int)]) =>
        state match {
          case Some((c, s)) => {
            try {
              val sum = s + in.toInt
              val counter = c + 1
              val out = sum / counter
              (out, Some(counter, sum))
            } catch {
              // devide sum by count because sum can be 0
              case _: NumberFormatException => (s / c, state)
            }
          }
          case None => (0, Some(1, 0))
        })
    s4.print


    env.execute("Flink Playground: Ave")
  }
}
