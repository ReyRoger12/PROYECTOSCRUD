import java.sql.*;
import java.util.Scanner;

public class CRUDTipoProyecto {
    static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    static final String DB_URL = "jdbc:mariadb://192.168.100.79:3306/dbtaller";
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
                System.out.print("Seleccione una opción: ");
                opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar el buffer

                switch (opcion) {
                    case 1:
                        agregarTipoProyecto(conn, scanner);
                        break;
                    case 2:
                        mostrarTiposProyecto(conn);
                        break;
                    case 3:
                        actualizarTipoProyecto(conn, scanner);
                        break;
                    case 4:
                        eliminarTipoProyecto(conn, scanner);
                        break;
                    case 5:
                        System.out.println("Saliendo...");
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } while (opcion != 5);

            conn.close();
            scanner.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    static void mostrarMenu() {
        System.out.println("\nCRUD de Tipo de Proyecto");
        System.out.println("1. Agregar Tipo de Proyecto");
        System.out.println("2. Mostrar Tipos de Proyecto");
        System.out.println("3. Actualizar Tipo de Proyecto");
        System.out.println("4. Eliminar Tipo de Proyecto");
        System.out.println("5. Salir");
    }

    static void agregarTipoProyecto(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Nombre del tipo de proyecto: ");
        String nombre = scanner.nextLine();

        String sql = "INSERT INTO tipoproyec (nombre_tipo) VALUES (?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, nombre);
        pstmt.executeUpdate();
        System.out.println("Tipo de proyecto agregado correctamente.");
    }

    static void mostrarTiposProyecto(Connection conn) throws SQLException {
        String sql = "SELECT clave_tipo, nombre_tipo FROM tipoproyec";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        System.out.println("\nLista de Tipos de Proyecto:");
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("clave_tipo") + ", Nombre: " + rs.getString("nombre_tipo"));
        }
    }

    static void actualizarTipoProyecto(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("ID del tipo de proyecto a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        System.out.print("Nuevo nombre del tipo de proyecto: ");
        String nombre = scanner.nextLine();

        String sql = "UPDATE tipoproyec SET nombre_tipo = ? WHERE clave_tipo = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, nombre);
        pstmt.setInt(2, id);
        int filasAfectadas = pstmt.executeUpdate();

        if (filasAfectadas > 0) {
            System.out.println("Tipo de proyecto actualizado correctamente.");
        } else {
            System.out.println("No se encontró el tipo de proyecto con ID " + id);
        }
    }

    static void eliminarTipoProyecto(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("ID del tipo de proyecto a eliminar: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM tipoproyec WHERE clave_tipo = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        int filasAfectadas = pstmt.executeUpdate();

        if (filasAfectadas > 0) {
            System.out.println("Tipo de proyecto eliminado correctamente.");
        } else {
            System.out.println("No se encontró el tipo de proyecto con ID " + id);
        }
    }
}
