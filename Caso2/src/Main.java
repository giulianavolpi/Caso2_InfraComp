import java.io.File;
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
                    GeneradorReferencias.ejecutar(scanner);
                    break;
                case "2":
                    System.out.print("Ingrese el número de marcos en memoria: ");
                    int numMarcos = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Ingrese el nombre del archivo de referencias (sin el txt): ");
                    String nombreArchivo = scanner.nextLine()+".txt";
                    System.out.println(new File(nombreArchivo).getAbsolutePath());
                    Memoria memoria = new Memoria(numMarcos);
                    Lector l = new Lector(memoria, nombreArchivo);
                    Actualizador a = new Actualizador(memoria);
                    l.start();
                    a.start();

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
