import java.sql.*;
import java.util.Scanner;

public class CRUDLineaInv {
    static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    static final String DB_URL = "jdbc:mariadb://192.168.171.200:3306/dbtaller"; 
    static final String USER = "Odin";
    static final String PASS = "Cuervo";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            do {
                mostrarMenu();
                opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar el buffer

                switch (opcion) {
                    case 1:
                        agregarLinea(conn, scanner);
                        break;
                    case 2:
                        mostrarLineas(conn);
                        break;
                    case 3:
                        actualizarLinea(conn, scanner);
                        break;
                    case 4:
                        eliminarLinea(conn, scanner);
                        break;
                    case 5:
                        System.out.println("Saliendo...");
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } while (opcion != 5);

            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    static void mostrarMenu() {
        System.out.println("CRUD de Línea de Investigación");
        System.out.println("1. Agregar Línea");
        System.out.println("2. Mostrar Líneas");
        System.out.println("3. Actualizar Línea");
        System.out.println("4. Eliminar Línea");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opción: ");
    }

    static void agregarLinea(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Nombre de la línea de investigación: ");
        String nombre = scanner.nextLine();

        String sql = "INSERT INTO lineainv (nombre_linea) VALUES (?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, nombre);
        pstmt.executeUpdate();
        System.out.println("Línea de investigación agregada.");
    }

    static void mostrarLineas(Connection conn) throws SQLException {
        String sql = "SELECT clave_linea, nombre_linea FROM lineainv";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("Lista de Líneas de Investigación:");
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("clave_linea") + ", Nombre: " + rs.getString("nombre_linea"));
        }
    }

    static void actualizarLinea(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("ID de la línea a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        System.out.print("Nuevo nombre de la línea: ");
        String nombre = scanner.nextLine();

        String sql = "UPDATE lineainv SET nombre_linea = ? WHERE clave_linea = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, nombre);
        pstmt.setInt(2, id);
        int filasAfectadas = pstmt.executeUpdate();

        if (filasAfectadas > 0) {
            System.out.println("Línea de investigación actualizada.");
        } else {
            System.out.println("No se encontró la línea con ID " + id);
        }
    }

    static void eliminarLinea(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("ID de la línea a eliminar: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM lineainv WHERE clave_linea = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        int filasAfectadas = pstmt.executeUpdate();

        if (filasAfectadas > 0) {
            System.out.println("Línea de investigación eliminada.");
        } else {
            System.out.println("No se encontró la línea con ID " + id);
        }
    }
}