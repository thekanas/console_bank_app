package by.stolybko.web.servlet;

import by.stolybko.database.dto.BankDTO;
import by.stolybko.database.entity.Bank;
import by.stolybko.service.service.BankService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/api/bank")
public class BankServlet extends HttpServlet {

    private final BankService bankService = BankService.getInstance();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String id = req.getParameter("id");
            if (id == null) {

                List<Bank> banks = bankService.getAll();

                resp.setContentType("application/json; charset=UTF-8");
                resp.setStatus(200);

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(resp.getWriter(), banks);

            } else {

                Bank bank = bankService.getBankById(Integer.valueOf(id));

                resp.setContentType("application/json; charset=UTF-8");
                resp.setStatus(200);

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(resp.getWriter(), bank);
            }

        } catch (Exception e) {
            resp.setStatus(400);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String delete = req.getParameter("delete");
            if (delete != null) {
                boolean isDelete = bankService.delete(Integer.valueOf(delete));
                if (isDelete) {
                    resp.setStatus(200);
                } else {
                    resp.setStatus(400);
                }
            } else {
                String id = req.getParameter("id");
                ObjectMapper objectMapper = new ObjectMapper();
                BankDTO bankDTO = objectMapper.readValue(req.getReader().lines().collect(Collectors.joining()), BankDTO.class);
                Bank bank;
                if (id == null) {

                    bank = bankService.save(bankDTO);

                } else {

                    bank = bankService.update(bankDTO, Integer.valueOf(id));
                }
                resp.setContentType("application/json; charset=UTF-8");
                resp.setStatus(200);
                objectMapper.writeValue(resp.getWriter(), bank);
            }

        } catch (Exception e) {
            resp.setStatus(404);
        }
    }
}
