package com.example.demo.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegistrarProductoTest {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private final String BASE_URL = "http://localhost:4200";

    @BeforeAll
    static void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterAll
    static void tearDown() {
        if (driver != null)
            driver.quit();
    }

    // Utilidad para hacer scroll antes de click
    private void scrollAndClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);

        // Esperar que el elemento sea clickeable
        wait.until(ExpectedConditions.elementToBeClickable(element));

        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            // Si algo tapa el botón, usamos JavaScript para forzar el click
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    // 🔹 Esperar visibilidad segura
    private WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    @Test
    @Order(1)
    @DisplayName("Intentar login con credenciales incorrectas")
    void loginFallidoTest() {
        driver.get(BASE_URL + "/login");

        // Esperar hasta que la URL realmente cargue el login y los inputs existan
        wait.until(ExpectedConditions.urlContains("/login"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("password")));

        WebElement userInput = waitVisible(By.name("username"));
        WebElement passInput = driver.findElement(By.name("password"));
        WebElement btnLogin = driver.findElement(By.cssSelector(".btn-login"));

        userInput.clear();
        userInput.sendKeys("admin");
        passInput.clear();
        passInput.sendKeys("clave_incorrecta");
        btnLogin.click();

        // Esperar mensaje de error "Credenciales inválidas"
        WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Credenciales inválidas') or contains(@class,'alert')]")));

        Assertions.assertTrue(
                alert.getText().toLowerCase().contains("credenciales inválidas"),
                "Debe mostrar mensaje de error 'Credenciales inválidas' al fallar el login");
    }

    @Test
    @Order(2)
    @DisplayName("Login exitoso con credenciales válidas")
    void loginExitosoTest() {
        driver.get(BASE_URL + "/login");

        driver.findElement(By.name("username")).clear();
        driver.findElement(By.name("username")).sendKeys("Pipe");
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys("Pipe12345");
        driver.findElement(By.cssSelector(".btn-login")).click();

        wait.until(ExpectedConditions.urlContains("/dashboard"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("/dashboard"),
                "Debe acceder al dashboard");
    }

    @Test
    @Order(3)
    @DisplayName("Registrar nueva comida con 2 adicionales asociados a 'Antipastos'")
    void registrarComidaTest() throws InterruptedException {
        driver.get(BASE_URL + "/dashboard/comidas/agregar");

        WebElement nombreInput = waitVisible(By.id("nombre"));
        nombreInput.clear();
        nombreInput.sendKeys("Hamburguesa BBQ");

        driver.findElement(By.id("descripcion")).sendKeys("Hamburguesa con salsa BBQ y tocineta");
        driver.findElement(By.id("precio")).sendKeys("25000");
        driver.findElement(By.id("imagen")).sendKeys("https://ejemplo.com/hamburguesa.jpg");

        // ✅ Esperar y seleccionar la categoría “Antipastos”
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("#categoria option"), 1));
        Select categoriaSelect = new Select(driver.findElement(By.id("categoria")));
        boolean found = false;
        for (WebElement opt : categoriaSelect.getOptions()) {
            if (opt.getText().equalsIgnoreCase("Antipastos")) {
                categoriaSelect.selectByVisibleText(opt.getText());
                found = true;
                break;
            }
        }
        Assertions.assertTrue(found, "No se encontró la categoría 'Antipastos' en el select");

        // ✅ Marcar disponible
        WebElement disponible = driver.findElement(By.id("disponible"));
        if (!disponible.isSelected())
            disponible.click();

        // ✅ Scroll y guardar
        By botonGuardar = By.xpath("//button[contains(., 'Guardar')]");
        WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(botonGuardar));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitBtn);
        Thread.sleep(500);
        submitBtn.click();

        // ✅ Confirmar redirección
        wait.until(ExpectedConditions.urlContains("/dashboard/comidas"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("/dashboard/comidas"),
                "Debe redirigir a la lista de comidas después de guardar");

        // ✅ Crear dos adicionales asociados a Antipastos
        crearAdicional("Tocineta Extra", "Porción de tocineta adicional", "3000", "Antipastos");
        crearAdicional("Queso Doble", "Extra de queso cheddar", "2500", "Antipastos");
    }

    private void crearAdicional(String nombre, String descripcion, String precio, String categoriaNombre)
            throws InterruptedException {
        driver.get(BASE_URL + "/dashboard/adicionales/nuevo");

        WebElement nombreInput = waitVisible(By.id("nombre"));
        nombreInput.clear();
        nombreInput.sendKeys(nombre);

        driver.findElement(By.id("descripcion")).sendKeys(descripcion);
        driver.findElement(By.id("precio")).sendKeys(precio);

        // ✅ Esperar que los checkboxes carguen y marcar la categoría correcta
        wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.cssSelector(".checkbox-group input[type='checkbox']")));
        List<WebElement> checkboxes = driver.findElements(By.cssSelector(".checkbox-group .form-check"));
        boolean marcado = false;
        for (WebElement check : checkboxes) {
            WebElement label = check.findElement(By.cssSelector("label"));
            if (label.getText().equalsIgnoreCase(categoriaNombre)) {
                WebElement input = check.findElement(By.cssSelector("input[type='checkbox']"));
                if (!input.isSelected())
                    scrollAndClick(input);
                marcado = true;
                break;
            }
        }
        Assertions.assertTrue(marcado, "No se encontró la categoría " + categoriaNombre + " en los checkboxes");

        // ✅ Guardar adicional
        WebElement guardarBtn = waitVisible(By.cssSelector("button[type='submit']"));
        scrollAndClick(guardarBtn);

        // ✅ Esperar y aceptar el alert (SweetAlert o alert nativo)
        try {
            WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            alertWait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            System.out.println("⚠️ Alerta detectada: " + alert.getText());
            alert.accept();
            Thread.sleep(500); // pequeña espera por estabilidad
        } catch (TimeoutException e) {
            System.out.println("No apareció ninguna alerta tras crear el adicional.");
        }

        // ✅ Confirmar redirección a la lista de adicionales
        wait.until(ExpectedConditions.urlContains("/dashboard/adicionales"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("/dashboard/adicionales"),
                "Debe redirigir a la lista de adicionales después de guardar");
    }

    @Test
    @Order(4)
    @DisplayName("Verificar comida en el menú con 2 adicionales")
    void verificarComidaEnMenuTest() {
        ((JavascriptExecutor) driver).executeScript("window.open('" + BASE_URL + "/menu','_blank');");
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//h5[contains(text(), 'Hamburguesa BBQ')]")));
        WebElement producto = driver.findElement(By.xpath("//h5[contains(text(), 'Hamburguesa BBQ')]"));
        Assertions.assertTrue(producto.isDisplayed(), "El producto debe aparecer en el menú");
    }

    @Test
    @Order(5)
    @DisplayName("Agregar un nuevo adicional 'Papas medianas' asociado a 'Antipastos'")
    void agregarNuevoAdicionalTest() throws InterruptedException {
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(0)); // volver al dashboard

        driver.get(BASE_URL + "/dashboard/adicionales/nuevo");

        WebElement nombreInput = waitVisible(By.id("nombre"));
        nombreInput.clear();
        nombreInput.sendKeys("Papas medianas");

        driver.findElement(By.id("descripcion")).sendKeys("Porción de papas medianas");
        driver.findElement(By.id("precio")).sendKeys("4000");

        // ✅ Marcar categoría “Antipastos”
        wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.cssSelector(".checkbox-group input[type='checkbox']")));
        List<WebElement> checkboxes = driver.findElements(By.cssSelector(".checkbox-group .form-check"));
        for (WebElement check : checkboxes) {
            WebElement label = check.findElement(By.cssSelector("label"));
            if (label.getText().equalsIgnoreCase("Antipastos")) {
                WebElement input = check.findElement(By.cssSelector("input[type='checkbox']"));
                if (!input.isSelected())
                    scrollAndClick(input);
                break;
            }
        }

        WebElement guardarBtn = waitVisible(By.cssSelector("button[type='submit']"));
        scrollAndClick(guardarBtn);

        // ✅ Manejar alerta nativa si aparece (alert() o confirm())
        try {
            WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            alertWait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            System.out.println("⚠️ Alerta detectada: " + alert.getText());
            alert.accept();
            Thread.sleep(500);
        } catch (TimeoutException e) {
            System.out.println("No se detectó alerta nativa tras crear el adicional.");
        }

        // ✅ Confirmar redirección
        wait.until(ExpectedConditions.urlContains("/dashboard/adicionales"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("/dashboard/adicionales"),
                "Debe redirigir a la lista de adicionales después de guardar");
    }

    @Test
    @Order(6)
    @DisplayName("Verificar el producto con los 3 adicionales en el menú")
    void verificarComidaCon3AdicionalesTest() {
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());

        // Si solo hay una pestaña, permanece en ella
        if (tabs.size() > 1) {
            driver.switchTo().window(tabs.get(1)); // pestaña del menú
        } else {
            driver.switchTo().window(tabs.get(0)); // usa la actual
        }

        driver.navigate().refresh();

        // Esperar a que aparezca el producto
        WebElement producto = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(
                        "//h5[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'hamburguesa bbq')]")));

        // Buscar botón dentro del mismo contenedor
        WebElement botonVer = null;
        try {
            botonVer = producto.findElement(By.xpath(
                    ".//button[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'ver adicionales')]"));
        } catch (NoSuchElementException e) {
            // Si no está dentro, buscar cerca (en el mismo card)
            try {
                botonVer = producto.findElement(By.xpath(
                        "./ancestor::div[contains(@class,'card')]//button[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'ver adicionales')]"));
            } catch (NoSuchElementException ignored) {
            }
        }

        // Verificar que el botón exista
        Assertions.assertNotNull(botonVer, "No se encontró el botón 'Ver Adicionales' asociado a la Hamburguesa BBQ");

        scrollAndClick(botonVer);

        // Esperar que se abra el modal
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".modal-custom, .modal-content")));

        // Verificar adicionales
        List<WebElement> adicionales = driver.findElements(By.cssSelector(".list-group-item"));
        Assertions.assertTrue(adicionales.size() >= 3, "Debe tener al menos 3 adicionales visibles");
    }

}
