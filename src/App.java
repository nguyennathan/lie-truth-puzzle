import controller.*;
import model.*;
import view.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class App {
    public static void main(String[] args) {
        BlockingQueue<Message> queue = new LinkedBlockingQueue<>();
        View view = new View(queue);
        StringEvaluator strEval = new StringEvaluator('H', 'G');
        Suspect suspect = new Suspect(strEval);
        Controller controller = new Controller(queue, view, suspect);
        controller.mainLoop();
    }
}
