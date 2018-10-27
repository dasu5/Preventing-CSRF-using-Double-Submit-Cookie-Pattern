import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class Validator extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        service(request, response);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String id, key;
        try {
            id = request.getParameter("id");
            key = request.getParameter("key");
            String CSRFTokenRecieved = request.getParameter("tokentxt");
            out.println("Registration Number :" + id);
            out.println("Password :" + key);
            out.println("Token Received :" + CSRFTokenRecieved);
            Cookie[] cookies = request.getCookies();
            String CSRFToken = cookies[0].getValue();
            out.println("Token :" + CSRFToken);
            if (CSRFTokenRecieved.equals(CSRFToken)) {
                out.println("Successfully Verfied !!");
            } else {
                out.println("Verification Failed !!");
            }
        } finally {
            out.close();
        }

    }
}
