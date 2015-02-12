package mobileenterprise.brandonward.myrecipies.domain;

import java.io.Serializable;

/**
 * Created by BrandonWard on 2/10/2015.
 */
public class Recipe implements Serializable {

    private String name;
    private String instructions;
    private double servings;
    private double cookTime;
    private String ingredients;
    private int id;

    public Recipe(){

    }
    public Recipe(String name, String instructions, String ingredients, double servings, double cookTime){
        this.name = name;
        this.instructions = instructions;
        this.ingredients = ingredients;
        this.servings = servings;
        this.cookTime = cookTime;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public double getServings() {
        return servings;
    }

    public void setServings(double servings) {
        this.servings = servings;
    }

    public double getCookTime() {
        return cookTime;
    }

    public void setCookTime(double cookTime) {
        this.cookTime = cookTime;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString(){
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
