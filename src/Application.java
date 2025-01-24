import Listener.Listenable;
import OutPuter.OutputAble;
import Progressor.IProcessor;

public class Application implements Runnable {
    private final Listenable listenable;
    private final OutputAble outputAble;
    private final IProcessor Processor;
    private final int numberOfQuery;

    public Application(int numberOfQuery, Listenable listenable, OutputAble outputAble, IProcessor Processor) {
        this.listenable = listenable;
        this.numberOfQuery = numberOfQuery;
        this.outputAble = outputAble;
        this.Processor = Processor;
    }

    @Override
    public void run() {
        String query;
        for (int i = 0; i < numberOfQuery; i++) {
            query = listenable.listen();
            String answer = Processor.Progress(query);
            outputAble.show(answer, i == numberOfQuery - 1);
        }
    }
}
