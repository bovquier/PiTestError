package com.bovquier.pitesterror;

import io.requery.Entity;
import io.requery.Key;
import io.requery.ManyToOne;

@Entity
interface Option {

    @Key
    int getId();

    int getOptionOrder();

    String getOptionId();

    String getLabel();

    @ManyToOne
    ContentEntity getContentEntity();
}
