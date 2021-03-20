package simulations

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._

class BootLoadSimulation extends Simulation {
  Class.forName("org.postgresql.Driver")
  //  val conn: Connection = DriverManager.getConnection("jdbc:postgresql://postgres-prd-11.cge4ghqf1t6d.us-east-1.rds.amazonaws.com:5432/offers?user=postgres&password=Nlp8W5TkXnT5")
  //  val st: Statement = conn.createStatement()
  //  val rs: ResultSet = st.executeQuery("SELECT msisdn FROM customer_data2 TABLESAMPLE SYSTEM(1)")
  //val sqlQueryFeeder = jdbcFeeder("jdbc:postgresql://postgres-prd-11.cge4ghqf1t6d.us-east-1.rds.amazonaws.com:5432/offers", "postgres", "Nlp8W5TkXnT5", "SELECT msisdn FROM customer_data2 TABLESAMPLE SYSTEM(1)")
  //  val msisdns = ListBuffer[String]()
  //
  //  while ( {
  //    rs.next
  //  }) {
  //    msisdns += rs.getString(1)
  //  }
  //  rs.close()


  val baseUrl: String = System.getProperty("TARGET_URL")
  val sim_users: Int = System.getProperty("SIM_USERS").toInt

  val httpConf: HttpProtocolBuilder = http.baseUrl(baseUrl)

  val scn: ScenarioBuilder = scenario("Offers")
    //.feed(sqlQueryFeeder)
    .repeat(30) {
      exec(
        http("get_offer")
          .get("http://localhost:8001/")
          .header("Content-Type", "text/html")
          .header("Host", "localhost")
      )
    }

  setUp(scn.inject(rampUsers(sim_users).during(30.seconds)).protocols(httpConf))
}
