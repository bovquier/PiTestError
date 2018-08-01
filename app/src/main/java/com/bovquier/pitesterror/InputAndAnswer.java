package com.bovquier.pitesterror;


import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Set;
import java.util.TreeSet;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.OneToOne;

@Entity
public abstract class InputAndAnswer implements Parcelable {

    @Key
    String key;

    String inputId;

    InputType inputType;

    String title;

    boolean required;

    @ForeignKey
    @OneToOne
    ContentEntity content;

    /**
     * Here we can have:
     * - path to PNG file
     * - RecordingData structure in case of screen recording attachment
     */
    String answer;

    Integer inputIndex;

    public static boolean isValid(InputAndAnswerEntity inputAndAnswer) {
        if (!inputAndAnswer.isRequired()) {
            return true;
        }

        switch (inputAndAnswer.getInputType()) {
            case IMAGE:
                return inputAndAnswer.getAnswer() != null && inputAndAnswer.getAnswer().length() > 0;
            case VIDEO:
                return inputAndAnswer.getAnswer() != null && inputAndAnswer.getAnswer().length() > 0;
            case SCREEN_RECORD:
                return inputAndAnswer.getAnswer() != null && inputAndAnswer.getAnswer().length() > 0;
            case SINGLE_SELECTION:
                return getAnswerAsInt(inputAndAnswer) != -1;
            case TEXT:
                // TODO: skipping min/max answer length as backend doesn't support that right now
                return inputAndAnswer.getAnswer() != null && inputAndAnswer.getAnswer().length() > 0;
            case MULTI_SELECTION:
                Set<Integer> answers = getAnswerAsSet(inputAndAnswer);
                // TODO: skipping min/max answers count as backend doesn't support that right now
                return answers.size() > 0;
        }

        return false;
    }

    public static int getAnswerAsInt(InputAndAnswerEntity inputAndAnswer) {
        if (inputAndAnswer.getAnswer() == null) return -1;

        try {
            return Integer.valueOf(inputAndAnswer.getAnswer());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static Set<Integer> getAnswerAsSet(InputAndAnswerEntity inputAndAnswer) {
        Set<Integer> ret = new TreeSet<>();

        if (inputAndAnswer.getAnswer() == null) return ret;

        try {
            JSONArray jsonArray = new JSONArray(inputAndAnswer.getAnswer());
            for (int i = 0; i < jsonArray.length(); ++i) {
                ret.add(jsonArray.getInt(i));
            }
        } catch (JSONException ignored) {
        }

        return ret;
    }
}