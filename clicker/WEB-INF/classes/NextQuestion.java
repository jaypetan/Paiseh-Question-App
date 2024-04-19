import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/nextQn")
public class NextQuestion extends HttpServlet {

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
            PreparedStatement pstUpdate = conn.prepareStatement("INSERT INTO currentQn (question) VALUES (?)");
        ) {
            String getQuestionNo = "SELECT question FROM currentQn";
            int questionNo = 999;

            try (PreparedStatement qnStmt = conn.prepareStatement(getQuestionNo)) {
            // Execute the query and process the ResultSet
                try (ResultSet rs = qnStmt.executeQuery()) {
                    if (rs.next()) {
                        questionNo = Integer.parseInt(rs.getString("question"));
                        if(questionNo < 5) {
                            questionNo++;
                        } else {
                            questionNo = 0;
                        }
                    }
                }
            }

            int deleteCount = pstRemove.executeUpdate();
            out.println("<p>Deleted records: " + deleteCount + "</p>");


            try {
                pstUpdate.setInt(1, questionNo);
                int updateCount = pstUpdate.executeUpdate();
                if (updateCount > 0) {
                    out.println("<p>Record inserted successfully!</p>");
                } else {
                    out.println("<p>Failed to insert the record.</p>");
                }
            } catch (NumberFormatException nfe) {
                out.println("<p>Invalid question number format: " + questionNo + "</p>");
            }
         
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
