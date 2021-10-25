import java.sql.{Connection,DriverManager, SQLException, PreparedStatement}
object WorkoutLog {
  def main(args: Array[String]): Unit={
    val url = "jdbc:mysql://localhost:3306/test"
    val driver = "com.mysql.jdbc.Driver"
    val username = "root"
    val password = "coff33addicT"
    var connection:Connection = null
    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)
      val statement = connection.createStatement
      val rs = statement.executeQuery("SELECT host,age FROM test")
      while (rs.next) {
        val host = rs.getString("host")
        val age = rs.getString("age")
        println("host = %s, age = %s".format(host, age))
      }
    } catch {
      case e: Exception => e.printStackTrace
    }

    println("Enter your username: ")
    var userN = scala.io.StdIn.readLine()
    println("Enter your password: ")
    var passW = scala.io.StdIn.readLine()

    checkUser(userN, passW)
    var workoutGroupList = List("chest", "legs","arms","legs")
    var logWorkout = true
    println("Press 1 to log a workout, 2 to 3")
    var option = scala.io.StdIn.readLine()
    while (logWorkout == true && option == "1"){
      println("What body part workout do you want to log? chest legs arms back")
      var workoutGroup = scala.io.StdIn.readLine()

    }

  }

  def checkUser(x : String, y : String): Unit = {
    println(x + " " + y)
  }


}
