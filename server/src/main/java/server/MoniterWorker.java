package server;

public class MoniterWorker extends Thread{
    
    private Gameserver boss;

    public MoniterWorker(Gameserver server){
        boss = server;
    }

    public void run(){
        System.out.println("Start a moniter worker");
        // keep running
        while (true){
            for (ClientWorker clientWorker: boss.getClientThreads()){
                if (clientWorker.getPlayer()!=null && clientWorker.getSocket().isClosed() && clientWorker.getPlayer().isConnected()){
                    try{
                        sleep(100);
                    } catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    if (clientWorker.getPlayer()!=null && clientWorker.getSocket().isClosed() && clientWorker.getPlayer().isConnected()) {
                        System.out.println("socket is closed of player " + clientWorker.getPlayer().getUsername());
                        clientWorker.getPlayer().setConnected(false);
                        clientWorker.interrupt();  // interrupt from wait()
                    }
                }
            }
        }
    }
}