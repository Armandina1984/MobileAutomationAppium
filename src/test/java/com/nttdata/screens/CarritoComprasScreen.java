
package com.nttdata.screens;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CarritoComprasScreen {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    private final String paquete =
            "com.saucelabs.mydemoapp.android:id/";

    private final By tituloProductos =
            By.id(paquete + "productTV");

    private final By galeriaProductos =
            By.id(paquete + "productRV");

    private final By botonCarrito =
            By.id(paquete + "cartRL");

    private final By pantallaCarrito =
            By.id(paquete + "cartCL");

    private final By carritoVacio =
            By.id(paquete + "noItemCL");

    public CarritoComprasScreen() {
        driver = AppConfigScreen.getDriver();

        wait = new WebDriverWait(
                driver, Duration.ofSeconds(15));
    }

    // Validar que cargue la galeria
    public void validarGaleria() {

        WebElement titulo = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        tituloProductos));

        if (!titulo.getText().equals("Products")) {
            throw new AssertionError(
                    "No se muestra la pantalla Products");
        }

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        galeriaProductos));

        System.out.println(
                "[QA] Galeria de productos cargada correctamente");
    }

    // Buscar y seleccionar un producto
    public void seleccionarProducto(String producto) {

        if (producto.equals("Sauce Labs Bolt - T-Shirt")) {
            producto = "Sauce Labs Bolt T-Shirt";
        }

        By localizador = By.xpath(
                "//*[@resource-id='" + paquete +
                        "titleTV' and @text='" + producto + "']");

        WebElement elemento = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        localizador));

        elemento.click();

        System.out.println(
                "[QA] Producto seleccionado: " + producto);
    }

    // Abrir carrito

    public void abrirCarrito() {

        // Comprobar si ya estamos en el carrito
        if (!driver.findElements(pantallaCarrito).isEmpty()
                || !driver.findElements(carritoVacio).isEmpty()) {

            System.out.println("[QA] El carrito ya esta abierto");
            return;
        }

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        botonCarrito)).click();

        // Esperar carrito con productos o carrito vacio
        wait.until(d ->
                !d.findElements(pantallaCarrito).isEmpty()
                        || !d.findElements(carritoVacio).isEmpty());

        System.out.println("[QA] Carrito abierto");
    }


    // Seleccionar cantidad del producto
    public void seleccionarCantidad(int unidades) {

        if (unidades < 1) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor a cero");
        }

        By cantidad = By.id(paquete + "noTV");
        By aumentar = By.id(paquete + "plusIV");
        By disminuir = By.id(paquete + "minusIV");

        int cantidadActual = Integer.parseInt(
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                cantidad)).getText());

        while (cantidadActual < unidades) {

            wait.until(
                    ExpectedConditions.elementToBeClickable(
                            aumentar)).click();

            cantidadActual++;

            final String cantidadEsperada =
                    String.valueOf(cantidadActual);

            wait.until(
                    ExpectedConditions.textToBePresentInElementLocated(
                            cantidad, cantidadEsperada));
        }

        while (cantidadActual > unidades) {

            wait.until(
                    ExpectedConditions.elementToBeClickable(
                            disminuir)).click();

            cantidadActual--;

            final String cantidadEsperada =
                    String.valueOf(cantidadActual);

            wait.until(
                    ExpectedConditions.textToBePresentInElementLocated(
                            cantidad, cantidadEsperada));
        }

        String cantidadFinal = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        cantidad)).getText();

        if (!cantidadFinal.equals(String.valueOf(unidades))) {
            throw new AssertionError(
                    "Cantidad incorrecta. Esperado: " + unidades +
                            ", obtenido: " + cantidadFinal);
        }

        System.out.println(
                "[QA] Cantidad seleccionada: " + unidades);
    }

    // Agregar producto al carrito
    public void agregarAlCarrito() {

        By agregar = By.id(paquete + "cartBt");

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        agregar)).click();

        System.out.println(
                "[QA] Se presiono Agregar al carrito");
    }

    // Validar producto y cantidad en el carrito
    public void validarCarrito(String producto, int unidades) {

        if (producto.equals("Sauce Labs Bolt - T-Shirt")) {
            producto = "Sauce Labs Bolt T-Shirt";
        }

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        pantallaCarrito));

        By nombreProducto = By.xpath(
                "//*[@resource-id='" + paquete +
                        "titleTV' and @text='" + producto + "']");

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        nombreProducto));

        By cantidadProducto = By.id(paquete + "noTV");

        String cantidadActual = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        cantidadProducto)).getText();

        if (!cantidadActual.equals(String.valueOf(unidades))) {
            throw new AssertionError(
                    "Cantidad incorrecta. Esperado: " + unidades +
                            ", obtenido: " + cantidadActual);
        }

        By totalArticulos = By.id(paquete + "itemsTV");

        String total = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        totalArticulos)).getText();

        String totalEsperado =
                unidades + (unidades == 1 ? " Item" : " Items");

        if (!total.equals(totalEsperado)) {
            throw new AssertionError(
                    "Total incorrecto. Esperado: " + totalEsperado +
                            ", obtenido: " + total);
        }

        System.out.println(
                "[QA] Carrito validado: " +
                        producto + " - " + unidades + " unidades");
    }

    // Limpiar productos existentes del carrito
    public void limpiarCarrito() {

        abrirCarrito();

        By eliminarProducto = By.id(paquete + "removeBt");



        int intentos = 0;

        while (!driver.findElements(eliminarProducto).isEmpty()) {

            if (intentos >= 20) {
                throw new AssertionError(
                        "No se pudo limpiar completamente el carrito");
            }

            wait.until(
                    ExpectedConditions.elementToBeClickable(
                            eliminarProducto)).click();

            intentos++;

            System.out.println(
                    "[QA] Producto eliminado del carrito");
        }

        System.out.println("[QA] Carrito limpio");
    }

    // Regresar a la galeria de productos
    public void regresarGaleria() {

        driver.navigate().back();

        wait.until(
                ExpectedConditions.textToBePresentInElementLocated(
                        tituloProductos, "Products"));

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        galeriaProductos));

        System.out.println("[QA] Regreso a la galeria");
    }
}
