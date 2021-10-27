import java.sql.{Connection,DriverManager}

object WorkoutLog {
  def main(args: Array[String]): Unit={

    //CONNECT TO MY SQL DATABASE
    val url = "jdbc:mysql://localhost:3306/workoutlog"
    val driver = "com.mysql.jdbc.Driver"
    val username = "root"
    val password = "coff33addicT"
    var connection:Connection = null

    connection = DriverManager.getConnection(url, username, password)
    val statement = connection.createStatement

    //GET USER
    println("Enter your user:")
    var user = scala.io.StdIn.readLine()


    //VARIABLES
    var logWorkout = true
    var workoutDate = ""
    var option = ""


    //FOR WHEN THE USER WANTS TO LOG A WORKOUT
    while (logWorkout == true){
      println("Press 1 to log a workout, 2 to check user workout logs, 3 to change user, 4 to see all workout logs of all users, 5 if you are finished, 6 if you wanna delete user logs")
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
        var continueLog = scala.io.StdIn.readLine()
        workoutDate = ""
        if (continueLog == "n")
          option = "doneLog"
         else if (continueLog == "y")
          print("Logging more workouts. ")
        else {
          print("Invalid input!")
          option = "doneLog"
        }
      }
      if (option == "2"){
        checkUser(user, connection)
      }
      else if (option =="3") {
        println("Enter a new user or an existing one: ")
        user = scala.io.StdIn.readLine()
        workoutDate=""
      }
      else if (option == "4"){
        printAllWorkoutLog(connection)
        println("Print complete!")
      }
      else if (option == "5")
        logWorkout=false
      else if (option == "6")
        deleteUserLog(user,connection)
      else if (option =="doneLog")
        println("")
      else
        println("Invalid option!")

    }

    println("THANK YOU FOR LOGGING YOUR WORKOUT!!")
  }

  //END OF MAIN

  //OPTION 1 step 1: METHOD THAT CHECK FOR CATEGORY AND CORRECT WORKOUT NAME
  def checkCategory(x: String): String = {
    var workoutGroup = x
    var workoutName = ""
    val workoutGroupList = List("chest", "legs","arms","back")
    val workoutList = List("bench press", "incline press","dumbbell fly", "squat","leg press", "leg curl", "bicep curl", "tricep extension", "hammer curl", "deadlift", "pull downs", "rows")
    while(!workoutGroupList.contains(workoutGroup)){
      println("Invalid category. Please select a valid category: 'chest' 'legs' 'arms' 'back'")
      workoutGroup = scala.io.StdIn.readLine()
    }
    do {
      if (workoutGroup == "chest")
        println("Which workout? 'bench press' 'incline press' 'dumbbell fly'")
      else if (workoutGroup == "legs")
        println("Which workout? 'squat' 'leg press' 'leg curl'")
      else if (workoutGroup == "arms")
        println("Which workout? 'bicep curl' 'tricep extension' 'hammer curl'")
      else if (workoutGroup == "back")
        println("Which workout? 'deadlift' 'pull downs' 'rows'")
      else
        print("Invalid input!")
      workoutName = scala.io.StdIn.readLine()
    }
    while(!workoutList.contains(workoutName))
    workoutName
  }





  //OPTION 1 step 2: TAKES THE INSERT VALUES FOR SET/REPS/WEIGHT
  def createWorkoutRecord(workoutDate : String, workoutName: String, connection: Connection, user: String): Unit = {
    println("How many lbs was your set?")
    var workoutWeight = scala.io.StdIn.readLine()
    println("How many sets did you do?")
    var workoutSet = scala.io.StdIn.readLine()
    println("How many reps did you do per set?")
    var workoutRep = scala.io.StdIn.readLine()
    logWorkoutDB(workoutDate, workoutName, workoutWeight, workoutSet, workoutRep, connection, user)
  }

  //OPTION 1 step 3: METHOD THAT INSERTS A RECORD INTO THE WORKOUTS TABLE
  def logWorkoutDB(date : String, name : String, weight : String, rep : String, set: String, connection: Connection, user : String): Unit ={
    var pstmt = connection.prepareStatement("INSERT INTO workouts(workoutDate,workoutName,workoutWeight,workoutSet,workoutRep, user) VALUES(?,?,?,?,?,?)")
    try{
      pstmt.setDate(1, java.sql.Date.valueOf(date))
      pstmt.setString(2, name)
      pstmt.setInt(3, weight.toInt)
      pstmt.setInt(4, rep.toInt)
      pstmt.setInt(5, set.toInt)
      pstmt.setString(6, user)
      pstmt.executeUpdate
      println("Your workout has been successfully log! Do you want to log more workout? y or n")
    }
    catch {
      case e: IllegalArgumentException => println("Invalid Input!! Record wasn't added. Do you want to add a workout? y or n")
    }
  }

  //OPTION 2: PRINT LOG OF THAT USER
  def checkUser(x : String, connection: Connection): Unit = {
    var pstmt = connection.prepareStatement("SELECT * FROM workouts WHERE user=?")
    pstmt.setString(1,x)
    var rs = pstmt.executeQuery()
    println("Date       Workout        WT(lbs) Set   Rep   User")
    while (rs.next) {
      val workoutDate = rs.getDate("workoutDate")
      val workoutName= rs.getString("workoutName")
      val workoutWeight = rs.getInt("workoutWeight")
      val workoutSet = rs.getInt("workoutSet")
      val workoutRep = rs.getInt("workoutRep")
      val user = rs.getString("user")
      println("%10s %-14s %-6s  %-5s %-5s %s".format(workoutDate, workoutName, workoutWeight, workoutSet, workoutRep, user))
    }
  }

  //OPTION 4 METHOD THAT PRINTS OUT ALL THE LOGS OF ALL USER
  def printAllWorkoutLog(connection: Connection): Unit = {
    val statement = connection.createStatement
    val rs = statement.executeQuery("SELECT * FROM workouts ORDER BY user")
    println("Date       Workout        WT(lbs) Set   Rep   User")
    while (rs.next) {
      val workoutDate = rs.getDate("workoutDate")
      val workoutName= rs.getString("workoutName")
      val workoutWeight = rs.getInt("workoutWeight")
      val workoutSet = rs.getInt("workoutSet")
      val workoutRep = rs.getInt("workoutRep")
      val user = rs.getString("user")
      println("%10s %-14s %-6s  %-5s %-5s %s".format(workoutDate, workoutName, workoutWeight, workoutSet, workoutRep, user))
    }
  }

  def deleteUserLog(user : String, connection: Connection): Unit = {
    println("Are you sure you wanna delete your logs? y or n")
    var yesOrNo = scala.io.StdIn.readLine()
    if (yesOrNo=="y"){
      var pstmt = connection.prepareStatement("DELETE FROM workouts WHERE user=?")
      pstmt.setString(1,user)
      pstmt.execute()
      println("Logs deleted!")

    }
    else if (yesOrNo=="n")
      println("Logs not deleted")
    else
      println("Invalid input!")
  }
}
