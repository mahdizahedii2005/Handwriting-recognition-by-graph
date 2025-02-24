import Listener.CmdListener;
import OutPuter.CmdOutput;
import Progressor.Processor;

public class Main {
    public static void main(String[] args) {
        CmdListener listener = new CmdListener();
        new Thread(new Application(listener, new CmdOutput(), new Processor())).start();
    }
}
