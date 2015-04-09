package mobileenterprise.brandonward.myrecipies.services;

import mobileenterprise.brandonward.myrecipies.domain.Recipe;

/**
 * Created by BrandonWard on 4/9/2015.
 */
public interface IRestSvc {

    public String retrieve();//These are all returning the JSON string representation of the objects.
    public String retrieve(int id);
    public String create(Recipe recipe);
    public String update(Recipe recipe);
    public String delete(int id);
}
