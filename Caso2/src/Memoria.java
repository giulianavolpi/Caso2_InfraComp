import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Memoria {
    
    private int hits;
    private int fallas;
    private int numMarcos;
    private HashMap<String,Pagina> memoria;
    private Queue<String> colaPaginas;

    public Memoria(int numMarcos){
        this.numMarcos = numMarcos;
        this.memoria = new HashMap<>();
        this.colaPaginas = new LinkedList<>();
        this.hits = 0;
        this.fallas = 0;
    }

    public synchronized void buscarPagina(String idPagina, boolean escritura){
        if(memoria.containsKey(idPagina)){
            hits++;
            Pagina pagina = memoria.get(idPagina);
            pagina.setReferenciado(true);
            if(escritura){
                pagina.setModificado(true);
            }
        }
        else{
            fallas++;
            if(memoria.size() >= numMarcos){
                reemplazarPagina(idPagina, escritura);
            }
            else{
                agregarNuevaPagina(idPagina, escritura);
            }
        }
    }

    private void reemplazarPagina(String id, boolean esEscritura) {
        List<String> clase0 = new ArrayList<>();
        List<String> clase1 = new ArrayList<>();
        List<String> clase2 = new ArrayList<>();
        List<String> clase3 = new ArrayList<>();

        for (String pid : colaPaginas) {
            Pagina p = memoria.get(pid);
            if (!p.isReferenciado() && !p.isModificado()) {
                clase0.add(pid);
            } else if (!p.isReferenciado()) {
                clase1.add(pid);
            } else if (!p.isModificado()) {
                clase2.add(pid);
            } else {
                clase3.add(pid);
            }
        }

        String paginaReemplazar = !clase0.isEmpty() ? clase0.get(0) : !clase1.isEmpty() ? clase1.get(0) :!clase2.isEmpty() ? clase2.get(0) :clase3.get(0);

        memoria.remove(paginaReemplazar);
        colaPaginas.remove(paginaReemplazar);
        agregarNuevaPagina(id, esEscritura);
    }


    private void agregarNuevaPagina(String id, boolean escritura) {
        Pagina nuevaPagina = new Pagina(id);
        nuevaPagina.setReferenciado(true);
        nuevaPagina.setModificado(escritura);
        memoria.put(id, nuevaPagina);
        colaPaginas.offer(id);
    }


    public synchronized void resetearBitsReferencia() {
        for (Pagina pagina : memoria.values()) {
            pagina.setReferenciado(false);
        }
    }

    public void mostrarEstadisticas() {
        System.out.println("Total de hits: " + hits);
        System.out.println("Total de fallos de página: " + fallas);
    }

}
