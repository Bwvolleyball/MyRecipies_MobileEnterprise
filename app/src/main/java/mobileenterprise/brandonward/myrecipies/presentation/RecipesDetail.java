package mobileenterprise.brandonward.myrecipies.presentation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mobileenterprise.brandonward.myrecipies.R;
import mobileenterprise.brandonward.myrecipies.domain.Recipe;
import mobileenterprise.brandonward.myrecipies.services.IRecipeSvc;
import mobileenterprise.brandonward.myrecipies.services.RecipeSvcSIOImpl;


public class RecipesDetail extends ActionBarActivity {

    private static final String UPDATE = "update";
    private static final String ADD = "add";

    private Context context;

    private String action;
    private Recipe recipe;
    private EditText name;
    private EditText instructions;
    private EditText ingredients;
    private EditText servings;
    private EditText cookTime;
    private Button confirmActionBtn;
    private Button cancelActionBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_detail);
        context = this;
        Intent intent = getIntent();
        action = intent.getStringExtra("action");
        recipe = (Recipe) intent.getSerializableExtra("item");
        name =(EditText) findViewById(R.id.recipe_name_detail);
        instructions = (EditText) findViewById(R.id.recipe_instructions_detail);
        ingredients = (EditText) findViewById(R.id.recipe_ingredients_detail);
        servings = (EditText) findViewById(R.id.recipe_servings_detail);
        cookTime = (EditText) findViewById(R.id.recipe_cooktime_detail);
        confirmActionBtn = (Button) findViewById(R.id.submit_recipe_detail);
        cancelActionBtn = (Button) findViewById(R.id.cancel_recipe_detail);
        if(action.equals(UPDATE)){
            confirmActionBtn.setText("Update");
            cancelActionBtn.setText("Delete");
            Toast.makeText(context, "id: "+ recipe.getId(), Toast.LENGTH_SHORT).show();
            name.setText(recipe.getName());
            instructions.setText(recipe.getInstructions());
            ingredients.setText(recipe.getIngredients());
            servings.setText(String.valueOf(recipe.getServings()));
            cookTime.setText(String.valueOf(recipe.getCookTime()));
        } else if (action.equals(ADD)){
            confirmActionBtn.setText("Submit");
            cancelActionBtn.setText("Cancel");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipes_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSubmit(View view){
        Intent intent = new Intent();
        if (recipe == null){
            recipe = new Recipe();
        }
        recipe.setName(name.getText().toString());
        recipe.setIngredients(ingredients.getText().toString());
        recipe.setInstructions(instructions.getText().toString());
        recipe.setCookTime(Double.parseDouble(cookTime.getText().toString()));
        recipe.setServings(Double.parseDouble(servings.getText().toString()));
        intent.putExtra("recipe", recipe);
        setResult(Activity.RESULT_OK, intent);
        finish();

    }

    public void onCancel(View view){
        Intent intent = new Intent();
        intent.putExtra("action", action);
        intent.putExtra("recipe", recipe);
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}
