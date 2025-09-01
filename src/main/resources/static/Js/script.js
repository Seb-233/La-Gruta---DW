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


// ----- Carrito (localStorage) -----
const CART_KEY = 'carrito';

function getCart() {
  try { return JSON.parse(localStorage.getItem(CART_KEY) || '[]'); }
  catch { return []; }
}
function saveCart(items) {
  localStorage.setItem(CART_KEY, JSON.stringify(items));
  updateCartBadge();
}
function addItem({ id, nombre, precio, tipo }) {
  const items = getCart();
  const idx = items.findIndex(it => it.id === String(id) && it.tipo === tipo);
  if (idx >= 0) {
    items[idx].cantidad += 1;
  } else {
    items.push({ id: String(id), nombre, precio: Number((precio+'').replace(/[^0-9.]/g,'')), tipo, cantidad: 1 });
  }
  saveCart(items);
}
function countItems() {
  return getCart().reduce((acc, it) => acc + (it.cantidad || 1), 0);
}
function updateCartBadge() {
  const badge = document.getElementById('cartCount');
  if (!badge) return;
  const n = countItems();
  if (n > 0) { badge.style.display = 'inline-block'; badge.textContent = String(n); }
  else { badge.style.display = 'none'; }
}

// Delegación: botones "Agregar" en modal/menu
document.addEventListener('click', (e) => {
  const btn = e.target.closest('[data-add]');
  if (!btn) return;
  const tipo = btn.getAttribute('data-add'); // 'comida' | 'adicional'
  const id = btn.getAttribute('data-id');
  const nombre = btn.getAttribute('data-nombre');
  const precio = btn.getAttribute('data-precio') || '0';

  addItem({ id, nombre, precio, tipo });

  // feedback rápido
  const old = btn.textContent;
  btn.textContent = 'Agregado ✓';
  btn.disabled = true;
  setTimeout(() => { btn.textContent = old; btn.disabled = false; }, 900);
});

// Init
document.addEventListener('DOMContentLoaded', updateCartBadge);


window.addEventListener("scroll", revealOnScroll);

