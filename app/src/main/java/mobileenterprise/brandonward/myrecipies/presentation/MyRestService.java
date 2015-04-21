package mobileenterprise.brandonward.myrecipies.presentation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import mobileenterprise.brandonward.myrecipies.R;
import mobileenterprise.brandonward.myrecipies.domain.Recipe;
import mobileenterprise.brandonward.myrecipies.services.IRestSvc;
import mobileenterprise.brandonward.myrecipies.services.MyRestServiceImpl;

public class MyRestService extends ActionBarActivity {

    private ArrayAdapter adapter;
    private Context self = null;
    private ListView listView = null;
    private static final String TAG = "MyRestService";

    private final static int ADD_CODE = 0;
    private final static int UPDATE_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rest_service);
        self = this;
        listView = (ListView) findViewById(R.id.myRestData);
        new RetrieveAsyncTask().execute();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe recipe = (Recipe) listView.getItemAtPosition(position);
                Intent intent = new Intent(self, RecipesDetail.class);
                intent.putExtra("item", recipe);
                intent.putExtra("action","update");
                startActivityForResult(intent, UPDATE_CODE);
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_rest, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;
        //noinspection SimplifiableIfStatement
        switch(id){
            case(R.id.action_rest_add):
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
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == Activity.RESULT_OK) {
            Recipe recipe = (Recipe) intent.getSerializableExtra("recipe");
            if (requestCode == ADD_CODE) {
                new CreateAsyncTask(recipe).execute();
            } else if (requestCode == UPDATE_CODE) {
                new UpdateAsyncTask(recipe).execute();
            }
            adapter.notifyDataSetChanged();
        } else if (resultCode == Activity.RESULT_CANCELED) {
            String action = intent.getStringExtra("action");
            if (action.equals("add")) {
                Toast.makeText(self, "Recipe Cancelled", Toast.LENGTH_SHORT).show();
            } else if (action.equals("update")) {
                Recipe recipe = (Recipe) intent.getSerializableExtra("recipe");
                Toast.makeText(self, "Recipe Deleted", Toast.LENGTH_SHORT).show();
                new DeleteAsyncTask(recipe).execute();
            }
        }
    }



    private class RetrieveAsyncTask extends AsyncTask<String, String, String> {//(params, progress-information, result) for arguments

        @Override//the web-service
        protected String doInBackground(String... params) {//String... is equivalent to saying String[] args
            IRestSvc impl = new MyRestServiceImpl();
            return impl.retrieve();
        }

        @Override//updates the UI
        protected void onPostExecute(String result){

            Log.i(TAG, "OnPostExecute");
            try{
                JSONArray jsonArray = new JSONArray(result);
                final int length = jsonArray.length();
                Log.i(TAG, "Number of entries " + length);
                Recipe[] recipes = new Recipe[length];
                for (int i = 0; i < length; i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Log.i(TAG, jsonObject.getString("id") + " " + jsonObject.getString("recipeName"));
                    Recipe recipe = new Recipe();
                    recipe.setName(jsonObject.getString("recipeName"));
                    recipe.setId(jsonObject.getInt("id"));
                    recipe.setCookTime(jsonObject.getDouble("cookTime"));
                    recipe.setIngredients(jsonObject.getString("ingredients"));
                    recipe.setInstructions(jsonObject.getString("instructions"));
                    recipe.setServings(jsonObject.getDouble("servings"));
                    recipes[i] = recipe;

                }
                adapter = new ArrayAdapter<Recipe>(self,android.R.layout.simple_list_item_1, recipes);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } catch(Exception e){
                e.printStackTrace();
            }
            Log.i(TAG, "Exiting onPostExecute");
        }
    }

    private class CreateAsyncTask extends AsyncTask<String, String, String> {//(params, progress-information, result) for arguments
        private Recipe recipe;
        public CreateAsyncTask(Recipe recipe){
            this.recipe = recipe;
        }
        @Override//the web-service
        protected String doInBackground(String... params) {//String... is equivalent to saying String[] args
            IRestSvc impl = new MyRestServiceImpl();
            return impl.create(recipe);
        }

        @Override//updates the UI
        protected void onPostExecute(String result) {
            new RetrieveAsyncTask().execute();
        }
    }

    private class UpdateAsyncTask extends AsyncTask<String, String, String> {//(params, progress-information, result) for arguments
        private Recipe recipe;

        public UpdateAsyncTask(Recipe recipe){
            this.recipe = recipe;
        }

        @Override//the web-service
        protected String doInBackground(String... params) {//String... is equivalent to saying String[] args
            IRestSvc impl = new MyRestServiceImpl();
            return impl.update(recipe);
        }

        @Override//updates the UI
        protected void onPostExecute(String result) {
            new RetrieveAsyncTask().execute();
        }
    }

    private class DeleteAsyncTask extends AsyncTask<String, String, String> {//(params, progress-information, result) for arguments
        private int id;
        public DeleteAsyncTask(Recipe recipe){
            id = recipe.getId();
        }
        @Override//the web-service
        protected String doInBackground(String... params) {//String... is equivalent to saying String[] args
            IRestSvc impl = new MyRestServiceImpl();
            return impl.delete(id);
        }

        @Override//updates the UI
        protected void onPostExecute(String result) {
            new RetrieveAsyncTask().execute();
        }
    }
}
