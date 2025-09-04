// ===== HEADER SCROLL EFFECT =====
window.addEventListener("scroll", function () {
  const header = document.querySelector(".header-transparent");
  if (window.scrollY > 50) {
    header.classList.add("scrolled");
  } else {
    header.classList.remove("scrolled");
  }
});

// ===== SMOOTH SCROLL (opcional para anclas internas) =====
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
  anchor.addEventListener("click", function (e) {
    e.preventDefault();
    const target = document.querySelector(this.getAttribute("href"));
    if (target) {
      window.scrollTo({
        top: target.offsetTop - 70, // deja espacio para el header fijo
        behavior: "smooth"
      });
    }
  });
});

// ===== SCROLL REVEAL =====
function revealOnScroll() {
  const reveals = document.querySelectorAll(".reveal");
  for (let i = 0; i < reveals.length; i++) {
    const windowHeight = window.innerHeight;
    const elementTop = reveals[i].getBoundingClientRect().top;
    const elementVisible = 150; // distancia antes de activar
    if (elementTop < windowHeight - elementVisible) {
      reveals[i].classList.add("active");
    }
  }
}


// Obtener carrito desde localStorage o inicializar vacío
let cart = JSON.parse(localStorage.getItem('cart')) || [];

// Guardar carrito en localStorage
function saveCart() {
  localStorage.setItem('cart', JSON.stringify(cart));
}

// Actualizar contador y contenido del carrito
function updateCartSidebar() {
  const cartCount = document.querySelector('.cart-count');
  const cartContent = document.getElementById('cartContent');
  const cartFooter = document.getElementById('cartFooter');
  const cartTotal = document.getElementById('cartTotal');

  if (!cartCount || !cartContent || !cartFooter || !cartTotal) return;

  cartCount.textContent = `${cart.length} ${cart.length === 1 ? 'producto' : 'productos'}`;

  if (cart.length === 0) {
    cartContent.innerHTML = `<div class="empty-cart">
        <img src="../images/shopping-bag.png" alt="Carrito vacío" width="64" height="64">
        <p>Tu carrito está vacío</p>
        <small>Agrega productos para comenzar tu pedido</small>
      </div>`;
    cartFooter.style.display = 'none';
  } else {
    let html = '';
    let total = 0;
    cart.forEach(item => {
      total += item.precio * item.cantidad;
      html += `<div class="cart-item d-flex justify-content-between align-items-center mb-2">
        <span>${item.nombre} x${item.cantidad}</span>
        <span>$${(item.precio * item.cantidad).toFixed(2)}</span>
      </div>`;
    });
    cartContent.innerHTML = html;
    cartTotal.textContent = total.toFixed(2);
    cartFooter.style.display = 'block';
  }
}

// Inicializar botones y cantidades
function initProductCards() {
  document.querySelectorAll('.card').forEach(card => {
    const minusBtn = card.querySelector('.minus');
    const plusBtn = card.querySelector('.plus');
    const quantitySpan = card.querySelector('.quantity');
    const addBtn = card.querySelector('.add-to-order');

    // Botón -
    minusBtn?.addEventListener('click', () => {
      let qty = parseInt(quantitySpan.textContent) || 1;
      if (qty > 1) {
        qty--;
        quantitySpan.textContent = qty;
      }
    });

    // Botón +
    plusBtn?.addEventListener('click', () => {
      let qty = parseInt(quantitySpan.textContent) || 1;
      qty++;
      quantitySpan.textContent = qty;
    });

    // Botón agregar
    addBtn?.addEventListener('click', () => {
      const id = card.dataset.id;
      const nombre = card.dataset.nombre;
      const precio = parseFloat(card.dataset.precio.replace(',', '.')) || 0; // por si hay coma
      const cantidad = parseInt(quantitySpan.textContent) || 1;

      if (!id || !nombre || precio <= 0) return;

      const existing = cart.find(item => item.id === id);
      if (existing) {
        existing.cantidad += cantidad;
      } else {
        cart.push({ id, nombre, precio, cantidad });
      }

      saveCart();
      updateCartSidebar();
      showOrderModal();

      quantitySpan.textContent = '1'; // reset
    });
  });
}


