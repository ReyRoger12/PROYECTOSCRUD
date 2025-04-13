import flet as ft
import mysql.connector

# Configuración de conexión a la base de datos
db_config = {
    "host": "192.168.56.1",
    "user": "root",
    "password": "ReyOscuro",
    "database": "el_negrito"
}

# Función de conexión
def get_connection():
    return mysql.connector.connect(**db_config)

def obtener_datos(tabla):
    conn = get_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute(f"SELECT * FROM {tabla}")
    datos = cursor.fetchall()
    cursor.close()
    conn.close()
    return datos

def insertar_dato(tabla, datos):
    conn = get_connection()
    cursor = conn.cursor()
    columnas = ", ".join(datos.keys())
    valores = ", ".join(["%s"] * len(datos))
    sql = f"INSERT INTO {tabla} ({columnas}) VALUES ({valores})"
    cursor.execute(sql, list(datos.values()))
    conn.commit()
    cursor.close()
    conn.close()

def actualizar_dato(tabla, id_valor, datos):
    conn = get_connection()
    cursor = conn.cursor()
    asignaciones = ", ".join([f"{col}=%s" for col in datos.keys()])
    sql = f"UPDATE {tabla} SET {asignaciones} WHERE id=%s"
    valores = list(datos.values()) + [id_valor]
    cursor.execute(sql, valores)
    conn.commit()
    cursor.close()
    conn.close()

def eliminar_dato(tabla, id_valor):
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute(f"DELETE FROM {tabla} WHERE id = %s", (id_valor,))
    conn.commit()
    cursor.close()
    conn.close()

