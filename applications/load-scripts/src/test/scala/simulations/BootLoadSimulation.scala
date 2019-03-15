package simulations

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._

class BootLoadSimulation extends Simulation {

  val baseUrl: String = System.getProperty("TARGET_URL")
  val sim_users: Int = System.getProperty("SIM_USERS").toInt

  val httpConf: HttpProtocolBuilder = http.baseUrl(baseUrl)

  val scn: ScenarioBuilder = scenario("Passthrough Page")
    .repeat(1) {
      exec(
        http("passthrough-messages")
          .get("/index.html")
      )
    }

  setUp(scn.inject(rampUsers(sim_users).during(1 seconds)).protocols(httpConf))
}
