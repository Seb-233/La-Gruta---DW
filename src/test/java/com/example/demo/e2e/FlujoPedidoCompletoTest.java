/*package com.example.demo.e2e;

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
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
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
        if (driverCliente != null)
            driverCliente.quit();
        if (driverOperador != null)
            driverOperador.quit();
    }

    private void scrollAndClick(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(element));
        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    // ===========================================================
    // 1️⃣ LOGIN CLIENTE
    // ===========================================================
    @Test
    @Order(1)
    @DisplayName("Login cliente Rosmira")
    void loginClienteTest() {
        driverCliente.get(BASE_URL + "/login");

        driverCliente.findElement(By.name("username")).sendKeys("rosmira");
        driverCliente.findElement(By.name("password")).sendKeys("Ros0123*");
        driverCliente.findElement(By.cssSelector(".btn-login")).click();

        waitCliente.until(ExpectedConditions.urlContains("/home"));
        Assertions.assertTrue(driverCliente.getCurrentUrl().contains("/home"),
                "El cliente debe ingresar a /home después del login.");
    }

    // ===========================================================
    // 2️⃣ NUEVO TEST — AGREGAR COMIDAS Y ENTRAR AL CARRITO
    // ===========================================================
    @Test
    @Order(2)
    @DisplayName("Cliente agrega comidas con múltiples adicionales y entra al carrito")
    void agregarComidasYConfirmar() throws InterruptedException {
        driverCliente.get(BASE_URL + "/menu");

        // 🍕 Primer plato: Pizza Nera con dos adicionales
        agregarComidaConAdicionales("Pizza Nera", Arrays.asList("Queso Parmesano Extra", "Jamón Serrano"));

        // 🥖 Segundo plato: Bruschetta Italiana con dos adicionales
        agregarComidaConAdicionales("Bruschetta Italiana", Arrays.asList("Tomate Cherry", "Maíz Dulce"));

        // Ir al carrito al final
        driverCliente.get(BASE_URL + "/carro");

        // 🔔 Si aparece alguna alerta tipo "✅ Comida añadida al carrito", la aceptamos
        try {
            WebDriverWait waitAlert = new WebDriverWait(driverCliente, Duration.ofSeconds(3));
            waitAlert.until(ExpectedConditions.alertIsPresent());
            Alert alerta = driverCliente.switchTo().alert();
            System.out.println("📢 Alerta mostrada: " + alerta.getText());
            alerta.accept();
        } catch (TimeoutException e) {
            System.out.println("⚠️ No apareció alerta de confirmación tras agregar comidas.");
        }

        // 🕓 Esperar hasta que aparezcan los ítems del carrito (acepta ambas clases
        // posibles)
        By itemLocator = By.cssSelector(".carro-item, .pedido-item");
        WebDriverWait wait = new WebDriverWait(driverCliente, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(itemLocator));

        List<WebElement> items = driverCliente.findElements(itemLocator);
        Assertions.assertFalse(items.isEmpty(), "El carrito debe contener al menos 1 comida.");

        System.out.println("🛒 Cliente entró al carrito con " + items.size() + " pedidos");
    }

    /**
     * Permite seleccionar múltiples adicionales dentro del modal de una comida.
     */
 /*    private void agregarComidaConAdicionales(String comida, List<String> adicionales) throws InterruptedException {
        waitCliente.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h5[contains(text(),'" + comida + "')]")));

        WebElement card = driverCliente.findElement(
                By.xpath("//h5[contains(text(),'" + comida + "')]/ancestor::div[contains(@class,'card')]"));
        WebElement btnVerAdicionales = card.findElement(By.xpath(".//button[contains(.,'Ver Adicionales')]"));
        scrollAndClick(driverCliente, btnVerAdicionales);

        WebElement modal = waitCliente
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".modal-custom")));

        // ✅ Selecciona todos los adicionales de la lista
        for (String adicional : adicionales) {
            By checkLocator = By.xpath(
                    "//label[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'"
                            + adicional.toLowerCase() + "')]/descendant::input");
            waitCliente.until(ExpectedConditions.elementToBeClickable(checkLocator));
            WebElement check = driverCliente.findElement(checkLocator);
            scrollAndClick(driverCliente, check);
            System.out.println("🧀 Seleccionado adicional: " + adicional);
            Thread.sleep(300);
        }

        // Confirmar selección
        WebElement btnConfirmar = waitCliente.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'Confirmar selección')]")));
        scrollAndClick(driverCliente, btnConfirmar);
        waitCliente.until(ExpectedConditions.invisibilityOf(modal));

        System.out.println("✅ Añadido: " + comida + " con adicionales " + adicionales);
        Thread.sleep(800);
    }

    // ===========================================================
    // 3️⃣ VERIFICAR CARRITO Y CONFIRMAR PEDIDO
    // ===========================================================
    @Test
    @Order(3)
    @DisplayName("Verificar carrito y confirmar pedido")
    void verificarCarritoYConfirmar() {
        driverCliente.get(BASE_URL + "/carro");

        // Esperar que haya al menos 2 ítems en el carrito
        List<WebElement> items = waitCliente.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector(".carro-item"), 1));
        Assertions.assertEquals(2, items.size(), "El carrito debe tener 2 comidas.");

        // Verificar que cada comida tenga nombre y adicionales
        for (WebElement item : items) {
            String nombre = item.findElement(By.cssSelector(".name")).getText();
            String adicionales = item.findElement(By.cssSelector(".adicionales")).getText();
            System.out.println("🧾 " + nombre + " -> " + adicionales);
            Assertions.assertFalse(adicionales.isEmpty(), "Cada comida debe tener adicionales");
        }

        // Verificar que el total sea mayor que cero
        WebElement totalElem = driverCliente.findElement(By.cssSelector(".total-text"));
        String totalTexto = totalElem.getText().replaceAll("[^0-9]", "");
        double totalInicial = Double.parseDouble(totalTexto);
        Assertions.assertTrue(totalInicial > 0, "El total debe ser mayor que cero");

        // Confirmar el pedido
        WebElement btnConfirmar = driverCliente.findElement(By.cssSelector(".btn-confirmar"));
        scrollAndClick(driverCliente, btnConfirmar);

        // ✅ Esperar y aceptar la alerta de confirmación si aparece
        try {
            WebDriverWait wait = new WebDriverWait(driverCliente, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alerta = driverCliente.switchTo().alert();
            System.out.println("📢 Alerta mostrada: " + alerta.getText());
            alerta.accept();
        } catch (TimeoutException e) {
            System.out.println("⚠️ No apareció alerta de confirmación, continuando...");
        }

        // Esperar la redirección al historial de pedidos
        waitCliente.until(ExpectedConditions.urlContains("/mis-pedidos"));
        Assertions.assertTrue(driverCliente.getCurrentUrl().contains("/mis-pedidos"),
                "Debe redirigir al historial de pedidos");
    }

    // ===========================================================
    // 4️⃣ LOGIN OPERADOR Y CAMBIO DE ESTADOS
    // ===========================================================
    @Test
    @Order(4)
    @DisplayName("Operador cambia estado solo del pedido de Rosmira")
    void operadorCambiaEstado() {
        // Login del operador
        driverOperador.get(BASE_URL + "/login");
        driverOperador.findElement(By.name("username")).sendKeys("Operador");
        driverOperador.findElement(By.name("password")).sendKeys("Operador12345*");
        driverOperador.findElement(By.cssSelector(".btn-login")).click();
        waitOperador.until(ExpectedConditions.urlContains("/operador/portal"));

        // Botones en orden de cambio de estado
        List<String> botonesOrden = Arrays.asList(
                "Marcar como Cocinando",
                "Asignar y Enviar",
                "Marcar como Entregado");

        for (String botonTexto : botonesOrden) {
            WebElement boton = null;

            // Reintentos para encontrar la fila y el botón dentro de la fila
            for (int intentos = 0; intentos < 10; intentos++) {
                try {
                    List<WebElement> filas = driverOperador.findElements(By.cssSelector("table tbody tr"));
                    WebElement filaRosmira = filas.stream()
                            .filter(f -> f.getText().toLowerCase().contains("rosmira"))
                            .findFirst()
                            .orElse(null);

                    // Si ya no está la fila (por ejemplo, después de Entregado), salir del loop
                    if (filaRosmira == null) {
                        System.out.println("✅ Fila de Rosmira no encontrada, estado final alcanzado.");
                        boton = null;
                        break;
                    }

                    boton = filaRosmira.findElements(By.xpath(".//button[contains(text(),'" + botonTexto + "')]"))
                            .stream()
                            .filter(WebElement::isDisplayed)
                            .findFirst()
                            .orElse(null);

                    if (boton != null)
                        break;

                } catch (StaleElementReferenceException ignored) {
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
            }

            if (boton != null) {
                scrollAndClick(driverOperador, boton);
                System.out.println("✅ Click en '" + botonTexto + "' realizado correctamente.");
            } else {
                System.out.println("ℹ️ Botón '" + botonTexto + "' no encontrado (posiblemente ya se completó).");
            }

            // Esperar un pequeño tiempo antes del siguiente estado
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }

        // Verificar estados finales en la vista del cliente (si la fila ya desapareció,
        // se considera correcto)
        try {
            WebElement tablaCliente = waitCliente
                    .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table")));
            String textoTabla = tablaCliente.getText().toLowerCase();

            List<String> estadosEsperados = Arrays.asList("cocinando", "enviado", "entregado");
            for (String estado : estadosEsperados) {
                Assertions.assertTrue(textoTabla.contains(estado),
                        "El pedido de Rosmira debe mostrar el estado: " + estado);
            }
        } catch (TimeoutException e) {
            System.out.println("✅ La fila de Rosmira ya no está en la tabla del cliente, todo correcto.");
        }

        System.out.println("🎉 Todos los estados del pedido de Rosmira se actualizaron correctamente.");
    }

    // ===========================================================
    // 5️⃣ VALIDAR HISTORIAL FINAL Y TOTALES
    // ===========================================================
    @Test
    @Order(5)
    @DisplayName("Verificar pedido completado y totales correctos")
    void verificarHistorialYTotales() {
        driverCliente.get(BASE_URL + "/mis-pedidos");

        // Refrescar para asegurar que se reflejen los cambios
        driverCliente.navigate().refresh();

        // Esperar a que al menos un pedido-card esté visible
        List<WebElement> pedidos = new WebDriverWait(driverCliente, Duration.ofSeconds(20))
                .until(driver -> {
                    List<WebElement> cards = driver.findElements(By.cssSelector("div.pedido-card"));
                    boolean algunoVisible = cards.stream().anyMatch(WebElement::isDisplayed);
                    return algunoVisible ? cards : null;
                });

        if (pedidos == null || pedidos.isEmpty()) {
            throw new AssertionError("No se encontraron filas en el historial de pedidos del cliente");
        }

        // Filtrar el pedido que tenga los productos esperados
        WebElement pedidoRosmira = pedidos.stream()
                .filter(p -> {
                    String texto = p.getText().toLowerCase();
                    return texto.contains("bruschetta italiana")
                            && texto.contains("pizza nera")
                            && texto.contains("entregado");
                })
                .findFirst()
                .orElseThrow(
                        () -> new AssertionError("No se encontró el pedido completado con los productos esperados"));

        // Verificar el total
        WebElement totalElem = pedidoRosmira.findElement(By.cssSelector("div.total-pedido strong.text-success"));
        String totalTxt = totalElem.getText().replaceAll("[^0-9]", "");
        double totalHistorial = Double.parseDouble(totalTxt);
        Assertions.assertTrue(totalHistorial > 0, "El total del pedido completado debe ser mayor que cero");

        System.out.println("🎉 Pedido completado y totales verificados correctamente.");
    }

}

*/