def main(page: ft.Page):
    page.title = "Catálogos POS - El Negrito"
    page.window_maximized = True

    def crear_tabla(nombre_tabla, nombre_sql, campos, campos_especiales=None, mostrar_relacion=None):
        campos_inputs = []
        id_actualizando = ft.Ref[int]()
        campos_especiales = campos_especiales or {}

        for campo in campos:
            if campo in campos_especiales:
                campos_inputs.append(campos_especiales[campo])
            else:
                campos_inputs.append(ft.TextField(label=campo))

        def agregar_o_actualizar_dato(e):
            nuevo = {}
            for campo, input_ctrl in zip(campos, campos_inputs):
                nuevo[campo] = input_ctrl.value

            if id_actualizando.current is not None:
                actualizar_dato(nombre_sql, id_actualizando.current, nuevo)
                id_actualizando.current = None
                agregar_btn.text = "Agregar"
            else:
                insertar_dato(nombre_sql, nuevo)

            limpiar_campos()
            actualizar_tabla()

        def limpiar_campos():
            for ctrl in campos_inputs:
                ctrl.value = ""
            id_actualizando.current = None
            agregar_btn.text = "Agregar"
            page.update()

        def editar_dato(item):
            for campo, input_ctrl in zip(campos, campos_inputs):
                input_ctrl.value = item[campo]
            id_actualizando.current = item["id"]
            agregar_btn.text = "Actualizar"
            page.update()

        def actualizar_tabla():
            tabla_datos.rows.clear()
            lista_datos = obtener_datos(nombre_sql)
            for item in lista_datos:
                fila = [ft.DataCell(ft.Text(str(item.get("id", ""))))]
                for campo in campos:
                    valor = item.get(campo, "")
                    if mostrar_relacion and campo in mostrar_relacion:
                        valor = mostrar_relacion[campo](valor)
                    fila.append(ft.DataCell(ft.Text(str(valor))))

                fila.append(
                    ft.DataCell(
                        ft.Row([
                            ft.IconButton(icon=ft.icons.EDIT, tooltip="Editar", on_click=lambda e, i=item: editar_dato(i)),
                            ft.IconButton(icon=ft.icons.DELETE, tooltip="Eliminar", on_click=lambda e, i=item: (eliminar_dato(nombre_sql, i["id"]), actualizar_tabla()))
                        ])
                    )
                )
                tabla_datos.rows.append(ft.DataRow(cells=fila))
            page.update()

        tabla_datos = ft.DataTable(
            columns=[ft.DataColumn(ft.Text(col)) for col in ["ID"] + campos + ["Acciones"]],
            rows=[]
        )

        agregar_btn = ft.ElevatedButton(text="Agregar", icon=ft.icons.ADD, on_click=agregar_o_actualizar_dato)

        layout = ft.Column(
            controls=[
                ft.Text(nombre_tabla, size=22, weight=ft.FontWeight.BOLD),
                *campos_inputs,
                agregar_btn,
                tabla_datos
            ],
            scroll=ft.ScrollMode.AUTO
        )

        actualizar_tabla()
        return layout

    def get_tab_categoria():
        dropdown_estado = ft.Dropdown(
            label="Estado",
            options=[ft.dropdown.Option("Activo"), ft.dropdown.Option("Inactivo")]
        )
        return crear_tabla(
            "Categorías",
            "categorias",
            ["Nombre", "Descripción", "Fecha_creacion", "Estado"],
            campos_especiales={"Estado": dropdown_estado}
        )

    def get_tab_articulo():
        categorias_opciones = obtener_datos("categorias")
        dropdown_categoria = ft.Dropdown(
            label="CategoriaID",
            options=[ft.dropdown.Option(str(cat["id"]), cat["Nombre"]) for cat in categorias_opciones]
        )
        dropdown_disponible = ft.Dropdown(
            label="Disponible",
            options=[ft.dropdown.Option("Sí"), ft.dropdown.Option("No")]
        )

        def mostrar_nombre_categoria(id_categoria):
            for cat in categorias_opciones:
                if str(cat["id"]) == str(id_categoria):
                    return cat["Nombre"]
            return "Desconocida"

        return crear_tabla(
            "Artículos",
            "articulos",
            ["Nombre", "Precio", "Stock", "Codigo", "Unidad", "Disponible", "CategoriaID"],
            campos_especiales={"Disponible": dropdown_disponible, "CategoriaID": dropdown_categoria},
            mostrar_relacion={"CategoriaID": mostrar_nombre_categoria}
        )

    def get_tab_cliente():
        return crear_tabla(
            "Clientes",
            "clientes",
            ["Nombre", "Telefono", "Correo", "Direccion", "RFC", "Fecha_registro"]
        )

    def get_tab_empleado():
        dropdown_activo = ft.Dropdown(
            label="Activo",
            options=[ft.dropdown.Option("Sí"), ft.dropdown.Option("No")]
        )
        return crear_tabla(
            "Empleados",
            "empleados",
            ["Nombre", "Puesto", "Salario", "Direccion", "Fecha_ingreso", "Activo"],
            campos_especiales={"Activo": dropdown_activo}
        )

    content_area = ft.Container(
        content=ft.Column(controls=[], expand=True),
        padding=20,
        expand=True,
        bgcolor=ft.colors.SURFACE_VARIANT,
        border_radius=20,
    )

    def cambiar_pestana(index):
        content_area.content.controls.clear()
        if index == 0:
            content_area.content.controls.append(get_tab_categoria())
        elif index == 1:
            content_area.content.controls.append(get_tab_articulo())
        elif index == 2:
            content_area.content.controls.append(get_tab_cliente())
        elif index == 3:
            content_area.content.controls.append(get_tab_empleado())
        page.update()

    nav_rail = ft.NavigationRail(
        selected_index=0,
        label_type=ft.NavigationRailLabelType.ALL,
        extended=True,
        destinations=[
            ft.NavigationRailDestination(icon=ft.icons.CATEGORY, label="Categorías"),
            ft.NavigationRailDestination(icon=ft.icons.INVENTORY_2, label="Artículos"),
            ft.NavigationRailDestination(icon=ft.icons.PEOPLE, label="Clientes"),
            ft.NavigationRailDestination(icon=ft.icons.BADGE, label="Empleados"),
        ],
        on_change=lambda e: cambiar_pestana(e.control.selected_index),
    )

    cambiar_pestana(0)

    page.add(
        ft.Column([
            ft.Container(
                content=ft.Text("Hamburguesas El Negrito - Sistema de Catálogos", size=28, weight=ft.FontWeight.BOLD),
                padding=20,
                bgcolor=ft.colors.PRIMARY_CONTAINER,
                alignment=ft.alignment.center_left
            ),
            ft.Row([
                nav_rail,
                content_area
            ], expand=True)
        ], expand=True)
    )

ft.app(target=main)
