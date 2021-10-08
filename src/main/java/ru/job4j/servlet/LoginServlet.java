package ru.job4j.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.model.User;
import ru.job4j.store.AdRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class LoginServlet extends HttpServlet {

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String email = "";
        String password = "";
        if ((session.getAttribute("email") != null)) {
            email = (String) session.getAttribute("email");
            session.removeAttribute("email");
        }
        if ((session.getAttribute("password") != null)) {
            password = (String) session.getAttribute("password");
            session.removeAttribute("password");
        }
        String json = GSON.toJson(User.of(email, password));
        resp.setContentType("application/json; charset=utf-8");
        OutputStream output = resp.getOutputStream();
        output.write(json.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                resp.getOutputStream(), StandardCharsets.UTF_8));
        User user = GSON.fromJson(req.getReader(), User.class);
        User originalUser = AdRepository.instOf().findUserByEmail(user.getEmail());
        if (originalUser == null || !originalUser.getPassword().equals(user.getPassword())) {
            writer.print("409 Conflict");
            writer.flush();
            return;
        }
        HttpSession sc = req.getSession();
        sc.setAttribute("user", originalUser);
        writer.print("200 OK");
        writer.flush();
    }
}
