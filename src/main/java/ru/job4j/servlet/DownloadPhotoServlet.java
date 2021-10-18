package ru.job4j.servlet;

import ru.job4j.services.Config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DownloadPhotoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        Path path = Path.of(
                Config.instOf().getProperty("images-path") + id
        );
        File photo;
        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            photo = new File(Config.instOf().getProperty("blank-image"));
        } else {
            photo = path.toFile();
        }
        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + photo.getName() + "\"");
        try (FileInputStream stream = new FileInputStream(photo)) {
            resp.getOutputStream().write(stream.readAllBytes());
        }
    }
}
