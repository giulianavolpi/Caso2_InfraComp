import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Giuliana volpi-Tomas Ostos-Sebastian Martinez");

        // Menu:
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("\nMenú:");
            System.out.println("1. Generar archivo de referencias");
            System.out.println("2. Simulador de memoria");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    System.out.println("Opción de Generar referencias:");
                    System.out.println("Ingrese el tamaño de página:");
                    int tamPagina = scanner.nextInt();
                    System.out.println("Ingrese el nombre del archivo de la imagen incluyendo la extencion:");
                    String nomImagen = scanner.next();
                    GeneradorReferencias generadorReferencias = new GeneradorReferencias(tamPagina, nomImagen);
                    String nombreArchivoFinal = generadorReferencias.generarReferencias();
                    System.out.println("El path y nombre del archivo es: " + nombreArchivoFinal + "");

                    break;
                case "2":
                    System.out.println("Opción no implementada");

                    break;
                case "3":
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        }

        scanner.close();
        System.out.println("Programa finalizado.");
    }

}
