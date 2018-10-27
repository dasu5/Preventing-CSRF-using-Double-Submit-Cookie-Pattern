import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


public class Login extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        reqService(request, response);
    }

    private static String generateCSRFToken() {
        String CSRFToken = null;
        byte[] bytes = new byte[16];
        try {
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();//only works with Java 8
            secureRandom.nextBytes(bytes);
            CSRFToken = new String(Base64.getEncoder().encode(bytes));//only works with Java 8
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return CSRFToken;
    }


    protected void reqService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String username, password;
        try {
            username = request.getParameter("username");
            password = request.getParameter("password");

            Cookie[] cookies = request.getCookies();//request for auto generated cookies in the browser
            cookies[0].setPath("/");

            if (username.equals("admin") && password.equals("admin")) {
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                String csrfToken = generateCSRFToken();
                System.out.println("Generated Token :" + csrfToken);
                Cookie cookie = new Cookie(cookies[0].getValue(), csrfToken);
                cookies[0].setValue(csrfToken);
                cookies[0].setPath("/");
                response.addCookie(cookie);
                session.setAttribute("csrfToken", csrfToken);
                response.sendRedirect("home.jsp");
            } else {
                out.println("Invalid username or password | try 'admin' for both username and password");
            }
        } finally {
            out.close();
        }

    }
}
