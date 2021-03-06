package io.github.dsl.teamf.kernel;

import io.github.dsl.teamf.kernel.behavioral.UiComponent;
import io.github.dsl.teamf.kernel.generator.Visitor;
import io.github.dsl.teamf.kernel.generator.Visitable;
import io.github.dsl.teamf.kernel.structural.quizz.QuizElement;
import io.github.dsl.teamf.kernel.structural.ui.Grid;
import io.github.dsl.teamf.kernel.structural.ui.Theme;

import java.util.ArrayList;
import java.util.List;


public class App implements NamedElement, Visitable {
	private Grid grid;
	private String name;

	public Theme getTheme() {
		return theme;
	}

	public void setTheme(Theme theme) {
		this.theme = theme;
	}

	private Theme theme;
	private List<UiComponent> componentList = new ArrayList<>();
	private List<QuizElement> quizElementList = new ArrayList<>();

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	public List<UiComponent> getComponentList() {
		return componentList;
	}

	public void setComponentList(List<UiComponent> componentList) {
		this.componentList = componentList;
	}

	public List<QuizElement> getQuizElementList() {
		return quizElementList;
	}

	public void setQuizElementList(List<QuizElement> quizElementList) {
		this.quizElementList = quizElementList;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
