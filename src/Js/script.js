// Base de datos de comidas (falsa) - Antipastos
const antipastos = [
    {
        id: 1,
        nombre: "Bruschetta",
        categoria: "Antipasto",
        descripcion: "Pan artesanal con tomate y albahaca fresca.",
        precio: "$5.99",
        imagen: "../images/Bruschetta.jpg"
    },
    {
        id: 2,
        nombre: "Carpaccio di Manzo",
        categoria: "Antipasto",
        descripcion: "Láminas finas de res con aceite de oliva y parmesano.",
        precio: "$8.99",
        imagen: "../images/carpaccio-di-manzo.jpg"
    },
    {
        id: 3,
        nombre: "Provolone al Forno",
        categoria: "Antipasto",
        descripcion: "Queso provolone derretido con hierbas italianas.",
        precio: "$7.50",
        imagen: "../images/provolone.webp"
    },
    {
        id: 4,
        nombre: "Olive Miste",
        categoria: "Antipasto",
        descripcion: "Mezcla de aceitunas marinadas con especias.",
        precio: "$6.49",
        imagen: "../images/aceitunas.jpg"
    },
    {
        id: 5,
        nombre: "Funghi Trifolati",
        categoria: "Antipasto",
        descripcion: "Champiñones salteados con ajo, perejil y vino blanco.",
        precio: "$7.00",
        imagen: "../images/funghi-trifolati.jpg"
    },
    {
        id: 6,
        nombre: "Focaccia",
        categoria: "Antipasto",
        descripcion: "Pan italiano con romero y aceite de oliva extra virgen.",
        precio: "$5.00",
        imagen: "../images/focatia.jpeg"
    }

];

// Pizzas
const pizzas = [
    {
        id: 7,
        nombre: "Pizza Nera",
        categoria: "Pizza",
        descripcion: "Masa con tinta de calamar, mozzarella y prosciutto.",
        precio: "$13.99",
        imagen: "../images/pizzaNera.webp"
    },
    {
        id: 8,
        nombre: "Pizza Pepperoni",
        categoria: "Pizza",
        descripcion: "Masa artesanal con pepperoni y mozzarella.",
        precio: "$12.49",
        imagen: "../images/Pizza Pepperoni.jpg"
    },
    {
        id: 9,
        nombre: "Pizza Margherita",
        categoria: "Pizza",
        descripcion: "Tomate, mozzarella fresca y hojas de albahaca.",
        precio: "$10.99",
        imagen: "../images/Margherita-Pizza-093.webp"
    },
    {
        id: 10,
        nombre: "Pizza Quattro Formaggi",
        categoria: "Pizza",
        descripcion: "Mozzarella, gorgonzola, parmesano y provolone.",
        precio: "$14.49",
        imagen: "../images/quattroformaggi.jpeg"
    },
    {
        id: 11,
        nombre: "Pizza Vegetariana",
        categoria: "Pizza",
        descripcion: "Pimientos, champiñones, calabacín y mozzarella.",
        precio: "$12.99",
        imagen: "../images/pizzavegetariana.jpeg"
    },
    {
        id: 12,
        nombre: "Pizza Diavola",
        categoria: "Pizza",
        descripcion: "Salami picante, tomate y mozzarella fresca.",
        precio: "$13.50",
        imagen: "../images/pizzadiavola.webp"
    }
];

// Pastas
const pastas = [
    {
        id: 13,
        nombre: "Pasta Carbonara",
        categoria: "Pasta",
        descripcion: "Espaguetis con crema, panceta y queso pecorino.",
        precio: "$11.99",
        imagen: "../images/pasta_carbonara.jpg"
    },
    {
        id: 14,
        nombre: "Pasta Bolognesa",
        categoria: "Pasta",
        descripcion: "Espaguetis con salsa bolognesa y queso parmesano.",
        precio: "$12.49",
        imagen: "../images/PastaBolognesa.jpg"
    },
    {
        id: 15,
        nombre: "Fettuccine Alfredo",
        categoria: "Pasta",
        descripcion: "Pasta cremosa con mantequilla y queso parmesano.",
        precio: "$13.99",
        imagen: "../images/1504715566-delish-fettuccine-alfredo.jpg"
    },
    {
        id: 16,
        nombre: "Ravioli de Espinaca",
        categoria: "Pasta",
        descripcion: "Relleno de espinaca y ricotta con salsa de tomate.",
        precio: "$10.99",
        imagen: "../images/raviolis de espinaca.jpeg"
    },
    {
        id: 17,
        nombre: "Pasta al Pesto",
        categoria: "Pasta",
        descripcion: "Pasta fresca con salsa de albahaca y piñones.",
        precio: "$12.00",
        imagen: "../images/pasta pesto.webp"
    },
    {
        id: 18,
        nombre: "Lasaña de Carne",
        categoria: "Pasta",
        descripcion: "Capas de pasta con carne, salsa de tomate y queso.",
        precio: "$14.49",
        imagen: "../images/lasana-de-carne-y-queso-2977.jpg"
    }
];

