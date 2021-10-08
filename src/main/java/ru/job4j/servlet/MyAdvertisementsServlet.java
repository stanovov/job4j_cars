package ru.job4j.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Данный сервлет фильтруется классом-фильтром AuthFilter
 */
public class MyAdvertisementsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Cache-Control", "no-cache");
        resp.sendRedirect(req.getContextPath() + "/my.html");
    }
}
