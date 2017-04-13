package com.kpn.datalab.flink.playground

import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow

object Job {

  def main(args: Array[String]) {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // env.setParallelism(1)

    val source = env.socketTextStream("localhost", 9999)
    val s1: DataStream[Int] = source.map { x => x.toInt }
    val s2: AllWindowedStream[Int, GlobalWindow] = s1.countWindowAll(5, 5)
    s2.sum(0)
      .print

    env.execute("Flink Playground: Save state of last 5 numbers")
  }
}

