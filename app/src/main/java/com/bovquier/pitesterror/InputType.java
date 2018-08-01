package com.bovquier.pitesterror;

import com.google.gson.annotations.SerializedName;

import io.requery.converter.EnumStringConverter;

public enum InputType {
    @SerializedName("image")
    IMAGE("image"),

    @SerializedName("video")
    VIDEO("video"),

    @SerializedName("screen_recording")
    SCREEN_RECORD("screen_recording"),

    @SerializedName("radio")
    SINGLE_SELECTION("single_selection"),

    @SerializedName("text")
    TEXT("text"),

    @SerializedName("checkbox")
    MULTI_SELECTION("multi_selection"),

    UNKNOWN("unknown");

    private String name;

    public String getName() {
        return name;
    }

    InputType(String name) {
        this.name = name;
    }

    public static InputType fromApi(String inputType) {
        if (inputType == null || inputType.length() == 0) {
            return UNKNOWN;
        }

        switch (inputType.toLowerCase()) {
            case "image":
                return IMAGE;
            case "video":
                return VIDEO;
            case "screen_recording":
                return SCREEN_RECORD;
            case "radio":
                return SINGLE_SELECTION;
            case "text":
                return TEXT;
            case "checkbox":
                return MULTI_SELECTION;
            default:
                return UNKNOWN;
        }
    }

    public static EnumStringConverter<InputType> getRequeryConverter() {

        return new EnumStringConverter<InputType>(InputType.class) {

            @Override
            public InputType convertToMapped(Class<? extends InputType> type, String value) {
                if (value == null || value.length() == 0) {
                    return UNKNOWN;
                }

                try {
                    return InputType.valueOf(value);
                } catch (IllegalArgumentException e) {
                    return UNKNOWN;
                }
            }
        };
    }

    public boolean isAsset() {
        switch (this) {
            case IMAGE:
            case VIDEO:
            case SCREEN_RECORD:
                return true;
            default:
                return false;
        }
    }
}