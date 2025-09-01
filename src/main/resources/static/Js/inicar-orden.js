// ===== INICIAR ORDEN - JAVASCRIPT =====

// Variables globales
let currentCategory = 'antipastos';
let cart = JSON.parse(localStorage.getItem('cart')) || [];
let currentProduct = null;
let selectedAdicionales = [];

// Datos de ejemplo (en producción vendrían del backend)
const menuData = {
    antipastos: [
        {
            id: 1,
            name: "Bruschetta Italiana",
            description: "Pan tostado con tomate fresco, albahaca, ajo y aceite de oliva extra virgen",
            price: 15900,
            image: "../images/bruschetta.jpg",
            category: "antipastos"
        },
        {
            id: 2,
            name: "Carpaccio di Manzo",
            description: "Finas láminas de carne de res con rúcula, parmesano y aceite de oliva",
            price: 28900,
            image: "../images/carpaccio-di-manzo.jpg",
            category: "antipastos"
        },
        {
            id: 3,
            name: "Antipasto Misto",
            description: "Selección de embutidos italianos, quesos, aceitunas y vegetales marinados",
            price: 32900,
            image: "../images/aceitunas.jpg",
            category: "antipastos"
        }
    ],
    pizzas: [
        {
            id: 4,
            name: "Pizza Margherita",
            description: "Salsa de tomate, mozzarella fresca, albahaca y aceite de oliva",
            price: 24900,
            image: "../images/Margherita-Pizza-093.webp",
            category: "pizzas"
        },
        {
            id: 5,
            name: "Pizza Nera",
            description: "Masa negra con calamares, gamberi y salsa especial",
            price: 35900,
            image: "../images/pizzaNera.webp",
            category: "pizzas"
        },
        {
            id: 6,
            name: "Pizza Pepperoni",
            description: "Salsa de tomate, mozzarella y pepperoni italiano",
            price: 27900,
            image: "../images/Pizza Pepperoni.jpg",
            category: "pizzas"
        }
    ],
    pastas: [
        {
            id: 7,
            name: "Pasta Carbonara",
            description: "Espaguetis con panceta, huevo, parmesano y pimienta negra",
            price: 26900,
            image: "../images/pasta_carbonara.jpg",
            category: "pastas"
        },
        {
            id: 8,
            name: "Pasta Bolognesa",
            description: "Tagliatelle con salsa bolognesa tradicional de carne",
            price: 24900,
            image: "../images/PastaBolognesa.jpg",
            category: "pastas"
        },
        {
            id: 9,
            name: "Fettuccine Alfredo",
            description: "Fettuccine con salsa cremosa de mantequilla y parmesano",
            price: 23900,
            image: "../images/1504715566-delish-fettuccine-alfredo.jpg",
            category: "pastas"
        }
    ],
    risottos: [
        {
            id: 10,
            name: "Risotto al Tartufo",
            description: "Risotto cremoso con trufa negra y parmesano",
            price: 38900,
            image: "../images/risotoAlTartufo.avif",
            category: "risottos"
        },
        {
            id: 11,
            name: "Risotto ai Funghi",
            description: "Risotto con mezcla de hongos porcini y champignones",
            price: 29900,
            image: "../images/risotofungui.png",
            category: "risottos"
        }
    ],
    postres: [
        {
            id: 12,
            name: "Tiramisu",
            description: "Postre tradicional italiano con café, mascarpone y cacao",
            price: 16900,
            image: "../images/tiramisu.jpg",
            category: "postres"
        },
        {
            id: 13,
            name: "Cannoli Siciliani",
            description: "Tubitos crujientes rellenos de ricotta y pistachos",
            price: 14900,
            image: "../images/Cannoli Siciliani.jpg",
            category: "postres"
        }
    ],
    bebidas: [
        {
            id: 14,
            name: "Vino Tinto Reservado",
            description: "Vino tinto italiano de la casa",
            price: 45900,
            image: "../images/5435_Vino_Tinto_Reservado0.jpg",
            category: "bebidas"
        },
        {
            id: 15,
            name: "Agua con Gas",
            description: "Agua mineral con gas San Pellegrino",
            price: 6900,
            image: "../images/agua con gas.jpg",
            category: "bebidas"
        }
    ]
};

