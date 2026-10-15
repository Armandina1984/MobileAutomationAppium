
package com.nttdata.stepsdefinitions;

import com.nttdata.steps.CarritoComprasSteps;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CarritoComprasStepsDefs {

    private CarritoComprasSteps steps;
    private String producto;
    private int unidades;

    @And("valido que carguen correctamente los productos en la galeria")
    public void validarGaleria() {
        steps = new CarritoComprasSteps();
        steps.validarGaleria();
    }

    @When("agrego {int} del siguiente producto {string}")
    public void agregarProducto(int cantidad, String nombreProducto) {
        this.producto = nombreProducto;
        this.unidades = cantidad;

        steps.agregarProducto(producto, unidades);
    }

    @Then("valido el carrito de compra actualice correctamente")
    public void validarCarrito() {
        steps.validarCarrito(producto, unidades);
    }
}
