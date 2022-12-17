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
import javafx.scene.text.*;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import java.security.*;
import java.text.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.time.*;
import java.time.format.*;

public class AdventureGame extends Application {

   private BorderPane root = new BorderPane();

   private final Button runBtn = new Button("Run Run as fast as you can you cant catch me I'm a button O-o");
   private final Button clearBtn = new Button("Clear");
   private final Button printBtn = new Button("Print");
   private final TextArea display = new TextArea();
   private final Label status = new Label("Status");
   private final StringBuilder output = new StringBuilder(128);
   private final Clipboard clipboard = Clipboard.getSystemClipboard();
   private final ClipboardContent content = new ClipboardContent();
   private final NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
   
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
      form = new EntryPane();
   
      form.setInstructions("");
      appTitle = "App";
                 
      items.add("Example only");
   
   } // end setup

   public void run() {
      String mainMenu = "What do you want to do?"; 
      mainMenu += "\n--------------------------------";
      mainMenu += "\n1 - Attack";
      mainMenu += "\n2 - Drink Health Potion";
      mainMenu += "\n3 - Run(If your scared)";
      mainMenu += "\nPress Cancel to quit";
      
      // general variables
      String gameStatus = "Welcome to the Game";
      String menu = "";
      String text = "";
      int healthMax = 100; // for both enemy and player
      int damageMax = 50; // for both enemy and player
      
      // enemy variables
      String enemy; // set randomly
      String enemies[] = {"Skeleton", "Ghoul", "Drowner", "Wraith"};
      int enemyHealth = 0 ; // generate randomly
      int enemyDamageTaken = 0; //set randomly 
      
      // player variables
      int input; 
      int myHealth;
      int myDamageTaken = 0; // set randomly
      int potionCount = 3;
      int potionHealAmmount = 30;
      int potionDropChance = 50; // percent chance a defeated enemy will drop a potion
      
      boolean playerLost = false;
      
      
      START:
      while(true) // Menu loop (outside loop)
      {
         menu = mainMenu;
         enemyHealth = getRandom(healthMax);
         myHealth = getRandom(healthMax);
         enemy = enemies[getRandom(enemies.length)];
         
         while(enemyHealth > 0) // game loop (2nd loop)
         {
            text = String.format("%s%nMy Health: %d\t%s's Health: %d%n%n%s", 
                                 gameStatus, myHealth, enemy, enemyHealth, menu);
            input = inputInt(text);
            if(input == -1)
               break;
            else if(input == 1) // attack
            {
               enemyDamageTaken = getRandom(damageMax);
               myDamageTaken = getRandom(damageMax);
               enemyHealth -= enemyDamageTaken;
               myHealth -= myDamageTaken;   
               
               gameStatus = String.format("You strike the %s for %d damage points." +
                                          "\nYou receive %d damage from %s in retaliation.%n" +
                                          "Potions remaining: %d",
                                          enemy, enemyDamageTaken, myDamageTaken, enemy, potionCount);
               
               if(myHealth < 1) 
               {
                  playerLost = true;
                  break;
               }
            }
            else if(input == 2) //potion 
            {
               if(potionCount > 0)
               {
                  myHealth += potionHealAmmount;
                  potionCount--;
                  
                  gameStatus = String.format("You strike the %s for %d damage points." +
                                             "\nYou receive %d damage from %s in retaliation.%n" +
                                             "Potions remaining: %d",
                                              enemy, enemyDamageTaken, myDamageTaken, enemy, potionCount);
               }
            }
            else if(input == 3) //run
            {
               Random r = new Random(); // make random run away
               int runAway = r.nextInt(10)+0;
               if(runAway < 2)
               {
                  myDamageTaken = getRandom(damageMax);
                  gameStatus = String.format("You failed to run away " + 
                                             "here's %d damage for being slow!", myDamageTaken); 
                  myHealth -= myDamageTaken;
                  
               }
               else
                  outputln("You ran away");
               continue START;
            }
            else
               break;
         } // end game loop
         // gameStatus = playerLost ? "The player lost" : "The player won";
         if(playerLost)
         {
            gameStatus = String.format("The %s won(aka you lost 0_-)  Potions remaining: %d",
                                       enemy, potionCount);
         }
         else
         {
            if(getRandom(100) < potionDropChance)
            {
               potionCount++;
            }
            gameStatus = String.format("You won with %d potions remaining",
                                       potionCount);
         
         }
         menu = "Enter 1 to play again";
         text = String.format("%s%n%s", gameStatus, menu);
         input = inputInt(text);
         if(input == -1) 
            break;
         else if(input == 1)
         {
            enemyHealth = 100;
            gameStatus = String.format("Enemy Health: %d", enemyHealth);
         }
         else
            break;
      } // end play again loop
   
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
