import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/updateQn")
public class UpdateQuestion extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/clicker?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                "myuser", "xxxx");
            PreparedStatement pstRemove = conn.prepareStatement("DELETE FROM currentQn");
            PreparedStatement pstUpdate = conn.prepareStatement("INSERT INTO currentQn (question) VALUES (1)");
        ) {
            int deleteCount = pstRemove.executeUpdate();
            out.println("<p>Deleted records: " + deleteCount + "</p>");

            int updateCount = pstUpdate.executeUpdate();
            out.println("<p>Record inserted successfully!</p>");

            // String questionNumb = "1";
            // questionNumb = request.getParameter("questionNo");
            // out.println("Received question number: " + questionNumb); // Check what is received

            // if (questionNumb != null && !questionNumb.isEmpty()) {
            //     try {
            //         pstUpdate.setInt(1, Integer.parseInt(questionNumb));
            //         int updateCount = pstUpdate.executeUpdate();
            //         if (updateCount > 0) {
            //             out.println("<p>Record inserted successfully!</p>");
            //         } else {
            //             out.println("<p>Failed to insert the record.</p>");
            //         }
            //     } catch (NumberFormatException nfe) {
            //         out.println("<p>Invalid question number format: " + questionNumb + "</p>");
            //     }
            // } else {
            //     out.println("<p>No question number provided.</p>");
            // }
        } catch (SQLException ex) {
            out.println("<p>SQL Error: " + ex.getMessage() + "</p>");
            ex.printStackTrace(out);  // Print stack trace to servlet output
        } catch (Exception ex) {
            out.println("<p>General Error: " + ex.getMessage() + "</p>");
            ex.printStackTrace(out);  // Print stack trace to servlet output
        } finally {
            out.close();
        }
    }
}
