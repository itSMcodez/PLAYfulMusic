package com.itsmcodez.playful.providers;
import android.content.SearchRecentSuggestionsProvider;

public class CustomSuggestionsProvider extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = "com.itsmcodez.playful.CustomSuggestionsProvider";
    public static final int MODE = DATABASE_MODE_QUERIES;
    
    public CustomSuggestionsProvider(){
        setupSuggestions(AUTHORITY, MODE);
    }
}
