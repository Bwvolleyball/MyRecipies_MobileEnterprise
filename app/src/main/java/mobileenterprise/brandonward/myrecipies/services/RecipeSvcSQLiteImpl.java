package mobileenterprise.brandonward.myrecipies.services;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.util.Log;

import java.util.*;

import mobileenterprise.brandonward.myrecipies.domain.Recipe;

/**
 * Created by BrandonWard on 3/10/2015.
 */
public class RecipeSvcSQLiteImpl extends SQLiteOpenHelper implements  IRecipeSvc {

    private static final String TAG = "RecipeSvcSQLiteImpl";
    private static final String DB_NAME="recipes.db";
    private static final int DB_VERSION = 1;
    private static final String CREATE_DB="CREATE TABLE recipe " +
            "(_id integer primary key autoincrement, recipe_name text, instructions text, ingredients text, servings double, cook_time double)";//TODO: Add the rest of my columns.
    private static final String[] COLUMNS = {"_id", "recipe_name", "instructions", "ingredients", "servings", "cook_time"};
    private SQLiteDatabase db = null;
    private static RecipeSvcSQLiteImpl instance = null;

    public static RecipeSvcSQLiteImpl getInstance(Context context){
        if (instance == null){
            instance = new RecipeSvcSQLiteImpl(context);
        }
        return instance;
    }

    private RecipeSvcSQLiteImpl(Context context){
        super(context, DB_NAME,null,DB_VERSION);
        db=getWritableDatabase();
    }
    @Override
    public Recipe create(Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put(COLUMNS[1], recipe.getName());
        values.put(COLUMNS[2], recipe.getInstructions());
        values.put(COLUMNS[3], recipe.getIngredients());
        values.put(COLUMNS[4], recipe.getServings());
        values.put(COLUMNS[5], recipe.getCookTime());
        int id =(int) db.insert("recipe", null, values);
        recipe.setId(id);
        return recipe;
    }

    @Override
    public List<Recipe> retrieve() {
        List<Recipe> recipes = new ArrayList<Recipe>();
        Cursor cursor = db.query("recipe",COLUMNS,null,null,null,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Recipe recipe = getRecipe(cursor);
            recipes.add(recipe);
            cursor.moveToNext();
        }
        cursor.close();
        return recipes;
    }

    private Recipe getRecipe(Cursor cursor){
        Recipe recipe = new Recipe();
        recipe.setId(cursor.getInt(0));
        recipe.setName(cursor.getString(1));
        recipe.setInstructions(cursor.getString(2));
        recipe.setIngredients(cursor.getString(3));
        recipe.setServings(cursor.getDouble(4));
        recipe.setCookTime(cursor.getDouble(5));
        return recipe;
    }

    @Override
    public Cursor retrieveCursor() {
        Log.i(TAG, "Entering retrieveCursor");
        Cursor cursor = db.query("recipe",COLUMNS,null,null,null,null,null);
        cursor.moveToFirst();
        return cursor;
    }

    @Override
    public Recipe update(Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put(COLUMNS[1], recipe.getName());
        values.put(COLUMNS[2], recipe.getInstructions());
        values.put(COLUMNS[3], recipe.getIngredients());
        values.put(COLUMNS[4], recipe.getServings());
        values.put(COLUMNS[5], recipe.getCookTime());
        db.update("recipe",values, "_id = ?", new String[]{String.valueOf(recipe.getId())});
        return recipe;
    }

    @Override
    public Recipe delete(Recipe recipe) {
        db.delete("recipe","_id "+"="+recipe.getId(), null);
        return recipe;
    }

    //The super calls this, we NEVER do except in the onUpgrade.
    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.i(TAG,"Creating "+DB_NAME);
        database.execSQL(CREATE_DB);
        //Temporary Dummy Data
        String sql = "INSERT INTO recipe (recipe_name, instructions, ingredients, servings, cook_time) VALUES ('Bacon', 'Test Data', 'Bacon', 1.0, 5.0)";
        database.execSQL(sql);
        Log.i(TAG, "Data Inserted");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.i(TAG, "EnteringOnUpgrade");
        String sql = "DROP TABLE IF EXISTS recipe";
        database.execSQL(sql);
        onCreate(database);
    }
}
