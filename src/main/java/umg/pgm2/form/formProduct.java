package umg.pgm2.form;

import umg.pgm2.form.baseDatos.Conexion.DatabaseConnection;
import umg.pgm2.form.baseDatos.Model.Producto;
import umg.pgm2.form.baseDatos.Services.ProductoService;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

public class formProduct {
    private JPanel JFrameProducto;
    private JLabel lblTitulo;
    private JLabel lblCodigo;
    private JTextField textFieldIdProducto;
    private JLabel lblDescripcion;
    private JTextField textFieldDescripcion;
    private JLabel lblOrigen;
    private JComboBox<String> comboBoxOrigen;
    private JButton buttonGrabar;
    private JButton buttonBuscar;
    private JButton buttonActualizar;
    private JButton buttonEliminar;
    private JButton buttonLimpiar;

    private ProductoService productoService;

    public formProduct() {
        initComponents();
        initDatabaseConnection();
    }

    private void initComponents() {
        comboBoxOrigen.addItem("Seleccione un origen");
        comboBoxOrigen.addItem("Japon");
        comboBoxOrigen.addItem("EEUU");
        comboBoxOrigen.addItem("China");
        comboBoxOrigen.addItem("España");

        buttonGrabar.addActionListener(e -> guardarProducto());
        buttonBuscar.addActionListener(e -> buscarProducto());
        buttonActualizar.addActionListener(e -> actualizarProducto());
        buttonEliminar.addActionListener(e -> eliminarProducto());
        buttonLimpiar.addActionListener(e -> limpiarCampos());
    }

    private void initDatabaseConnection() {
        try {
            Connection connection = DatabaseConnection.getConnection();
            productoService = new ProductoService(connection);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    private void guardarProducto() {
        String descripcion = textFieldDescripcion.getText();
        String origen = (String) comboBoxOrigen.getSelectedItem();

        if (!validarCampos(descripcion, origen)) return;

        try {
            Producto nuevoProducto = new Producto(0, descripcion, origen);
            productoService.crearProducto(nuevoProducto);
            JOptionPane.showMessageDialog(null, "Producto guardado con éxito. ID: " + nuevoProducto.getIdProducto());
            limpiarCampos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el producto: " + e.getMessage());
        }
    }

    private void buscarProducto() {
        String idStr = textFieldIdProducto.getText();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID de producto.");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Producto producto = productoService.obtenerProducto(id);
            if (producto != null) {
                mostrarProducto(producto);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un producto con ese ID.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID válido.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar el producto: " + e.getMessage());
        }
    }

    private void actualizarProducto() {
        String idStr = textFieldIdProducto.getText();
        String descripcion = textFieldDescripcion.getText();
        String origen = (String) comboBoxOrigen.getSelectedItem();

        if (!validarCampos(descripcion, origen)) return;

        try {
            int id = Integer.parseInt(idStr);
            Producto producto = new Producto(id, descripcion, origen);
            productoService.actualizarProducto(producto);
            JOptionPane.showMessageDialog(null, "Producto actualizado con éxito.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID válido.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el producto: " + e.getMessage());
        }
    }

    private void eliminarProducto() {
        String idStr = textFieldIdProducto.getText();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID de producto.");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            int confirmacion = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar este producto?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                productoService.eliminarProducto(id);
                JOptionPane.showMessageDialog(null, "Producto eliminado con éxito.");
                limpiarCampos();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID válido.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el producto: " + e.getMessage());
        }
    }

    private boolean validarCampos(String descripcion, String origen) {
        if (descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese una descripción.");
            return false;
        }
        if (origen.equals("Seleccione un origen")) {
            String nuevoOrigen = JOptionPane.showInputDialog(null, "Por favor, ingrese un origen válido:");
            if (nuevoOrigen == null || nuevoOrigen.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un origen válido.");
                return false;
            }
            comboBoxOrigen.addItem(nuevoOrigen);
            comboBoxOrigen.setSelectedItem(nuevoOrigen);
        }
        return true;
    }

    private void mostrarProducto(Producto producto) {
        textFieldIdProducto.setText(String.valueOf(producto.getIdProducto()));
        textFieldDescripcion.setText(producto.getDescripcion());
        comboBoxOrigen.setSelectedItem(producto.getOrigen());
    }

    private void limpiarCampos() {
        textFieldIdProducto.setText("");
        textFieldDescripcion.setText("");
        comboBoxOrigen.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("formProduct");
            frame.setContentPane(new formProduct().JFrameProducto);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}