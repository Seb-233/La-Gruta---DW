// adicionales.js
document.addEventListener('click', (e) => {
  const btn = e.target.closest('.btn.btn-primary');
  if (!btn) return;

  const item = {
    id: btn.getAttribute('data-id'),
    nombre: btn.getAttribute('data-nombre'),
    precio: parseFloat(btn.getAttribute('data-precio') || '0')
  };

  try {
    const key = 'carrito_adicionales';
    const curr = JSON.parse(localStorage.getItem(key) || '[]');
    curr.push(item);
    localStorage.setItem(key, JSON.stringify(curr));
    btn.textContent = 'Agregado ✓';
    setTimeout(() => (btn.textContent = 'Agregar'), 1200);
  } catch (err) {
    console.error('No se pudo agregar al carrito:', err);
  }
});
