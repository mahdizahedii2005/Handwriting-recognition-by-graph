package Progressor;

import Listener.Listenable;

public interface IProcessor {
    String Progress(String query, Listenable listenable);
}
