package umg.pgm2.form.baseDatos.Services;

import umg.pgm2.form.baseDatos.Dao.ProductoDAO;
import umg.pgm2.form.baseDatos.Model.Producto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProductoService {
    private ProductoDAO productoDAO;

    public ProductoService(Connection connection) {
        this.productoDAO = new ProductoDAO(connection);
    }

    public void crearProducto(Producto producto) throws SQLException {
        productoDAO.insert(producto);
    }

    public Producto obtenerProducto(int id) throws SQLException {
        return productoDAO.findById(id);
    }

    public List<Producto> obtenerTodosLosProductos() throws SQLException {
        return productoDAO.findAll();
    }

    public void actualizarProducto(Producto producto) throws SQLException {
        productoDAO.update(producto);
    }

    public void eliminarProducto(int id) throws SQLException {
        productoDAO.delete(id);
    }
}
