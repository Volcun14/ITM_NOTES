package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import Logic.GestorNotas;
import Entities.Nota;
import Entities.Usuario;

public class VentanaPrincipal extends JFrame {

    private JTextField campoBusqueda;
    private JList<String> listaNotas;
    private DefaultListModel<String> modeloLista;
    private JButton btnNueva;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnRecuperar;
    private JButton btnPerfil;
    private JTextArea vistaPrevia;
    private GestorNotas gestorNotas;
    private Usuario usuario;
    private JLabel labelUsuario;

    public VentanaPrincipal() {

        setTitle("Bloc de Notas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        campoBusqueda = new JTextField();
        modeloLista = new DefaultListModel<>();
        listaNotas = new JList<>(modeloLista);
        vistaPrevia = new JTextArea();
        vistaPrevia.setEditable(false);
        btnNueva = new JButton("Nueva Nota");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnRecuperar = new JButton("Recuperar");
        btnPerfil = new JButton("Perfil");
        this.gestorNotas = new GestorNotas();
        this.usuario = new Usuario("U01", "Usuario", "correo@email.com");
        labelUsuario = new JLabel("  " + usuario.getNombre());

        campoBusqueda.setPreferredSize(new Dimension(200, 25));
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.add(new JLabel("Buscar: "));
        panelSuperior.add(campoBusqueda);
        panelSuperior.add(labelUsuario);

        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setPreferredSize(new Dimension(250, 0));
        panelIzquierdo.add(new JScrollPane(listaNotas), BorderLayout.CENTER);

        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.add(new JScrollPane(vistaPrevia), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnNueva);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRecuperar);
        panelBotones.add(btnPerfil);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelIzquierdo, BorderLayout.WEST);
        add(panelDerecho, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        //Listeners
        btnNueva.addActionListener(e -> {
            EditorNota editor = new EditorNota(null, gestorNotas);
            editor.setVisible(true);
            actualizarUI();
        });

        btnEditar.addActionListener(e -> {
            String seleccionada = listaNotas.getSelectedValue();
            if (seleccionada == null) {
                JOptionPane.showMessageDialog(this, "Selecciona una nota primero");
                return;
            }
            String id = seleccionada.split(" - ")[0];
            Nota nota = gestorNotas.ObtenerNotaPorId(id);
            if (nota != null) {
                EditorNota editor = new EditorNota(nota, gestorNotas);
                editor.setVisible(true);
                actualizarUI();
            }
        });

        btnEliminar.addActionListener(e -> {
            int indice = listaNotas.getSelectedIndex();
            if (indice == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una nota primero");
                return;
            }
            int confirmacion = JOptionPane.showConfirmDialog(this, "Eliminar esta nota?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                String id = modeloLista.getElementAt(indice).split(" - ")[0];
                gestorNotas.eliminarNota(id);
                actualizarUI();
            }
        });

        btnRecuperar.addActionListener(e -> {
            Nota recuperada = gestorNotas.recuperarNota();
            if (recuperada == null) {
                JOptionPane.showMessageDialog(this, "No hay notas para recuperar");
                return;
            }
            actualizarUI();
            JOptionPane.showMessageDialog(this, "Nota recuperada: " + recuperada.getTitulo());
        });

        btnPerfil.addActionListener(e -> {
            JTextField campoNombre = new JTextField(usuario.getNombre());
            JTextField campoCorreo = new JTextField(usuario.getCorreo());
            JPanel panel = new JPanel(new GridLayout(2, 2));
            panel.add(new JLabel("Nombre: "));
            panel.add(campoNombre);
            panel.add(new JLabel("Correo: "));
            panel.add(campoCorreo);
            int resultado = JOptionPane.showConfirmDialog(this, panel, "Editar Perfil", JOptionPane.OK_CANCEL_OPTION);
            if (resultado == JOptionPane.OK_OPTION) {
                usuario.setNombre(campoNombre.getText());
                usuario.setCorreo(campoCorreo.getText());
                labelUsuario.setText("  " + usuario.getNombre());
                JOptionPane.showMessageDialog(this, "Perfil actualizado");
            }
        });

        listaNotas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String seleccionada = listaNotas.getSelectedValue();
                if (seleccionada != null) {
                    String id = seleccionada.split(" - ")[0];
                    Nota nota = gestorNotas.ObtenerNotaPorId(id);
                    if (nota != null) {
                        vistaPrevia.setText(nota.getContenido());
                    }
                }
            }
        });

        listaNotas.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                label.setOpaque(true);
                if (!isSelected) {
                    String id = value.toString().split(" - ")[0];
                    Nota nota = gestorNotas.ObtenerNotaPorId(id);
                    if (nota != null && nota.getCategoria() != null) {
                        String hex = nota.getCategoria().getColor();
                        if (hex != null && !hex.equals("null") && hex.startsWith("#")) {
                            try {
                                label.setBackground(Color.decode(hex));
                            } catch (NumberFormatException ex) {
                                label.setBackground(Color.WHITE);
                            }
                        }
                    } else {
                        label.setBackground(Color.WHITE);
                    }
                }
                return label;
            }
        });

        campoBusqueda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String criterio = campoBusqueda.getText();
                if (!criterio.trim().isEmpty()) {
                    mostrarNotas(gestorNotas.buscarNotas(criterio));
                } else {
                    actualizarUI();
                }
            }
        });

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                guardarUsuario();
            }
        });
        
        cargarUsuario();
        actualizarUI();
    }

    //Metodos
    public void mostrarNotas(List<Nota> notas) {
        modeloLista.clear();
        for (Nota nota : notas) {
            String etiqueta = "";
            if (nota.getCategoria() != null && nota.getCategoria().getNombre() != null) {
                String nombreCat = nota.getCategoria().getNombre();
                etiqueta = nombreCat.length() >= 3 ? nombreCat.substring(0, 3).toUpperCase() : nombreCat.toUpperCase();
            }
            modeloLista.addElement(nota.getId() + " - " + nota.getTitulo() + "   " + etiqueta);
        }
    }

    public void actualizarUI() {
        List<Nota> notas = gestorNotas.cargarNotas();
        mostrarNotas(notas);
        listaNotas.repaint();
    }
    
    public void guardarUsuario() {
        try {
            java.io.FileWriter escritor = new java.io.FileWriter("usuario.txt");
            escritor.write(usuario.getNombre() + "\n");
            escritor.write(usuario.getCorreo() + "\n");
            escritor.close();
        } catch (java.io.IOException e) {
            // si falla no hace nada
        }
    }

    public void cargarUsuario() {
        try {
            java.io.BufferedReader lector = new java.io.BufferedReader(new java.io.FileReader("usuario.txt"));
            String nombre = lector.readLine();
            String correo = lector.readLine();
            lector.close();
            if (nombre != null && correo != null) {
                usuario.setNombre(nombre);
                usuario.setCorreo(correo);
                labelUsuario.setText("  " + usuario.getNombre());
            }
        } catch (java.io.IOException e) {
            // si no existe el archivo usa los valores por defecto
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}