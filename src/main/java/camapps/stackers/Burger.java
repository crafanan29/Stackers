package camapps.stackers;
import java.util.ArrayList;
import java.util.Random;

public class Burger
{
    // The Random object is correctly final.
    private final Random random = new Random();

    // Creating an ArrayList object
    private final ArrayList<String> myBurger = new ArrayList<>(); // The user's stack

    private final ArrayList<String> cheeseBurger;
    private final ArrayList<String> plainBurger;
    private final ArrayList<String> doubleCheeseBurger;
    private final ArrayList<String> plainCheeseBurger;
    private final ArrayList<String> plainDoubleBurger;
    private final ArrayList<String> plainDoubleCheeseBurger;
    private final ArrayList<String> veggieBurger; // Easter Egg Burger
    private final ArrayList<String> lettuceWrapCheeseBurger;

    private final ArrayList<ArrayList<String>> allRecipes;

    // STATE VARIABLES:
    private ArrayList<String> currentRecipe;  // The recipe the player is currently building

    public int nextIngredient;        // Tracks the next required ingredient position

    public Burger() // Default Constructor to add content to the ArrayLists
    {               // and initialize the ArrayLists
        // Populating cheeseBurger
        this.cheeseBurger = new ArrayList<>();
        this.cheeseBurger.add("Bottom Bun"); this.cheeseBurger.add("Patty"); this.cheeseBurger.add("Cheese");
        this.cheeseBurger.add("Onion");this.cheeseBurger.add("Lettuce"); this.cheeseBurger.add("Tomato");
        this.cheeseBurger.add("Top Bun");

        // Populating plainBurger
        this.plainBurger = new ArrayList<>();
        this.plainBurger.add("Bottom Bun"); this.plainBurger.add("Patty"); this.plainBurger.add("Top Bun");

        // Populate doubleCheeseBurger
        this.doubleCheeseBurger = new ArrayList<>();
        this.doubleCheeseBurger.add("Bottom Bun"); this.doubleCheeseBurger.add("Patty"); this.doubleCheeseBurger.add("Cheese");
        this.doubleCheeseBurger.add("Patty"); this.doubleCheeseBurger.add("Cheese"); this.doubleCheeseBurger.add("Onion");
        this.doubleCheeseBurger.add("Lettuce"); this.doubleCheeseBurger.add("Tomato");this.doubleCheeseBurger.add("Top Bun");

        //Populate plainCheeseBurger
       this.plainCheeseBurger = new ArrayList<>();
       this.plainCheeseBurger.add("Bottom Bun"); this.plainCheeseBurger.add("Patty");
       this.plainCheeseBurger.add("Cheese"); this.plainCheeseBurger.add("Top Bun");

       //Populate plainDoubleBurger
        this.plainDoubleBurger = new ArrayList<>();
        this.plainDoubleBurger.add("Bottom Bun"); this.plainDoubleBurger.add("Patty");
        this.plainDoubleBurger.add("Patty"); this.plainDoubleBurger.add("Top Bun");

        //Populate plainDoubleCheeseBurger
        this.plainDoubleCheeseBurger = new ArrayList<>();
        this.plainDoubleCheeseBurger.add("Bottom Bun"); this.plainDoubleCheeseBurger.add("Patty");
        this.plainDoubleCheeseBurger.add("Cheese"); this.plainDoubleCheeseBurger.add("Patty");
        this.plainDoubleCheeseBurger.add("Cheese"); this.plainDoubleCheeseBurger.add("Top Bun");

        // Populate veggieBurger
        this.veggieBurger = new ArrayList<>();
        this.veggieBurger.add("Bottom Bun"); this.veggieBurger.add("Lettuce");
        this.veggieBurger.add("Onion"); this.veggieBurger.add("Tomato"); this.veggieBurger.add("Top Bun");

        //Populate lettuceWrapCheeseBurger
        this.lettuceWrapCheeseBurger = new ArrayList<>();
        this.lettuceWrapCheeseBurger.add("Lettuce"); this.lettuceWrapCheeseBurger.add("Patty"); this.lettuceWrapCheeseBurger.add("Cheese");
        this.lettuceWrapCheeseBurger.add("Onion"); this.lettuceWrapCheeseBurger.add("Tomato"); this.lettuceWrapCheeseBurger.add("Lettuce");

       // Populating allRecipes with all known Burger Recipes.
        this.allRecipes = new ArrayList<>();
        this.allRecipes.add(this.cheeseBurger);
        this.allRecipes.add(this.plainBurger);
        this.allRecipes.add(this.doubleCheeseBurger);
        this.allRecipes.add(this.plainCheeseBurger);
        this.allRecipes.add(this.plainDoubleBurger);
        this.allRecipes.add(this.plainDoubleCheeseBurger);
        this.allRecipes.add(this.veggieBurger);
        this.allRecipes.add(this.lettuceWrapCheeseBurger);

        // GENERATE RANDOM RECIPE TO MAKE
        this.randomizeRecipe();

        // Reset game state
        this.nextIngredient = 0;
        this.myBurger.clear();  // Makes sure that the stack is empty.
    }

