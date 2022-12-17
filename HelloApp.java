import edu.wvc.*;
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.canvas.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import java.security.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.time.*;
import java.time.format.*;

public class HelloApp extends Application {

   private BorderPane root = new BorderPane();

   private final Button runBtn = new Button("Run");
   private final Button clearBtn = new Button("Clear Output");
   private final Button printBtn = new Button("Print");
   private final TextArea display = new TextArea();
   private final Label status = new Label("Status");
   private final StringBuilder output = new StringBuilder(128);
   private final Clipboard clipboard = Clipboard.getSystemClipboard();
   private final ClipboardContent content = new ClipboardContent();
   
   private final Canvas canvas = new Canvas(600,600);

   private SecureRandom random = new SecureRandom();
   private String appTitle;
   private int outputCount;
   private EntryPane form;
   HBox buttonBox = new HBox(10);

   private Parent createContent() {
      root.setPrefSize(800,600);
      root.setPadding(new Insets(10));
   
      display.setPrefColumnCount(50);
      display.setWrapText(true);
      display.setEditable(false);
      display.setStyle("-fx-font-family: monospace;" +
             "-fx-font-weight: bold;" +
             "-fx-font-size: 18;");
   
      runBtn.setOnAction(
         e -> {
            run();
         });
      clearBtn.setOnAction(
         e -> {
            clearOutput();
         });
      printBtn.setOnAction(
         event -> {
            content.putString(display.getText());
            clipboard.setContent(content);
            String filename = appTitle + ".txt";
            try {
               writeToFile(display.getText(),new File(filename));
               getHostServices().showDocument(filename);
            } catch (IOException ex) {
               ex.printStackTrace();
            }
         });
   
      buttonBox.getChildren().addAll(runBtn, clearBtn, printBtn, list);
   
      root.setTop(form);
      root.setCenter(display);
      root.setBottom(status);
      return root;
   } // end createContent()

   @Override
   public void start(Stage stage) {
      setup();
      form.add(buttonBox, 1, form.nextRow());
   
      stage.setTitle(appTitle);
      Scene scene = new Scene(createContent());
      stage.setScene(scene);
      stage.show();
   } // end start()

    // Replace "String" below with the object type that populates the ComboBox
   private final ObservableList<String> items = FXCollections.observableArrayList();
   private final ComboBox<String> list = new ComboBox<>(items);
   
   private void setup() {
      form = new EntryPane("Enter your name", "Enter your age");
   
      form.setInstructions("");
      appTitle = "App";
                 
      items.add("Example only");
   
   } // end setup

   public void run() {
      //String message = input("Enter a message");
      String name = form.getField(0);
      String age = form.getField(1);
      outputln(name);
      outputln(age); 
        
      // Ex of data types
      int ammountOfPizzas = 26; // going to use over byte, short and long
      long shlong = 7; // very large numebr 
      double wage = 13.4;
      char gender = 'F';
      boolean isMarried = true; // t/f
      // Not as important
      byte shong = 5;
      short me = 44;  
      
   } // end run

   // helper methods can go here

   private Optional<String> getDialogText(String prompt) {
      TextInputDialog dialog = new TextInputDialog();
      dialog.setTitle("Dialog");
      dialog.setHeaderText(prompt);
      Optional<String> text = dialog.showAndWait();
      return text;
   }

   private String input(String prompt) {
      Optional<String> text = getDialogText(prompt);
      return text.orElse("");
   }
   
   private int inputInt(String prompt) {
      try {
         return Integer.parseInt(input(prompt));
      } catch (NumberFormatException e) {
         return -1;
      }
   }

   private long inputLong(String prompt) {
      try {
         return Long.parseLong(input(prompt));
      } catch (NumberFormatException e) {
         return -1;
      }
   }

   private double inputDouble(String prompt) {
      try {
         return Double.parseDouble(input(prompt));
      } catch (NumberFormatException e) {
         return -1;
      }
   }

   private float inputFloat(String prompt) {
      try {
         return Float.parseFloat(input(prompt));
      } catch (NumberFormatException e) {
         return -1;
      }
   }

   private char inputChar(String prompt) {
      try {
         return input(prompt).charAt(0);
      } catch (StringIndexOutOfBoundsException e) {
         return " ".charAt(0);
      }
   }

   private String[] getLinesFromFile(String fileName) {
      ArrayList<String> lines = readListFromFile(fileName);
      return lines.toArray(new String[lines.size()]);
   }

   private ArrayList<String> readListFromFile(String fileName) {
      ArrayList<String> linesFromFile = new ArrayList<String>();
      Scanner reader = null;
      try {
         reader = new Scanner(new File(fileName));
         while (reader.hasNextLine()) {
            linesFromFile.add(reader.nextLine());
         }
      } catch (IOException e) {
         showMessage(e.getMessage());
      }
      if (reader != null) reader.close();
      print("Lines read: " + linesFromFile.size());
      return linesFromFile;
   }

   private String getFileAsString(String filename) throws FileNotFoundException, IOException {
      StringBuilder retVal = new StringBuilder();
      BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass()
             .getResourceAsStream(filename)));
      for (String line; (line = br.readLine()) != null; ) {
         retVal.append(line);
         retVal.append("\n");
      }
      return retVal.toString();
   }

   private void writeToFile(String string,File file) throws IOException {
      try (BufferedReader reader = new BufferedReader(new StringReader(string));
          PrintWriter writer = new PrintWriter(new FileWriter(file))
      ) {
         reader.lines().forEach(writer::println);
      }
   }
   
   public String insertLineBreaks(String text, int max) {
      StringBuilder sb = new StringBuilder(text);
      int i = 0;
      while((i = sb.indexOf(" ", i + max)) != -1)
         sb.replace(i, i + 1, "\n");
      return sb.toString();
   }

   private void showMessage(String message) {
      Alert alert = new Alert(AlertType.INFORMATION,message);
      alert.showAndWait();
   }

   private void output(Object value) {
      String stringValue = String.valueOf(value);
      if (stringValue.equals("")) {
         return;
      }
      output.append(stringValue);
      updateOutput();
   }

   private void outputln(Object value) {
      String stringValue = String.valueOf(value);
      if (stringValue.equals("")) {
         return;
      }
      output.append(stringValue).append("\n");
      updateOutput();
   }

   private void outputln() {
      output.append("\n");
      updateOutput();
   }

   private void updateOutput() {
      display.setText(output.toString());
      outputCount++;
   } // end updateOutput()

   private void clearOutput() {
      output.setLength(0);
      outputCount = 0;
      display.setText(output.toString());
   }

   private SecureRandom getRandom() {
      return random;
   } // end getRandom()

   private int getRandom(int max) {
      return random.nextInt(max);
   } // end getRandom()

   private int getRandom(int min,int max) {
      return random.nextInt(max - min + 1) + min;
   } // end getRandom()

   private void println() {
      System.out.println();
   }

   private void print(Object value) {
      System.out.print(value);
   }

   private void println(Object value) {
      System.out.println(value);
   }

   public static void main(String[] args) {
      launch(args);
   } // end main

} // end class
