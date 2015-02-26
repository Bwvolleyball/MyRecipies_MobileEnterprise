package mobileenterprise.brandonward.myrecipies.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobileenterprise.brandonward.myrecipies.domain.Recipe;

/**
 * Created by BrandonWard on 2/10/2015.
 */
public class RecipeSvcSIOImpl implements IRecipeSvc {

    private final static String TAG = "RecipeSvcSIOImpl";
    private Context context = null;
    private List<Recipe> recipes = new ArrayList<Recipe>();
    private final static String FILENAME = "recipes.sio";

    private static RecipeSvcSIOImpl instance = null;

    private RecipeSvcSIOImpl(Context context){
        this.context = context;
        readFile();//populate the contacts list.
    }

    public static RecipeSvcSIOImpl getInstance(Context context){
        if (instance == null){
            instance = new RecipeSvcSIOImpl(context);
        }
        return instance;
    }


    @Override
    public Recipe create(Recipe recipe) {
        recipe.setId(recipes.size());
        Toast.makeText(context, "id: "+recipe.getId(), Toast.LENGTH_SHORT).show();
        recipes.add(recipe);//update the cache
        writeFile();//write the file
        return recipe;
    }

    @Override
    public List<Recipe> retrieve() {
        return recipes;//return the cache
    }

    @Override
    public Recipe update(Recipe recipe) {
        recipes.set(recipe.getId(), recipe);
        writeFile();
        return recipe;
    }

    @Override
    public Recipe delete(Recipe recipe) {
        recipes.remove(recipe.getId());
        for (int i=(recipe.getId()); i<recipes.size();i++){
            Recipe forRecipe = recipes.get(i);
            forRecipe.setId(forRecipe.getId()-1);
            recipes.set(i,forRecipe);
        }
        writeFile();
        return recipe;
    }



    private void readFile(){
        try{
            FileInputStream fis = context.openFileInput(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            recipes = (List<Recipe>) ois.readObject();
            ois.close();
            fis.close();
        } catch(Exception e){
            Log.i(TAG, "Exception " + e.getMessage());
            writeFile();
        }

    }

    private void writeFile(){
        try{
            FileOutputStream fos = context.openFileOutput(FILENAME, context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(recipes);
            oos.flush();
            oos.close();
            fos.close();
        } catch(Exception e){
            Log.i(TAG, "Exception " + e.getMessage());
        }

    }
}
