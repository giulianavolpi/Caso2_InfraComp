import java.io.*;
import java.util.*;

public class GeneradorReferencias {

    public static void ejecutar(Scanner scanner) {
        try {
            System.out.print("Ingrese el tamaño de página (en bytes): ");
            int tamPagina = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Ingrese el nombre del archivo BMP (sin la extensión): ");
            String nombreArchivo = scanner.nextLine();

            String rutaRelativa = "Caso2/src/" + nombreArchivo + ".bmp";

            File archivo = new File(rutaRelativa);

            if (!archivo.exists()) {
                System.err.println("El archivo no se encontró en: " + archivo.getAbsolutePath());
                return;
            }

            String rutaFinal = archivo.getPath();
            Imagen imagenIn = new Imagen(rutaFinal);

            int nf = imagenIn.alto;
            int nc = imagenIn.ancho;
            System.out.println("Ancho imagen: " + nc);
            System.out.println("Alto imagen: " + nf);

            List<String> referencias = new ArrayList<>();

            int offset = 0;
            int offsetImagen = offset;
            offset += nf * nc * 3;

            int offsetFiltroX = offset;
            offset += 3 * 3 * 4; // 9 enteros x 4 bytes

            int offsetFiltroY = offset;
            offset += 3 * 3 * 4;

            int offsetRespuesta = offset;
            offset += nf * nc * 3;

            int totalBytes = offset;
            int numPaginas = (int) Math.ceil((double) totalBytes / tamPagina);

            for (int i = 1; i < nf - 1; i++) {
                for (int j = 1; j < nc - 1; j++) {
                    // Accesos a imagen original
                    for (int ki = -1; ki <= 1; ki++) {
                        for (int kj = -1; kj <= 1; kj++) {
                            int x = i + ki;
                            int y = j + kj;
                            for (int canal = 0; canal < 3; canal++) {
                                int pos = offsetImagen + ((x * nc + y) * 3 + canal);
                                int pagina = pos / tamPagina;
                                int desplazamiento = pos % tamPagina;
                                referencias.add(String.format("Imagen[%d][%d].%s,%d,%d,R", x, y, canalRGB(canal),
                                        pagina, desplazamiento));
                            }

                            for (int fj = 0; fj < 3; fj++) {
                                int index = (ki+1) * 3 + kj+1;
                                int posX = offsetFiltroX + index * 4;
                                int paginaX = posX / tamPagina;
                                int despX = posX % tamPagina;
                                referencias.add(String.format("SOBEL_X[%d][%d],%d,%d,R", ki+1, kj+1, paginaX, despX));
                            }

                            for (int fj = 0; fj < 3; fj++) {
                                int index = (ki+1) * 3 + kj+1;
                                int posY = offsetFiltroY + index * 4;
                                int paginaY = posY / tamPagina;
                                int despY = posY % tamPagina;
                                referencias.add(String.format("SOBEL_Y[%d][%d],%d,%d,R", ki+1, kj+1, paginaY, despY));
                            }
                        }
                    }

                    // Escritura en respuesta
                    for (int canal = 0; canal < 3; canal++) {
                        int pos = offsetRespuesta + ((i * nc + j) * 3 + canal);
                        int pagina = pos / tamPagina;
                        int desplazamiento = pos % tamPagina;
                        referencias.add(
                                String.format("Rta[%d][%d].%s,%d,%d,W", i, j, canalRGB(canal), pagina, desplazamiento));
                    }
                }
            }

            String nombreSalida = "referencias_" + nombreArchivo.replace(".bmp", "") + ".txt";
            try (PrintWriter writer = new PrintWriter(new FileWriter(nombreSalida))) {
                writer.println("TP=" + tamPagina);
                writer.println("NF=" + nf);
                writer.println("NC=" + nc);
                writer.println("NR=" + referencias.size());
                writer.println("NP=" + numPaginas);
                for (String ref : referencias) {
                    writer.println(ref);
                }
            }

            System.out.println("Archivo de referencias generado exitosamente: " + nombreSalida);

        } catch (Exception e) {
            System.err.println("Error generando las referencias: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String canalRGB(int canal) {
        switch (canal) {
            case 0:
                return "r";
            case 1:
                return "g";
            case 2:
                return "b";
            default:
                return "?";
        }
    }
}