// Adicionales de ejemplo
const adicionalesData = [
    {
        id: 101,
        name: "Queso Parmesano Extra",
        price: 3500,
        image: "../images/provolone.webp",
        categories: ["pastas", "pizzas", "risottos"]
    },
    {
        id: 102,
        name: "Aceitunas Negras",
        price: 2500,
        image: "../images/aceitunas.jpg",
        categories: ["antipastos", "pizzas"]
    },
    {
        id: 103,
        name: "Pan de Ajo",
        price: 4500,
        image: "../images/focatia.jpeg",
        categories: ["antipastos", "pastas", "pizzas"]
    },
    {
        id: 104,
        name: "Hongos Trifolati",
        price: 5500,
        image: "../images/funghi-trifolati.jpg",
        categories: ["pastas", "pizzas", "risottos"]
    }
];

// Inicialización
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
    loadCategory(currentCategory);
    updateCartDisplay();
    setupEventListeners();
});

function initializeApp() {
    // Cargar carrito desde localStorage
    cart = JSON.parse(localStorage.getItem('cart')) || [];
    updateCartCount();
}

function setupEventListeners() {
    // Botones de categoría
    document.querySelectorAll('.btn-category').forEach(btn => {
        btn.addEventListener('click', function() {
            const category = this.dataset.category;
            switchCategory(category);
        });
    });

    // Botones de entrega
    document.querySelectorAll('.btn-delivery').forEach(btn => {
        btn.addEventListener('click', function() {
            document.querySelectorAll('.btn-delivery').forEach(b => b.classList.remove('active'));
            this.classList.add('active');
        });
    });

    // Modal de adicionales
    setupModalEventListeners();
}

function setupModalEventListeners() {
    const modal = document.getElementById('adicionalesModal');
    const decreaseBtn = document.getElementById('decreaseQty');
    const increaseBtn = document.getElementById('increaseQty');
    const addToCartBtn = document.getElementById('addToCartBtn');

    decreaseBtn.addEventListener('click', () => changeModalQuantity(-1));
    increaseBtn.addEventListener('click', () => changeModalQuantity(1));
    addToCartBtn.addEventListener('click', addToCartFromModal);

    // Reset modal cuando se cierra
    modal.addEventListener('hidden.bs.modal', function() {
        selectedAdicionales = [];
        document.getElementById('modalQuantity').textContent = '1';
    });
}

function switchCategory(category) {
    // Actualizar botones activos
    document.querySelectorAll('.btn-category').forEach(btn => {
        btn.classList.remove('active');
    });
    document.querySelector(`[data-category="${category}"]`).classList.add('active');

    // Cargar productos de la categoría
    currentCategory = category;
    loadCategory(category);
}

function loadCategory(category) {
    const categoryTitle = document.getElementById('categoryTitle');
    const productList = document.getElementById('productList');

    // Actualizar título
    const categoryNames = {
        antipastos: 'Antipastos',
        pizzas: 'Pizzas',
        pastas: 'Pastas',
        risottos: 'Risottos',
        postres: 'Postres',
        bebidas: 'Bebidas'
    };
    categoryTitle.innerHTML = `<h2>${categoryNames[category]}</h2>`;

    // Cargar productos
    const products = menuData[category] || [];
    productList.innerHTML = '';

    products.forEach(product => {
        const productCard = createProductCard(product);
        productList.appendChild(productCard);
    });
}

function createProductCard(product) {
    const card = document.createElement('div');
    card.className = 'product-card';
    card.innerHTML = `
        <img src="${product.image}" alt="${product.name}" class="product-image">
        <div class="product-info">
            <div>
                <h3 class="product-name">${product.name}</h3>
                <p class="product-description">${product.description}</p>
            </div>
            <div class="product-footer">
                <span class="product-price">$${formatPrice(product.price)}</span>
                <button class="btn-add-product" onclick="openAdicionalesModal(${product.id})">+</button>
            </div>
        </div>
    `;
    return card;
}

