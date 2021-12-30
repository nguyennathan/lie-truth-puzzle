package view;

import controller.AskMessage;
import controller.GuiltyMessage;
import controller.Message;
import controller.NotGuiltyMessage;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.BlockingQueue;


public class View extends JFrame {

    private BlockingQueue<Message> queue;
    private JTextField questionField;
    private JLabel wellFormedErrorLabel;
    private JLabel answerLabel;
    private JLabel revealLabel;

    public View(BlockingQueue<Message> queue) {
        this.queue = queue;


        JLabel objectiveLabel = new JLabel(
                "<html>Objective: Determine if a crime suspect is guilty or not by asking them \"well-formed\" " +
                "questions. In response to each question, the suspect either decides to lie or tell the truth.</html>");
        objectiveLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel symbolsLabel = new JLabel("<html>Let H be the statement \"you will answer this question truthfully\"." +
                "<br>Let G be the statement \"you are guilty\".</html>");
        symbolsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton detailsButton = new JButton("Click for Details");
        detailsButton.addActionListener(e -> {
            openDetails();
        });
        detailsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.X_AXIS));
        questionPanel.add(new JLabel("Question to ask suspect: \"Is it true that "));
        questionField = new JTextField();
        questionPanel.add(questionField);
        questionPanel.add(new JLabel("?\""));
        questionPanel.setMaximumSize(new Dimension(500,20));
        questionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        wellFormedErrorLabel = new JLabel();
        wellFormedErrorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton askButton = new JButton("ASK");
        askButton.addActionListener(e -> {
            try {
                queue.put(new AskMessage(questionField.getText()));
            }
            catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        });
        askButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        answerLabel = new JLabel();
        answerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel verdictPanel = new JPanel();
        JButton guiltyButton = new JButton("GUILTY");
        guiltyButton.addActionListener(e -> {
            try {
                queue.put(new GuiltyMessage());
            }
            catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        });
        JButton notGuiltyButton = new JButton("NOT GUILTY");
        notGuiltyButton.addActionListener(e -> {
            try {
                queue.put(new NotGuiltyMessage());
            }
            catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        });
        verdictPanel.setLayout(new BoxLayout(verdictPanel, BoxLayout.X_AXIS));
        verdictPanel.add(new JLabel("Your verdict: "));
        verdictPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        verdictPanel.add(guiltyButton);
        verdictPanel.add(Box.createRigidArea(new Dimension(15, 10)));
        verdictPanel.add(notGuiltyButton);

        revealLabel = new JLabel();
        revealLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        JPanel wholePanel = new JPanel();
        wholePanel.setLayout(new BoxLayout(wholePanel, BoxLayout.Y_AXIS));
        wholePanel.add(objectiveLabel);
        wholePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        wholePanel.add(symbolsLabel);
        wholePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        wholePanel.add(detailsButton);
        wholePanel.add(Box.createRigidArea(new Dimension(0, 50)));
        wholePanel.add(questionPanel);
        wholePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        wholePanel.add(wellFormedErrorLabel);
        wholePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        wholePanel.add(askButton);
        wholePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        wholePanel.add(answerLabel);
        wholePanel.add(Box.createRigidArea(new Dimension(0, 30)));
        wholePanel.add(verdictPanel);
        wholePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        wholePanel.add(revealLabel);

        this.add(wholePanel);


        this.setSize(new Dimension(650,400));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

    }

    public void setWellFormedErrorLabelText(String str) {
        wellFormedErrorLabel.setText(str);
    }


    public void setAnswerLabelText(String str) {
        answerLabel.setText(str);
    }


    public void setRevealLabelText(String str) {
        revealLabel.setText(str);
    }


    public void openDetails() {
        JFrame detailsFrame = new JFrame("Details");
        JPanel detailsPanel = new JPanel();
        JLabel wellFormedDefinition = new JLabel("<html>Definition of well-formed statement:<br>" +
                "X is a well-formed statement if and only if one of the following is true:<br><br>" +
                "    X=H<br>" +
                "    X=G<br>" +
                "    X=(!A), for some well-formed statement A. It means \"not A\".<br>" +
                "    X=(A&&B), for some well-formed statements A,B. It means \"A and B\".<br>" +
                "    X=(A||B), for some well-formed statements A,B. It means \"A or B\".<br><br>" +
                "Mind the parentheses. (A||B) might be well-formed, but A||B is not.</html>");
        JLabel trueFalseClarification = new JLabel("<html>Clarification on lies and truths: The suspect either lies or tells the truth. That may sound tautological,\n" +
                "but in this game, truths and lies are defined to be intentional. If the suspect decides to lie, " +
                "they first determine the question's actual answer, and then they say the opposite. It is " +
                "possible for a person to give a yes or no response that is neither a truth nor a lie. " +
                "E.g. they let a coin flip determine their response.</html>");
        detailsPanel.add(wellFormedDefinition);
        detailsPanel.add(Box.createVerticalGlue());
        detailsPanel.add(trueFalseClarification);
        detailsPanel.add(Box.createRigidArea(new Dimension(3, 20)));

        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        detailsFrame.add(detailsPanel);

        detailsFrame.setSize(new Dimension(500, 350));
        detailsFrame.setVisible(true);
    }


}
