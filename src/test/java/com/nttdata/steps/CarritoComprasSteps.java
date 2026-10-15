
package com.nttdata.steps;

import com.nttdata.screens.CarritoComprasScreen;

public class CarritoComprasSteps {

    private final CarritoComprasScreen screen;

    public CarritoComprasSteps() {
        screen = new CarritoComprasScreen();
    }


    public void validarGaleria() {
        screen.limpiarCarrito();
        screen.regresarGaleria();
        screen.validarGaleria();
    }


    public void agregarProducto(String producto, int unidades) {
        screen.seleccionarProducto(producto);
        screen.seleccionarCantidad(unidades);
        screen.agregarAlCarrito();
    }

    public void validarCarrito(String producto, int unidades) {
        screen.abrirCarrito();
        screen.validarCarrito(producto, unidades);
    }
}
