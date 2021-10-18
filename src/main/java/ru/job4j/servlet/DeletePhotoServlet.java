package ru.job4j.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.services.Config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class DeletePhotoServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(DeletePhotoServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        Path path = Path.of(
                Config.instOf().getProperty("images-path") + id
        );
        String response = "200 OK";
        if (Files.exists(path) && Files.isRegularFile(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                LOG.error("Couldn't delete file", e);
                response = "409 Conflict";
            }
        }
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                resp.getOutputStream(), StandardCharsets.UTF_8));
        writer.print(response);
        writer.flush();
    }
}