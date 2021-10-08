package ru.job4j.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.model.User;
import ru.job4j.store.AdRepository;
import ru.job4j.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class RegServlet extends HttpServlet {

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                resp.getOutputStream(), StandardCharsets.UTF_8));
        User user = GSON.fromJson(req.getReader(), User.class);
        final Store store = AdRepository.instOf();
        store.saveUser(user);
        if (user.getId() == 0) {
            writer.print("409 Conflict");
            writer.flush();
            return;
        }
        HttpSession session = req.getSession();
        session.setAttribute("email", user.getEmail());
        session.setAttribute("password", user.getPassword());
        writer.print("200 OK");
        writer.flush();
    }
}
