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
    println("Enter your user:")
    var user = scala.io.StdIn.readLine()


    //VARIABLES
    var logWorkout = true
    var workoutDate = ""
    var option = ""


    //FOR WHEN THE USER WANTS TO LOG A WORKOUT
    while (logWorkout == true){
      println("Press 1 to log a workout, 2 to check user workout logs")
      option = scala.io.StdIn.readLine()
      while(option =="1") {
        if (workoutDate == "") {
          println("What date is your workout? (yyyy-mm-dd format)")
          workoutDate = scala.io.StdIn.readLine()
        }
        println("What category workout do you want to log? chest legs arms back")
        var workoutGroup = scala.io.StdIn.readLine()
        var workoutName = checkCategory(workoutGroup)
        createWorkoutRecord(workoutDate, workoutName, connection, user)
        println("Your workout has been success fully log! Do you wanna log more workout? y or n")
        var continueLog = scala.io.StdIn.readLine()
        if (continueLog == "n")
          option = "0"
        else if (continueLog == "y")
          print("Logging more workouts. ")
        else
          println("invalid command. Do you wanna log more workout? y or n")
      }
      if (option == "2"){
        checkUser(user, connection)
        logWorkout = false
      }

    }

    println("THANK YOU FOR LOGGING YOUR WORKOUT!!")
  }

  //END OF MAIN
  //LIST OF METHODS
  def checkUser(x : String, connection: Connection): Unit = {
    val statement = connection.createStatement
    var pstmt = connection.prepareStatement("SELECT * FROM workouts WHERE user=?")
    pstmt.setString(1,x)
    var rs = pstmt.executeQuery()
    while (rs.next) {
      val workoutDate = rs.getDate("workoutDate")
      val workoutName= rs.getString("workoutName")
      val workoutWeight = rs.getInt("workoutWeight")
      val workoutSet = rs.getInt("workoutSet")
      val workoutRep = rs.getInt("workoutRep")
      val user = rs.getString("user")
      println("Date: %s | Workout: %s | Weight: %s lbs | Set: %s | Rep: %s | User: %s".format(workoutDate, workoutName, workoutWeight, workoutSet, workoutRep, user))
    }

  }

  //METHOD THAT INSERTS A RECORD INTO THE WORKOUTS TABLE
  def logWorkoutDB(date : String, name : String, weight : String, rep : String, set: String, connection: Connection, user : String): Unit ={
    var statement = connection.createStatement
    var pstmt = connection.prepareStatement("INSERT INTO workouts(workoutDate,workoutName,workoutWeight,workoutSet,workoutRep, user) VALUES(?,?,?,?,?,?)")
    pstmt.setDate(1, java.sql.Date.valueOf(date))
    pstmt.setString(2, name)
    pstmt.setInt(3, weight.toInt)
    pstmt.setInt(4, rep.toInt)
    pstmt.setInt(5, set.toInt)
    pstmt.setString(6, user)
    pstmt.executeUpdate
  }

  //METHOD THAT CHECK FOR CATEGORY AND CORRECT WORKOUT NAME
  def checkCategory(x: String): String = {
    var workoutGroup = x
    var workoutName = ""
    val workoutGroupList = List("chest", "legs","arms","back")
    val workoutList = List("bench press", "incline press","dumbbell flies", "squat","leg press", "leg curl", "bicep curl", "tricep extension", "hammer curl", "deadlift", " pull downs", "rows")
    while(!workoutGroupList.contains(workoutGroup)){
      println("Invalid category. Please select a valid category: 'chest' 'legs' 'arms' legs'")
      workoutGroup = scala.io.StdIn.readLine()
    }
    do {
      if (workoutGroup == "chest")
        println("Which workout? 'bench press' 'incline press' 'dumbbell flies'")
      else if (workoutGroup == "legs")
        println("Which workout? 'squat' 'leg press' 'leg curl'")
      else if (workoutGroup == "arms")
        println("Which workout? 'bicep curl' 'tricep extension' 'hammer curl'")
      else if (workoutGroup == "back")
        println("Which workout? 'deadlift' 'pull downs' 'rows'")
      workoutName= scala.io.StdIn.readLine()
    }
    while(!workoutList.contains(workoutName))
    workoutName
  }

  //TAKES THE INSERT VALUES FOR SET/REPS/WEIGHT
  def createWorkoutRecord(workoutDate : String, workoutName: String, connection: Connection, user: String): Unit = {
    println("How many lbs was your set?")
    var workoutWeight = scala.io.StdIn.readLine()
    println("How many sets did you do?")
    var workoutSet = scala.io.StdIn.readLine()
    println("How many reps did you do per set?")
    var workoutRep = scala.io.StdIn.readLine()
    logWorkoutDB(workoutDate, workoutName, workoutWeight, workoutSet, workoutRep, connection, user)

  }


}
