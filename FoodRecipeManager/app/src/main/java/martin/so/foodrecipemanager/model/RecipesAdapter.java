package martin.so.foodrecipemanager.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import martin.so.foodrecipemanager.R;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView Adapter class. Handles the recipe card views.
 */
public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    private List<Recipe> recipes;
    private List<Recipe> recipesCopy;
    private LayoutInflater inflater;
    private Context context;
    private ItemClickListener recipeClickListener;

    public RecipesAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.recipes = recipes;
        recipesCopy = new ArrayList<>(recipes);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recipe_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Recipe recipe = recipes.get(position);

        // Load the recipe photo from internal storage.
        Bitmap bitmapRecipePhoto = new ImageHandler(context).loadImageFile(recipe.getName());

        if (bitmapRecipePhoto != null) {
            holder.recipePhoto.setImageBitmap(bitmapRecipePhoto);
        } else {
            holder.recipePhoto.setImageResource(R.drawable.placeholder_recipe_photo);
        }

        holder.recipeName.setText(recipe.getName());
        holder.recipeCategory.setText(recipe.getCategory());
        holder.recipeType.setText(recipe.getType());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public Recipe getItem(int id) {
        return recipes.get(id);
    }

    /**
     * This determines what data will be displayed on the cards in the RecyclerView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView recipePhoto;
        TextView recipeName;
        TextView recipeCategory;
        TextView recipeType;

        ViewHolder(View itemView) {
            super(itemView);
            recipePhoto = itemView.findViewById(R.id.imageViewThumbnailCard);
            recipeName = itemView.findViewById(R.id.textViewRecipeNameCard);
            recipeCategory = itemView.findViewById(R.id.textViewRecipeCategoryCard);
            recipeType = itemView.findViewById(R.id.textViewRecipeTypeCard);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (recipeClickListener != null)
                recipeClickListener.onItemClick(view, getAdapterPosition());
        }
    }


    /**
     * Filter the recipe list based on the text parameter.
     * Notifies that the recipe list has changed.
     *
     * @param text The text that is to be found in the recipes' names.
     */
    public void filter(String text) {
        recipes.clear();
        if (text.isEmpty()) {
            recipes.addAll(recipesCopy);
        } else {
            text = text.toLowerCase();
            for (Recipe recipe : recipesCopy) {
                if (recipe.getName().toLowerCase().contains(text)) {
                    recipes.add(recipe);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setList(List<Recipe> newRecipes) {
        recipes = newRecipes;
        recipesCopy = new ArrayList<>(recipes);
    }

    public void setClickListener(ItemClickListener recipeClickListener) {
        this.recipeClickListener = recipeClickListener;
    }

    /**
     * Parent will implement this method to respond to click events.
     */
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}