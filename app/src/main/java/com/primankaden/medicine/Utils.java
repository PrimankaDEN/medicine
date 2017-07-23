package com.primankaden.medicine;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by PrimankaDEN on 23.07.2017.
 */

public class Utils {
    public static void hideKeyboard(View v) {
        if (v != null && v.getContext() != null) {
            if (!v.isFocused()) {
                v.requestFocus();
            }
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public static void displayKeyboard(View v) {
        if (v != null && v.getContext() != null) {
            if (!v.isFocused()) {
                v.requestFocus();
            }
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    public static SpannableString highlightTextPart(String fullText, String highlightPart, int color) {
        SpannableString spannableString = new SpannableString(fullText);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(color);
        int start = fullText.toLowerCase().indexOf(highlightPart.toLowerCase());
        int end = start + highlightPart.length();
        if (start >= 0) {
            spannableString.setSpan(foregroundColorSpan, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        return spannableString;
    }
}
