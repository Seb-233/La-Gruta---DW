 package com.example.demo.e2e;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FlujoPedidoCompletoTest {

    private static WebDriver driverCliente;
    private static WebDriver driverOperador;
    private static WebDriverWait waitCliente;
    private static WebDriverWait waitOperador;
    private final String BASE_URL = "http://localhost:4200";

    @BeforeAll
    static void setUp() {
        WebDriverManager.chromedriver().setup();

        driverCliente = new ChromeDriver();
        driverOperador = new ChromeDriver();

        driverCliente.manage().window().maximize();
        driverOperador.manage().window().maximize();

        driverCliente.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driverOperador.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        waitCliente = new WebDriverWait(driverCliente, Duration.ofSeconds(15));
        waitOperador = new WebDriverWait(driverOperador, Duration.ofSeconds(15));
    }

    @AfterAll
    static void tearDown() {
        if (driverCliente != null) driverCliente.quit();
        if (driverOperador != null) driverOperador.quit();
    }

    private WebElement waitVisible(WebDriverWait wait, By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private void scrollAndClick(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(element))
                .click();
    }

    // ===========================================================
    // 1️⃣ LOGIN CLIENTE
    // ===========================================================
    @Test
    @Order(1)
    @DisplayName("Login cliente Oscar")
    void loginClienteTest() {
        driverCliente.get(BASE_URL + "/login");

        driverCliente.findElement(By.name("username")).sendKeys("oscar");
        driverCliente.findElement(By.name("password")).sendKeys("12345");
        driverCliente.findElement(By.cssSelector(".btn-login")).click();

        waitCliente.until(ExpectedConditions.urlContains("/dashboard"));
        Assertions.assertTrue(driverCliente.getCurrentUrl().contains("/dashboard"),
                "El cliente debe ingresar al dashboard.");
    }

    // ===========================================================
    // 2️⃣ AGREGAR DOS COMIDAS CON ADICIONALES
    // ===========================================================
    @Test
    @Order(2)
    @DisplayName("Agregar 2 comidas con 2 adicionales cada una al carrito")
    void agregarComidasTest() throws InterruptedException {
        driverCliente.get(BASE_URL + "/menu");

        agregarComidaConAdicionales("Pizza Margherita",
                Arrays.asList("Queso Parmesano Extra", "Rúcula Fresca"));

        Thread.sleep(1500); // pequeña pausa antes de la segunda pizza

        agregarComidaConAdicionales("Pizza Nera",
                Arrays.asList("Tomate Cherry", "Jamón Serrano"));
    }

    private void agregarComidaConAdicionales(String comida, List<String> adicionales) throws InterruptedException {
        System.out.println("🧩 Agregando " + comida);

        // Esperar que aparezca la tarjeta
        waitCliente.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h5[contains(text(),'" + comida + "')]")));

        WebElement card = driverCliente.findElement(
                By.xpath("//h5[contains(text(),'" + comida + "')]/ancestor::div[contains(@class,'card')]"));

        // Intentar abrir el modal (Ver adicionales o Agregar)
        List<WebElement> btnVerAdicionales = card.findElements(By.xpath(".//button[contains(.,'Ver adicionales')]"));
        if (!btnVerAdicionales.isEmpty()) {
            scrollAndClick(driverCliente, btnVerAdicionales.get(0));
        } else {
            WebElement botonAgregar = card.findElement(By.xpath(".//button[contains(.,'Agregar')]"));
            scrollAndClick(driverCliente, botonAgregar);
        }

        // Esperar modal visible
        WebElement modal = waitCliente.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".modal-content")));

        // Marcar los adicionales
        for (String adicional : adicionales) {
            System.out.println("   ➕ Seleccionando adicional: " + adicional);
            By chkLocator = By.xpath("//label[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'"
                    + adicional.toLowerCase() + "')]/preceding-sibling::input[@type='checkbox']");
            waitCliente.until(ExpectedConditions.presenceOfElementLocated(chkLocator));
            WebElement check = driverCliente.findElement(chkLocator);
            ((JavascriptExecutor) driverCliente).executeScript("arguments[0].scrollIntoView({block:'center'});", check);
            if (!check.isSelected()) check.click();
        }

        // Agregar al carrito
        WebElement btnAgregar = waitCliente.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'Agregar al carrito')]")));
        scrollAndClick(driverCliente, btnAgregar);

        // Esperar que cierre el modal
        waitCliente.until(ExpectedConditions.invisibilityOf(modal));

        Thread.sleep(1000);
    }

    // ===========================================================
    // 3️⃣ VERIFICAR CARRITO Y CONFIRMAR PEDIDO
    // ===========================================================
    @Test
    @Order(3)
    @DisplayName("Verificar carrito y confirmar pedido")
    void verificarCarritoYConfirmar() {
        driverCliente.get(BASE_URL + "/carro");

        List<WebElement> items = waitCliente.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector(".carro-item"), 1));
        Assertions.assertEquals(2, items.size(), "El carrito debe tener 2 comidas.");

        for (WebElement item : items) {
            String nombre = item.findElement(By.cssSelector(".name")).getText();
            String adicionales = item.findElement(By.cssSelector(".adicionales")).getText();
            System.out.println("🧾 " + nombre + " -> " + adicionales);
            Assertions.assertFalse(adicionales.isEmpty(), "Cada comida debe tener adicionales");
        }

        WebElement totalElem = driverCliente.findElement(By.cssSelector(".total-text"));
        String totalTexto = totalElem.getText().replaceAll("[^0-9]", "");
        double totalInicial = Double.parseDouble(totalTexto);
        Assertions.assertTrue(totalInicial > 0, "El total debe ser mayor que cero");

        WebElement btnConfirmar = driverCliente.findElement(By.cssSelector(".btn-confirmar"));
        scrollAndClick(driverCliente, btnConfirmar);

        waitCliente.until(ExpectedConditions.urlContains("/mis-pedidos"));
        Assertions.assertTrue(driverCliente.getCurrentUrl().contains("/mis-pedidos"),
                "Debe redirigir al historial de pedidos");
    }

    // ===========================================================
    // 4️⃣ LOGIN OPERADOR Y CAMBIO DE ESTADOS
    // ===========================================================
    @Test
    @Order(4)
    @DisplayName("Operador cambia estado del pedido")
    void operadorCambiaEstado() {
        driverOperador.get(BASE_URL + "/login");
        driverOperador.findElement(By.name("username")).sendKeys("Operador");
        driverOperador.findElement(By.name("password")).sendKeys("12345");
        driverOperador.findElement(By.cssSelector(".btn-login")).click();

        waitOperador.until(ExpectedConditions.urlContains("/dashboard"));
        driverOperador.get(BASE_URL + "/dashboard/pedidos");

        WebElement filaPedido = waitVisible(waitOperador, By.cssSelector("table tbody tr:first-child"));
        scrollAndClick(driverOperador, filaPedido);

        for (String estado : Arrays.asList("Preparando", "En camino", "Entregado")) {
            System.out.println("🚚 Cambiando estado a: " + estado);
            WebElement selectEstado = waitVisible(waitOperador, By.cssSelector("select.estado"));
            Select s = new Select(selectEstado);
            s.selectByVisibleText(estado);

            WebElement btnGuardar = driverOperador.findElement(By.cssSelector(".btn-guardar-estado"));
            scrollAndClick(driverOperador, btnGuardar);

            waitOperador.until(ExpectedConditions.or(
                    ExpectedConditions.textToBePresentInElementValue(selectEstado, estado),
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector(".toast-success"))));

            driverCliente.navigate().refresh();
            waitCliente.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table")));
            String textoTabla = driverCliente.findElement(By.cssSelector("table")).getText().toLowerCase();
            Assertions.assertTrue(textoTabla.contains(estado.toLowerCase()),
                    "El cliente debe ver el estado actualizado: " + estado);
        }
    }

    // ===========================================================
    // 5️⃣ VALIDAR HISTORIAL FINAL Y TOTALES
    // ===========================================================
    @Test
    @Order(5)
    @DisplayName("Verificar pedido completado y totales correctos")
    void verificarHistorialYTotales() {
        driverCliente.get(BASE_URL + "/perfil/pedidos-completados");

        List<WebElement> filas = waitCliente.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("table tbody tr"), 0));

        boolean encontrado = filas.stream().anyMatch(
                f -> f.getText().toLowerCase().contains("pizza margherita")
                        && f.getText().toLowerCase().contains("pizza nera")
                        && f.getText().toLowerCase().contains("entregado"));

        Assertions.assertTrue(encontrado, "El pedido debe aparecer en el historial como completado");

        WebElement totalElem = driverCliente.findElement(By.xpath("//td[contains(text(),'Total')]/following-sibling::td"));
        String totalTxt = totalElem.getText().replaceAll("[^0-9]", "");
        double totalHistorial = Double.parseDouble(totalTxt);
        Assertions.assertTrue(totalHistorial > 0, "El total del pedido completado debe ser mayor que cero");
    }
}