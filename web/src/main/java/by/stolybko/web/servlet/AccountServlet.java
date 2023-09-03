package by.stolybko.web.servlet;

import by.stolybko.database.dto.AccountDTO;
import by.stolybko.database.dto.AccountShowDTO;
import by.stolybko.database.entity.Account;
import by.stolybko.service.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * класс для предоставления CRUD операций в клиент-серверной модели для сущности Account
 */
@WebServlet("/api/account")
public class AccountServlet extends HttpServlet {

    private final AccountService accountService = AccountService.getInstance();

    /**
     * метод предоставляет операцию READ
     * Если запрос содержит параметр "id" - выводится сущность с указанным id
     * Если запрос не содержит параметр "id" - выводятся все сущности
     * В случае успеха операции возвращается ответ с status code "200"
     * В случае ошибки операции возвращается ответ с status code "400"
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String id = req.getParameter("id");
            if (id == null) {

                List<AccountShowDTO> accounts = accountService.getAll();

                resp.setContentType("application/json; charset=UTF-8");
                resp.setStatus(200);

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(resp.getWriter(), accounts);

            } else {

                AccountShowDTO account = accountService.getAccountById(Long.valueOf(id));

                resp.setContentType("application/json; charset=UTF-8");
                resp.setStatus(200);

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(resp.getWriter(), account);
            }

        } catch (Exception e) {
            resp.setStatus(400);
        }
    }

    /**
     * метод предоставляет операции CREATE, UPDATE, DELETE
     * Если запрос содержит параметр "delete" - удаляется сущность с id равным значению параметра
     * Если запрос не содержит параметр "delete" но содержит параметр "id" - обновляется сущность с указанным id
     * Если запрос не содержит параметр "delete" и "id" - создается новая сущность
     * В случае успеха операции возвращается ответ с status code "200"
     * В случае ошибки операции возвращается ответ с status code "400" или "404"
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String delete = req.getParameter("delete");
            if (delete != null) {
                boolean isDelete = accountService.delete(Long.valueOf(delete));
                if (isDelete) {
                    resp.setStatus(200);
                } else {
                    resp.setStatus(400);
                }
            } else {
                String id = req.getParameter("id");
                ObjectMapper objectMapper = new ObjectMapper();
                AccountDTO accountDTO = objectMapper.readValue(req.getReader().lines().collect(Collectors.joining()), AccountDTO.class);
                Account account;
                if (id == null) {

                    account = accountService.save(accountDTO);

                } else {

                    account = accountService.update(accountDTO, Long.valueOf(id));
                }
                resp.setContentType("application/json; charset=UTF-8");
                resp.setStatus(200);
                objectMapper.writeValue(resp.getWriter(), account);
            }

        } catch (Exception e) {
            resp.setStatus(404);
        }
    }
}
