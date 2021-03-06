package com.net128.test.gatling.wechatin

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class MessageInboundSimulation extends Simulation {

  val nUsers = Integer.getInteger("users", 100)
  val uri = sys.props.getOrElse("uri", "http://localhost:15001")
  val httpConf = http
    .disableWarmUp
    .baseUrl(uri)
    .acceptHeader("application/json;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Java/1.8.0_131")
    .contentTypeHeader(HttpHeaderValues.TextXml)

  val scn = scenario("Message Inbound Simulation")
    .repeat(50) {
      pause(300 milliseconds)
      .exec(http("receive message")
        .post("/wechat")
        .body(RawFileBody("bodies/inboundXmlMessage.xml")).asXml
        .check(status.is(200))
      )
    }

  setUp(
    scn.inject(
      atOnceUsers(nUsers)
    ).protocols(httpConf))
}
