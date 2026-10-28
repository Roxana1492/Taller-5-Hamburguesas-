package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.excepciones.IngredienteRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoFaltanteException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

/**
 * Pruebas de la clase Restaurante. Los archivos de prueba están en la carpeta data/pruebas
 */
public class RestauranteTest
{
    private Restaurante restaurante;

    @BeforeEach
    void setUp( ) throws Exception
    {
        restaurante = new Restaurante( );
    }

    /**
     * Carga los archivos de prueba que no tienen errores
     */
    private void cargarArchivosBuenos( ) throws Exception
    {
        File ingredientes = new File( "./data/pruebas/ingredientes_ok.txt" );
        File menu = new File( "./data/pruebas/menu_ok.txt" );
        File combos = new File( "./data/pruebas/combos_ok.txt" );
        restaurante.cargarInformacionRestaurante( ingredientes, menu, combos );
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

    /**
     * Retorna el archivo donde queda la factura de un pedido
     */
    private File archivoFactura( Pedido p )
    {
        return new File( "./facturas/factura_" + p.getIdPedido( ) + ".txt" );
    }

    // ----------------------------------------------
    // Restaurante nuevo
    // ----------------------------------------------

    @Test
    void testRestauranteVacio( )
    {
        assertNull( restaurante.getPedidoEnCurso( ), "No debería haber pedido en curso." );
        assertEquals( 0, restaurante.getPedidos( ).size( ), "No debería haber pedidos." );
        assertEquals( 0, restaurante.getIngredientes( ).size( ), "No debería haber ingredientes." );
        assertEquals( 0, restaurante.getMenuBase( ).size( ), "No debería haber productos." );
        assertEquals( 0, restaurante.getMenuCombos( ).size( ), "No debería haber combos." );
    }

    // ----------------------------------------------
    // Cargar información
    // ----------------------------------------------

    @Test
    void testCargarArchivosDelTaller( ) throws Exception
    {
        File ingredientes = new File( "./data/ingredientes.txt" );
        File menu = new File( "./data/menu.txt" );
        File combos = new File( "./data/combos.txt" );
        restaurante.cargarInformacionRestaurante( ingredientes, menu, combos );

        assertEquals( 15, restaurante.getIngredientes( ).size( ), "No se cargaron todos los ingredientes." );
        assertEquals( 22, restaurante.getMenuBase( ).size( ), "No se cargaron todos los productos." );
        assertEquals( 4, restaurante.getMenuCombos( ).size( ), "No se cargaron todos los combos." );
    }

    @Test
    void testCargarIngredientes( ) throws Exception
    {
        cargarArchivosBuenos( );

        // El archivo tiene una línea vacía que no se debe tener en cuenta
        assertEquals( 3, restaurante.getIngredientes( ).size( ), "La cantidad de ingredientes no es la esperada." );

        Ingrediente primero = restaurante.getIngredientes( ).get( 0 );
        assertEquals( "lechuga", primero.getNombre( ) );
        assertEquals( 1000, primero.getCostoAdicional( ) );

        Ingrediente ultimo = restaurante.getIngredientes( ).get( 2 );
        assertEquals( "queso americano", ultimo.getNombre( ) );
        assertEquals( 2500, ultimo.getCostoAdicional( ) );
    }

    @Test
    void testCargarMenu( ) throws Exception
    {
        cargarArchivosBuenos( );

        assertEquals( 3, restaurante.getMenuBase( ).size( ), "La cantidad de productos no es la esperada." );

        ProductoMenu primero = restaurante.getMenuBase( ).get( 0 );
        assertEquals( "corral", primero.getNombre( ) );
        assertEquals( 14000, primero.getPrecio( ) );

        ProductoMenu ultimo = restaurante.getMenuBase( ).get( 2 );
        assertEquals( "gaseosa", ultimo.getNombre( ) );
        assertEquals( 5000, ultimo.getPrecio( ) );
    }

    @Test
    void testCargarCombos( ) throws Exception
    {
        cargarArchivosBuenos( );

        assertEquals( 2, restaurante.getMenuCombos( ).size( ), "La cantidad de combos no es la esperada." );

        Combo comboCorral = restaurante.getMenuCombos( ).get( 0 );
        assertEquals( "combo corral", comboCorral.getNombre( ) );
        // (14000 + 5500 + 5000) con 10% de descuento = 22050
        assertEquals( 22050, comboCorral.getPrecio( ), "El precio del combo no es el esperado." );

        Combo comboPequeno = restaurante.getMenuCombos( ).get( 1 );
        assertEquals( "combo pequeño", comboPequeno.getNombre( ) );
        // (14000 + 5000) con 9.5% de descuento = 17195
        assertEquals( 17195, comboPequeno.getPrecio( ), "El precio del combo no es el esperado." );
    }

    @Test
    void testIngredienteRepetido( ) throws Exception
    {
        File ingredientes = new File( "./data/pruebas/ingredientes_repetidos.txt" );
        File menu = new File( "./data/pruebas/menu_ok.txt" );
        File combos = new File( "./data/pruebas/combos_ok.txt" );
        try
        {
            restaurante.cargarInformacionRestaurante( ingredientes, menu, combos );
            fail( "Debería lanzar IngredienteRepetidoException." );
        }
        catch( IngredienteRepetidoException e )
        {
            assertEquals( "El ingrediente lechuga está repetido", e.getMessage( ) );
        }
    }

    @Test
    void testProductoRepetido( ) throws Exception
    {
        File ingredientes = new File( "./data/pruebas/ingredientes_ok.txt" );
        File menu = new File( "./data/pruebas/menu_repetido.txt" );
        File combos = new File( "./data/pruebas/combos_ok.txt" );
        try
        {
            restaurante.cargarInformacionRestaurante( ingredientes, menu, combos );
            fail( "Debería lanzar ProductoRepetidoException." );
        }
        catch( ProductoRepetidoException e )
        {
            assertEquals( "El producto corral está repetido", e.getMessage( ) );
        }
    }

    @Test
    void testComboRepetido( ) throws Exception
    {
        File ingredientes = new File( "./data/pruebas/ingredientes_ok.txt" );
        File menu = new File( "./data/pruebas/menu_ok.txt" );
        File combos = new File( "./data/pruebas/combos_repetidos.txt" );
        try
        {
            restaurante.cargarInformacionRestaurante( ingredientes, menu, combos );
            fail( "Debería lanzar ProductoRepetidoException." );
        }
        catch( ProductoRepetidoException e )
        {
            assertEquals( "El producto combo corral está repetido", e.getMessage( ) );
        }
    }

    @Test
    void testComboConProductoQueNoExiste( ) throws Exception
    {
        File ingredientes = new File( "./data/pruebas/ingredientes_ok.txt" );
        File menu = new File( "./data/pruebas/menu_ok.txt" );
        File combos = new File( "./data/pruebas/combos_producto_faltante.txt" );
        try
        {
            restaurante.cargarInformacionRestaurante( ingredientes, menu, combos );
            fail( "Debería lanzar ProductoFaltanteException." );
        }
        catch( ProductoFaltanteException e )
        {
            assertEquals( "El producto malteada no aparece en la información del restaurante", e.getMessage( ) );
        }
    }

    @Test
    void testPrecioNoEsNumero( ) throws Exception
    {
        File ingredientes = new File( "./data/pruebas/ingredientes_formato_invalido.txt" );
        File menu = new File( "./data/pruebas/menu_ok.txt" );
        File combos = new File( "./data/pruebas/combos_ok.txt" );
        try
        {
            restaurante.cargarInformacionRestaurante( ingredientes, menu, combos );
            fail( "Debería lanzar NumberFormatException." );
        }
        catch( NumberFormatException e )
        {
            // Esto es lo esperado
        }
    }

    @Test
    void testArchivoNoExiste( ) throws Exception
    {
        File ingredientes = new File( "./data/pruebas/no_existe.txt" );
        File menu = new File( "./data/pruebas/menu_ok.txt" );
        File combos = new File( "./data/pruebas/combos_ok.txt" );
        try
        {
            restaurante.cargarInformacionRestaurante( ingredientes, menu, combos );
            fail( "Debería lanzar FileNotFoundException." );
        }
        catch( FileNotFoundException e )
        {
            // Esto es lo esperado
        }
    }

    // ----------------------------------------------
    // Pedidos
    // ----------------------------------------------

    @Test
    void testIniciarPedido( ) throws Exception
    {
        restaurante.iniciarPedido( "Ana", "Calle 19" );

        assertNotNull( restaurante.getPedidoEnCurso( ), "Debería haber un pedido en curso." );
        assertEquals( "Ana", restaurante.getPedidoEnCurso( ).getNombreCliente( ) );
    }

    @Test
    void testIniciarPedidoConOtroEnCurso( ) throws Exception
    {
        restaurante.iniciarPedido( "Ana", "Calle 19" );
        try
        {
            restaurante.iniciarPedido( "Luis", "Carrera 7" );
            fail( "Debería lanzar YaHayUnPedidoEnCursoException." );
        }
        catch( YaHayUnPedidoEnCursoException e )
        {
            assertEquals( "Ya existe un pedido en curso, para el cliente Ana así que no se puede crear un pedido para Luis", e.getMessage( ) );
        }
        // El pedido en curso debe seguir siendo el de Ana
        assertEquals( "Ana", restaurante.getPedidoEnCurso( ).getNombreCliente( ) );
    }

    @Test
    void testCerrarSinPedidoEnCurso( ) throws Exception
    {
        try
        {
            restaurante.cerrarYGuardarPedido( );
            fail( "Debería lanzar NoHayPedidoEnCursoException." );
        }
        catch( NoHayPedidoEnCursoException e )
        {
            assertEquals( "Actualmente no hay un pedido en curso", e.getMessage( ) );
        }
    }

    @Test
    void testCerrarYGuardarPedido( ) throws Exception
    {
        cargarArchivosBuenos( );
        restaurante.iniciarPedido( "Ana", "Calle 19" );
        Pedido pedido = restaurante.getPedidoEnCurso( );
        pedido.agregarProducto( restaurante.getMenuBase( ).get( 0 ) );
        pedido.agregarProducto( restaurante.getMenuCombos( ).get( 0 ) );

        restaurante.cerrarYGuardarPedido( );

        File factura = archivoFactura( pedido );
        assertNull( restaurante.getPedidoEnCurso( ), "Después de cerrar no debería haber pedido en curso." );
        assertTrue( factura.exists( ), "Se debería crear el archivo de la factura." );
        assertEquals( pedido.generarTextoFactura( ), leerArchivo( factura ), "El archivo no tiene la factura del pedido." );

        factura.delete( );
    }

    @Test
    void testPedidosCerradosSeGuardan( ) throws Exception
    {
        restaurante.iniciarPedido( "Ana", "Calle 19" );
        Pedido primero = restaurante.getPedidoEnCurso( );
        restaurante.cerrarYGuardarPedido( );

        restaurante.iniciarPedido( "Luis", "Carrera 7" );
        Pedido segundo = restaurante.getPedidoEnCurso( );
        restaurante.cerrarYGuardarPedido( );

        assertEquals( 2, restaurante.getPedidos( ).size( ), "Los pedidos cerrados se deberían guardar." );
        assertEquals( primero, restaurante.getPedidos( ).get( 0 ) );
        assertEquals( segundo, restaurante.getPedidos( ).get( 1 ) );

        archivoFactura( primero ).delete( );
        archivoFactura( segundo ).delete( );
    }

    @Test
    void testNuevoPedidoDespuesDeCerrar( ) throws Exception
    {
        restaurante.iniciarPedido( "Ana", "Calle 19" );
        Pedido primero = restaurante.getPedidoEnCurso( );
        restaurante.cerrarYGuardarPedido( );

        restaurante.iniciarPedido( "Luis", "Carrera 7" );

        assertEquals( "Luis", restaurante.getPedidoEnCurso( ).getNombreCliente( ), "Se debería poder iniciar otro pedido." );
        assertEquals( 1, restaurante.getPedidos( ).size( ), "El pedido en curso no debería estar en la lista de pedidos." );

        archivoFactura( primero ).delete( );
    }
}
