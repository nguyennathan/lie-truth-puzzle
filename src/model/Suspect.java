package model;

public class Suspect {

    private boolean truthful;
    private boolean guilty;
    private StringEvaluator evaluator;


    public boolean getTruthful() {
        return truthful;
    }
    public boolean getGuilty() {
        return guilty;
    }


    public Suspect(StringEvaluator evaluator) {
        randomizeTruthful();
        randomizeGuilty();
        this.evaluator = evaluator;
    }


    public void randomizeTruthful() {
        truthful = Math.random() >= 0.5;
    }


    public void randomizeGuilty() {
        guilty = Math.random() >= 0.5;
    }


    public boolean answerQuestion(String statement) throws NotWellFormedException {
        boolean statementTruth = evaluator.evaluate(statement, this);
        boolean answer = truthful ? statementTruth : !statementTruth;
        randomizeTruthful();    // intend to randomize after every answer
        return answer;
    }

}
