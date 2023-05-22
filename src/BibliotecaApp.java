import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class BibliotecaApp extends JFrame {
    private JTextField nombreLibroTextField;
    private JTextField idLibroTextField;
    private JTextField paginasLibroTextField;
    private JTextArea resultadoTextArea;
    private JButton agregarLibroButton;
    private JButton eliminarLibroButton;
    private JButton buscarLibroButton;
    private JButton calcularTotalPaginasButton;

    private ArrayList<Libro> libros;

    public BibliotecaApp() {
        libros = new ArrayList<>();

        // Configuración de la interfaz gráfica
        setTitle("Biblioteca App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        JPanel formularioPanel = new JPanel();
        formularioPanel.setLayout(new GridLayout(4, 2));
        formularioPanel.add(new JLabel("Nombre del libro:"));
        nombreLibroTextField = new JTextField();
        formularioPanel.add(nombreLibroTextField);
        formularioPanel.add(new JLabel("ID del libro:"));
        idLibroTextField = new JTextField();
        formularioPanel.add(idLibroTextField);
        formularioPanel.add(new JLabel("Número de páginas:"));
        paginasLibroTextField = new JTextField();
        formularioPanel.add(paginasLibroTextField);
        agregarLibroButton = new JButton("Agregar libro");
        formularioPanel.add(agregarLibroButton);
        eliminarLibroButton = new JButton("Eliminar libro");
        formularioPanel.add(eliminarLibroButton);

        resultadoTextArea = new JTextArea();
        resultadoTextArea.setEditable(false);

        JPanel botonesPanel = new JPanel();
        botonesPanel.setLayout(new FlowLayout());
        buscarLibroButton = new JButton("Buscar libro");
        botonesPanel.add(buscarLibroButton);
        calcularTotalPaginasButton = new JButton("Calcular total de páginas");
        botonesPanel.add(calcularTotalPaginasButton);

        add(formularioPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultadoTextArea), BorderLayout.CENTER);
        add(botonesPanel, BorderLayout.SOUTH);

        // Acciones de los botones
        agregarLibroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarLibro();
            }
        });

        eliminarLibroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarLibro();
            }
        });

        buscarLibroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarLibro();
            }
        });

        calcularTotalPaginasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularTotalPaginas();
            }
        });
    }

    private void agregarLibro() {
        String nombre = nombreLibroTextField.getText();
        int id = Integer.parseInt(idLibroTextField.getText());
        int paginas = Integer.parseInt(paginasLibroTextField.getText());

        Libro nuevoLibro = new Libro(nombre, id, paginas);
        if (!libros.contains(nuevoLibro)) {
            libros.add(nuevoLibro);
            resultadoTextArea.setText("Libro agregado correctamente.");
        } else {
            resultadoTextArea.setText("Error: Ya existe un libro con ese nombre.");
        }
    }

    private void eliminarLibro() {
        String nombre = nombreLibroTextField.getText();
        int id = Integer.parseInt(idLibroTextField.getText());

        libros.removeIf(libro -> libro.getNombre().equals(nombre) || libro.getId() == id);
        resultadoTextArea.setText("Libro(s) eliminado(s) correctamente.");
    }

    private void buscarLibro() {
        String nombre = nombreLibroTextField.getText();
        int id = Integer.parseInt(idLibroTextField.getText());

        if (nombre.isEmpty() && id == 0) {
            resultadoTextArea.setText("Error: Debe proporcionar un nombre o un ID de libro.");
            return;
        }

        if (!nombre.isEmpty()) {
            boolean encontrado = false;
            for (Libro libro : libros) {
                if (libro.getNombre().equals(nombre)) {
                    resultadoTextArea.setText(libro.toString());
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                resultadoTextArea.setText("No se encontró ningún libro con ese nombre.");
            }
        } else {
            Collections.sort(libros);
            int indice = busquedaBinaria(libros, id);
            if (indice != -1) {
                resultadoTextArea.setText(libros.get(indice).toString());
            } else {
                resultadoTextArea.setText("No se encontró ningún libro con ese ID.");
            }
        }
    }

    private int busquedaBinaria(ArrayList<Libro> libros, int id) {
        int inicio = 0;
        int fin = libros.size() - 1;

        while (inicio <= fin) {
            int medio = (inicio + fin) / 2;
            if (libros.get(medio).getId() == id) {
                return medio;
            } else if (libros.get(medio).getId() < id) {
                inicio = medio + 1;
            } else {
                fin = medio - 1;
            }
        }

        return -1;
    }

    private int calcularTotalPaginasRecursivo(ArrayList<Libro> libros, int indice) {
        if (indice == libros.size()) {
            return 0;
        }

        return libros.get(indice).getPaginas() + calcularTotalPaginasRecursivo(libros, indice + 1);
    }

    private void calcularTotalPaginas() {
        int totalPaginas = calcularTotalPaginasRecursivo(libros, 0);
        resultadoTextArea.setText("Cantidad total de páginas en la biblioteca: " + totalPaginas);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                BibliotecaApp bibliotecaApp = new BibliotecaApp();
                bibliotecaApp.setVisible(true);
            }
        });
    }
}

class Libro implements Comparable<Libro> {
    private String nombre;
    private int id;
    private int paginas;

    public Libro(String nombre, int id, int paginas) {
        this.nombre = nombre;
        this.id = id;
        this.paginas = paginas;
    }

    public String getNombre() {
        return nombre;
    }

    public int getId() {
        return id;
    }

    public int getPaginas() {
        return paginas;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Libro libro = (Libro) obj;
        return nombre.equals(libro.nombre);
    }

    @Override
    public int hashCode() {
        return nombre.hashCode();
    }

    @Override
    public int compareTo(Libro libro) {
        return Integer.compare(this.id, libro.id);
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + ", ID: " + id + ", Páginas: " + paginas;
    }
}
