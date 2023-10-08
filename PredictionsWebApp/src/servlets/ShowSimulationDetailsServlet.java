package servlets;

import DTO.WorldDTO;
import Exception.ERROR.ErrorException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import servletutils.ServletUtils;
import servletutils.SimulationManager;

import java.io.PrintWriter;

@WebServlet("/file/show-details")
public class ShowSimulationDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws java.io.IOException {

    }

    @Override
    protected void doPost(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws java.io.IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String fileNameToSearchFor = request.getParameter("fileName");
        SimulationManager simulationManager = ServletUtils.getSimulationManager(getServletContext());
        try {
            PrintWriter out = response.getWriter();
            WorldDTO simulationDetails = simulationManager.getSimulationDetails(fileNameToSearchFor);
            out.println("Simulation details:");
            out.println("Environment Properties: " + simulationDetails.getEnvironmentProperties());
            out.println("Rules: " + simulationDetails.getRules());
            out.println("Entities: " + simulationDetails.getEntities());


        } catch (ErrorException e) {
            throw new RuntimeException(e);
        }

    }

}
