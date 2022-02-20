package io.github.dsl.teamf.kernel.structural.quizz;

import io.github.dsl.teamf.kernel.behavioral.ButtonComponent;
import io.github.dsl.teamf.kernel.behavioral.CheckBoxComponent;
import io.github.dsl.teamf.kernel.generator.Visitor;

public class MultipleAnswer extends Answer{

    private CheckBoxComponent answer;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public CheckBoxComponent getAnswer() {
        return answer;
    }

    public void setAnswer(CheckBoxComponent answer) {
        answer.setFunctionName("onMultipleAnswerChange(this.state.quiz,value,option)");
        answer.setVariableName("this.state.quiz.questions[this.state.quiz.indexQuestion].answers");
        this.answer = answer;
    }
}