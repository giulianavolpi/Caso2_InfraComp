
// import java.io.*;
// import java.util.*;

// public class GeneradorReferencias {

//     public static void ejecutar(Scanner scanner) {
//         try {
//             System.out.print("Ingrese el tamaño de página (en bytes): ");
//             int tamPagina = scanner.nextInt();
//             scanner.nextLine();

//             System.out.print("Ingrese el nombre del archivo BMP (incluya la extensión): ");
//             String nombreArchivo = scanner.nextLine();

//             Imagen imagenIn = new Imagen(nombreArchivo);
//             Imagen imagenOut = new Imagen(nombreArchivo);

//             int nf = imagenIn.alto;
//             int nc = imagenIn.ancho;
//             System.out.println("Ancho imagen: " + imagenIn.ancho);
//             System.out.println("Alto imagen: " + imagenIn.alto);

//             // Definimos las dimensiones de los filtros Sobel (3x3)
//             int[][] filtroX = FiltroSobel.SOBEL_X;
//             int[][] filtroY = FiltroSobel.SOBEL_Y;

//             List<String> referencias = new ArrayList<>();

//             int offset = 0;
//             int offsetImagen = offset;
//             int sizeImagen = nf * nc * 3; // 3 canales RGB por pixel
//             offset += sizeImagen;

//             int offsetFiltroX = offset;
//             int sizeFiltro = 3 * 3 * 4; // 3x3 enteros, 4 bytes cada uno
//             offset += sizeFiltro;

//             int offsetFiltroY = offset;
//             offset += sizeFiltro;

//             int offsetRespuesta = offset;
//             int sizeRespuesta = nf * nc * 3;
//             offset += sizeRespuesta;

//             int totalMemoria = offset;
//             int numPaginas = (int) Math.ceil((double) totalMemoria / tamPagina);

//             // Simulamos el método applySobel
//             for (int i = 1; i < nf - 1; i++) {
//                 for (int j = 1; j < nc - 1; j++) {
//                     // Lectura de imagen - 3x3 pixeles, cada uno RGB
//                     for (int ki = -1; ki <= 1; ki++) {
//                         for (int kj = -1; kj <= 1; kj++) {
//                             int x = i + ki;
//                             int y = j + kj;
//                             for (int canal = 0; canal < 3; canal++) {
//                                 int pos = offsetImagen + ((x * nc + y) * 3 + canal);
//                                 int pagina = pos / tamPagina;
//                                 int desplazamiento = pos % tamPagina;
//                                 referencias.add(String.format("Imagen[%d][%d].%s,%d,%d,R", x, y, canalRGB(canal),
//                                         pagina, desplazamiento));
//                             }
//                         }
//                     }

//                     // Lectura de filtros
//                     for (int fi = 0; fi < 3; fi++) {
//                         for (int fj = 0; fj < 3; fj++) {
//                             int index = fi * 3 + fj;
//                             int posX = offsetFiltroX + index * 4;
//                             int paginaX = posX / tamPagina;
//                             int despX = posX % tamPagina;
//                             referencias.add(String.format("SOBEL_X[%d][%d],%d,%d,R", fi, fj, paginaX, despX));

//                             int posY = offsetFiltroY + index * 4;
//                             int paginaY = posY / tamPagina;
//                             int despY = posY % tamPagina;
//                             referencias.add(String.format("SOBEL_Y[%d][%d],%d,%d,R", fi, fj, paginaY, despY));
//                         }
//                     }

//                     // Escritura en la matriz respuesta
//                     for (int canal = 0; canal < 3; canal++) {
//                         int pos = offsetRespuesta + ((i * nc + j) * 3 + canal);
//                         int pagina = pos / tamPagina;
//                         int desplazamiento = pos % tamPagina;
//                         referencias.add(
//                                 String.format("Rta[%d][%d].%s,%d,%d,W", i, j, canalRGB(canal), pagina, desplazamiento));
//                     }
//                 }
//             }

//             String archivoSalida = "referencias_" + nombreArchivo.replace(".bmp", "") + ".txt";
//             try (PrintWriter writer = new PrintWriter(new FileWriter(archivoSalida))) {
//                 writer.println("TP=" + tamPagina);
//                 writer.println("NF=" + nf);
//                 writer.println("NC=" + nc);
//                 writer.println("NR=" + referencias.size());
//                 writer.println("NP=" + numPaginas);
//                 for (String ref : referencias) {
//                     writer.println(ref);
//                 }
//             }

//             System.out.println("Archivo de referencias generado exitosamente: " + archivoSalida);

//         } catch (Exception e) {
//             System.err.println("Error generando las referencias: " + e.getMessage());
//             e.printStackTrace();
//         }
//     }

//     private static String canalRGB(int canal) {
//         switch (canal) {
//             case 0:
//                 return "R";
//             case 1:
//                 return "G";
//             case 2:
//                 return "B";
//             default:
//                 return "";
//         }
//     }
// }

import java.io.*;
import java.util.*;

public class GeneradorReferencias {

    public static void ejecutar(Scanner scanner) {
        try {
            System.out.print("Ingrese el tamaño de página (en bytes): ");
            int tamPagina = scanner.nextInt();
            scanner.nextLine();

            // System.out.print("Ingrese el nombre del archivo BMP (incluya la extensión):
            // ");
            // String nombreArchivo = scanner.nextLine();
            // File archivo = new File(nombreArchivo);
            // System.out.println("¿Existe el archivo? " + archivo.exists());
            // System.out.println("Ruta absoluta: " + archivo.getAbsolutePath());

            // Imagen imagenIn = new Imagen("src/" + nombreArchivo);
            // Imagen imagenOut = new Imagen(nombreArchivo);

            System.out.print("Ingrese el nombre del archivo BMP (incluya la extensión): ");
            String nombreArchivo = scanner.nextLine();

            // Ruta relativa donde están los archivos dentro del proyecto
            String rutaRelativa = "Caso2/src/" + nombreArchivo;

            File archivo = new File(rutaRelativa);

            if (!archivo.exists()) {
                System.err.println("❌ El archivo no se encontró en: " + archivo.getAbsolutePath());
                return;
            }

            // Usamos la ruta válida encontrada
            String rutaFinal = archivo.getPath();
            Imagen imagenIn = new Imagen(rutaFinal);
            Imagen imagenOut = new Imagen(rutaFinal);

            int nf = imagenIn.alto;
            int nc = imagenIn.ancho;
            System.out.println("Ancho imagen: " + nc);
            System.out.println("Alto imagen: " + nf);

            int[][] filtroX = FiltroSobel.SOBEL_X;
            int[][] filtroY = FiltroSobel.SOBEL_Y;

            List<String> referencias = new ArrayList<>();

            // Offsets en memoria virtual
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
                        }
                    }

                    // Accesos a filtros
                    for (int fi = 0; fi < 3; fi++) {
                        for (int fj = 0; fj < 3; fj++) {
                            int index = fi * 3 + fj;

                            int posX = offsetFiltroX + index * 4;
                            int paginaX = posX / tamPagina;
                            int despX = posX % tamPagina;
                            referencias.add(String.format("SOBEL_X[%d][%d],%d,%d,R", fi, fj, paginaX, despX));

                            int posY = offsetFiltroY + index * 4;
                            int paginaY = posY / tamPagina;
                            int despY = posY % tamPagina;
                            referencias.add(String.format("SOBEL_Y[%d][%d],%d,%d,R", fi, fj, paginaY, despY));
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
