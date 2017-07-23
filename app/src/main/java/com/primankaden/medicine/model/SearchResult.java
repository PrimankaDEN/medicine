package com.primankaden.medicine.model;

import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;

import com.primankaden.medicine.App;
import com.primankaden.medicine.R;
import com.primankaden.medicine.Utils;

/**
 * Created by PrimankaDEN on 23.07.2017.
 */

public class SearchResult {
    private static final int MAX_PART_SIZE = 50;

    private final Item question;
    private final SpannableString spannedTitle, spannedDescription;

    public SearchResult(Item question, String searchTerm) {
        Spanned spannedContent = Html.fromHtml(question.getTitle());
        SpannableString spannableContent = new SpannableString(spannedContent);
        int position = spannableContent.toString().toLowerCase().indexOf(searchTerm.toLowerCase());
        SpannableString text = new SpannableString("..." + spannableContent.subSequence(position - MAX_PART_SIZE > 0 ? position - MAX_PART_SIZE : 0,
                position + MAX_PART_SIZE < spannableContent.length() ? position + MAX_PART_SIZE : spannableContent.length()) + "...");
        int color = App.getContext().getResources().getColor(R.color.colorAccent);
        text = Utils.highlightTextPart(text.toString(), searchTerm.toLowerCase(), color);

        this.question = question;
        this.spannedTitle = Utils.highlightTextPart(question.getTitle(), searchTerm.toLowerCase(), color);
        this.spannedDescription = text;
    }

    public Item getQuestion() {
        return question;
    }

    public SpannableString getSpannedTitle() {
        return spannedTitle;
    }

    public SpannableString getSpannedDescription() {
        return spannedDescription;
    }
}
