// ==================== COMIDAS ====================

// Cargar todas las comidas
async function cargarComidas() {
    try {
        const res = await fetch("/api/comidas");
        if (!res.ok) throw new Error("Error al obtener comidas");
        const comidas = await res.json();
        const tabla = document.getElementById("tablaComidas");
        tabla.innerHTML = "";
        comidas.forEach(c => {
            tabla.innerHTML += `
                <tr>
                    <td>${c.id}</td>
                    <td>${c.nombre}</td>
                    <td>${c.precio}</td>
                    <td>
                        <button onclick="editarComida(${c.id}, '${c.nombre}', ${c.precio})">Editar</button>
                        <button onclick="eliminarComida(${c.id})">Eliminar</button>
                    </td>
                </tr>`;
        });
    } catch (error) {
        console.error(error);
    }
}

// Guardar o actualizar comida
async function guardarComida(event) {
    event.preventDefault();
    const id = document.getElementById("id").value;
    const nombre = document.getElementById("nombre").value;
    const precio = document.getElementById("precio").value;

    const url = "/api/comidas" + (id ? "/" + id : "");
    const method = id ? "PUT" : "POST";

    await fetch(url, {
        method: method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ id, nombre, precio })
    });

    document.getElementById("formComida").reset();
    cargarComidas();
}

// Rellenar formulario con datos de la comida
function editarComida(id, nombre, precio) {
    document.getElementById("id").value = id;
    document.getElementById("nombre").value = nombre;
    document.getElementById("precio").value = precio;
}

// Eliminar comida
async function eliminarComida(id) {
    await fetch("/api/comidas/" + id, { method: "DELETE" });
    cargarComidas();
}

// Asignar evento al formulario de comida
document.getElementById("formComida")?.addEventListener("submit", guardarComida);

// ==================== CLIENTES ====================

// Cargar todos los clientes
async function cargarClientes() {
    try {
        const res = await fetch("/api/clientes");
        if (!res.ok) throw new Error("Error al obtener clientes");
        const clientes = await res.json();
        const tabla = document.getElementById("tablaClientes");
        tabla.innerHTML = "";
        clientes.forEach(c => {
            tabla.innerHTML += `
                <tr>
                    <td>${c.id}</td>
                    <td>${c.nombre}</td>
                    <td>${c.email}</td>
                    <td>
                        <button onclick="editarCliente(${c.id}, '${c.nombre}', '${c.email}')">Editar</button>
                        <button onclick="eliminarCliente(${c.id})">Eliminar</button>
                    </td>
                </tr>`;
        });
    } catch (error) {
        console.error(error);
    }
}

// Guardar o actualizar cliente
async function guardarCliente(event) {
    event.preventDefault();
    const id = document.getElementById("id").value;
    const nombre = document.getElementById("nombre").value;
    const email = document.getElementById("email").value;

    const url = "/api/clientes" + (id ? "/" + id : "");
    const method = id ? "PUT" : "POST";

    await fetch(url, {
        method: method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ id, nombre, email })
    });

    document.getElementById("formCliente").reset();
    cargarClientes();
}

// Rellenar formulario con datos del cliente
function editarCliente(id, nombre, email) {
    document.getElementById("id").value = id;
    document.getElementById("nombre").value = nombre;
    document.getElementById("email").value = email;
}

// Eliminar cliente
async function eliminarCliente(id) {
    await fetch("/api/clientes/" + id, { method: "DELETE" });
    cargarClientes();
}

// Asignar evento al formulario de cliente
document.getElementById("formCliente")?.addEventListener("submit", guardarCliente);
