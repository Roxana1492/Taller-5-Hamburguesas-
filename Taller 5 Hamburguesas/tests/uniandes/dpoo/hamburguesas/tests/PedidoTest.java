package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

/**
 * Pruebas de la clase Pedido
 */
public class PedidoTest
{
    private Pedido pedido;
    private ProductoMenu corral;
    private ProductoMenu papas;
    private ProductoMenu gaseosa;

    /**
     * Archivo donde se guarda la factura en las pruebas
     */
    private File archivoFactura;

    @BeforeEach
    void setUp( ) throws Exception
    {
        pedido = new Pedido( "Ana", "Calle 19 #1-23" );
        corral = new ProductoMenu( "corral", 14000 );
        papas = new ProductoMenu( "papas medianas", 5500 );
        gaseosa = new ProductoMenu( "gaseosa", 5000 );
        archivoFactura = new File( "./data/pruebas/factura_prueba.txt" );
    }

    @AfterEach
    void tearDown( ) throws Exception
    {
        // Se borra la factura creada en la prueba
        if( archivoFactura.exists( ) )
        {
            archivoFactura.delete( );
        }
    }

    /**
     * Agrega al pedido: un corral (14000), un corral con queso y sin cebolla (16500) y un combo corral (22050)
     */
    private void agregarVariosProductos( )
    {
        pedido.agregarProducto( corral );

        ProductoAjustado corralConQueso = new ProductoAjustado( corral );
        corralConQueso.agregarIngrediente( new Ingrediente( "queso americano", 2500 ) );
        corralConQueso.eliminarIngrediente( new Ingrediente( "cebolla", 1000 ) );
        pedido.agregarProducto( corralConQueso );

        ArrayList<ProductoMenu> items = new ArrayList<ProductoMenu>( );
        items.add( corral );
        items.add( papas );
        items.add( gaseosa );
        pedido.agregarProducto( new Combo( "combo corral", 0.1, items ) );
    }

    /**
     * Lee todo el contenido de un archivo de texto
     */
    private String leerArchivo( File archivo ) throws IOException
    {
        String contenido = "";
        BufferedReader br = new BufferedReader( new FileReader( archivo ) );
        String linea = br.readLine( );
        while( linea != null )
        {
            contenido += linea + "\n";
            linea = br.readLine( );
        }
        br.close( );
        return contenido;
    }

    @Test
    void testGetNombreCliente( )
    {
        assertEquals( "Ana", pedido.getNombreCliente( ), "El nombre del cliente no es el esperado." );
    }

    @Test
    void testIdConsecutivo( )
    {
        Pedido otroPedido = new Pedido( "Luis", "Carrera 7" );
        assertEquals( pedido.getIdPedido( ) + 1, otroPedido.getIdPedido( ), "Los id de los pedidos deben ser consecutivos." );
    }

    @Test
    void testIdDiferente( )
    {
        Pedido otroPedido = new Pedido( "Ana", "Calle 19 #1-23" );
        assertNotEquals( pedido.getIdPedido( ), otroPedido.getIdPedido( ), "Dos pedidos no pueden tener el mismo id." );
    }

    @Test
    void testPrecioPedidoVacio( )
    {
        assertEquals( 0, pedido.getPrecioTotalPedido( ), "Un pedido vacío debe costar 0." );
    }

    @Test
    void testPrecioTotalUnProducto( )
    {
        pedido.agregarProducto( corral );
        // 14000 + IVA (2660) = 16660
        assertEquals( 16660, pedido.getPrecioTotalPedido( ), "El precio total debe incluir el IVA." );
    }

    @Test
    void testPrecioTotalVariosProductos( )
    {
        agregarVariosProductos( );
        // Neto: 14000 + 16500 + 22050 = 52550
        // IVA: 52550 * 0.19 = 9984
        // Total: 62534
        assertEquals( 62534, pedido.getPrecioTotalPedido( ), "El precio total del pedido no es el esperado." );
    }

    @Test
    void testAgregarProductoRepetido( )
    {
        pedido.agregarProducto( gaseosa );
        pedido.agregarProducto( gaseosa );
        // Neto 10000 + IVA 1900
        assertEquals( 11900, pedido.getPrecioTotalPedido( ), "Se debe poder pedir el mismo producto dos veces." );
    }

    @Test
    void testFacturaPedidoVacio( )
    {
        String esperado = "Cliente: Ana\n" + "Dirección: Calle 19 #1-23\n" + "----------------\n" + "----------------\n" + "Precio Neto:  0\n"
                + "IVA:          0\n" + "Precio Total: 0\n";
        assertEquals( esperado, pedido.generarTextoFactura( ), "La factura del pedido vacío no es la esperada." );
    }

    @Test
    void testFacturaVariosProductos( )
    {
        agregarVariosProductos( );

        String esperado = "Cliente: Ana\n" + "Dirección: Calle 19 #1-23\n" + "----------------\n" + "corral\n" + "            14000\n" + "corral\n"
                + "    +queso americano                2500\n" + "    -cebolla\n" + "            16500\n" + "Combo combo corral\n" + " Descuento: 0.1\n"
                + "            22050\n" + "----------------\n" + "Precio Neto:  52550\n" + "IVA:          9984\n" + "Precio Total: 62534\n";
        assertEquals( esperado, pedido.generarTextoFactura( ), "La factura del pedido no es la esperada." );
    }

    @Test
    void testGuardarFactura( ) throws Exception
    {
        agregarVariosProductos( );

        pedido.guardarFactura( archivoFactura );

        assertTrue( archivoFactura.exists( ), "El archivo de la factura debería existir." );
        assertEquals( pedido.generarTextoFactura( ), leerArchivo( archivoFactura ), "El archivo no tiene el texto de la factura." );
    }

    @Test
    void testGuardarFacturaDosVeces( ) throws Exception
    {
        // Primero se guarda la factura vacía y luego con un producto
        pedido.guardarFactura( archivoFactura );
        pedido.agregarProducto( corral );
        pedido.guardarFactura( archivoFactura );

        assertEquals( pedido.generarTextoFactura( ), leerArchivo( archivoFactura ), "El archivo debe quedar con la última factura." );
    }

    @Test
    void testGuardarFacturaCarpetaNoExiste( )
    {
        File archivoMalo = new File( "./carpeta_que_no_existe/factura.txt" );
        try
        {
            pedido.guardarFactura( archivoMalo );
            fail( "Debería lanzar FileNotFoundException." );
        }
        catch( FileNotFoundException e )
        {
            // Esto es lo esperado
        }
    }
}
