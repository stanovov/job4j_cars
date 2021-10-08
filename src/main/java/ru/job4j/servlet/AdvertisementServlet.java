package ru.job4j.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.model.Advertisement;
import ru.job4j.services.AdvertisementService;
import ru.job4j.store.AdRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class AdvertisementServlet extends HttpServlet {

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");
        OutputStream output = resp.getOutputStream();
        String json;
        if (req.getParameter("id") != null) {
            int id = Integer.parseInt(req.getParameter("id"));
            Advertisement advertisement = AdRepository.instOf().findAdvertisementById(id);
            json = GSON.toJson(advertisement);
        } else {
            Map<String, String> params = req.getParameterMap().entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            v -> v.getValue()[0]
                    ));
            Collection<Advertisement> advertisements = AdvertisementService.instOf()
                    .getAdvertisements(params);
            json = GSON.toJson(advertisements);
        }
        output.write(json.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("UTF-8");
        Advertisement advertisement = GSON.fromJson(req.getReader(), Advertisement.class);
        AdRepository.instOf().saveAdvertisement(advertisement);
        String json = GSON.toJson(advertisement);
        OutputStream output = resp.getOutputStream();
        output.write(json.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }
}
