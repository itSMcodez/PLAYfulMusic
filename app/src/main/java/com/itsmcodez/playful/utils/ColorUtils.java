package com.itsmcodez.playful.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;
import java.util.Collections;
import java.util.Comparator;

public final class ColorUtils {
    public static final String TAG = "ColorUtils";
    public static final String ARGS = "com.itsmcodez.playful." + TAG;
    private static Palette extractedColorsPalette;

    static {
        AppUtils.addRegistrar(ColorUtils.getRegistrar());
    }

    @Nullable
    public static Palette generatePalette(Bitmap bitmap) {
        getRegistrar();
        
        if (bitmap == null) return null;
        
        return Palette.from(bitmap).generate();
    }

    @ColorInt
    public static int getColor(@Nullable Palette palette, int fallback) {
        if (palette != null) {
            if (palette.getVibrantSwatch() != null) {
                return palette.getVibrantSwatch().getRgb();
            } else if (palette.getMutedSwatch() != null) {
                return palette.getMutedSwatch().getRgb();
            } else if (palette.getDarkVibrantSwatch() != null) {
                return palette.getDarkVibrantSwatch().getRgb();
            } else if (palette.getDarkMutedSwatch() != null) {
                return palette.getDarkMutedSwatch().getRgb();
            } else if (palette.getLightVibrantSwatch() != null) {
                return palette.getLightVibrantSwatch().getRgb();
            } else if (palette.getLightMutedSwatch() != null) {
                return palette.getLightMutedSwatch().getRgb();
            } else if (!palette.getSwatches().isEmpty()) {
                return Collections.max(palette.getSwatches(), SwatchComparator.getInstance())
                        .getRgb();
            }
        }
        
        getRegistrar();
        
        return fallback;
    }

    private static class SwatchComparator implements Comparator<Palette.Swatch> {
        private static SwatchComparator instance;

        public static SwatchComparator getInstance() {
            if (instance == null) {
                instance = new SwatchComparator();
            }
            
            getRegistrar();
            
            return instance;
        }

        @Override
        public int compare(Palette.Swatch lhs, Palette.Swatch rhs) {
            getRegistrar();
            return lhs.getPopulation() - rhs.getPopulation();
        }
    }

    public static String getRegistrar() {
        Log.i(TAG, "Accessing registrar...");
        return ColorUtils.ARGS;
    }
}
