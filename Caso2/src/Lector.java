import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Lector extends Thread {

    private Memoria memoria;
    private String nombreArchivo;

    public Lector(Memoria mem, String file){
        this.memoria = mem;
        this.nombreArchivo = file;
    }

    @Override
    public void run(){
        long inicioTiempo = System.nanoTime();
        int contador = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))){
            String line = "";
            for(int i = 0; i<6; i++){
                line = br.readLine();
            }
            while(line!= null){
                String[] partes = line.split(",");
                String idPagina = partes[1];
                boolean escritura = "W".equals(partes[3]);
                memoria.buscarPagina(idPagina, escritura);
                line = br.readLine();
            }

            if(contador%10000==0){
                Thread.sleep(1);
            }
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        memoria.mostrarEstadisticas();
        long finTiempo = System.nanoTime();
        double tiempo = (finTiempo-inicioTiempo)/1e9;
        System.out.println("Tiempo en segundos: "+ tiempo);
    }
}
