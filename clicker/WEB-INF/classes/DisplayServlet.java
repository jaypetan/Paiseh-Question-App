// To save as "<TOMCAT_HOME>\webapps\clicker\WEB-INF\classes\DisplayServlet.java".
import java.io.*;
import java.sql.*;
import jakarta.servlet.*;             // Tomcat 10
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;


@WebServlet("/display")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class DisplayServlet extends HttpServlet {

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
      out.println("<head><title>Barchart results</title></head>");
      out.println("<body>");

      try (
         // Step 1: Allocate a database 'Connection' object
         Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:3306/clicker?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",   //change part3 to database name
               "myuser", "xxxx");   // For MySQL
               // The format is: "jdbc:mysql://hostname:port/databaseName", "username", "password"

         // Step 2: Allocate a 'Statement' object in the Connection
         Statement stmt = conn.createStatement();
      ) {
         //edit here
         ResultSet rs = stmt.executeQuery("SELECT questionNo, choice, COUNT(*) as count FROM responses GROUP BY questionNo, choice");

         // Prepare data for the chart
         StringBuilder data = new StringBuilder();
         data.append("['Question No.', 'Choice A', 'Choice B', 'Choice C', 'Choice D'],");  // Header row
         int maxQuestionNo = 0;  // Track the maximum question number
         int[][] choiceCounts = new int[4][10];  // 2D array to store counts for each choice and question number
         while (rs.next()) {
            int questionNo = rs.getInt("questionNo");
            String choice = rs.getString("choice");
            int count = rs.getInt("count");

            // Update the maximum question number
            if (questionNo > maxQuestionNo) {
               maxQuestionNo = questionNo;
            }

           // Convert choice letter to corresponding index (A -> 0, B -> 1, etc.)
           int choiceIndex = choice.charAt(0) - 'A';

            // Update the count for the current choice and question number
           choiceCounts[choiceIndex][questionNo] = count;
         }

         // Append data rows for each question
         for (int i = 1; i <= maxQuestionNo; i++) {
             data.append("['Qns ").append(i).append("',")
               .append(choiceCounts[0][i]).append(",")
               .append(choiceCounts[1][i]).append(",")
               .append(choiceCounts[2][i]).append(",")
               .append(choiceCounts[3][i]).append("],");
         }

         // Print the HTML chart div and JavaScript code
         out.println("<div id='chart_div' style='width: 900px; height: 500px;'></div>");
         out.println("<script type='text/javascript' src='https://www.gstatic.com/charts/loader.js'></script>");
         out.println("<script type='text/javascript'>");
         out.println("  google.charts.load('current', {'packages':['corechart', 'bar']});");
         out.println("  google.charts.setOnLoadCallback(drawAxisTickColors);");
         out.println("  function drawAxisTickColors() {");
         out.println("    var data = google.visualization.arrayToDataTable([" + data.toString() + "]);");
         out.println("    var options = {");
         out.println("      title: 'Responses',");
         out.println("      chartArea: {width: '50%'},");
         out.println("      hAxis: { title: 'Total Entries', minValue: 0, textStyle: { bold: true, fontSize: 12, color: '#4d4d4d' },");
         out.println("               titleTextStyle: { bold: true, fontSize: 18, color: '#4d4d4d' } },");
         out.println("      vAxis: { title: 'Question Number', textStyle: { fontSize: 14, bold: true, color: '#848484' },");
         out.println("               titleTextStyle: { fontSize: 14, bold: true, color: '#848484' } } };");
         out.println("    var chart = new google.visualization.BarChart(document.getElementById('chart_div'));");
         out.println("    chart.draw(data, options);");
         out.println("  }");
         out.println("</script>");

         
      } catch(SQLException ex) {
         out.println("<p>Error: " + ex.getMessage() + "</p>");
         out.println("<p>Check Tomcat console for details.</p>");
         ex.printStackTrace();
      } 
 
      out.println("</body></html>");
      out.close();
   }
}