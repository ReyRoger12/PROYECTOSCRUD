import java.sql.*;
import java.util.Scanner;

public class CRUDProfesor {

    static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    static final String DB_URL = "jdbc:mariadb://192.168.100.79:3306/dbtaller"; 
    static final String USER = "Rey"; 
    static final String PASS = "ReyMilitar5"; 

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
                        agregarProfesor(conn, scanner);
                        break;
                    case 2:
                        mostrarProfesores(conn);
                        break;
                    case 3:
                        actualizarProfesor(conn, scanner);
                        break;
                    case 4:
                        eliminarProfesor(conn, scanner);
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
        System.out.println("CRUD de Profesor");
        System.out.println("1. Agregar Profesor");
        System.out.println("2. Mostrar Profesores");
        System.out.println("3. Actualizar Profesor");
        System.out.println("4. Eliminar Profesor");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opción: ");
    }

    static void agregarProfesor(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Nombre del profesor: ");
        String nombre = scanner.nextLine();

        String sql = "INSERT INTO profesor (nombre_profesor) VALUES (?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, nombre);
        pstmt.executeUpdate();
        System.out.println("Profesor agregado.");
    }

    static void mostrarProfesores(Connection conn) throws SQLException {
        String sql = "SELECT clave_profesor, nombre_profesor FROM profesor";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("Lista de Profesores:");
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("clave_profesor") + ", Nombre: " + rs.getString("nombre_profesor"));
        }
    }

    static void actualizarProfesor(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("ID del profesor a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        System.out.print("Nuevo nombre del profesor: ");
        String nombre = scanner.nextLine();

        String sql = "UPDATE profesor SET nombre_profesor = ? WHERE clave_profesor = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, nombre);
        pstmt.setInt(2, id);
        int filasAfectadas = pstmt.executeUpdate();

        if (filasAfectadas > 0) {
            System.out.println("Profesor actualizado.");
        } else {
            System.out.println("No se encontró el profesor con ID " + id);
        }
    }

    static void eliminarProfesor(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("ID del profesor a eliminar: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM profesor WHERE clave_profesor = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        int filasAfectadas = pstmt.executeUpdate();

        if (filasAfectadas > 0) {
            System.out.println("Profesor eliminado.");
        } else {
            System.out.println("No se encontró el profesor con ID " + id);
        }
    }
}