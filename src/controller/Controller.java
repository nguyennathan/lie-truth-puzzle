package controller;

import model.NotWellFormedException;
import model.Suspect;
import view.View;

import java.util.concurrent.BlockingQueue;

public class Controller {

    private BlockingQueue<Message> queue;
    private View view;
    private Suspect suspect;


    public Controller(BlockingQueue<Message> queue, View view, Suspect suspect) {
        this.queue = queue;
        this.view = view;
        this.suspect = suspect;
    }


    public void mainLoop() {
        while (view.isDisplayable()) {
            Message currMessage = null;
            try {
                currMessage = queue.take();
            }
            catch (InterruptedException exception) {
                exception.printStackTrace();
            }

            if (currMessage.getClass() == AskMessage.class) {
                String question = ((AskMessage) currMessage).getQuestion();
                boolean answer;
                view.setWellFormedErrorLabelText("");
                view.setAnswerLabelText("");
                view.setRevealLabelText("");
                try {
                    answer = suspect.answerQuestion(question);
                    if (answer == true) {
                        view.setAnswerLabelText("Suspect's answer: yes");
                    }
                    else {
                        view.setAnswerLabelText("Suspect's answer: no");
                    }
                }
                catch (NotWellFormedException exception) {
                    view.setWellFormedErrorLabelText("The question is not well-formed!");
                }
                suspect.randomizeTruthful();
            }
            else if (currMessage.getClass() == GuiltyMessage.class) {
                view.setWellFormedErrorLabelText("");
                view.setAnswerLabelText("");
                view.setRevealLabelText("");
                if (suspect.getGuilty() == true) {
                    view.setRevealLabelText("You correctly determined that the suspect is guilty! " +
                            "A new suspect enters (for a different crime).");
                }
                else {
                    view.setRevealLabelText("Incorrect. The suspect is not guilty. " +
                            "A new suspect enters (for a different crime).");
                }
                suspect.randomizeGuilty();
            }
            else if (currMessage.getClass() == NotGuiltyMessage.class) {
                view.setWellFormedErrorLabelText("");
                view.setAnswerLabelText("");
                view.setRevealLabelText("");
                if (suspect.getGuilty() == false) {
                    view.setRevealLabelText("You correctly determined that the suspect is not guilty! " +
                            "A new suspect enters (for a different crime).");
                }
                else {
                    view.setRevealLabelText("Incorrect. The suspect is guilty. " +
                            "A new suspect enters (for a different crime).");
                }
                suspect.randomizeGuilty();
            }
        }
    }

}
