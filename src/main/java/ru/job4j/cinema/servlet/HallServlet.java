package ru.job4j.cinema.servlet;

import ru.job4j.cinema.service.JSONTicketService;
import ru.job4j.cinema.service.TicketService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class HallServlet extends HttpServlet {

    private final TicketService service = new JSONTicketService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        var outputMessage = service.getOrders();
        PrintWriter writer = new PrintWriter(resp.getOutputStream(),
                true, StandardCharsets.UTF_8);
        writer.print(outputMessage);
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        var inputData = req.getReader().lines()
                .collect(Collectors.joining(System.lineSeparator()));
        var message = service.reservePlace(inputData);
        PrintWriter writer = new PrintWriter(resp.getOutputStream(),
                true, StandardCharsets.UTF_8);
        writer.print(message);
        writer.flush();
    }
}