// Risottos
const risottos = [
    {
        id: 19,
        nombre: "Risotto ai Funghi",
        categoria: "Risotto",
        descripcion: "Arroz cremoso con champiñones y queso parmesano.",
        precio: "$14.99",
        imagen: "../images/risotofungui.png"
    },
    {
        id: 20,
        nombre: "Risotto al Limone",
        categoria: "Risotto",
        descripcion: "Delicado risotto con limón, mantequilla y albahaca.",
        precio: "$15.49",
        imagen: "../images/risoto limone.jpeg"
    },
    {
        id: 21,
        nombre: "Risotto alla Milanese",
        categoria: "Risotto",
        descripcion: "Risotto con azafrán, mantequilla y queso parmesano.",
        precio: "$16.00",
        imagen: "../images/a-photo-of-a-golden-bowl-of-risotto-alla-milanese_-_FRHET7GiQzKK-GCOiiuuRw-TN3e3HqSRTevX3WpZcG0ig1.jpeg"
    },
    {
        id: 22,
        nombre: "Risotto al Mar",
        categoria: "Risotto",
        descripcion: "Risotto con camarones, calamares y mejillones frescos.",
        precio: "$18.50",
        imagen: "../images/risoto mar.jpeg"
    },
    {
        id: 23,
        nombre: "Risotto al Pesto",
        categoria: "Risotto",
        descripcion: "Risotto con salsa pesto y piñones tostados.",
        precio: "$15.00",
        imagen: "../images/risoto pesto.jpeg"
    },
    {
        id: 24,
        nombre: "Risotto de Calabaza",
        categoria: "Risotto",
        descripcion: "Risotto cremoso con puré de calabaza y salvia.",
        precio: "$16.49",
        imagen: "../images/risoto calabaza.jpeg"
    }
];

// Postres
const postres = [
    {
        id: 25,
        nombre: "Tiramisú",
        categoria: "Postre",
        descripcion: "Clásico postre italiano con café y mascarpone.",
        precio: "$6.99",
        imagen: "../images/tiramisu.jpg"
    },
    {
        id: 26,
        nombre: "Panna Cotta",
        categoria: "Postre",
        descripcion: "Postre cremoso con coulis de frutos rojos.",
        precio: "$5.99",
        imagen: "../images/creamy-vegan-panna-cotta-dairy-free-fresh-and-light-dessert-thumb-12.jpg"
    },
    {
        id: 27,
        nombre: "Cannoli Siciliani",
        categoria: "Postre",
        descripcion: "Crujientes tubos rellenos de ricotta y frutas confitadas.",
        precio: "$7.50",
        imagen: "../images/Cannoli Siciliani.jpg"
    },
    {
        id: 28,
        nombre: "Gelato",
        categoria: "Postre",
        descripcion: "Helado artesanal italiano en sabores variados.",
        precio: "$4.99",
        imagen: "../images/gelato.webp"
    },
    {
        id: 29,
        nombre: "Torta della Nonna",
        categoria: "Postre",
        descripcion: "Tarta de crema pastelera y piñones tostados.",
        precio: "$6.49",
        imagen: "../images/torta della nonna.webp"
    },
    {
        id: 30,
        nombre: "Profiteroles",
        categoria: "Postre",
        descripcion: "Bolas de masa rellenas de crema y bañadas en chocolate.",
        precio: "$5.99",
        imagen: "../images/profiteroles.webp"
    }
];


