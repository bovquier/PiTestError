package com.bovquier.pitesterror;

import android.util.Log;

import org.json.JSONArray;

import java.util.Set;
import java.util.TreeSet;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class MultiSelectionPresenter implements MultiSelectionContract.Presenter {
    Set<Integer> answers = new TreeSet<Integer>();
    InputAndAnswerEntity model;
    Scheduler observeOn;
    Scheduler subscribeOn;

    CompositeSubscription compositeSubscription = new CompositeSubscription();

    public MultiSelectionPresenter(
            Scheduler observeOn,
            Scheduler subscribeOn
    ) {
        this.observeOn = observeOn;
        this.subscribeOn = subscribeOn;
    }

    @Override
    public void setModel(InputAndAnswerEntity input) {
        this.model = input;
    }

    @Override
    public void bind(MultiSelectionContract.View view) {
        view.setTitle(model.getTitle(), model.isRequired());

        Subscription subscribe = Observable
                .from(model.getContent().getOptionEntityList())
                .subscribeOn(observeOn)
                .observeOn(observeOn)
                .subscribe(
                        view::addOption,
                        error -> Log.e("asd", error.getLocalizedMessage()),
                        () -> {
                            readAnswer();

                            for (Integer answer : answers) {
                                view.selectOption(answer);
                            }
                        }
                );
        compositeSubscription.add(subscribe);
    }

    private Action1<Throwable> empty() {
        return null;
    }

    @Override
    public void unbind() {
        compositeSubscription.unsubscribe();
    }

    @Override
    public void optionSelected(int checkedId, boolean isChecked) {
        if (isChecked) {
            answers.add(checkedId);
        } else {
            answers.remove(checkedId);
        }

        storeAnswer();
    }

    void readAnswer() {
        answers = InputAndAnswerEntity.getAnswerAsSet(model);
    }

    private void storeAnswer() {
        JSONArray jsonArray = new JSONArray();

        for (Integer answer : answers) {
            jsonArray.put(answer);
        }

        model.setAnswer(jsonArray.toString());
    }
}