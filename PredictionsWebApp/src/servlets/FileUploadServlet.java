package servlets;

import Exception.ERROR.ErrorException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import servletutils.ServletUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Scanner;
@WebServlet("/file/upload")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        Part fileNamePart = request.getPart("file"); // Retrieves <input type="file" name="filename">
        String fileName = fileNamePart.getSubmittedFileName();
        System.out.println("fileName: " + fileName);
        Collection<Part> fileParts = request.getParts();
        StringBuilder fileContent = new StringBuilder();
        createFileContent(fileParts, fileContent);
        try {
            ServletUtils.getSimulationManager(getServletContext()).addSimulation(fileContent.toString(), fileName);
        } catch (ErrorException e) {
            throw new RuntimeException(e);
        }
    }

    private void createFileContent(Collection<Part> parts, StringBuilder fileContent) throws IOException {
        for (Part part : parts) {
            //printPart(part, out);
            //to write the content of the file to an actual file in the system (will be created at c:\samplefile)
            //part.write("samplefile");
            //to write the content of the file to a string
            fileContent.append(readFromInputStream(part.getInputStream()));
        }
    }


    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

    private void printFileContent(String content, PrintWriter out) {
        out.println("File content:");
        out.println(content);
    }

}
