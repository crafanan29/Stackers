package camapps.stackers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.InputStream;

public class BurgerController {

    // --- 1. Game Logic Instance ---
    private final Burger game = new Burger();

    // --- 2. State for Visual Stacking ---
    private double yOffset = 0.0;
    private final Map<String, Image> imageMap = new HashMap<>();

    // --- 3. FXML Component Links (MUST MATCH play-scene.fxml) ---
    @FXML
    private StackPane burgerAssemblyPlate;

    @FXML
    private Label recipeDisplayLabel;

    @FXML
    private Label messageLabel;

    /**
     * Loads images from the classpath and starts the game.
     */
    @FXML
    public void initialize()
    {
        try {
            // Populate the image map. Keys must match the userData in play-scene.fxml
            imageMap.put("Bottom Bun", loadImage("/camapps/stackers/images/BottomBun.png"));
            imageMap.put("Patty", loadImage("/camapps/stackers/images/Patty.png"));
            imageMap.put("Cheese", loadImage("/camapps/stackers/images/Cheese.png"));
            imageMap.put("Lettuce", loadImage("/camapps/stackers/images/Lettuce.png"));
            imageMap.put("Tomato", loadImage("/camapps/stackers/images/Tomato.png"));
            imageMap.put("Top Bun", loadImage("/camapps/stackers/images/TopBun.png"));
            imageMap.put("Onion", loadImage("/camapps/stackers/images/Onions.png"));
        } catch (Exception e)
        {
            System.err.println("Error loading one or more image resources: " + e.getMessage());
            e.printStackTrace();
            if (messageLabel != null)
            {
                messageLabel.setText("ERROR: Failed to load burger images. Check file paths.");
            }
            return;
        }

        startGame();
    }

    /** Loads an Image object from the classpath. */
    private Image loadImage(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                throw new IllegalArgumentException("Resource not found: " + path);
            }
            return new Image(is);
        } catch (Exception e) {
            throw new RuntimeException("Could not load image at path: " + path, e);
        }
    }

    /** Resets the game state and UI to start a new round. */
    private void startGame()
    {
        game.randomizeRecipe();         //Randomizes recipe each time the game is started.
        game.getMyBurger().clear();
        game.nextIngredient = 0;

        burgerAssemblyPlate.getChildren().clear();
        burgerAssemblyPlate.getChildren().add(new Label("START STACKING HERE"));
        yOffset = 0.0;

        String recipeName = getRecipeName(game);
        ArrayList<String> recipeList = game.getCurrentRecipe();
        recipeDisplayLabel.setText("Order Up: " + recipeName + "\nRequired Sequence: " + recipeList);
        messageLabel.setText("Start stacking the " + recipeName + "!");
    }

    /** Helper method to determine the friendly name of the current recipe. */
    private String getRecipeName(Burger game)
    {
        ArrayList<String> current = game.getCurrentRecipe();
        if (current.equals(game.getCheeseBurger()))
        {
            return "CHEESE BURGER";
        }
        else if (current.equals(game.getDoubleCheeseBurger()))
        {
            return "DOUBLE CHEESE BURGER";
        }
        else if (current.equals(game.getPlainCheeseBurger()))
        {
            return "PLAIN CHEESE BURGER";
        }
        else if (current.equals(game.getPlainBurger()))
        {
            return "PLAIN BURGER";
        }
        return "CUSTOM BURGER";
    }

    /** CORE: Handles the click event for ALL ingredient buttons. */
    @FXML
    private void handleIngredientDrop(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String ingredientName = (String) clickedButton.getUserData();

        boolean isCorrect = game.processDrop(ingredientName);

        if (isCorrect) {
            String nextExpected = (game.nextIngredient < game.getCurrentRecipe().size()) ?
                    game.getCurrentRecipe().get(game.nextIngredient) : "Press ORDER UP!";
            messageLabel.setText("SUCCESS! Next up: " + nextExpected);

            // VISUAL STACKING:
            if (game.getMyBurger().size() == 1)
            {
                burgerAssemblyPlate.getChildren().clear();
            }

            Image ingredientImageSource = imageMap.get(ingredientName);
            ImageView ingredientImage = new ImageView(ingredientImageSource);
            ingredientImage.setFitWidth(200);
            ingredientImage.setFitHeight(100);
            ingredientImage.setTranslateY(yOffset);

            burgerAssemblyPlate.getChildren().add(ingredientImage);

            yOffset -= 30.0;

        }
        else
        {
            String expected = (game.nextIngredient < game.getCurrentRecipe().size()) ?
                    game.getCurrentRecipe().get(game.nextIngredient) :
                    "None (Burger is full)";

            messageLabel.setText("WRONG! Expected " + expected + ". Reset the plate.");
            // Code where if the user presses another button, the program keep telling the user to restart.
            resetButton(event);

        }
    }

    /** Handles the click event for the "Send Burger" (Submit) button. */
    @FXML
    private void submitBurger(ActionEvent event)
    {
        if (game.getCurrentRecipe().size() > game.getMyBurger().size())
        {
            messageLabel.setText("Burger is incomplete! Finish stacking. Needed: ");
            return;
        }

        boolean isMatch = game.checkBurger();

        if (isMatch) {
            messageLabel.setText("PERFECT! Order fulfilled! Starting new game...");
            startGame();
        } else {
            messageLabel.setText("MISTAKE! The stack order or contents are wrong. Starting new game...");
            startGame();
        }
    }

    /** Handles the click event for the "Delete (Reset)" button. */
    @FXML
    private void resetButton(ActionEvent event) {
        messageLabel.setText("Stack Cleared! Start over on the same order.");
        game.getMyBurger().clear();
        game.nextIngredient = 0;
        burgerAssemblyPlate.getChildren().clear();
        burgerAssemblyPlate.getChildren().add(new Label("START STACKING HERE"));
        yOffset = 0.0;
    }
}