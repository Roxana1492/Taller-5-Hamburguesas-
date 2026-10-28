package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

/**
 * Pruebas unitarias de la clase Combo.
 *
 * Escenarios:
 * 1. "combo corral": corral (14000) + papas medianas (5500) + gaseosa (5000) = 24500 con 10% de descuento.
 * 2. Combo sin descuento.
 * 3. Combo con 100% de descuento.
 */
public class ComboTest
{
    private ArrayList<ProductoMenu> items;
    private Combo comboCorral;

    @BeforeEach
    void setUp( ) throws Exception
    {
        items = new ArrayList<ProductoMenu>( );
        items.add( new ProductoMenu( "corral", 14000 ) );
        items.add( new ProductoMenu( "papas medianas", 5500 ) );
        items.add( new ProductoMenu( "gaseosa", 5000 ) );
        comboCorral = new Combo( "combo corral", 0.1, items );
    }

    @Test
    void testGetNombre( )
    {
        assertEquals( "combo corral", comboCorral.getNombre( ), "El nombre del combo no es el esperado." );
    }

    @Test
    void testGetPrecioConDescuento( )
    {
        // 24500 - 10% = 22050
        assertEquals( 22050, comboCorral.getPrecio( ), "El precio debe ser la suma de los productos menos el descuento." );
    }

    @Test
    void testGetPrecioSinDescuento( )
    {
        Combo sinDescuento = new Combo( "combo normal", 0, items );
        assertEquals( 24500, sinDescuento.getPrecio( ), "Sin descuento, el precio debe ser la suma de los productos." );
    }

    @Test
    void testGetPrecioDescuentoTotal( )
    {
        Combo gratis = new Combo( "combo gratis", 1, items );
        assertEquals( 0, gratis.getPrecio( ), "Con 100% de descuento el combo debe ser gratis." );
    }

    @Test
    void testGetPrecioComboVacio( )
    {
        Combo vacio = new Combo( "combo vacío", 0.1, new ArrayList<ProductoMenu>( ) );
        assertEquals( 0, vacio.getPrecio( ), "Un combo sin productos debe costar 0." );
    }

    @Test
    void testCambiarListaOriginal( )
    {
        items.add( new ProductoMenu( "helado", 3000 ) );
        assertEquals( 22050, comboCorral.getPrecio( ), "Modificar la lista original no debe afectar al combo." );
    }

    @Test
    void testGenerarTextoFactura( )
    {
        String esperado = "Combo combo corral\n" + " Descuento: 0.1\n" + "            22050\n";
        assertEquals( esperado, comboCorral.generarTextoFactura( ), "El texto de la factura no es el esperado." );
    }
}
