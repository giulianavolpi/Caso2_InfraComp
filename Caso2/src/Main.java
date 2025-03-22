import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("\n==== MENÚ ====");
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
                    System.out.println("Opción aún no implementada.");
                    break;
                case "3":
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }

        scanner.close();
        System.out.println("Programa finalizado.");
    }
}
