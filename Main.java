import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Main {

    private static Connection connection;
    public static void main(String[] args){
        String url = "jdbc:postgresql://localhost:5432/assignment3";
        //your postgres username
        String user = "postgres";
        //your postgres password
        String password = "password";
        try{
            //allows this class to load dynamically after compile time
            Class.forName("org.postgresql.Driver");
            //connects to the database
            connection = DriverManager.getConnection(url, user, password);
            Scanner scanner = new Scanner(System.in);
            System.out.print("APPLICATION TESTER\n");
            System.out.print("1. Runs the getAllStudents() function\n");
            System.out.print("2. Runs the addStudent(first_name, last_name, email, enrollment_date) function given the appropriate arguments\n");
            System.out.print("3. Runs the updateStudentEmail(student_id, new_email) function given the appropriate arguments\n");
            System.out.print("4. Runs the deleteStudent(student_id) function given the appropriate argument\n");
            System.out.print("Choose a valid number:");
            int userInput = scanner.nextInt();

            if (userInput == 1) {
                getAllStudents();
            }
            else if (userInput == 2){
                scanner.nextLine(); // Consume newline character
                System.out.print("Enter the student's first name: \n");
                String firstName = scanner.nextLine();

                System.out.print("Enter the student's last name: \n");
                String lastName = scanner.nextLine();

                System.out.print("Enter the student's email: \n");
                String email = scanner.nextLine();

                System.out.print("Enter the student's enrollment date in the form (mm/dd/yyyy): \n");
                String enrollDate = scanner.nextLine();

                addStudent(firstName,lastName,email,enrollDate);
            }

            else if (userInput == 3){
                scanner.nextLine(); // Consume newline character
                System.out.print("Enter the student's id to be updated: ");
                int student_id = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                System.out.print("Enter the new student email: ");
                String newEmail = scanner.nextLine();

                updateStudentEmail(student_id,newEmail);
            }
            else if (userInput == 4){
                scanner.nextLine(); // Consume newline character
                System.out.print("Enter the student's id to be deleted: ");
                int student_id = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
                deleteStudent(student_id);
            }else{
                System.out.print("Please enter a valid choice next time");
            }

            scanner.close();
        }
        catch(Exception e){
            System.out.println(e);
        }


    }

    //function to get all students from the database
    public static void getAllStudents() throws SQLException {
        //creates a statement
        Statement statement = connection.createStatement();
        //gives the statement the query to be executed
        statement.executeQuery("SELECT * FROM students");
        //gets the result from the statement
        ResultSet resultSet = statement.getResultSet();
        //loops through the results printing all data attributes
        while(resultSet.next()){
            System.out.print(resultSet.getInt("student_id") + "\t");
            System.out.print(resultSet.getString("first_name") + "\t");
            System.out.print(resultSet.getString("last_name") + "\t");
            System.out.print(resultSet.getString("email") + "\t");
            System.out.println(resultSet.getDate("enrollment_date"));
        }
    }

    //function to add a  student in the database
    public static void addStudent(String first_name, String last_name, String email, String enrollment_date){
        //the query the database will receive to add a new student however put ? for the values and using a prepared statement to prevent data injection
        String insertSQL = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?,?,?,?)";
        //creates a prepared statement using the query written from above
        try(PreparedStatement pstmt = connection.prepareStatement(insertSQL)){
            //sets the value of the parameter index at 1 to be first_name
            pstmt.setString(1,first_name);
            //sets the value of the parameter index at 2 to be last_name
            pstmt.setString(2,last_name);
            //sets the value of the parameter index at 3 to be email
            pstmt.setString(3,email);
            // convert date from String to java.util.Date form
            java.util.Date myDate = new java.util.Date(enrollment_date);
            // convert date from java.util.Date to java.sql.Date form
            java.sql.Date sqlDate = new java.sql.Date(myDate.getTime());
            //sets the value of the parameter index at 4 to be the new sqlDate
            pstmt.setDate(4,sqlDate);

            // Execute the query
            int rowsInserted = pstmt.executeUpdate();

            // Check if the insertion was successful
            if (rowsInserted > 0) {
                System.out.println("Data inserted successfully.");
            } else {
                System.out.println("Failed to insert data.");
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

    //function to update a given students email
    public static void updateStudentEmail(int student_id, String new_email){
        //the query the database will receive to update a given student however put ? for the values and using a prepared statement to prevent data injection
        String updateSQL = "UPDATE students SET email = ? WHERE student_id = ?";
        //creates a prepared statement using the query written from above
        try(PreparedStatement pstmt = connection.prepareStatement(updateSQL)){
            //sets the value for the parameter at 1 (SET email = ?) to be new_email
            pstmt.setString(1,new_email);
            //sets the value for the parameter at 2 (WHERE student_id = ?) to be student_id
            pstmt.setInt(2,student_id);

            // Execute the query
            int rowsUpdated = pstmt.executeUpdate();

            // Check if the update was successful
            if (rowsUpdated > 0) {
                System.out.println("Email updated successfully.");
            } else {
                System.out.println("Failed to update email.");
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

    //function to delete a given student
    public static void deleteStudent(int student_id){
        //the query the database will receive to delete a given student however put ? for the values and using a prepared statement to prevent data injection
        String deleteSQL = "DELETE from students WHERE student_id = ?";
        //creates a prepared statement using the query written from above
        try(PreparedStatement pstmt = connection.prepareStatement(deleteSQL)){
            //sets the value for the parameter at 1 (WHERE student_id = ?) to be student_id
            pstmt.setInt(1,student_id);

            // Execute the query
            int rowsDeleted = pstmt.executeUpdate();

            // Check if the deletion was successful
            if (rowsDeleted > 0) {
                System.out.println("Student deleted successfully.");
            } else {
                System.out.println("Failed to delete student.");
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

}


