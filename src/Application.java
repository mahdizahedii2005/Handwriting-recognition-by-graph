import Listener.Listenable;
import OutPuter.OutputAble;
import Progressor.IProcessor;

public class Application implements Runnable {
    private final Listenable listenable;
    private final OutputAble outputAble;
    private final IProcessor Processor;

    public Application(Listenable listenable, OutputAble outputAble, IProcessor Processor) {
        this.listenable = listenable;
        this.outputAble = outputAble;
        this.Processor = Processor;
    }

    @Override
    public void run() {
        String query;
        while (listenable.hasAnyCommand()) {
            query = listenable.listen();
            String answer = Processor.Progress(query, listenable);
            outputAble.show(answer);
        }
    }
}
