package com.example.demo.model;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.repository.AdicionalRepository;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.ComidaRepository;
import com.example.demo.repository.DomiciliarioRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DatabaseInit implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private ComidaRepository comidaRepository;
    
    @Autowired
    private AdicionalRepository adicionalRepository;

    @Autowired
    private DomiciliarioRepository domiciliarioRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Crear usuarios de ejemplo
        if (userRepository.count() == 0) {
            User admin = new User("admin", "admin123");
            admin.setRole("ADMIN");
            userRepository.save(admin);

            User admin1 = new User("Juan","12345");
            admin1.setRole("ADMIN");
            userRepository.save(admin1);

            
            User user = new User("usuario", "user123");
            user.setRole("USER");
            userRepository.save(user);
        }
        // Crear domiciliarios de ejemplo
        if (domiciliarioRepository.count() == 0) {
            Domiciliario dom1 = new Domiciliario(null, "Carlos Pérez", "carlos@domicilio.com", "3001234567", "Moto", "ABC123", true, "Bogotá");
            Domiciliario dom2 = new Domiciliario(null, "Laura Gómez", "laura@domicilio.com", "3109876543", "Bicicleta", null, true, "Chapinero");
            Domiciliario dom3 = new Domiciliario(null, "Juan Rodríguez", "juan@domicilio.com", "3205558888", "Carro", "XYZ789", false, "Usaquén");

            domiciliarioRepository.saveAll(Arrays.asList(dom1, dom2, dom3));
        }
        
        // Crear categorías
        if (categoriaRepository.count() == 0) {
            Categoria antipastos = new Categoria();
            antipastos.setNombre("Antipastos");
            antipastos.setSlug("antipastos");
            antipastos.setDescripcion("Entradas tradicionales italianas");
            antipastos.setImagen("/images/bruschetta.jpg");
            antipastos.setOrden(1);
            categoriaRepository.save(antipastos);
            
            Categoria pizzas = new Categoria();
            pizzas.setNombre("Pizzas");
            pizzas.setSlug("pizzas");
            pizzas.setDescripcion("Pizzas artesanales al horno de leña");
            pizzas.setImagen("/images/pizzaNera.webp");
            pizzas.setOrden(2);
            categoriaRepository.save(pizzas);
            
            Categoria pastas = new Categoria();
            pastas.setNombre("Pastas");
            pastas.setSlug("pastas");
            pastas.setDescripcion("Pastas frescas hechas en casa");
            pastas.setImagen("/images/pasta_carbonara.jpg");
            pastas.setOrden(3);
            categoriaRepository.save(pastas);
            
            Categoria risottos = new Categoria();
            risottos.setNombre("Risottos");
            risottos.setSlug("risottos");
            risottos.setDescripcion("Risottos cremosos con ingredientes selectos");
            risottos.setImagen("/images/risotoAlTartufo.avif");
            risottos.setOrden(4);
            categoriaRepository.save(risottos);
            
            Categoria postres = new Categoria();
            postres.setNombre("Postres");
            postres.setSlug("postres");
            postres.setDescripcion("Dulces tradicionales italianos");
            postres.setImagen("/images/tiramisu.jpg");
            postres.setOrden(5);
            categoriaRepository.save(postres);
            
            Categoria bebidas = new Categoria();
            bebidas.setNombre("Bebidas");
            bebidas.setSlug("bebidas");
            bebidas.setDescripcion("Vinos, cervezas y bebidas sin alcohol");
            bebidas.setImagen("/images/Bebidas.png");
            bebidas.setOrden(6);
            categoriaRepository.save(bebidas);
            
            // Crear comidas de ejemplo
            crearComidasEjemplo(antipastos, pizzas, pastas, risottos, postres, bebidas);
            
            // Crear adicionales de ejemplo
            crearAdicionalesEjemplo(antipastos, pizzas, pastas, risottos);
        }
    }
    
    private void crearComidasEjemplo(Categoria antipastos, Categoria pizzas, Categoria pastas, 
                                   Categoria risottos, Categoria postres, Categoria bebidas) {
        
       // ANTIPASTOS
        Comida bruschetta = new Comida();
        bruschetta.setNombre("Bruschetta Italiana");
        bruschetta.setDescripcion("Pan tostado con tomate fresco, albahaca, ajo y aceite de oliva extra virgen");
        bruschetta.setPrecio(15900.0);
        // 👇 URL completa al backend
        bruschetta.setImagen("http://localhost:8080/images/bruschetta.jpg");
        bruschetta.setCategoria(antipastos);
        bruschetta.setTiempoPreparacion(10);
        bruschetta.setIngredientes("Pan, tomate, albahaca, ajo, aceite de oliva");
        comidaRepository.save(bruschetta);

        Comida carpaccio = new Comida();
        carpaccio.setNombre("Carpaccio di Manzo");
        carpaccio.setDescripcion("Finas láminas de carne de res con rúcula, parmesano y aceite de oliva");
        carpaccio.setPrecio(28900.0);
        // 👇 URL completa al backend
        carpaccio.setImagen("http://localhost:8080/images/carpaccio-di-manzo.jpg");
        carpaccio.setCategoria(antipastos);
        carpaccio.setTiempoPreparacion(15);
        carpaccio.setEsEspecialidad(true);
        comidaRepository.save(carpaccio);

        
        // PIZZAS
        Comida margherita = new Comida();
        margherita.setNombre("Pizza Margherita");
        margherita.setDescripcion("Salsa de tomate, mozzarella fresca, albahaca y aceite de oliva");
        margherita.setPrecio(24900.0);
        // ✅ URL absoluta
        margherita.setImagen("http://localhost:8080/images/Margherita-Pizza-093.webp");
        margherita.setCategoria(pizzas);
        margherita.setTiempoPreparacion(20);
        comidaRepository.save(margherita);

        // 🍕 Pizza Nera
        Comida pizzaNera = new Comida();
        pizzaNera.setNombre("Pizza Nera");
        pizzaNera.setDescripcion("Masa negra con calamares, gamberi y salsa especial");
        pizzaNera.setPrecio(35900.0);
        // ✅ URL absoluta
        pizzaNera.setImagen("http://localhost:8080/images/pizzaNera.webp");
        pizzaNera.setCategoria(pizzas);
        pizzaNera.setTiempoPreparacion(25);
        pizzaNera.setEsEspecialidad(true);
        comidaRepository.save(pizzaNera);

        Comida pepperoni = new Comida();
        pepperoni.setNombre("Pizza Pepperoni");
        pepperoni.setDescripcion("Salsa de tomate, mozzarella y pepperoni italiano");
        pepperoni.setPrecio(27900.0);
        // ⚡ Ojo: asegúrate de que en /static/images/ exista exactamente este archivo (sin espacios)
        pepperoni.setImagen("http://localhost:8080/images/PizzaPepperoni.jpg");
        pepperoni.setCategoria(pizzas);
        pepperoni.setTiempoPreparacion(20);
        comidaRepository.save(pepperoni);

        
        // PASTAS
        Comida carbonara = new Comida();
        carbonara.setNombre("Pasta Carbonara");
        carbonara.setDescripcion("Espaguetis con panceta, huevo, parmesano y pimienta negra");
        carbonara.setPrecio(26900.0);
        // ✅ URL absoluta
        carbonara.setImagen("http://localhost:8080/images/pasta_carbonara.jpg");
        carbonara.setCategoria(pastas);
        carbonara.setTiempoPreparacion(18);
        comidaRepository.save(carbonara);

        Comida bolognesa = new Comida();
        bolognesa.setNombre("Pasta Bolognesa");
        bolognesa.setDescripcion("Tagliatelle con salsa bolognesa tradicional de carne");
        bolognesa.setPrecio(24900.0);
        // ✅ URL absoluta
        bolognesa.setImagen("http://localhost:8080/images/PastaBolognesa.jpg");
        bolognesa.setCategoria(pastas);
        bolognesa.setTiempoPreparacion(22);
        comidaRepository.save(bolognesa);

        Comida alfredo = new Comida();
        alfredo.setNombre("Fettuccine Alfredo");
        alfredo.setDescripcion("Fettuccine con salsa cremosa de mantequilla y parmesano");
        alfredo.setPrecio(23900.0);
        // ✅ URL absoluta
        alfredo.setImagen("http://localhost:8080/images/1504715566-delish-fettuccine-alfredo.jpg");
        alfredo.setCategoria(pastas);
        alfredo.setTiempoPreparacion(16);
        comidaRepository.save(alfredo);

        
        // RISOTTOS
        Comida risottoTartufo = new Comida();
        risottoTartufo.setNombre("Risotto al Tartufo");
        risottoTartufo.setDescripcion("Risotto cremoso con trufa negra y parmesano");
        risottoTartufo.setPrecio(38900.0);
        risottoTartufo.setImagen("http://localhost:8080/images/risotoAlTartufo.avif"); // ✅
        risottoTartufo.setCategoria(risottos);
        risottoTartufo.setTiempoPreparacion(25);
        risottoTartufo.setEsEspecialidad(true);
        comidaRepository.save(risottoTartufo);

        Comida risottoFunghi = new Comida();
        risottoFunghi.setNombre("Risotto ai Funghi");
        risottoFunghi.setDescripcion("Risotto con mezcla de hongos porcini y champiñones");
        risottoFunghi.setPrecio(29900.0);
        risottoFunghi.setImagen("http://localhost:8080/images/risotofungui.png"); // ✅
        risottoFunghi.setCategoria(risottos);
        risottoFunghi.setTiempoPreparacion(22);
        comidaRepository.save(risottoFunghi);

        // POSTRES
        Comida tiramisu = new Comida();
        tiramisu.setNombre("Tiramisu");
        tiramisu.setDescripcion("Postre tradicional italiano con café, mascarpone y cacao");
        tiramisu.setPrecio(16900.0);
        tiramisu.setImagen("http://localhost:8080/images/tiramisu.jpg"); // ✅
        tiramisu.setCategoria(postres);
        tiramisu.setTiempoPreparacion(5);
        comidaRepository.save(tiramisu);

        Comida cannoli = new Comida();
        cannoli.setNombre("Cannoli Siciliani");
        cannoli.setDescripcion("Tubitos crujientes rellenos de ricotta y pistachos");
        cannoli.setPrecio(14900.0);
        cannoli.setImagen("http://localhost:8080/images/CannoliSiciliani.jpg"); // ✅ ojo con espacio
        cannoli.setCategoria(postres);
        cannoli.setTiempoPreparacion(8);
        comidaRepository.save(cannoli);

        
       // BEBIDAS
        Comida vinoTinto = new Comida();
        vinoTinto.setNombre("Vino Tinto Reservado");
        vinoTinto.setDescripcion("Vino tinto italiano de la casa");
        vinoTinto.setPrecio(45900.0);
        vinoTinto.setImagen("http://localhost:8080/images/5435_Vino_Tinto_Reservado0.jpg"); // ✅ ruta absoluta
        vinoTinto.setCategoria(bebidas);
        vinoTinto.setTiempoPreparacion(2);
        comidaRepository.save(vinoTinto);

        Comida aguaGas = new Comida();
        aguaGas.setNombre("Agua con Gas");
        aguaGas.setDescripcion("Agua mineral con gas San Pellegrino");
        aguaGas.setPrecio(6900.0);
        aguaGas.setImagen("http://localhost:8080/images/agua_con_gas.jpg"); // ✅ ruta absoluta y sin espacios
        aguaGas.setCategoria(bebidas);
        aguaGas.setTiempoPreparacion(1);
        comidaRepository.save(aguaGas);

    }
    
    private void crearAdicionalesEjemplo(Categoria antipastos, Categoria pizzas, 
    Categoria pastas, Categoria risottos) {
// 1. Queso Parmesano Extra
Adicional parmesano = new Adicional();
parmesano.setNombre("Queso Parmesano Extra");
parmesano.setDescripcion("Parmesano reggiano rallado fresco");
parmesano.setPrecio(3500.0);
parmesano.setImagen("/images/provolone.webp");
parmesano.setTipo("queso");
parmesano.getCategorias().add(pastas); // 👈 relación
adicionalRepository.save(parmesano);

// 2. Aceitunas Negras
Adicional aceitunas = new Adicional();
aceitunas.setNombre("Aceitunas Negras");
aceitunas.setDescripcion("Aceitunas kalamata marinadas");
aceitunas.setPrecio(2500.0);
aceitunas.setImagen("/images/aceitunas.jpg");
aceitunas.setTipo("acompañamiento");
aceitunas.getCategorias().add(pastas);
adicionalRepository.save(aceitunas);

// 3. Pan de Ajo
Adicional panAjo = new Adicional();
panAjo.setNombre("Pan de Ajo");
panAjo.setDescripcion("Pan focaccia con ajo y hierbas");
panAjo.setPrecio(4500.0);
panAjo.setImagen("/images/focaccia.jpeg");
panAjo.setTipo("acompañamiento");
panAjo.getCategorias().add(pastas);
adicionalRepository.save(panAjo);

// 4. Hongos Trifolati
Adicional hongos = new Adicional();
hongos.setNombre("Hongos Trifolati");
hongos.setDescripcion("Hongos salteados con ajo y perejil");
hongos.setPrecio(5500.0);
hongos.setImagen("/images/funghi-trifolati.jpg");
hongos.setTipo("acompañamiento");
hongos.getCategorias().add(risottos);
adicionalRepository.save(hongos);

// 5. Queso Mozzarella Extra
Adicional quesoExtra = new Adicional();
quesoExtra.setNombre("Queso Mozzarella Extra");
quesoExtra.setDescripcion("Mozzarella di bufala adicional");
quesoExtra.setPrecio(4000.0);
quesoExtra.setImagen("/images/provolone.webp");
quesoExtra.setTipo("queso");
quesoExtra.getCategorias().add(pizzas);
adicionalRepository.save(quesoExtra);

// 6. Rúcula Fresca
Adicional rucula = new Adicional();
rucula.setNombre("Rúcula Fresca");
rucula.setDescripcion("Hojas de rúcula fresca");
rucula.setPrecio(2000.0);
rucula.setImagen("/images/aceitunas.jpg");
rucula.setTipo("vegetal");
rucula.getCategorias().add(antipastos);
adicionalRepository.save(rucula);

// 7. Tomate Cherry
Adicional tomateCherry = new Adicional();
tomateCherry.setNombre("Tomate Cherry");
tomateCherry.setDescripcion("Tomates cherry frescos");
tomateCherry.setPrecio(2000.0);
tomateCherry.setImagen("/images/tomate_cherry.jpg");
tomateCherry.setTipo("vegetal");
tomateCherry.getCategorias().add(pizzas);
adicionalRepository.save(tomateCherry);

// 8. Jamón Serrano
Adicional jamon = new Adicional();
jamon.setNombre("Jamón Serrano");
jamon.setDescripcion("Lonjas finas de jamón serrano");
jamon.setPrecio(6000.0);
jamon.setImagen("/images/jamon.jpg");
jamon.setTipo("proteina");
jamon.getCategorias().add(pizzas);
adicionalRepository.save(jamon);

// 9. Albahaca Fresca
Adicional albahaca = new Adicional();
albahaca.setNombre("Albahaca Fresca");
albahaca.setDescripcion("Hojas de albahaca fresca");
albahaca.setPrecio(1500.0);
albahaca.setImagen("/images/albahaca.jpg");
albahaca.setTipo("vegetal");
albahaca.getCategorias().add(pizzas);
adicionalRepository.save(albahaca);

// 10. Salsa Pesto
Adicional pesto = new Adicional();
pesto.setNombre("Salsa Pesto");
pesto.setDescripcion("Salsa de albahaca y piñones");
pesto.setPrecio(3000.0);
pesto.setImagen("/images/pesto.jpg");
pesto.setTipo("salsa");
pesto.getCategorias().add(pastas);
adicionalRepository.save(pesto);

// 11. Salsa Alfredo
Adicional alfredo = new Adicional();
alfredo.setNombre("Salsa Alfredo");
alfredo.setDescripcion("Crema con queso parmesano");
alfredo.setPrecio(3500.0);
alfredo.setImagen("/images/alfredo.jpg");
alfredo.setTipo("salsa");
alfredo.getCategorias().add(pastas);
adicionalRepository.save(alfredo);

// 12. Chorizo
Adicional chorizo = new Adicional();
chorizo.setNombre("Chorizo Picante");
chorizo.setDescripcion("Chorizo curado en rodajas");
chorizo.setPrecio(5000.0);
chorizo.setImagen("/images/chorizo.jpg");
chorizo.setTipo("proteina");
chorizo.getCategorias().add(pizzas);
adicionalRepository.save(chorizo);

// 13. Pollo
Adicional pollo = new Adicional();
pollo.setNombre("Pollo Grillado");
pollo.setDescripcion("Tiras de pollo a la plancha");
pollo.setPrecio(5500.0);
pollo.setImagen("/images/pollo.jpg");
pollo.setTipo("proteina");
pollo.getCategorias().add(pastas);
adicionalRepository.save(pollo);

// 14. Espinaca
Adicional espinaca = new Adicional();
espinaca.setNombre("Espinaca Fresca");
espinaca.setDescripcion("Hojas frescas de espinaca");
espinaca.setPrecio(1800.0);
espinaca.setImagen("/images/espinaca.jpg");
espinaca.setTipo("vegetal");
espinaca.getCategorias().add(risottos);
adicionalRepository.save(espinaca);

// 15. Pimientos Asados
Adicional pimientos = new Adicional();
pimientos.setNombre("Pimientos Asados");
pimientos.setDescripcion("Tiras de pimientos asados");
pimientos.setPrecio(2200.0);
pimientos.setImagen("/images/pimientos.jpg");
pimientos.setTipo("vegetal");
pimientos.getCategorias().add(pizzas);
adicionalRepository.save(pimientos);

// 16. Tocino
Adicional tocino = new Adicional();
tocino.setNombre("Tocino Crujiente");
tocino.setDescripcion("Tiras de tocino dorado");
tocino.setPrecio(5000.0);
tocino.setImagen("/images/tocino.jpg");
tocino.setTipo("proteina");
tocino.getCategorias().add(pizzas);
adicionalRepository.save(tocino);

// 17. Maíz Dulce
Adicional maiz = new Adicional();
maiz.setNombre("Maíz Dulce");
maiz.setDescripcion("Granos de maíz dulce");
maiz.setPrecio(1500.0);
maiz.setImagen("/images/maiz.jpg");
maiz.setTipo("vegetal");
maiz.getCategorias().add(pizzas);
adicionalRepository.save(maiz);

// 18. Champiñones
Adicional champinones = new Adicional();
champinones.setNombre("Champiñones Frescos");
champinones.setDescripcion("Champiñones laminados frescos");
champinones.setPrecio(3000.0);
champinones.setImagen("/images/champinones.jpg");
champinones.setTipo("acompañamiento");
champinones.getCategorias().add(pastas);
adicionalRepository.save(champinones);

// 19. Tomate Seco
Adicional tomateSeco = new Adicional();
tomateSeco.setNombre("Tomate Seco");
tomateSeco.setDescripcion("Tomates secos al sol");
tomateSeco.setPrecio(2800.0);
tomateSeco.setImagen("/images/tomate_seco.jpg");
tomateSeco.setTipo("acompañamiento");
tomateSeco.getCategorias().add(risottos);
adicionalRepository.save(tomateSeco);

// 20. Aceite de Trufa
Adicional trufa = new Adicional();
trufa.setNombre("Aceite de Trufa");
trufa.setDescripcion("Aceite aromático de trufa");
trufa.setPrecio(8000.0);
trufa.setImagen("/images/trufa.jpg");
trufa.setTipo("condimento");
trufa.getCategorias().add(pastas);
adicionalRepository.save(trufa);



}

}
