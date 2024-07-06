package knapsack;

public class Main {
	
    public static void main(String[] args) {
        System.out.println("START");
        //for (int i = 0; i < 30; i++) {

            //long startRun = System.nanoTime();
            
	        ManagerActor mainActor = new ManagerActor();
	        Customer c = new Customer(mainActor.getAddress());
	        c.getAddress().sendMessage(new BootstrapMessage());
	
	        /*while (!c.isExecutionCompleted()) {
	
	            try {
	                Thread.sleep(100);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }*/
	
	        //long endRun = System.nanoTime();
	        //long timeRun = endRun - startRun;
	        //double runTimeInSec = (double) timeRun / 1_000_000_000.0;
	
	        //System.out.print(runTimeInSec + "+");
        
	        //String resultado = "Tempo de execução: " + runTimeInSec + " segundos";
	        //System.out.println(resultado);
        //}    
    }
}
