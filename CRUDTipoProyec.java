import java.sql.*;
import java.util.Scanner;

public class CRUDTipoProyec {
    private static final String URL = "jdbc:mysql://localhost:3306/dbtaller";
    private static final String USER = "root";
    private static final String PASSWORD = "mariadb";
    private static Connection connection;
    
    public static void main(String[] args) {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Scanner scanner = new Scanner(System.in);
            int opcion;
            do {
                System.out.println("\n1. Crear tipo de proyecto");
                System.out.println("2. Mostrar tipos de proyecto");
                System.out.println("3. Actualizar tipo de proyecto");
                System.out.println("4. Eliminar tipo de proyecto");
                System.out.println("5. Salir");
                System.out.print("Seleccione una opción: ");
                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        crearTipoProyecto(scanner);
                        break;
                    case 2:
                        mostrarTiposProyecto();
                        break;
                    case 3:
                        actualizarTipoProyecto(scanner);
                        break;
                    case 4:
                        eliminarTipoProyecto(scanner);
                        break;
                    case 5:
                        System.out.println("Saliendo del programa...");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            } while (opcion != 5);
            scanner.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void crearTipoProyecto(Scanner scanner) throws SQLException {
        System.out.print("Ingrese el nombre del tipo de proyecto: ");
        String nombre = scanner.nextLine();
        String sql = "INSERT INTO tipoproyec (nombre_tipo) VALUES (?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, nombre);
        statement.executeUpdate();
        System.out.println("Tipo de proyecto creado exitosamente.");
    }

    private static void mostrarTiposProyecto() throws SQLException {
        String sql = "SELECT * FROM tipoproyec";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            System.out.println("ID: " + resultSet.getInt("clave_tipo") + ", Nombre: " + resultSet.getString("nombre_tipo"));
        }
    }

    private static void actualizarTipoProyecto(Scanner scanner) throws SQLException {
        System.out.print("Ingrese el ID del tipo de proyecto a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Ingrese el nuevo nombre del tipo de proyecto: ");
        String nuevoNombre = scanner.nextLine();
        String sql = "UPDATE tipoproyec SET nombre_tipo = ? WHERE clave_tipo = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, nuevoNombre);
        statement.setInt(2, id);
        statement.executeUpdate();
        System.out.println("Tipo de proyecto actualizado exitosamente.");
    }

    private static void eliminarTipoProyecto(Scanner scanner) throws SQLException {
        System.out.print("Ingrese el ID del tipo de proyecto a eliminar: ");
        int id = scanner.nextInt();
        String sql = "DELETE FROM tipoproyec WHERE clave_tipo = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.executeUpdate();
        System.out.println("Tipo de proyecto eliminado exitosamente.");
    }
}
