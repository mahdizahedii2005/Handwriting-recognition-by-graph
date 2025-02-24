package OutPuter;

public class CmdOutput implements OutputAble {
    @Override
    public void show(String output) {
        System.out.print(output);
    }
}
