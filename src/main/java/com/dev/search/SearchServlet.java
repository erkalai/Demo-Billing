package com.dev.search;



import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class SearchServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchTerm = request.getParameter("query");

        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        List<String> result = new ArrayList<>();

        // Database connection setup (use your database credentials)
        String jdbcURL = "jdbc:sqlserver://localhost:1433;databaseName=BusBooking;encrypt=true;trustServerCertificate=true";
        String jdbcUsername = "root";
        String jdbcPassword = "root";

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword)) {
            String sql = "SELECT bus_station FROM bus_stations_list WHERE bus_station LIKE ?";
            System.out.println("inside try");
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, "%" + searchTerm + "%");

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    result.add(resultSet.getString("bus_station"));
                    
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Convert the list to JSON format

        System.out.println("JSON");
        String jsonResponse = new com.google.gson.Gson().toJson(result);
        out.write(jsonResponse);
        System.out.println(jsonResponse);
    }
}

