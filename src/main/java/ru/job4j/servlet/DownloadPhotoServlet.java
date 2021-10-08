package ru.job4j.servlet;

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
        Path path = Path.of("C:\\images\\advertisements\\" + id);
        File photo;
        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            photo = new File("C:\\images\\advertisements\\0.png");
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
