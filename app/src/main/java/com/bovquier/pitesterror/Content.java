package com.bovquier.pitesterror;


import java.util.List;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.OneToMany;
import io.requery.OneToOne;
import io.requery.OrderBy;

@Entity
interface Content {

    @Key
    @Generated
    int getId();

    long getMinTextLength();

    long getMaxTextLength();

    long getMinNumberOfChoices();

    long getMaxNumberOfChoices();

    @OneToOne
    InputAndAnswerEntity getInputAndAnswerEntity();

    @OneToMany
    @OrderBy(value = "optionOrder")
    List<OptionEntity> getOptionEntityList();
}
