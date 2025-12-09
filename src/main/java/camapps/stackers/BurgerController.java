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

    // Game Logic
    private final Burger game = new Burger();

    // State for Visual Stacking
    private double yOffset = 100.0;
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
        try
        {
            // Paths are corrected based on previous debugging
            imageMap.put("Bottom Bun", loadImage("/images/BottomBun.png"));
            imageMap.put("Patty", loadImage("/images/Patty.png"));
            imageMap.put("Cheese", loadImage("/images/Cheese.png"));
            imageMap.put("Lettuce", loadImage("/images/Lettuce.png"));
            imageMap.put("Tomato", loadImage("/images/Tomato.png"));
            imageMap.put("Top Bun", loadImage("/images/TopBun.png"));
            imageMap.put("Onion", loadImage("/images/Onions.png"));
        }
        catch (Exception e)
        {
            System.err.println("FATAL ERROR: Failed to load burger images during initialization.");
            System.err.println(e.getMessage());
            e.printStackTrace();
            if (messageLabel != null)
            {
                messageLabel.setText("FATAL ERROR: Failed to load burger images. Check console for exact file path error!");
            }
            return;
        }

        startGame();
    }

    //Loads an Image object from the classpath.
    private Image loadImage(String path)
    {
        try (InputStream is = getClass().getResourceAsStream(path))
        {
            if (is == null)
            {
                throw new IllegalArgumentException("Resource NOT FOUND at classpath path: " + path);
            }
            return new Image(is);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Could not load image at path: " + path, e);
        }
    }

    //Resets the game state and UI to start a new round.
    private void startGame()
    {
        game.randomizeRecipe();         //Randomizes recipe each time the game is started.
        game.getMyBurger().clear();
        game.nextIngredient = 0;

        burgerAssemblyPlate.getChildren().clear();
        burgerAssemblyPlate.getChildren().add(new Label("BURGER APPEARS HERE"));
        // Reset yOffset to the new starting position
        yOffset = 100.0;

        String recipeName = getRecipeName(game);
        ArrayList<String> recipeList = game.getCurrentRecipe();
        recipeDisplayLabel.setText("Order Up: " + recipeName + "\nRequired Sequence: " + recipeList);
        messageLabel.setText("Start stacking the " + recipeName + "!");
    }

    //Helper method to determine the friendly name of the current recipe.
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
        else if(current.equals(game.getPlainDoubleBurger()))
        {
            return "DOUBLE PLAIN BURGER";
        }
        else if(current.equals(game.getPlainDoubleCheeseBurger()))
        {
            return "DOUBLE PLAIN CHEESE BURGER";
        }
        else if (current.equals(game.getPlainCheeseBurger()))
        {
            return "PLAIN CHEESE BURGER";
        }
        else if (current.equals(game.getPlainBurger()))
        {
            return "PLAIN BURGER";
        }
        else if (current.equals(game.getVeggieBurger()))
        {
            return "VEGGIE BURGER";
        }
        else if(current.equals(game.getLettuceWrapCheeseBurger()))
        {
            return "LETTUCE WRAP CHEESE BURGER";
        }
        return "CUSTOM BURGER";
    }

    //CORE: Handles the click event for ALL ingredient buttons.
    @FXML
    private void handleIngredientDrop(ActionEvent event)
    {
        Button clickedButton = (Button) event.getSource();
        String ingredientName = (String) clickedButton.getUserData();

        // CRITICAL CHECK: Ensure the image map has this ingredient
        if (!imageMap.containsKey(ingredientName))
        {
            messageLabel.setText("INTERNAL ERROR: Image for '" + ingredientName + "' was not loaded. Cannot stack.");
            System.err.println("Image map is missing key: '" + ingredientName + "'. CHECK YOUR INITIALIZE METHOD FOR FAILED IMAGE LOADS.");
            return;
        }

        boolean isCorrect = game.processDrop(ingredientName);

        if (isCorrect)
        {
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

            // UPDATED: Decrement by a smaller amount (20.0) for a tighter stack.
            yOffset -= 20.0;

        }
        else
        {
            String expected = (game.nextIngredient < game.getCurrentRecipe().size()) ?
                    game.getCurrentRecipe().get(game.nextIngredient) :
                    "None (Burger is full)";

            messageLabel.setText("WRONG! Expected " + expected + ". Forced restart.");
            resetGame(); // Calls the internal private reset method
        }
    }

    //Handles the click event for the "Send Burger" (Submit) button.
    @FXML
    private void submitBurger(ActionEvent event)
    {
        if (game.getCurrentRecipe().size() > game.getMyBurger().size())
        {
            // The index of the first missing ingredient is equal to the number of ingredients already stacked
            int nextRequiredIndex = game.getMyBurger().size();

            // Get the name of the missing ingredient from the recipe list
            String missingIngredient = game.getCurrentRecipe().get(nextRequiredIndex);

            messageLabel.setText("BURGER INCOMPLETE! You are currently missing: " + missingIngredient + ". Keep stacking!");
            return;
        }

        boolean isMatch = game.checkBurger();   // Confirms if the user created the correct burger.

        if (isMatch)
        {
            messageLabel.setText("PERFECT! Order fulfilled! Starting new game...");
            startGame();
        } else
        {
            messageLabel.setText("MISTAKE! The stack order or contents are wrong. Starting new game...");
            startGame();
        }
    }

    // Resets the current stack and game state (called internally on error or win/loss)
    private void resetGame()
    {
        messageLabel.setText("Stack Cleared! Start over on the same order.");
        game.getMyBurger().clear();
        game.nextIngredient = 0;
        burgerAssemblyPlate.getChildren().clear();
        burgerAssemblyPlate.getChildren().add(new Label("BURGER GOES HERE"));
        // Reset yOffset to the new starting position
        yOffset = 100.0;
    }
}