package by.stolybko.web.servlet;

import by.stolybko.database.dto.UserDTO;
import by.stolybko.database.dto.UserShowDTO;
import by.stolybko.service.service.UserService;
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
 * класс для предоставления CRUD операций в клиент-серверной модели для сущности User
 */
@WebServlet("/api/user")
public class UserServlet extends HttpServlet {

    private final UserService userService = UserService.getInstance();

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

                List<UserShowDTO> users = userService.getAll();

                resp.setContentType("application/json; charset=UTF-8");
                resp.setStatus(200);

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(resp.getWriter(), users);

            } else {

                UserShowDTO user = userService.getUserById(Long.valueOf(id));

                resp.setContentType("application/json; charset=UTF-8");
                resp.setStatus(200);

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(resp.getWriter(), user);
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
                boolean isDelete = userService.delete(Long.valueOf(delete));
                if (isDelete) {
                    resp.setStatus(200);
                } else {
                    resp.setStatus(400);
                }
            } else {
                String id = req.getParameter("id");
                ObjectMapper objectMapper = new ObjectMapper();
                UserDTO user = objectMapper.readValue(req.getReader().lines().collect(Collectors.joining()), UserDTO.class);
                UserShowDTO userShowDTO;
                if (id == null) {

                    userShowDTO = userService.save(user);

                } else {

                    userShowDTO = userService.update(user, Long.valueOf(id));
                }
                resp.setContentType("application/json; charset=UTF-8");
                resp.setStatus(200);
                objectMapper.writeValue(resp.getWriter(), userShowDTO);
            }

        } catch (Exception e) {
            resp.setStatus(404);
        }
    }
}
