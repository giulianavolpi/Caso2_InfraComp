public class Actualizador extends Thread{
    private Memoria memoria;

    public Actualizador(Memoria mem){
        this.memoria = mem;
    }

    @Override
    public void run(){
        while(true){
            try {
                Thread.sleep(1);
                memoria.resetearBitsReferencia();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
