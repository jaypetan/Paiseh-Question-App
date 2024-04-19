import java.io.*;
import java.sql.*;
import jakarta.servlet.*;            // Tomcat 10 (Jakarta EE 9)
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
//import javax.servlet.*;            // Tomcat 9 (Java EE 8 / Jakarta EE 8)
//import javax.servlet.http.*;
//import javax.servlet.annotation.*;

@WebServlet("/checkQn")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class checkCurrentQn extends HttpServlet {

   // The doGet() runs once per HTTP GET request to this servlet.
   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
      // Set the MIME type for the response message
      response.setContentType("text/html");
      // Get a output writer to write the response message into the network socket
      PrintWriter out = response.getWriter();
      // Print an HTML page as the output of the query
      try (
         // Step 1: Allocate a database 'Connection' object
         Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:3306/clicker?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "myuser", "xxxx");   // For MySQL
               // The format is: "jdbc:mysql://hostname:port/databaseName", "username", "password"

         // Step 2: Allocate a 'Statement' object in the Connection
         Statement stmt = conn.createStatement();
      ) {
         String getCurrentQn = "select question FROM currentQn";
         int currentQn = 999;
         String choice = request.getParameter("choice");

         try (PreparedStatement qnStmt = conn.prepareStatement(getCurrentQn)) {
            // Execute the query and process the ResultSet
                try (ResultSet rs = qnStmt.executeQuery()) {
                    if (rs.next()) {
                        currentQn = rs.getInt("question");
                    }
                }
         }
         out.print(currentQn); //Ouput the current question number
         
      } catch(SQLException ex) {
         out.print("Error");  // Handle database connection failure
      }
      out.close();
   }
}