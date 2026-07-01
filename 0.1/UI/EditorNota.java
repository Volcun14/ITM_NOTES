package UI;

import Entities.Categoria;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Logic.GestorNotas;
import Entities.Nota;
import java.util.Date;

public class EditorNota extends JDialog {

    private JTextField cajaTitulo;
    private JTextArea cajaContenido;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private GestorNotas gestorNotas;
    private Nota notaActual;
    private JTextField cajaNombreCategoria;
    private JButton btnColor;
    private Color colorSeleccionado = Color.WHITE;

    public EditorNota(Nota nota, GestorNotas gestorNotas) {
        this.gestorNotas = gestorNotas;
        this.notaActual = nota;

        setTitle(nota == null ? "Nueva Nota" : "Editar Nota");
        setSize(500, 400);
        setModal(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cajaTitulo = new JTextField();
        cajaContenido = new JTextArea();
        cajaContenido.setLineWrap(true);
        cajaContenido.setWrapStyleWord(true);
        cajaNombreCategoria = new JTextField();
        btnColor = new JButton("Seleccionar");
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.add(new JLabel("Título: "), BorderLayout.WEST);
        panelTitulo.add(cajaTitulo, BorderLayout.CENTER);

        JPanel panelCategoria = new JPanel(new GridLayout(2, 2));
        panelCategoria.add(new JLabel("Categoría: "));
        panelCategoria.add(cajaNombreCategoria);
        panelCategoria.add(new JLabel("Color: "));
        panelCategoria.add(new JLabel("Color: "));
        panelCategoria.add(btnColor);

        JPanel panelNorte = new JPanel(new GridLayout(2, 1));
        panelNorte.add(panelTitulo);
        panelNorte.add(panelCategoria);

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelNorte, BorderLayout.NORTH);
        add(new JScrollPane(cajaContenido), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        if (nota != null) {
            cajaTitulo.setText(nota.getTitulo());
            cajaContenido.setText(nota.getContenido());
            if (nota.getCategoria() != null) {
                cajaNombreCategoria.setText(nota.getCategoria().getNombre());
                String hex = nota.getCategoria().getColor();
                if (hex != null && !hex.equals("null") && hex.startsWith("#")) {
                    try {
                        colorSeleccionado = Color.decode(hex);
                        btnColor.setBackground(colorSeleccionado);
                    } catch (NumberFormatException e) {
                        colorSeleccionado = Color.WHITE;
                    }
                }
            }
        }

        //Listeners
        btnGuardar.addActionListener(e -> guardarCambios());
        btnCancelar.addActionListener(e -> dispose());
        btnColor.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Selecciona un color", colorSeleccionado);
            if (color != null) {
            colorSeleccionado = color;
            btnColor.setBackground(color);
        }
});
    }
    //Metodos
    public void guardarCambios() {
        String titulo = cajaTitulo.getText();
        String contenido = cajaContenido.getText();

        if (titulo.trim().isEmpty() || contenido.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El título y contenido no pueden estar vacíos");
            return;
        }

        String colorHex = String.format("#%02x%02x%02x",
            colorSeleccionado.getRed(),
            colorSeleccionado.getGreen(),
            colorSeleccionado.getBlue());

        Categoria categoria = null;
        if (!cajaNombreCategoria.getText().trim().isEmpty()) {
            categoria = new Categoria(cajaNombreCategoria.getText(), "", colorHex);
        }

        if (notaActual == null) {
            String id = gestorNotas.generarId();
            Nota nueva = new Nota(id, categoria, titulo, contenido, new Date(), new Date(), null);
            gestorNotas.agregarNota(nueva);
        } else {
            notaActual.setCategoria(categoria);
            gestorNotas.editarNota(notaActual.getId(), titulo, contenido);
        }

        limpiarCampos();
        dispose();
}

    public void limpiarCampos() {
        cajaTitulo.setText("");
        cajaContenido.setText("");
    }
}