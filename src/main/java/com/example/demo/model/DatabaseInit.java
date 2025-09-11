package com.example.demo.model;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.repository.AdicionalRepository;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.ComidaRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DatabaseInit implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoriaRepository categoriaRepository;
    private final ComidaRepository comidaRepository;
    private final AdicionalRepository adicionalRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Crear usuarios de ejemplo
        if (userRepository.count() == 0) {
            User admin = new User("admin", "admin123");
            admin.setRole("ADMIN");
            userRepository.save(admin);
            
            User user = new User("usuario", "user123");
            user.setRole("USER");
            userRepository.save(user);
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
        bruschetta.setImagen("/images/bruschetta.jpg");
        bruschetta.setCategoria(antipastos);
        bruschetta.setTiempoPreparacion(10);
        bruschetta.setIngredientes("Pan, tomate, albahaca, ajo, aceite de oliva");
        comidaRepository.save(bruschetta);
        
        Comida carpaccio = new Comida();
        carpaccio.setNombre("Carpaccio di Manzo");
        carpaccio.setDescripcion("Finas láminas de carne de res con rúcula, parmesano y aceite de oliva");
        carpaccio.setPrecio(28900.0);
        carpaccio.setImagen("/images/carpaccio-di-manzo.jpg");
        carpaccio.setCategoria(antipastos);
        carpaccio.setTiempoPreparacion(15);
        carpaccio.setEsEspecialidad(true);
        comidaRepository.save(carpaccio);
        
        // PIZZAS
        Comida margherita = new Comida();
        margherita.setNombre("Pizza Margherita");
        margherita.setDescripcion("Salsa de tomate, mozzarella fresca, albahaca y aceite de oliva");
        margherita.setPrecio(24900.0);
        margherita.setImagen("/images/Margherita-Pizza-093.webp");
        margherita.setCategoria(pizzas);
        margherita.setTiempoPreparacion(20);
        comidaRepository.save(margherita);
        
        Comida pizzaNera = new Comida();
        pizzaNera.setNombre("Pizza Nera");
        pizzaNera.setDescripcion("Masa negra con calamares, gamberi y salsa especial");
        pizzaNera.setPrecio(35900.0);
        pizzaNera.setImagen("/images/pizzaNera.webp");
        pizzaNera.setCategoria(pizzas);
        pizzaNera.setTiempoPreparacion(25);
        pizzaNera.setEsEspecialidad(true);
        comidaRepository.save(pizzaNera);
        
        Comida pepperoni = new Comida();
        pepperoni.setNombre("Pizza Pepperoni");
        pepperoni.setDescripcion("Salsa de tomate, mozzarella y pepperoni italiano");
        pepperoni.setPrecio(27900.0);
        pepperoni.setImagen("/images/Pizza Pepperoni.jpg");
        pepperoni.setCategoria(pizzas);
        pepperoni.setTiempoPreparacion(20);
        comidaRepository.save(pepperoni);
        
        // PASTAS
        Comida carbonara = new Comida();
        carbonara.setNombre("Pasta Carbonara");
        carbonara.setDescripcion("Espaguetis con panceta, huevo, parmesano y pimienta negra");
        carbonara.setPrecio(26900.0);
        carbonara.setImagen("/images/pasta_carbonara.jpg");
        carbonara.setCategoria(pastas);
        carbonara.setTiempoPreparacion(18);
        comidaRepository.save(carbonara);
        
        Comida bolognesa = new Comida();
        bolognesa.setNombre("Pasta Bolognesa");
        bolognesa.setDescripcion("Tagliatelle con salsa bolognesa tradicional de carne");
        bolognesa.setPrecio(24900.0);
        bolognesa.setImagen("/images/PastaBolognesa.jpg");
        bolognesa.setCategoria(pastas);
        bolognesa.setTiempoPreparacion(22);
        comidaRepository.save(bolognesa);
        
        Comida alfredo = new Comida();
        alfredo.setNombre("Fettuccine Alfredo");
        alfredo.setDescripcion("Fettuccine con salsa cremosa de mantequilla y parmesano");
        alfredo.setPrecio(23900.0);
        alfredo.setImagen("/images/1504715566-delish-fettuccine-alfredo.jpg");
        alfredo.setCategoria(pastas);
        alfredo.setTiempoPreparacion(16);
        comidaRepository.save(alfredo);
        
        // RISOTTOS
        Comida risottoTartufo = new Comida();
        risottoTartufo.setNombre("Risotto al Tartufo");
        risottoTartufo.setDescripcion("Risotto cremoso con trufa negra y parmesano");
        risottoTartufo.setPrecio(38900.0);
        risottoTartufo.setImagen("/images/risotoAlTartufo.avif");
        risottoTartufo.setCategoria(risottos);
        risottoTartufo.setTiempoPreparacion(25);
        risottoTartufo.setEsEspecialidad(true);
        comidaRepository.save(risottoTartufo);
        
        Comida risottoFunghi = new Comida();
        risottoFunghi.setNombre("Risotto ai Funghi");
        risottoFunghi.setDescripcion("Risotto con mezcla de hongos porcini y champignones");
        risottoFunghi.setPrecio(29900.0);
        risottoFunghi.setImagen("/images/risotofungui.png");
        risottoFunghi.setCategoria(risottos);
        risottoFunghi.setTiempoPreparacion(22);
        comidaRepository.save(risottoFunghi);
        
        // POSTRES
        Comida tiramisu = new Comida();
        tiramisu.setNombre("Tiramisu");
        tiramisu.setDescripcion("Postre tradicional italiano con café, mascarpone y cacao");
        tiramisu.setPrecio(16900.0);
        tiramisu.setImagen("/images/tiramisu.jpg");
        tiramisu.setCategoria(postres);
        tiramisu.setTiempoPreparacion(5);
        comidaRepository.save(tiramisu);
        
        Comida cannoli = new Comida();
        cannoli.setNombre("Cannoli Siciliani");
        cannoli.setDescripcion("Tubitos crujientes rellenos de ricotta y pistachos");
        cannoli.setPrecio(14900.0);
        cannoli.setImagen("/images/Cannoli Siciliani.jpg");
        cannoli.setCategoria(postres);
        cannoli.setTiempoPreparacion(8);
        comidaRepository.save(cannoli);
        
        // BEBIDAS
        Comida vinoTinto = new Comida();
        vinoTinto.setNombre("Vino Tinto Reservado");
        vinoTinto.setDescripcion("Vino tinto italiano de la casa");
        vinoTinto.setPrecio(45900.0);
        vinoTinto.setImagen("/images/5435_Vino_Tinto_Reservado0.jpg");
        vinoTinto.setCategoria(bebidas);
        vinoTinto.setTiempoPreparacion(2);
        comidaRepository.save(vinoTinto);
        
        Comida aguaGas = new Comida();
        aguaGas.setNombre("Agua con Gas");
        aguaGas.setDescripcion("Agua mineral con gas San Pellegrino");
        aguaGas.setPrecio(6900.0);
        aguaGas.setImagen("/images/agua con gas.jpg");
        aguaGas.setCategoria(bebidas);
        aguaGas.setTiempoPreparacion(1);
        comidaRepository.save(aguaGas);
    }
    

 private Adicional upsertAdicional(
            String nombre,
            String descripcion,
            Double precio,
            String imagen,
            String tipo,
            Categoria... categorias
    ) {
        Adicional a = adicionalRepository.findByNombre(nombre).orElseGet(Adicional::new);

        a.setNombre(nombre);
        a.setDescripcion(descripcion);
        a.setPrecio(precio);
        a.setImagen(imagen);
        a.setTipo(tipo);

        // Reasignar de forma determinista (idempotente)
        a.getCategorias().clear();
        a.getCategorias().addAll(Arrays.asList(categorias));

        return adicionalRepository.save(a);
    }

    private void crearAdicionalesEjemplo(Categoria antipastos, Categoria pizzas, 
    Categoria pastas, Categoria risottos) {
  // 2) Upserts (idempotentes) — si existe por nombre, actualiza y re-vincula categorías:
        upsertAdicional("Queso Parmesano Extra", "Parmesano reggiano rallado fresco", 3500.0, "/images/provolone.webp", "queso", pastas);
        upsertAdicional("Aceitunas Negras", "Aceitunas kalamata marinadas", 2500.0, "/images/aceitunas.jpg", "acompañamiento", pastas);
        upsertAdicional("Pan de Ajo", "Pan focaccia con ajo y hierbas", 4500.0, "/images/focaccia.jpeg", "acompañamiento", pastas);
        upsertAdicional("Hongos Trifolati", "Hongos salteados con ajo y perejil", 5500.0, "/images/funghi-trifolati.jpg", "acompañamiento", risottos);
        upsertAdicional("Queso Mozzarella Extra", "Mozzarella di bufala adicional", 4000.0, "/images/provolone.webp", "queso", pizzas);
        upsertAdicional("Rúcula Fresca", "Hojas de rúcula fresca", 2000.0, "/images/aceitunas.jpg", "vegetal", antipastos);
        upsertAdicional("Tomate Cherry", "Tomates cherry frescos", 2000.0, "/images/tomate_cherry.jpg", "vegetal", pizzas);
        upsertAdicional("Jamón Serrano", "Lonjas finas de jamón serrano", 6000.0, "/images/jamon.jpg", "proteina", pizzas);
        upsertAdicional("Albahaca Fresca", "Hojas de albahaca fresca", 1500.0, "/images/albahaca.jpg", "vegetal", pizzas);
        upsertAdicional("Salsa Pesto", "Salsa de albahaca y piñones", 3000.0, "/images/pesto.jpg", "salsa", pastas);
        upsertAdicional("Salsa Alfredo", "Crema con queso parmesano", 3500.0, "/images/alfredo.jpg", "salsa", pastas);
        upsertAdicional("Chorizo Picante", "Chorizo curado en rodajas", 5000.0, "/images/chorizo.jpg", "proteina", pizzas);
        upsertAdicional("Pollo Grillado", "Tiras de pollo a la plancha", 5500.0, "/images/pollo.jpg", "proteina", pastas);
        upsertAdicional("Espinaca Fresca", "Hojas frescas de espinaca", 1800.0, "/images/espinaca.jpg", "vegetal", risottos);
        upsertAdicional("Pimientos Asados", "Tiras de pimientos asados", 2200.0, "/images/pimientos.jpg", "vegetal", pizzas);
        upsertAdicional("Tocino Crujiente", "Tiras de tocino dorado", 5000.0, "/images/tocino.jpg", "proteina", pizzas);
        upsertAdicional("Maíz Dulce", "Granos de maíz dulce", 1500.0, "/images/maiz.jpg", "vegetal", pizzas);
        upsertAdicional("Champiñones Frescos", "Champiñones laminados frescos", 3000.0, "/images/champinones.jpg", "acompañamiento", pastas);
        upsertAdicional("Tomate Seco", "Tomates secos al sol", 2800.0, "/images/tomate_seco.jpg", "acompañamiento", risottos);
        upsertAdicional("Aceite de Trufa", "Aceite aromático de trufa", 8000.0, "/images/trufa.jpg", "condimento", pastas);
        }

}
