import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

import static javafx.scene.chart.XYChart.Data;
import static javafx.scene.chart.XYChart.Series;

public class Sort extends Application {
    private Slider slider = new Slider();

    public static void main(String[] args) {
        launch(args);
    }

    private static final int
            BAR_COUNT = 14,
            MAX_BAR_HEIGHT = 50;

    private static final String
            COLOR_ACTIVE = "-fx-bar-fill: #fff",
            COLOR_INITIAL = "-fx-bar-fill: #888",
            COLOR_FINALIZED = "-fx-bar-fill: #190707";

    private static final int
            DELAY_MILLIS = 700;
    private int pointer = 1;
    private int notSwapped = 0;
    private int finished = 0;
    int low = 0;
    int high = 13;


    private ObservableList<Data<String, Number>> bars;
    private BarChart<String, Number> chart;
    private FlowPane inputs;


    //Quicksort Dingen van jesse
    private boolean firstLoop = false;
    private HashMap<String, Integer> myMap;
    private HashMap<String, Integer> emptyMap;
    private boolean checkEmpty = false;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Sorting Animations");
        stage.setWidth(1024);
        stage.setHeight(576);

        final BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10));

        makeChart(pane);
        makeButtons(pane);

        stage.setScene(new Scene(pane));
        stage.show();

        randomizeAll(pane);
    }

    private void makeChart(BorderPane pane) {
        chart = new BarChart<>(new CategoryAxis(), new NumberAxis(0, MAX_BAR_HEIGHT, 0));
        chart.setLegendVisible(false);
        chart.getYAxis().setTickLabelsVisible(false);
        chart.getYAxis().setOpacity(0);
        chart.getXAxis().setTickLabelsVisible(false);
        chart.getXAxis().setOpacity(0);
        chart.setHorizontalGridLinesVisible(false);
        chart.setVerticalGridLinesVisible(false);

        bars = FXCollections.observableArrayList();
        chart.getData().add(new Series<>(bars));

        for (int i = 0; i < BAR_COUNT; i++) {
            Data<String, Number> dataObject = new Data<>(Integer.toString(i + 1), 1);
            bars.add(dataObject); // node will be present after this
            addPainting(dataObject.getNode(), COLOR_INITIAL); // do this after bars.add
        }
        pane.setCenter(chart);
    }


    private void makeButtons(BorderPane pane) {
        inputs = new FlowPane();
        inputs.setHgap(5);
        inputs.setVgap(5);
        createButton("Nieuwe Chart", () -> randomizeAll(pane));
        createButton("Bubblesort start", () -> bubbleSort(pane));
        createButton("Bubblesort per stap", () -> oneStepBubble(pane));
        createButton("Insertionsort start", () -> insertionsort(pane));
        createButton("Insertionsort per stap", () -> oneStepInsertion(pane));
        createButton("Quicksort start", () -> quicksort(pane));
        createButton("Quicksort per stap", () -> oneStepQuick(pane));

        makeSlider(50, 500, 250);

        pane.setBottom(inputs);
    }

    private void makeSlider(int min, int max, int standaard) {
        slider.setMin(min);
        slider.setMax(max);
        slider.setValue(standaard);
        slider.setMajorTickUnit(standaard-min);
        slider.setBlockIncrement(max/10);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);

        inputs.getChildren().add(slider);
    }

    private void makeTopText(BorderPane pane, String text) {
        Text t = new Text();
        t.setText(text);
        BorderPane.setAlignment(pane, Pos.TOP_CENTER);
        pane.setTop(t);

    }

    private void addPainting(Node newNode, String colorInitial) {
        if (newNode != null) {
            newNode.setStyle(colorInitial);
        }
    }

    private void createButton(String label, Runnable method) {
        final Button test = new Button(label);
        test.setOnAction(event -> method.run());
        inputs.getChildren().add(test);
    }

    private void disableButtons(Button button, boolean b) {
        button.setDisable(b);
    }

    private void randomizeAll(BorderPane pane) {
        finished = 0;
        pointer = 1;
        Random random = new Random();
        for (Data<String, Number> bar : bars) {
            bar.setYValue(random.nextInt(MAX_BAR_HEIGHT) + 1);
        }
    }


    /**
     * Bubble Sort algorithm
     */
    private void bubbleSort(BorderPane pane){

        List<Data<String, Number>> list = bars;

        new Timer().schedule(
                new TimerTask() {

                    @Override
                    public void run() {
                        oneStepBubble(pane);

                    }
                }, 0, 550 - (int) slider.getValue());


    }

    private double getValue(List<Data<String, Number>> list, int index) {
        return list.get(index).getYValue().doubleValue();
    }


    /**
     * Bubble Sort one step algorithm
     */
    private void oneStepBubble(BorderPane pane) {
        if (finished != 1) {
            List<Data<String, Number>> list = bars;
            double temp;
            if (getValue(list, pointer - 1) > getValue(list, pointer)) {
                temp = getValue(list, pointer - 1);
                list.get(pointer - 1).setYValue(list.get(pointer).getYValue());
                list.get(pointer).setYValue(temp);
            } else {
                notSwapped += 1;
            }
            if (pointer >= 13) {
                pointer = 1;
                if (notSwapped >= 13) {
                    finished = 1;
                }
                else {
                    notSwapped = 0;
                }
            } else {
                pointer += 1;
            }

        }
    }
    private void insertionsort(BorderPane pane) {
        List<Data<String, Number>> list = bars;

        new Timer().schedule(
                new TimerTask() {

                    @Override
                    public void run() {
                        oneStepInsertion(pane);

                    }
                }, 0, 550 - (int) slider.getValue());


    }


    private void oneStepInsertion(BorderPane pane) {
        if(finished != 1) {
            List<Data<String, Number>> list = bars;
            int j = pointer;
            int temp = (int) getValue(list, pointer);
            int newValue = temp;
            while (j > 0 && getValue(list, j - 1) > newValue) {
                list.get(j).setYValue(getValue(list, j - 1));
                j--;
            }
            list.get(j).setYValue(newValue);
            if (pointer >= 13) {
                finished = 1;
            }
            else {
                pointer++;
            }

        }

    }
    private void quicksort(BorderPane pane) {
        List<Data<String, Number>> list = bars;
        int a = 0;
        int b = list.size() -1;
        quickSort(list, a, b);
    }
    private void oneStepQuick(BorderPane pane) {
        List<Data<String, Number>> list = bars;

        if (firstLoop == false) {
            int a = 0;
            int b = list.size() -1;
            quickSort(list, a, b, true);
            firstLoop = true;
        } else {
            int a = myMap.get("A");
            int b = myMap.get("B");
            int wallIndex = myMap.get("Wallindex");
            quickSort(list, a, wallIndex-1, true);
            quickSort(list, wallIndex+1, b, true);

        }

    }

    public void quickSort(List<Data<String, Number>> list, int a, int b) {

        /**
         * Kijk of A groter of gelijk is aan B. A begint op 0 (laagste index) en B begint op de hoogste index
         * Zie hiervoor ook list.size()-1
         */
        if (a >= b) {
            return;
        }

        /** Bepaal de Pivot voor de quicksort */
        int pivotValue = (int) getValue(list, b);
        int wallIndex = a;

        /**
         * Is er een waarde groter dan de Pivot? Switch!
         * Wanneer een waarde niet groter is dan de pivot, wordt de wall +1 gedaan.
         * */
        for (int i = a; i < b; i++) {
            if(getValue(list, i) < pivotValue) {
                int temp = (int) getValue(list, wallIndex);
                list.get(wallIndex).setYValue(list.get(i).getYValue());
                list.get(i).setYValue(temp);
                wallIndex++;
            }
        }

        /** Zorg dat de Pivot ook geswitched wordt met de "muur" die
         *  gemaakt is in de functie hierbijven (zie wallIndex) */
        if (wallIndex != b) {
            int temp = (int) getValue(list, wallIndex);
            list.get(wallIndex).setYValue(list.get(b).getYValue());
            list.get(b).setYValue(temp);
        }

        /** Alle getallen in de Array moeten bij langs zijn gegaan. 
         *  Pas als als A in beide gevallen hieronder groter is dan B, is hij klaar.
         */
        quickSort(list, a, wallIndex-1);
        quickSort(list, wallIndex+1, b);
    }

    public void quickSort(List<Data<String, Number>> list, int a, int b, boolean perStapDoen) {

        if (a >= b) {
            checkEmpty = true;
            return;
        }

        int pivotValue = (int) getValue(list, b);
        int wallIndex = a;

        for (int i = a; i < b; i++) {
            if(getValue(list, i) < pivotValue) {
                int temp = (int) getValue(list, wallIndex);
                list.get(wallIndex).setYValue(list.get(i).getYValue());
                list.get(i).setYValue(temp);
                wallIndex++;
            }
        }

        if (wallIndex != b) {
            int temp = (int) getValue(list, wallIndex);
            list.get(wallIndex).setYValue(list.get(b).getYValue());
            list.get(b).setYValue(temp);
        }

        System.out.println("A is eerst " + a + ", B is eerst " + b + ", En wallindex is eerst: " + wallIndex);

        myMap = new HashMap<String, Integer>();
        myMap.put("A", a);
        myMap.put("B", b);
        myMap.put("Wallindex", wallIndex);
    }

    private int findNextR(final int l, final int size) {
        List<Data<String, Number>> list = bars;
        for (int i = l; i < size; ++i) {
            if (getValue(list, i) < 0)
                return i;
        }
        return size - 1;
    }

    private int partition(int l, int r) {
        List<Data<String, Number>> list = bars;
        long pivot = (long) getValue(list, ((l+r) / 2));
        while (l <= r) {
            while (getValue(list, r) > pivot)
                r--;
            while (getValue(list, l) < pivot)
                l++;
            if (l <= r) {
                long tmp = (long) getValue(list, r);
                list.get(r).setYValue(l);
                list.get(l).setYValue(tmp);
                l++;
                r--;
            }
        }
        return l;
    }
}