    public void randomizeRecipe()
    {
        int randomRecipe = random.nextInt(this.allRecipes.size());
        this.currentRecipe = this.allRecipes.get(randomRecipe);
    }

    /**
     * Returns the recipe randomly selected for the current game session.
     * This method is crucial for the Controller to know the target recipe.
     */
    public ArrayList<String> getCurrentRecipe()
    {
        return currentRecipe;
    }

    // Getter for the user's stack (useful for display)
    public ArrayList<String> getMyBurger()
    {
        return myBurger;
    }

    // Getter methods for fixed recipes (used by the Controller's helper method)
    public ArrayList<String> getCheeseBurger()
    {
        return cheeseBurger;
    }

    public ArrayList<String> getPlainBurger()
    {
        return plainBurger;
    }

    public ArrayList<String> getDoubleCheeseBurger()
    {
        return doubleCheeseBurger;
    }

    public ArrayList<String> getPlainCheeseBurger()
    {
        return plainCheeseBurger;
    }

    public ArrayList<String> getPlainDoubleBurger()
    {
        return plainDoubleBurger;
    }

    public ArrayList<String> getPlainDoubleCheeseBurger()
    {
        return plainDoubleCheeseBurger;
    }

    public ArrayList<String> getVeggieBurger() { return veggieBurger;}

    public ArrayList<String> getLettuceWrapCheeseBurger() { return lettuceWrapCheeseBurger;}

    /**
     * Checks if the dropped ingredient matches the next expected ingredient in the sequence.
     * This method runs during assembly.
     */
    public boolean processDrop(String droppedIngredientName)
    {
        if (nextIngredient >= currentRecipe.size())
        {
            System.out.println("Burger is already finished! Press Send Burger to score.");
            return false;
        }

        String expectedIngredient = currentRecipe.get(nextIngredient);

        if (droppedIngredientName.equalsIgnoreCase(expectedIngredient)) {
            // Note: In the final app, these system prints will be replaced by GUI updates
            System.out.println("MATCH! Added: " + droppedIngredientName);

            this.myBurger.add(droppedIngredientName); // Add to the user's stack
            nextIngredient++;                     // Advance the pointer

            return true;
        } else {
            System.out.println("MISMATCH! Expected " + expectedIngredient + ", but got " + droppedIngredientName);
            return false;
        }
    }

    /**
     * Checks if the final burger stack matches the target recipe exactly.
     */
    public boolean checkBurger()
    {
        // Removed the redundant local variable 'targetRecipe'
        boolean isMatch = this.myBurger.equals(this.currentRecipe);

        if(isMatch)
        {
            System.out.println("It's a match");
        }
        else
        {
            System.out.println("Does not match");
        }
        return isMatch;
    }
}
