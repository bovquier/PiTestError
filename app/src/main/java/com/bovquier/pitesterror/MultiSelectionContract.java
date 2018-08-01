package com.bovquier.pitesterror;

public class MultiSelectionContract {
    public interface View {
        void addOption(OptionEntity option);

        void selectOption(int id);

        void setTitle(String title, boolean required);
    }

    public interface Presenter {
        void setModel(InputAndAnswerEntity input);

        void bind(View view);

        void unbind();

        void optionSelected(int checkedId, boolean isChecked);
    }
}