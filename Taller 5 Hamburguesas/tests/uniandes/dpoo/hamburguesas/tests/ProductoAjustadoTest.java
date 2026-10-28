package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

/**
 * Pruebas unitarias de la clase ProductoAjustado.
 *
 * Escenarios:
 * 1. Producto ajustado sin modificaciones.
 * 2. Producto ajustado con ingredientes agregados (incluso repetidos).
 * 3. Producto ajustado con ingredientes eliminados.
 * 4. Producto ajustado con ingredientes agregados y eliminados.
 */
public class ProductoAjustadoTest
{
    private ProductoMenu base;
    private Ingrediente queso;
    private Ingrediente tocineta;
    private Ingrediente cebolla;
    private ProductoAjustado ajustado;

    @BeforeEach
    void setUp( ) throws Exception
    {
        base = new ProductoMenu( "corral", 14000 );
        queso = new Ingrediente( "queso americano", 2500 );
        tocineta = new Ingrediente( "tocineta express", 2500 );
        cebolla = new Ingrediente( "cebolla", 1000 );
        ajustado = new ProductoAjustado( base );
    }

    @Test
    void testGetNombre( )
    {
        assertEquals( "corral", ajustado.getNombre( ), "El nombre debe ser el del producto base." );
    }

    @Test
    void testGetPrecioSinModificaciones( )
    {
        assertEquals( 14000, ajustado.getPrecio( ), "Sin ajustes, el precio debe ser el del producto base." );
    }

    @Test
    void testGetPrecioConAgregados( )
    {
        ajustado.agregarIngrediente( queso );
        ajustado.agregarIngrediente( tocineta );
        assertEquals( 14000 + 2500 + 2500, ajustado.getPrecio( ), "El precio debe sumar el costo de los ingredientes agregados." );
    }

    @Test
    void testGetPrecioConIngredienteRepetido( )
    {
        ajustado.agregarIngrediente( queso );
        ajustado.agregarIngrediente( queso );
        assertEquals( 14000 + 2 * 2500, ajustado.getPrecio( ), "Un ingrediente agregado dos veces debe cobrarse dos veces." );
    }

    @Test
    void testGetPrecioConEliminados( )
    {
        ajustado.eliminarIngrediente( cebolla );
        assertEquals( 14000, ajustado.getPrecio( ), "Eliminar ingredientes no debe cambiar el precio." );
    }

    @Test
    void testGetPrecioConAgregadosYEliminados( )
    {
        ajustado.agregarIngrediente( queso );
        ajustado.eliminarIngrediente( cebolla );
        assertEquals( 16500, ajustado.getPrecio( ), "Solo los ingredientes agregados deben cambiar el precio." );
    }

    @Test
    void testNoModificaProductoBase( )
    {
        ajustado.agregarIngrediente( queso );
        assertEquals( 14000, base.getPrecio( ), "Ajustar un producto no debe modificar el producto base." );
    }

    @Test
    void testGenerarTextoFacturaSinModificaciones( )
    {
        String esperado = "corral\n" + "            14000\n";
        assertEquals( esperado, ajustado.generarTextoFactura( ), "El texto de la factura no es el esperado." );
    }

    @Test
    void testGenerarTextoFacturaConAjustes( )
    {
        ajustado.agregarIngrediente( queso );
        ajustado.eliminarIngrediente( cebolla );

        String esperado = "corral\n" + "    +queso americano                2500\n" + "    -cebolla\n" + "            16500\n";
        assertEquals( esperado, ajustado.generarTextoFactura( ), "El texto de la factura no es el esperado." );
    }
}
