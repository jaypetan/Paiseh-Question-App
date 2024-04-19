import java.io.*;
import java.sql.*;
import jakarta.servlet.*;            // Tomcat 10 (Jakarta EE 9)
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
//import javax.servlet.*;            // Tomcat 9 (Java EE 8 / Jakarta EE 8)
//import javax.servlet.http.*;
//import javax.servlet.annotation.*;

@WebServlet("/select")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class choices extends HttpServlet {

   // The doGet() runs once per HTTP GET request to this servlet.
   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
      // Set the MIME type for the response message
      response.setContentType("text/html");
      // Get a output writer to write the response message into the network socket
      PrintWriter out = response.getWriter();
      // Print an HTML page as the output of the query
      out.println("<!DOCTYPE html>");
      out.println("<html>");
      out.println("<head><title>Loading</title>");
      out.println("<style type='text/css'>");
      out.println("*{ background: black url(loading.gif) no-repeat center center;");
      out.println("height: 100vh; width: 100%; position: fixed; z-index: 100;");
      out.println("color: white; text-align: center; }");
      out.println("</style></head>");
      out.println("<body>");

      try (
         // Step 1: Allocate a database 'Connection' object
         Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:3306/clicker?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "myuser", "xxxx");   // For MySQL
               // The format is: "jdbc:mysql://hostname:port/databaseName", "username", "password"

         // Step 2: Allocate a 'Statement' object in the Connection
         Statement stmt = conn.createStatement();
      ) {
         // Step 3 & 4 of the database servlet
         // Assume that the URL is http://ip‐addr:port/clicker/select?choice=x
         // Assume that the questionNo is 8
         String getCurrentQn = "select question FROM currentQn";
         int currentQn = 999;
         String choice = request.getParameter("choice");
         out.println("<h1> SELECTED: " + choice + "</h1>");

         try (PreparedStatement qnStmt = conn.prepareStatement(getCurrentQn)) {
            // Execute the query and process the ResultSet
                try (ResultSet rs = qnStmt.executeQuery()) {
                    if (rs.next()) {
                        currentQn = rs.getInt("question");
                    }
                }
            }

         String sqlStr = "INSERT INTO responses (questionNo, choice) VALUES (" + currentQn + " , '"
            + choice + "')";
         int count = stmt.executeUpdate(sqlStr);   // run the SQL statement
         // === Step 4 ends HERE - Do NOT delete the following codes ===
      } catch(SQLException ex) {
         out.println("<p>Error: " + ex.getMessage() + "</p>");
         out.println("<p>Check Tomcat console for details.</p>");
         ex.printStackTrace();
      }  // Step 5: Close conn and stmt - Done automatically by try-with-resources (JDK 7)
 
      out.println("</body></html>");
      out.close();
   }
}