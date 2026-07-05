import java.sql.*;

public class CheckDb {
    public static void main(String[] args) throws Exception {
        // Connect to the same H2 in-memory DB the Spring app uses
        // Note: This won't work for in-memory since it's a separate JVM.
        // Instead we use the H2 TCP server approach - but since we can't,
        // we'll use the file-based approach by checking schema.sql exists
        // and confirming via the API endpoints.
        System.out.println("Use /h2-console at http://localhost:8080/h2-console");
        System.out.println("JDBC URL: jdbc:h2:mem:scamshielddb");
        System.out.println("Username: sa  Password: (empty)");
    }
}
