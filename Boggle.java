import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Boggle extends Application {
    private final int M = 4;        // rows
    private final int N = 4;        // columns
    char[][] board = new char[M][N]; // board
    private final String alphabet = "abcdefghijklmnoprstuz"; //alphabet without q,v,w,x,y
    private double Xaxis = 90;
    private double Yaxis = 50;
    private List dict = new ArrayList<String>(); // dictionary
    private List<String> wordList = new ArrayList<String>(); // all correct found words in board
    private int totalCorrect = 0;
    List correctWords = new ArrayList<String>();  // correct

    @Override
    public void start(Stage stage) throws IOException {
        Group root = new Group();
        Scene scene = new Scene(root, 700, 700, Color.WHITE);
        stage.setTitle("Boggle Game");
        stage.setScene(scene);
        stage.show();
        createBoard(scene, root);

        Label label1 = new Label("Answer:");
        label1.setLayoutX(20);
        label1.setLayoutY(13);

        Label label2 = new Label("");
        label2.setLayoutX(20);
        label2.setLayoutY(50);

        Label label3 = new Label("Score: 0");
        label3.setLayoutX(150);
        label3.setLayoutY(300);

        Label label4 = new Label("Correct guessed words: \n");
        label4.setLayoutX(380);
        label4.setLayoutY(10);

        TextField textField = new TextField();
        textField.setLayoutX(100);
        textField.setLayoutY(10);

        Button button1 = new Button("Check");
        button1.setLayoutX(270);
        button1.setLayoutY(10);

        root.getChildren().addAll(textField, label1, button1, label2, label3, label4);

        createDict();
        searchWords();
        button1.setOnAction(e -> label1.setText(textField.getText()));
        button1.setOnAction(e -> {
            String answer = textField.getText();
            if(wordList.contains(answer)) {
                if (correctWords.contains(answer)) {
                    label2.setText("This answer has already been (succesfully) guessed!");
                } else {
                    label2.setText("Your answer: " + answer + " is right!");
                    totalCorrect++;
                    label3.setText("Score: " + totalCorrect);
                    String text = label4.getText();
                    text += "\n" + answer;
                    label4.setText(text);
                    correctWords.add(answer);
                    if (correctWords.size() == wordList.size()) {
                        label2.setText("Gefeliciteerd je hebt alle antwoorden geraden!");
                    }
                }
            }
            else{
                    label2.setText("Your answer: " + answer + " is wrong..");
                }



        });

    }

    private void createBoard(Scene scene, Group root) {
        Random random = new Random();
        for (int n = 0; n < M; n++) {
            Yaxis += 50;
            Xaxis = 90;
            for (int i = 0; i < N; i++) {
                int rand = random.nextInt(20);
                char character = alphabet.charAt(rand);
                board[n][i] = character;
                String str = String.valueOf(character);
                Text t = new Text(Xaxis, Yaxis, str.toUpperCase());
                root.getChildren().add(t);
                Xaxis += 50;
            }
        }
    }

    private void createDict() {
        try {
            File file = new File("C:\\Users\\markshizzle\\Desktop\\Boggle\\src\\dict.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            int i = 0;
            while ((line = bufferedReader.readLine()) != null) {
                String temp = line;
                dict.add(temp);
                i++;
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchWords() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                boolean[][] visited = new boolean[M][N];
                recursiveWordSearch(x, y, "", visited);
            }
        }
    }
     public void recursiveWordSearch(int x, int y, String word, boolean[][] visited) {
        if(visited[x][y] == true) {
            return;
        }

        visited[x][y] = true;
         word += board[x][y];
         if (dict.contains(word) && !wordList.contains(word)) {
             wordList.add(word);
             System.out.println(word);
         }
         int amount = 0;

         for (String item : (Iterable<String>) dict) {
             if (item.startsWith(word)) {
                 amount++;
             }
         }
         if (amount == 0) return;

         if ((x + 1) <= 3) {
             recursiveWordSearch(x + 1, y, word, visited); //move right
         }
         if ((y + 1) <= 3) {
             recursiveWordSearch(x, y + 1, word, visited); //move up
         }
         if ((x - 1) >= 0) {
             recursiveWordSearch(x - 1, y, word, visited); //move left
         }
         if ((y - 1) >= 0) {
             recursiveWordSearch(x, y - 1, word, visited); //move down

         }
         if ((x + 1) <= 3 && (y + 1) <= 3) {
             recursiveWordSearch(x + 1, y + 1, word, visited); //move up-right
         }
         if ((x + 1) <= 3 && (y - 1) >= 0) {
             recursiveWordSearch(x + 1, y - 1, word, visited); //move up-left
         }
         if ((x - 1) >= 0 && (y - 1) >= 0) {
             recursiveWordSearch(x - 1, y - 1, word, visited); //move down-left
         }
         if ((x - 1) >= 0 && (y + 1) <= 3) {
             recursiveWordSearch(x - 1, y + 1, word, visited); //move down-right
         }
     }
}