// Modal único
function showOrderModal() {
  const orderModalEl = document.getElementById('orderModal');
  if (!orderModalEl) return;

  const orderModal = new bootstrap.Modal(orderModalEl);
  orderModal.show();
}

// Inicializar todo
document.addEventListener('DOMContentLoaded', () => {
  initProductCards();
  updateCartSidebar();

  // Botón "ir a pagar" del modal
  document.getElementById('goToPayBtn')?.addEventListener('click', () => {
    window.location.href = '/la_gruta/iniciar-orden';
  });

  // Botón "continuar agregando" del modal
  document.getElementById('continueOrderBtn')?.addEventListener('click', () => {
    const modalEl = document.getElementById('orderModal');
    const modal = bootstrap.Modal.getInstance(modalEl);
    modal?.hide();
  });
});


// --- SECCIÓN CHECKOUT EN INICIAR-ORDEN.HTML ---

// Renderiza los productos en la sección de checkout con imagen y todo
function renderCheckoutProductsWithImages() {
  const container = document.getElementById('checkoutProducts');
  if (!container) return;

  if (cart.length === 0) {
    container.innerHTML = '<p>Tu carrito está vacío.</p>';
    return;
  }

  let html = '<div class="row g-3">';
  let total = 0;
  cart.forEach(item => {
    total += item.precio * item.cantidad;
    html += `
      <div class="col-12">
        <div class="card d-flex flex-row align-items-center p-2">
          <img src="${item.imagen || '../images/shopping-bag.png'}" class="card-img-top" alt="${item.nombre}" style="width:80px; height:80px; object-fit:cover; margin-right:10px;">
          <div class="card-body p-2 d-flex flex-column">
            <h6 class="card-title mb-1">${item.nombre}</h6>
            <span>Cantidad: ${item.cantidad}</span>
            <span>Precio: $${(item.precio * item.cantidad).toFixed(2)}</span>
          </div>
        </div>
      </div>
    `;
  });
  html += `</div>
           <div class="mt-2"><strong>Total: $${total.toFixed(2)}</strong></div>`;

  container.innerHTML = html;
}

// Mostrar sección checkout en iniciar-orden.html
function showCheckoutSection() {
  const checkoutSection = document.getElementById('checkoutSection');
  if (!checkoutSection) return;

  checkoutSection.style.display = 'block';
  renderCheckoutProductsWithImages();

  // Scroll suave hasta la sección
  checkoutSection.scrollIntoView({ behavior: 'smooth' });
}

// Botón "Continuar pedido" en sidebar
document.getElementById('goToPayBtnSidebar')?.addEventListener('click', () => {
  showCheckoutSection();
});

// Botón "Ir a pagar" del modal
document.getElementById('goToPayBtn')?.addEventListener('click', () => {
  const modalEl = document.getElementById('orderModal');
  const modal = bootstrap.Modal.getInstance(modalEl);
  modal?.hide();

  showCheckoutSection();
});

// Manejo del formulario de checkout
document.getElementById('checkoutForm')?.addEventListener('submit', function(e) {
  e.preventDefault();

  const nombre = document.getElementById('nombreCliente').value.trim();
  const direccion = document.getElementById('direccion').value.trim();
  const telefono = document.getElementById('telefono').value.trim();

  if (!nombre || !telefono) {
    alert('Por favor completa los campos obligatorios.');
    return;
  }

  alert(`Pedido confirmado!\nNombre: ${nombre}\nTeléfono: ${telefono}\nDirección: ${direccion || 'N/A'}`);

  // Limpiar carrito y formulario
  cart = [];
  localStorage.setItem('cart', JSON.stringify(cart));
  renderCheckoutProductsWithImages();
  this.reset();
});


