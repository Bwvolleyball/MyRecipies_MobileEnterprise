package mobileenterprise.brandonward.myrecipies.presentation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import mobileenterprise.brandonward.myrecipies.R;
import mobileenterprise.brandonward.myrecipies.domain.Recipe;
import mobileenterprise.brandonward.myrecipies.services.IRecipeSvc;
import mobileenterprise.brandonward.myrecipies.services.RecipeSvcSIOImpl;


public class MainRecipiesMaster extends ActionBarActivity {

    private Context context = null;
    private ListView recipeList = null;
    private ArrayAdapter<Recipe> adapter;
    private IRecipeSvc recipeSvc;

    private final static int ADD_CODE = 0;
    private final static int UPDATE_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recipies_master);
        context = this;
        recipeList = (ListView) findViewById(R.id.recipes_list_master);
        recipeSvc = RecipeSvcSIOImpl.getInstance(context);
        List<Recipe> recipes = recipeSvc.retrieve();
        adapter = new ArrayAdapter<Recipe>(context, android.R.layout.simple_list_item_1, recipes);
        recipeList.setAdapter(adapter);
        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String item = (String) listView.getItemAtPosition(position);
                Recipe item = (Recipe) recipeList.getItemAtPosition(position);
                Toast.makeText(context, "The item: " + item, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, RecipesDetail.class);
                intent.putExtra("item", item);
                intent.putExtra("action", "update");
                startActivityForResult(intent,UPDATE_CODE);
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_recipies_master, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;

        switch (id) {
            case R.id.action_add_button:
                intent = new Intent(this, RecipesDetail.class);
                intent.putExtra("action", "add");
                startActivityForResult(intent, ADD_CODE);
                break;
            case R.id.action_settings:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        if (resultCode == Activity.RESULT_OK) {
            Recipe recipe = (Recipe) intent.getSerializableExtra("recipe");
            if (requestCode == ADD_CODE) {
                recipeSvc.create(recipe);
            } else if (requestCode == UPDATE_CODE) {
                recipeSvc.update(recipe);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
