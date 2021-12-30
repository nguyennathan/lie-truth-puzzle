package controller;

public class AskMessage implements Message {

    private String question;


    public AskMessage(String question) {
        this.question = question;
    }


    public String getQuestion() {
        return question;
    }

}
