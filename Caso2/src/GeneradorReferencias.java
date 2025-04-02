import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class GeneradorReferencias {

    private final int tamañoPagina;
    private final String nombreImagen;
    private final Path rutaImagenEntrada;
    private final Path rutaImagenSalida;
    private final Map<String, String> mapaPaginasImagen = new HashMap<>();
    private final Map<String, String> mapaPaginasRespuesta = new HashMap<>();

    public GeneradorReferencias(int tamañoPagina, String nombreImagen) {
        this.tamañoPagina = tamañoPagina;
        this.nombreImagen = nombreImagen;
        String directorioActual = System.getProperty("user.dir");
        this.rutaImagenEntrada = Paths.get(directorioActual + "/Caso2/src/" + nombreImagen);
        System.out.println(rutaImagenEntrada);

        String rutaRelativa = "/Caso2/src/" + nombreImagen;
        this.rutaImagenSalida = Paths.get(directorioActual + rutaRelativa);
    }

    public String generarReferencias() {
        Imagen imagenEntrada = new Imagen(rutaImagenEntrada.toString());
        Imagen imagenSalida = new Imagen(rutaImagenEntrada.toString());
        FiltroSobel filtro = new FiltroSobel(imagenEntrada, imagenSalida);
        filtro.applySobel();

        String rutaImagenProcesada = rutaImagenSalida.toString().replace(".bmp", "_sal.bmp");
        imagenSalida.escribirImagen(rutaImagenProcesada);

        String rutaArchivoSalida = System.getProperty("user.dir") + "/Caso2/ArchivoReferencias-" +
                (nombreImagen.replaceAll("\\.bmp$", "")) + ".txt";

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivoSalida, false))) {
            escribirEncabezado(escritor, imagenEntrada);
            procesarImagen(escritor, imagenEntrada);
            return rutaArchivoSalida;
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo: " + e.getMessage());
            return "Error";
        }
    }

    private void escribirEncabezado(BufferedWriter escritor, Imagen imagen) throws IOException {
        escritor.write("TP=" + tamañoPagina + "\n");
        escritor.write("NF=" + imagen.alto + "\n");
        escritor.write("NC=" + imagen.ancho + "\n");

        int referenciasSobel = ((imagen.alto - 2) * (imagen.ancho - 2) * 81) * (int) Math.ceil(4.0 / tamañoPagina);
        int referenciasTotales = ((imagen.alto - 2) * (imagen.ancho - 2) * 3) + referenciasSobel;
        escritor.write("NR=" + referenciasTotales + "\n");

        int totalPaginas = (int) Math.ceil((2 * (imagen.alto * imagen.ancho * 3) + 72) / (double) tamañoPagina);
        escritor.write("NP=" + totalPaginas + "\n");
    }

    private void procesarImagen(BufferedWriter escritor, Imagen imagen) throws IOException {
        int paginaActual = 0;
        int desplazamientoActual = 0;

        int paginaFiltroX = calcularPaginaFiltro(imagen.alto * imagen.ancho * 3);
        int desplazamientoFiltroX = calcularDesplazamiento(imagen.alto * imagen.ancho * 3, paginaFiltroX);

        int paginaFiltroY = calcularPaginaFiltro(imagen.alto * imagen.ancho * 3 + 36);
        int desplazamientoFiltroY = calcularDesplazamiento(imagen.alto * imagen.ancho * 3 + 36, paginaFiltroY);

        int paginaRespuesta = calcularPaginaFiltro(imagen.alto * imagen.ancho * 3 + 72);
        int desplazamientoRespuesta = calcularDesplazamiento(imagen.alto * imagen.ancho * 3 + 72, paginaRespuesta);

        generarRespuestas(imagen, paginaRespuesta, desplazamientoRespuesta);
        procesarMatrizImagen(escritor, imagen, paginaActual, desplazamientoActual,
                paginaFiltroX, desplazamientoFiltroX,
                paginaFiltroY, desplazamientoFiltroY);
    }

    private int calcularPaginaFiltro(int tamaño) {
        return (int) Math.floor(tamaño / (double) tamañoPagina);
    }

    private int calcularDesplazamiento(int tamaño, int pagina) {
        return tamaño - (pagina * tamañoPagina);
    }

    private void generarRespuestas(Imagen imagen, int paginaRespuesta, int desplazamientoRespuesta) {
        for (int fila = 0; fila < imagen.alto; fila++) {
            for (int columna = 0; columna < imagen.ancho; columna++) {
                for (int canal = 0; canal < 3; canal++) {
                    if (desplazamientoRespuesta >= tamañoPagina) {
                        paginaRespuesta++;
                        desplazamientoRespuesta = 0;
                    }
                    String tipoCanal = obtenerCanal(canal);
                    String clave = String.format("Rta[%d][%d].%s", fila, columna, tipoCanal);
                    String valor = String.format("Rta[%d][%d].%s, %d, %d, W",
                            fila, columna, tipoCanal,
                            paginaRespuesta, desplazamientoRespuesta);

                    mapaPaginasImagen.put(clave, valor);
                    desplazamientoRespuesta++;
                }
            }
        }
    }

    private void procesarMatrizImagen(BufferedWriter escritor, Imagen imagen,
            int pagina, int desplazamiento,
            int paginaFiltroX, int desplazamientoFiltroX,
            int paginaFiltroY, int desplazamientoFiltroY) throws IOException {
        for (int fila = 1; fila < imagen.alto - 1; fila++) {
            for (int columna = 1; columna < imagen.ancho - 1; columna++) {
                procesarVecindario(escritor, imagen, fila, columna, pagina, desplazamiento,
                        paginaFiltroX, desplazamientoFiltroX,
                        paginaFiltroY, desplazamientoFiltroY);
                escribirResultado(escritor, fila, columna);
            }
        }
    }

    private void procesarVecindario(BufferedWriter escritor, Imagen imagen,
            int fila, int columna, int pagina, int desplazamiento,
            int paginaFiltroX, int desplazamientoFiltroX,
            int paginaFiltroY, int desplazamientoFiltroY) throws IOException {
        for (int kfila = -1; kfila <= 1; kfila++) {
            for (int kcolumna = -1; kcolumna <= 1; kcolumna++) {
                procesarPixel(escritor, imagen, fila + kfila, columna + kcolumna, pagina, desplazamiento);
                procesarFiltros(escritor, kfila, kcolumna, paginaFiltroX, desplazamientoFiltroX,
                        paginaFiltroY, desplazamientoFiltroY);
            }
        }
    }

    private void procesarPixel(BufferedWriter escritor, Imagen imagen,
            int fila, int columna, int pagina, int desplazamiento) throws IOException {
        for (int canal = 0; canal < 3; canal++) {
            if (desplazamiento >= tamañoPagina) {
                pagina++;
                desplazamiento = 0;
            }

            String tipoCanal = obtenerCanal(canal);
            String clave = String.format("Imagen[%d][%d].%s", fila, columna, tipoCanal);
            String mensaje = mapaPaginasImagen.get(clave);

            if (mensaje == null) {
                mensaje = String.format("Imagen[%d][%d].%s, %d, %d, R",
                        fila, columna, tipoCanal, pagina, desplazamiento);
                desplazamiento++;
                mapaPaginasImagen.put(clave, mensaje);
            }
            escritor.write(mensaje + "\n");
        }
    }

    private void procesarFiltros(BufferedWriter escritor, int kfila, int kcolumna,
            int paginaFiltroX, int desplazamientoFiltroX,
            int paginaFiltroY, int desplazamientoFiltroY) throws IOException {
        procesarFiltro(escritor, "SOBEL_X", kfila, kcolumna, paginaFiltroX, desplazamientoFiltroX);
        procesarFiltro(escritor, "SOBEL_Y", kfila, kcolumna, paginaFiltroY, desplazamientoFiltroY);
    }

    private void procesarFiltro(BufferedWriter escritor, String tipoFiltro,
            int kfila, int kcolumna, int pagina, int desplazamiento) throws IOException {
        if (desplazamiento + 4 >= tamañoPagina) {
            int espacioRestante = tamañoPagina - desplazamiento;
            if (espacioRestante > 0) {
                escribirFiltro(escritor, tipoFiltro, kfila, kcolumna, pagina, desplazamiento);
                pagina++;
                escribirFiltro(escritor, tipoFiltro, kfila, kcolumna, pagina, 0);
                desplazamiento = desplazamiento + 4 - tamañoPagina;
            } else {
                pagina++;
                desplazamiento = 0;
                escribirFiltro(escritor, tipoFiltro, kfila, kcolumna, pagina, desplazamiento);
                desplazamiento += 4;
            }
        } else {
            escribirFiltro(escritor, tipoFiltro, kfila, kcolumna, pagina, desplazamiento);
            desplazamiento += 4;
        }
    }

    private void escribirFiltro(BufferedWriter escritor, String tipoFiltro,
            int kfila, int kcolumna, int pagina, int desplazamiento) throws IOException {
        String filtro = String.format("%s[%d][%d], %d, %d, R",
                tipoFiltro, 1 + kfila, 1 + kcolumna, pagina, desplazamiento);
        escritor.write(filtro + "\n");
        escritor.write(filtro + "\n");
        escritor.write(filtro + "\n");
    }

    private void escribirResultado(BufferedWriter escritor, int fila, int columna) throws IOException {
        for (int canal = 0; canal < 3; canal++) {
            String tipoCanal = obtenerCanal(canal);
            String clave = String.format("Rta[%d][%d].%s", fila, columna, tipoCanal);
            String respuesta = mapaPaginasImagen.get(clave);
            escritor.write(respuesta + "\n");
        }
    }

    private String obtenerCanal(int canal) {
        switch (canal) {
            case 0:
                return "r";
            case 1:
                return "g";
            case 2:
                return "b";
            default:
                return "";
        }
    }
}