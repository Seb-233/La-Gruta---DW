package com.example.demo.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.AdicionalRepository;
import com.example.demo.repository.ComidaRepository;

@Component
public class DatabaseInit implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private ComidaRepository comidaRepository;
    
    @Autowired
    private AdicionalRepository adicionalRepository;

    @Override
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
    
    private void crearAdicionalesEjemplo(Categoria antipastos, Categoria pizzas, 
                                       Categoria pastas, Categoria risottos) {
        
        // Adicionales para pastas, pizzas y risottos
        Adicional parmesano = new Adicional();
        parmesano.setNombre("Queso Parmesano Extra");
        parmesano.setDescripcion("Parmesano reggiano rallado fresco");
        parmesano.setPrecio(3500.0);
        parmesano.setImagen("/images/provolone.webp");
        parmesano.setCategoria(pastas);
        parmesano.setTipo("queso");
        adicionalRepository.save(parmesano);
        
        // Adicionales para antipastos y pizzas
        Adicional aceitunas = new Adicional();
        aceitunas.setNombre("Aceitunas Negras");
        aceitunas.setDescripcion("Aceitunas kalamata marinadas");
        aceitunas.setPrecio(2500.0);
        aceitunas.setImagen("/images/aceitunas.jpg");
        aceitunas.setCategoria(antipastos);
        aceitunas.setTipo("acompañamiento");
        adicionalRepository.save(aceitunas);
        
        // Pan de ajo para varias categorías
        Adicional panAjo = new Adicional();
        panAjo.setNombre("Pan de Ajo");
        panAjo.setDescripcion("Pan focaccia con ajo y hierbas");
        panAjo.setPrecio(4500.0);
        panAjo.setImagen("/images/focatia.jpeg");
        panAjo.setCategoria(pastas);
        panAjo.setTipo("acompañamiento");
        adicionalRepository.save(panAjo);
        
        // Hongos para pastas, pizzas y risottos
        Adicional hongos = new Adicional();
        hongos.setNombre("Hongos Trifolati");
        hongos.setDescripcion("Hongos salteados con ajo y perejil");
        hongos.setPrecio(5500.0);
        hongos.setImagen("/images/funghi-trifolati.jpg");
        hongos.setCategoria(risottos);
        hongos.setTipo("acompañamiento");
        adicionalRepository.save(hongos);
        
        // Adicionales específicos para pizzas
        Adicional quesoExtra = new Adicional();
        quesoExtra.setNombre("Queso Mozzarella Extra");
        quesoExtra.setDescripcion("Mozzarella di bufala adicional");
        quesoExtra.setPrecio(4000.0);
        quesoExtra.setImagen("/images/provolone.webp");
        quesoExtra.setCategoria(pizzas);
        quesoExtra.setTipo("queso");
        adicionalRepository.save(quesoExtra);
        
        // Rúcula para antipastos y pizzas
        Adicional rucula = new Adicional();
        rucula.setNombre("Rúcula Fresca");
        rucula.setDescripcion("Hojas de rúcula fresca");
        rucula.setPrecio(2000.0);
        rucula.setImagen("/images/aceitunas.jpg"); // Usar imagen temporal
        rucula.setCategoria(antipastos);
        rucula.setTipo("vegetal");
        adicionalRepository.save(rucula);
    }
}

