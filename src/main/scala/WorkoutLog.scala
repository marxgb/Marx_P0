import java.sql.{Connection,DriverManager, SQLException, PreparedStatement}
import java.text.DateFormat
import java.text.SimpleDateFormat
object WorkoutLog {
  def main(args: Array[String]): Unit={

    //CONNECT TO MY SQL DATABASE
    val url = "jdbc:mysql://localhost:3306/workoutlog"
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

    //GET USER
    println("Enter your username: ")
    var userN = scala.io.StdIn.readLine()
    println("Enter your password: ")
    var passW = scala.io.StdIn.readLine()

    //VARIABLES
    checkUser(userN, passW)
    var workoutGroupList = List("chest", "legs","arms","legs")
    var workoutList = List("bench press", "incline press","dumbbell flies")
    var logWorkout = true
    var workoutWeight = ""
    var workoutRep = ""
    var workoutSet = ""
    var workoutName = ""
    var workoutDate = ""
    println("Press 1 to log a workout, 2 to 3")
    var option = scala.io.StdIn.readLine()

    //FOR WHEN THE USER WANTS TO LOG A WORKOUT
    while (logWorkout == true && option == "1"){
      if (workoutDate == ""){
        println("What date is your workout? (yyyy-mm-dd format)")
        workoutDate = scala.io.StdIn.readLine()
      }
      println("What category workout do you want to log? chest legs arms back")
      var workoutGroup = scala.io.StdIn.readLine()
      if(!workoutGroupList.contains(workoutGroup)){
        println("Invalid category")
      }
      else{
        println("Which workout? 'bench press' 'incline press' 'dumbbell flies'")
        workoutName = scala.io.StdIn.readLine()
        println("How many lbs was your set?")
        workoutWeight = scala.io.StdIn.readLine()
        println("How many sets did you do?")
        workoutSet = scala.io.StdIn.readLine()
        println("How many reps did you do per set?")
        workoutRep = scala.io.StdIn.readLine()
        logWorkoutDB(workoutDate, workoutName, workoutWeight, workoutSet, workoutRep, connection)
        println("Your workout has been success fully log! Do you wanna log more workout? y or n")
        var continueLog = scala.io.StdIn.readLine()
        if (continueLog == "n"){
          logWorkout = false
        }
        else if (continueLog == "y"){
          print("Logging more workouts. ")
        }
        else{
          println("invalid command")
        }

      }


      println("THANK YOU FOR LOGGING YOUR WORKOUT!!")
    }




  }

  def checkUser(x : String, y : String): Unit = {
    println(x + " " + y)
  }
  def logWorkoutDB(date : String, name : String, weight : String, rep : String, set: String, connection: Connection): Unit ={
    var statement = connection.createStatement
    /*
      var pstmt = "INSERT INTO workouts(workoutDate,workoutName,workoutWeight,workoutSet,workoutRep) VALUES(STR_TO_DATE('10-14-2021','%d-%m-%Y'),'Bench Press',185,5,5)"
      statement.executeUpdate(pstmt)
      statement.executeUpdate(pstmt)
    */
      var pstmt = connection.prepareStatement("INSERT INTO workouts(workoutDate,workoutName,workoutWeight,workoutSet,workoutRep) VALUES(?,?,?,?,?)")
      pstmt.setDate(1, java.sql.Date.valueOf(date))
      pstmt.setString(2, name)
      pstmt.setInt(3, weight.toInt)
      pstmt.setInt(4, rep.toInt)
      pstmt.setInt(5, set.toInt)

      pstmt.executeUpdate






  }


}
