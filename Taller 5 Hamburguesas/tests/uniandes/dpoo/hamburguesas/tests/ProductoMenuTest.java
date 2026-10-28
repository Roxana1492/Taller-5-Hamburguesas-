package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

/**
 * Pruebas unitarias de la clase ProductoMenu.
 *
 * Escenario: un producto "corral" con precio base 14000 y un producto de precio 0.
 */
public class ProductoMenuTest
{
    private ProductoMenu corral;
    private ProductoMenu gratis;

    @BeforeEach
    void setUp( ) throws Exception
    {
        corral = new ProductoMenu( "corral", 14000 );
        gratis = new ProductoMenu( "agua de la llave", 0 );
    }

    @Test
    void testGetNombre( )
    {
        assertEquals( "corral", corral.getNombre( ), "El nombre del producto no es el esperado." );
        assertEquals( "agua de la llave", gratis.getNombre( ), "El nombre del producto no es el esperado." );
    }

    @Test
    void testGetPrecio( )
    {
        assertEquals( 14000, corral.getPrecio( ), "El precio del producto no es el esperado." );
        assertEquals( 0, gratis.getPrecio( ), "El precio de un producto gratis debería ser 0." );
    }

    @Test
    void testGenerarTextoFactura( )
    {
        String esperado = "corral\n" + "            14000\n";
        assertEquals( esperado, corral.generarTextoFactura( ), "El texto de la factura no es el esperado." );
    }

    @Test
    void testGenerarTextoFacturaContieneNombreYPrecio( )
    {
        String texto = gratis.generarTextoFactura( );
        assertTrue( texto.contains( "agua de la llave" ), "La factura debe incluir el nombre del producto." );
        assertTrue( texto.contains( "0" ), "La factura debe incluir el precio del producto." );
        assertTrue( texto.endsWith( "\n" ), "El texto de la factura debe terminar en salto de línea." );
    }
}
