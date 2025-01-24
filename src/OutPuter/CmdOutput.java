package OutPuter;

public class CmdOutput implements OutputAble {
    @Override
    public void show(String output, boolean check) {
        if (check && !output.isEmpty()) output = output.substring(0, output.length() - 1);
        System.out.print(output);
    }
}
