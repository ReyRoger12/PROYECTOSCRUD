import mysql.connector

class CRUDTipoProyecto:
    def __init__(self):
        self.conn = mysql.connector.connect(
            host='192.168.100.79',
            user='root',
            password='Fmsinter2',
            database='dbtaller',
            collation='utf8mb4_general_ci'
        )
        self.cursor = self.conn.cursor()

    def mostrar_menu(self):
        print("\nCRUD de Tipo de Proyecto")
        print("1. Agregar Tipo de Proyecto")
        print("2. Mostrar Tipos de Proyecto")
        print("3. Actualizar Tipo de Proyecto")
        print("4. Eliminar Tipo de Proyecto")
        print("5. Salir")

    def agregar_tipo_proyecto(self):
        nombre = input("Nombre del tipo de proyecto: ")
        sql = "INSERT INTO tipoproyec (nombre_tipo) VALUES (%s)"
        self.cursor.execute(sql, (nombre,))
        self.conn.commit()
        print("Tipo de proyecto agregado correctamente.")

    def mostrar_tipos_proyecto(self):
        self.cursor.execute("SELECT clave_tipo, nombre_tipo FROM tipoproyec")
        for clave_tipo, nombre_tipo in self.cursor.fetchall():
            print(f"ID: {clave_tipo}, Nombre: {nombre_tipo}")

    def actualizar_tipo_proyecto(self):
        id_tipo = int(input("ID del tipo de proyecto a actualizar: "))
        nombre = input("Nuevo nombre del tipo de proyecto: ")
        sql = "UPDATE tipoproyec SET nombre_tipo = %s WHERE clave_tipo = %s"
        self.cursor.execute(sql, (nombre, id_tipo))
        self.conn.commit()
        print("Tipo de proyecto actualizado correctamente.")

    def eliminar_tipo_proyecto(self):
        id_tipo = int(input("ID del tipo de proyecto a eliminar: "))
        sql = "DELETE FROM tipoproyec WHERE clave_tipo = %s"
        self.cursor.execute(sql, (id_tipo,))
        self.conn.commit()
        print("Tipo de proyecto eliminado correctamente.")

    def ejecutar(self):
        while True:
            self.mostrar_menu()
            opcion = input("Seleccione una opción: ")
            if opcion == "1":
                self.agregar_tipo_proyecto()
            elif opcion == "2":
                self.mostrar_tipos_proyecto()
            elif opcion == "3":
                self.actualizar_tipo_proyecto()
            elif opcion == "4":
                self.eliminar_tipo_proyecto()
            elif opcion == "5":
                print("Saliendo...")
                break
            else:
                print("Opción inválida.")

        self.cursor.close()
        self.conn.close()

if __name__ == "__main__":
    crud = CRUDTipoProyecto()
    crud.ejecutar()