function openAdicionalesModal(productId) {
    const product = findProductById(productId);
    if (!product) return;

    currentProduct = product;
    selectedAdicionales = [];

    // Llenar información del producto
    document.getElementById('modalProductImage').src = product.image;
    document.getElementById('modalProductName').textContent = product.name;
    document.getElementById('modalProductDescription').textContent = product.description;
    document.getElementById('modalProductPrice').textContent = formatPrice(product.price);
    document.getElementById('modalTotalPrice').textContent = formatPrice(product.price);

    // Cargar adicionales disponibles para esta categoría
    loadAdicionales(product.category);

    // Mostrar modal
    const modal = new bootstrap.Modal(document.getElementById('adicionalesModal'));
    modal.show();
}

function loadAdicionales(category) {
    const adicionalesList = document.getElementById('adicionalesList');
    const availableAdicionales = adicionalesData.filter(adicional => 
        adicional.categories.includes(category)
    );

    adicionalesList.innerHTML = '';

    if (availableAdicionales.length === 0) {
        adicionalesList.innerHTML = '<p class="text-muted">No hay adicionales disponibles para este producto.</p>';
        return;
    }

    availableAdicionales.forEach(adicional => {
        const adicionalItem = document.createElement('div');
        adicionalItem.className = 'adicional-item';
        adicionalItem.innerHTML = `
            <div class="adicional-info">
                <img src="${adicional.image}" alt="${adicional.name}" class="adicional-image">
                <div class="adicional-details">
                    <h6>${adicional.name}</h6>
                </div>
            </div>
            <span class="adicional-price">+$${formatPrice(adicional.price)}</span>
            <input type="checkbox" class="adicional-checkbox" data-adicional-id="${adicional.id}" 
                   onchange="toggleAdicional(${adicional.id}, this.checked)">
        `;
        adicionalesList.appendChild(adicionalItem);
    });
}

function toggleAdicional(adicionalId, isSelected) {
    const adicional = adicionalesData.find(a => a.id === adicionalId);
    if (!adicional) return;

    if (isSelected) {
        selectedAdicionales.push(adicional);
    } else {
        selectedAdicionales = selectedAdicionales.filter(a => a.id !== adicionalId);
    }

    updateModalPrice();
}

function changeModalQuantity(change) {
    const quantityElement = document.getElementById('modalQuantity');
    let quantity = parseInt(quantityElement.textContent);
    quantity = Math.max(1, quantity + change);
    quantityElement.textContent = quantity;
    updateModalPrice();
}

function updateModalPrice() {
    const quantity = parseInt(document.getElementById('modalQuantity').textContent);
    const basePrice = currentProduct.price;
    const adicionalesPrice = selectedAdicionales.reduce((sum, adicional) => sum + adicional.price, 0);
    const totalPrice = (basePrice + adicionalesPrice) * quantity;

    document.getElementById('modalTotalPrice').textContent = formatPrice(totalPrice);
}

function addToCartFromModal() {
    const quantity = parseInt(document.getElementById('modalQuantity').textContent);
    
    const cartItem = {
        id: Date.now(), // ID único para el item del carrito
        productId: currentProduct.id,
        name: currentProduct.name,
        image: currentProduct.image,
        basePrice: currentProduct.price,
        adicionales: [...selectedAdicionales],
        quantity: quantity,
        totalPrice: (currentProduct.price + selectedAdicionales.reduce((sum, a) => sum + a.price, 0)) * quantity
    };

    cart.push(cartItem);
    saveCart();
    updateCartDisplay();
    updateCartCount();

    // Cerrar modal
    const modal = bootstrap.Modal.getInstance(document.getElementById('adicionalesModal'));
    modal.hide();

    // Feedback visual
    showAddedToCartFeedback();
}

function removeFromCart(cartItemId) {
    cart = cart.filter(item => item.id !== cartItemId);
    saveCart();
    updateCartDisplay();
    updateCartCount();
}

function updateCartItemQuantity(cartItemId, newQuantity) {
    if (newQuantity <= 0) {
        removeFromCart(cartItemId);
        return;
    }

    const item = cart.find(item => item.id === cartItemId);
    if (item) {
        const unitPrice = item.basePrice + item.adicionales.reduce((sum, a) => sum + a.price, 0);
        item.quantity = newQuantity;
        item.totalPrice = unitPrice * newQuantity;
        saveCart();
        updateCartDisplay();
        updateCartCount();
    }
}

