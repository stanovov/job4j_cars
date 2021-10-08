package ru.job4j.servlet;

import ru.job4j.model.Advertisement;
import ru.job4j.model.User;
import ru.job4j.store.AdRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ReadOnlyAdvertisementServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Cache-Control", "no-cache");
        User user = (User) req.getSession().getAttribute("user");
        int advertisementId = Integer.parseInt(req.getParameter("id"));
        if (user != null) {
            Advertisement advertisement = AdRepository.instOf().findAdvertisementById(advertisementId);
            if (advertisement != null && user.equals(advertisement.getAuthor())) {
                req.getRequestDispatcher("/edit.html").forward(req, resp);
                return;
            }
        }
        req.getRequestDispatcher("/readonly.html").forward(req, resp);
    }
}
