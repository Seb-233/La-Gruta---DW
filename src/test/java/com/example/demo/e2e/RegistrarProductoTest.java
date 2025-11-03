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
        if (driver != null) driver.quit();
    }

    // 🔹 Utilidad para hacer scroll antes de click
    private void scrollAndClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
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

        WebElement userInput = waitVisible(By.name("username"));
        WebElement passInput = driver.findElement(By.name("password"));
        WebElement btnLogin = driver.findElement(By.cssSelector(".btn-login"));

        userInput.clear();
        userInput.sendKeys("admin");
        passInput.clear();
        passInput.sendKeys("clave_incorrecta");
        btnLogin.click();

        // ✅ Validar si aparece mensaje de error
        WebElement alert = waitVisible(By.cssSelector(".alert-danger, .alert, .text-danger"));
        Assertions.assertTrue(
                alert.getText().toLowerCase().contains("error")
                        || alert.getText().toLowerCase().contains("incorrect"),
                "Debe mostrar mensaje de error al fallar el login");
    }

    @Test
    @Order(2)
    @DisplayName("Login exitoso con credenciales válidas")
    void loginExitosoTest() {
        driver.get(BASE_URL + "/login");

        driver.findElement(By.name("username")).clear();
        driver.findElement(By.name("username")).sendKeys("Juan");
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys("12345");
        driver.findElement(By.cssSelector(".btn-login")).click();

        wait.until(ExpectedConditions.urlContains("/dashboard"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("/dashboard"),
                "Debe acceder al dashboard");
    }

    @Test
    @Order(3)
    @DisplayName("Registrar nueva comida con 2 adicionales asociados a 'Antipastos'")
    void registrarComidaTest() {
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
        if (!disponible.isSelected()) disponible.click();

        // ✅ Scroll y guardar
        WebElement submitBtn = waitVisible(By.cssSelector("button[type='submit']"));
        scrollAndClick(submitBtn);

        // ✅ Confirmar redirección
        wait.until(ExpectedConditions.urlContains("/dashboard/comidas"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("/dashboard/comidas"),
                "Debe redirigir a la lista de comidas después de guardar");

        // ✅ Crear dos adicionales asociados a Antipastos
        crearAdicional("Tocineta Extra", "Porción de tocineta adicional", "3000", "Antipastos");
        crearAdicional("Queso Doble", "Extra de queso cheddar", "2500", "Antipastos");
    }

    private void crearAdicional(String nombre, String descripcion, String precio, String categoriaNombre) {
        driver.get(BASE_URL + "/dashboard/adicionales/nuevo");

        WebElement nombreInput = waitVisible(By.id("nombre"));
        nombreInput.clear();
        nombreInput.sendKeys(nombre);

        driver.findElement(By.id("descripcion")).sendKeys(descripcion);
        driver.findElement(By.id("precio")).sendKeys(precio);

        // ✅ Marcar categoría “Antipastos”
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".checkbox-group input[type='checkbox']")));
        List<WebElement> checkboxes = driver.findElements(By.cssSelector(".checkbox-group .form-check"));
        boolean marcado = false;
        for (WebElement check : checkboxes) {
            WebElement label = check.findElement(By.cssSelector("label"));
            if (label.getText().equalsIgnoreCase(categoriaNombre)) {
                WebElement input = check.findElement(By.cssSelector("input[type='checkbox']"));
                if (!input.isSelected()) scrollAndClick(input);
                marcado = true;
                break;
            }
        }
        Assertions.assertTrue(marcado, "No se encontró la categoría " + categoriaNombre + " en los checkboxes");

        WebElement guardarBtn = waitVisible(By.cssSelector("button[type='submit']"));
        scrollAndClick(guardarBtn);

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

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h5[contains(text(), 'Hamburguesa BBQ')]")));
        WebElement producto = driver.findElement(By.xpath("//h5[contains(text(), 'Hamburguesa BBQ')]"));
        Assertions.assertTrue(producto.isDisplayed(), "El producto debe aparecer en el menú");
    }

    @Test
    @Order(5)
    @DisplayName("Agregar un nuevo adicional 'Papas medianas' asociado a 'Antipastos'")
    void agregarNuevoAdicionalTest() {
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(0)); // volver al dashboard

        driver.get(BASE_URL + "/dashboard/adicionales/nuevo");

        WebElement nombreInput = waitVisible(By.id("nombre"));
        nombreInput.clear();
        nombreInput.sendKeys("Papas medianas");

        driver.findElement(By.id("descripcion")).sendKeys("Porción de papas medianas");
        driver.findElement(By.id("precio")).sendKeys("4000");

        // ✅ Marcar categoría “Antipastos”
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".checkbox-group input[type='checkbox']")));
        List<WebElement> checkboxes = driver.findElements(By.cssSelector(".checkbox-group .form-check"));
        for (WebElement check : checkboxes) {
            WebElement label = check.findElement(By.cssSelector("label"));
            if (label.getText().equalsIgnoreCase("Antipastos")) {
                WebElement input = check.findElement(By.cssSelector("input[type='checkbox']"));
                if (!input.isSelected()) scrollAndClick(input);
                break;
            }
        }

        WebElement guardarBtn = waitVisible(By.cssSelector("button[type='submit']"));
        scrollAndClick(guardarBtn);

        wait.until(ExpectedConditions.urlContains("/dashboard/adicionales"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("/dashboard/adicionales"),
                "Debe redirigir a la lista de adicionales después de guardar");
    }

    @Test
    @Order(6)
    @DisplayName("Verificar el producto con los 3 adicionales en el menú")
    void verificarComidaCon3AdicionalesTest() {
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1)); // pestaña del menú

        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h5[contains(text(), 'Hamburguesa BBQ')]")));

        WebElement producto = driver.findElement(By.xpath("//h5[contains(text(), 'Hamburguesa BBQ')]"));
        scrollAndClick(producto.findElement(By.xpath(".//button[contains(text(),'Ver Adicionales')]")));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".modal-custom, .modal-content")));
        List<WebElement> adicionales = driver.findElements(By.cssSelector(".list-group-item"));
        Assertions.assertTrue(adicionales.size() >= 3, "Debe tener al menos 3 adicionales visibles");
    }
}
