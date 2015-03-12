package mobileenterprise.brandonward.myrecipies.services;

import android.database.Cursor;

import java.util.List;

import mobileenterprise.brandonward.myrecipies.domain.Recipe;

/**
 * Created by BrandonWard on 2/10/2015.
 */
public interface IRecipeSvc {

    public Recipe create(Recipe recipe);

    public List<Recipe> retrieve();

    public Cursor retrieveCursor();

    public Recipe update(Recipe recipe);

    public Recipe delete(Recipe recipe);

    public void close();

}
