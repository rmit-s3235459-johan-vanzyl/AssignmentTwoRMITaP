package com.teammj.model.persons.base;


import com.teammj.model.DATA;
import com.teammj.model.persons.Referee;
import org.w3c.dom.Element;

import java.util.UUID;

/**
 * Official super class for inheritance.
 *
 * @author Johan van Zyl
 * @author Michael Guida
 * @see Referee
 */
public abstract class Official extends Person {

    protected Official(String name, int age, DATA.STATE fromState, Element element, DATA.PERSON_TYPE personType, UUID... uuid) {
        super(name, age, fromState, element, personType, uuid);
    }

}
