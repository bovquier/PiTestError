package com.bovquier.pitesterror

import rx.schedulers.TestScheduler
import spock.lang.Specification

class MultiSelectionPresenterTest extends Specification {
    def view = Mock(MultiSelectionContract.View)

    def testScheduler = new TestScheduler()

    MultiSelectionPresenter presenter
    def model
    def option1
    def option2

    def "setup"() {
        option1 = new OptionEntity()
        option1.setId(4)

        option2 = new OptionEntity()
        option2.setId(8)

        def contentEntity = new ContentEntity()
        contentEntity.getOptionEntityList().add(option1)
        contentEntity.getOptionEntityList().add(option2)

        model = new InputAndAnswerEntity()
        model.setTitle("title")
        model.setAnswer("[1, 2, 3]")
        model.setContent(contentEntity)

        presenter = new MultiSelectionPresenter(testScheduler)
    }

    def "should read answers on bind"() {
        given:
        presenter.setModel(model)

        and:
        presenter.@answers = []

        when:
        presenter.bind(view)

        and:
        testScheduler.triggerActions()

        then:
        presenter.answers.size() == 3
        presenter.answers.asList() == [1, 2, 3]
    }

    def "should set options on view bind"() {
        given:
        presenter.setModel(model)

        when:
        presenter.bind(view)

        and:
        testScheduler.triggerActions()

        then:
        1 * view.selectOption(1)

        then:
        1 * view.selectOption(2)

        then:
        1 * view.selectOption(3)
    }

    def "should set answer on option selected"() {
        given:
        presenter.setModel(model)

        and:
        presenter.bind(view)

        and:
        testScheduler.triggerActions()

        assert presenter.answers.asList() == [1, 2, 3]

        when:
        presenter.optionSelected(4, true)

        then:
        presenter.answers.asList() == [1, 2, 3, 4]
    }

    def "should remove answer on option deselected"() {
        given:
        presenter.setModel(model)

        and:
        presenter.bind(view)

        and:
        testScheduler.triggerActions()

        assert presenter.answers.asList() == [1, 2, 3]

        when:
        presenter.optionSelected(2, false)

        then:
        presenter.answers.asList() == [1, 3]
    }

    def "should store answer in model"() {
        given:
        presenter.setModel(model)

        and:
        presenter.bind(view)

        and:
        testScheduler.triggerActions()

        assert presenter.answers.asList() == [1, 2, 3]

        assert model.getAnswer() == "[1, 2, 3]"

        when:
        presenter.optionSelected(2, false)

        then:
        presenter.model.getAnswer() == "[1,3]"
    }

    def "should store answer in database"() {
        given:
        presenter.setModel(model)

        and:
        presenter.bind(view)

        and:
        testScheduler.triggerActions()

        assert presenter.answers.asList() == [1, 2, 3]

        assert model.getAnswer() == "[1, 2, 3]"

        when:
        presenter.optionSelected(2, false)

        then:
        true
    }

    def "should ignore invalid json when reading answer"() {
        given:
        model.setAnswer("foo bar []")

        assert presenter.answers.asList() == []

        presenter.setModel(model)

        when:
        presenter.readAnswer()

        then:
        presenter.answers.asList() == []

        and:
        0 * _
    }
}