// Bebidas
const bebidas = [
    {
        id: 31,
        nombre: "Vino Tinto",
        categoria: "Bebida",
        descripcion: "Selección de vino tinto italiano de la casa.",
        precio: "$14.99",
        imagen: "../images/5435_Vino_Tinto_Reservado0.jpg"
    },
    {
        id: 32,
        nombre: "Vino Blanco",
        categoria: "Bebida",
        descripcion: "Vino blanco afrutado ideal para pescados y mariscos.",
        precio: "$13.49",
        imagen: "../images/vino blanco.webp"
    },
    {
        id: 33,
        nombre: "Cerveza Artesanal",
        categoria: "Bebida",
        descripcion: "Cerveza de producción local con notas florales.",
        precio: "$6.99",
        imagen: "../images/cerveza artesanala.webp"
    },
    {
        id: 34,
        nombre: "Agua con Gas",
        categoria: "Bebida",
        descripcion: "Agua mineral italiana con burbujas finas.",
        precio: "$3.50",
        imagen: "../images/agua con gas.jpg"
    },
    {
        id: 35,
        nombre: "Agua Sin Gas",
        categoria: "Bebida",
        descripcion: "Agua mineral italiana natural sin gas.",
        precio: "$2.99",
        imagen: "../images/agua sin gas.webp"
    },
    {
        id: 36,
        nombre: "Café Espresso",
        categoria: "Bebida",
        descripcion: "Café italiano fuerte y aromático.",
        precio: "$2.50",
        imagen: "../images/cafe expresso.webp"
    }
];


// Función para mostrar los platos en formato de tarjetas para el menú completo
function mostrarComidas() {
    const antipastosList = document.getElementById('antipastos-list');
    const pizzasList = document.getElementById('pizzas-list');
    const pastasList = document.getElementById('pastas-list');
    const risottosList = document.getElementById('risottos-list');
    const postresList = document.getElementById('postres-list');
    const bebidasList = document.getElementById('bebidas-list');

    // Limpiar el contenido previo de las secciones
    antipastosList.innerHTML = '';
    pizzasList.innerHTML = '';
    pastasList.innerHTML = '';
    risottosList.innerHTML = '';
    postresList.innerHTML = '';
    bebidasList.innerHTML = '';

    // Función para crear las tarjetas de cada plato
    function crearTarjeta(comida) {
        const tarjeta = document.createElement('div');
        tarjeta.classList.add('col-md-4');
        tarjeta.classList.add('menu-card');
        tarjeta.innerHTML = `
            <div class="card menu-card">
                <img src="${comida.imagen}" class="card-img-top" alt="${comida.nombre}" />
                <div class="card-body">
                    <h5 class="card-title">${comida.nombre}</h5>
                    <p class="card-text">${comida.descripcion}</p>
                    <button class="btn-agregar">Agregar al domicilio</button>
                </div>
            </div>
        `;
        return tarjeta;
    }

    // Función para agregar comidas a cada sección (Antipastos, Pizzas, etc.)
    antipastos.forEach(comida => {
        antipastosList.appendChild(crearTarjeta(comida));
    });

    pizzas.forEach(comida => {
        pizzasList.appendChild(crearTarjeta(comida));
    });

    pastas.forEach(comida => {
        pastasList.appendChild(crearTarjeta(comida));
    });

    risottos.forEach(comida => {
        risottosList.appendChild(crearTarjeta(comida));
    });

    postres.forEach(comida => {
        postresList.appendChild(crearTarjeta(comida));
    });

    bebidas.forEach(comida => {
        bebidasList.appendChild(crearTarjeta(comida));
    });
}


// Función para mostrar las comidas en formato de tabla
function mostrarComidasEnTabla() {

    const tbody = document.querySelector("#platos-lista");  // Selecciona el cuerpo de la tabla

    // Limpiar el contenido de la tabla antes de añadir nuevos datos
    tbody.innerHTML = '';

    // Función para agregar una fila por cada comida
    function agregarFila(comida) {
        const fila = document.createElement('tr');

        fila.innerHTML = `
            <td><img src="${comida.imagen}" alt="${comida.nombre}" width="100"></td>
            <td>${comida.nombre}</td>
            <td>${comida.categoria}</td>
            <td>${comida.descripcion}</td>
            <td>${comida.precio}</td>
        `;

        tbody.appendChild(fila);
    }

    // Agregar las comidas de cada categoría a la tabla
    antipastos.forEach(comida => {
        agregarFila(comida);
    });

    pizzas.forEach(comida => {
        agregarFila(comida);
    });

    pastas.forEach(comida => {
        agregarFila(comida);
    });

    risottos.forEach(comida => {
        agregarFila(comida);
    });

    postres.forEach(comida => {
        agregarFila(comida);
    });

    bebidas.forEach(comida => {
        agregarFila(comida);
    });

}

document.addEventListener("DOMContentLoaded", mostrarComidasEnTabla);

// Ejecutar la función para mostrar las comidas cuando se cargue la página
window.onload = mostrarComidas;