function updateCartDisplay() {
    const cartContent = document.getElementById('cartContent');
    const cartFooter = document.getElementById('cartFooter');
    const cartCount = document.querySelector('.cart-count');

    if (cart.length === 0) {
        cartContent.innerHTML = `
            <div class="empty-cart">
                <img src="../images/shopping-bag.png" alt="Carrito vacío" width="64" height="64">
                <p>Tu carrito está vacío</p>
                <small>Agrega productos para comenzar tu pedido</small>
            </div>
        `;
        cartFooter.style.display = 'none';
        cartCount.textContent = '0 productos';
        return;
    }

    // Mostrar items del carrito
    cartContent.innerHTML = '';
    cart.forEach(item => {
        const cartItemElement = createCartItemElement(item);
        cartContent.appendChild(cartItemElement);
    });

    // Mostrar total
    const total = cart.reduce((sum, item) => sum + item.totalPrice, 0);
    document.getElementById('cartTotal').textContent = formatPrice(total);
    cartFooter.style.display = 'block';

    // Actualizar contador
    const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
    cartCount.textContent = `${totalItems} producto${totalItems !== 1 ? 's' : ''}`;
}

function createCartItemElement(item) {
    const element = document.createElement('div');
    element.className = 'cart-item';
    
    const adicionalesText = item.adicionales.length > 0 
        ? item.adicionales.map(a => a.name).join(', ')
        : '';

    element.innerHTML = `
        <img src="${item.image}" alt="${item.name}" class="cart-item-image">
        <div class="cart-item-info">
            <div class="cart-item-name">${item.name}</div>
            ${adicionalesText ? `<div class="cart-item-adicionales">${adicionalesText}</div>` : ''}
            <div class="cart-item-controls">
                <div class="quantity-controls">
                    <button class="quantity-btn" onclick="updateCartItemQuantity(${item.id}, ${item.quantity - 1})">-</button>
                    <span>${item.quantity}</span>
                    <button class="quantity-btn" onclick="updateCartItemQuantity(${item.id}, ${item.quantity + 1})">+</button>
                </div>
                <div class="cart-item-price">$${formatPrice(item.totalPrice)}</div>
            </div>
        </div>
    `;
    return element;
}

function updateCartCount() {
    const cartBadge = document.getElementById('cartCount');
    const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
    
    if (totalItems > 0) {
        cartBadge.textContent = totalItems;
        cartBadge.style.display = 'inline-block';
    } else {
        cartBadge.style.display = 'none';
    }
}

function saveCart() {
    localStorage.setItem('cart', JSON.stringify(cart));
}

function findProductById(id) {
    for (const category in menuData) {
        const product = menuData[category].find(p => p.id === id);
        if (product) return product;
    }
    return null;
}

function formatPrice(price) {
    return price.toLocaleString('es-CO');
}

function showAddedToCartFeedback() {
    // Crear elemento de feedback
    const feedback = document.createElement('div');
    feedback.className = 'alert alert-success position-fixed';
    feedback.style.cssText = `
        top: 20px;
        right: 20px;
        z-index: 9999;
        animation: slideInRight 0.3s ease;
    `;
    feedback.innerHTML = `
        <i class="fas fa-check-circle"></i>
        Producto agregado al carrito
    `;
    
    document.body.appendChild(feedback);
    
    // Remover después de 3 segundos
    setTimeout(() => {
        feedback.remove();
    }, 3000);
}

// Función para cargar datos desde el backend (para implementar después)
async function loadMenuFromBackend() {
    try {
        const response = await fetch('/api/menu');
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error loading menu:', error);
        return menuData; // Fallback a datos locales
    }
}

async function loadAdicionalesFromBackend(categoryId) {
    try {
        const response = await fetch(`/api/adicionales/categoria/${categoryId}`);
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error loading adicionales:', error);
        return adicionalesData; // Fallback a datos locales
    }
}

