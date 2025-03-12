import mysql.connector
from mysql.connector import Error

def conectar():
    try:
        conn = mysql.connector.connect(
            host='192.168.100.79',
            database='dbtaller',
            user='root',
            password='Fmsinter2',
            collation='utf8mb4_general_ci'
        )
        if conn.is_connected():
            return conn
    except Error as e:
        print(f"Error al conectar a la base de datos: {e}")
        return None

def mostrar_menu():
    print("\nCRUD de Profesor")
    print("1. Agregar Profesor")
    print("2. Mostrar Profesores")
    print("3. Actualizar Profesor")
    print("4. Eliminar Profesor")
    print("5. Salir")
    return int(input("Seleccione una opción: "))

def agregar_profesor(conn, cursor):
    nombre = input("Nombre del profesor: ")
    sql = "INSERT INTO profesor (nombre_profesor) VALUES (%s)"
    cursor.execute(sql, (nombre,))
    conn.commit()
    print("Profesor agregado.")

def mostrar_profesores(cursor):
    sql = "SELECT clave_profesor, nombre_profesor FROM profesor"
    cursor.execute(sql)
    for row in cursor.fetchall():
        print(f"ID: {row[0]}, Nombre: {row[1]}")

def actualizar_profesor(conn, cursor):
    id_profesor = int(input("ID del profesor a actualizar: "))
    nuevo_nombre = input("Nuevo nombre del profesor: ")
    sql = "UPDATE profesor SET nombre_profesor = %s WHERE clave_profesor = %s"
    cursor.execute(sql, (nuevo_nombre, id_profesor))
    conn.commit()
    print("Profesor actualizado.")

def eliminar_profesor(conn, cursor):
    id_profesor = int(input("ID del profesor a eliminar: "))
    sql = "DELETE FROM profesor WHERE clave_profesor = %s"
    cursor.execute(sql, (id_profesor,))
    conn.commit()
    print("Profesor eliminado.")

def main():
    conn = conectar()
    if conn:
        cursor = conn.cursor()
        while True:
            opcion = mostrar_menu()
            if opcion == 1:
                agregar_profesor(conn, cursor)
            elif opcion == 2:
                mostrar_profesores(cursor)
            elif opcion == 3:
                actualizar_profesor(conn, cursor)
            elif opcion == 4:
                eliminar_profesor(conn, cursor)
            elif opcion == 5:
                print("Saliendo...")
                break
            else:
                print("Opción inválida.")
        cursor.close()
        conn.close()

if __name__ == "__main__":
    main()
