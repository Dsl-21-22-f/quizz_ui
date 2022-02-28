package io.github.dsl.teamf.antlr;

import java.util.ArrayList;
import java.util.List;

import io.github.dsl.teamf.antlr.grammar.*;
import io.github.dsl.teamf.kernel.App;
import io.github.dsl.teamf.kernel.behavioral.*;
import io.github.dsl.teamf.kernel.structural.BoxLayout;
import io.github.dsl.teamf.kernel.structural.GridLayout;
import io.github.dsl.teamf.kernel.structural.Layout;
import io.github.dsl.teamf.kernel.structural.Size;

public class ModelBuilder extends QuizzBaseListener {

    /********************
     ** Business Logic **
     ********************/

    private App app = null;
    private boolean built = false;

    public App retrieve() {
        if (built) {
            return app;
        }
        throw new RuntimeException("Cannot retrieve a model that was not created!");
    }

    public String unquote(String quotedString) {
        return quotedString.substring(1, quotedString.length() - 1);
    }

    /*******************
     ** Symbol tables **
     *******************/
    List<Layout> declaredLayouts = new ArrayList<>();
    List<Theme> declaredThemes = new ArrayList<>();
    GridLayout grid;
    List<Layout> row;

    List<UIComponent> currentBoxContents = new ArrayList<>();
    BoxLayout box;
    TextComponent text;

    /**************************
     ** Listening mechanisms **
     **************************/

    @Override
    public void enterRoot(QuizzParser.RootContext ctx) {
        built = false;
        app = new App();
    }

    @Override
    public void exitRoot(QuizzParser.RootContext ctx) {
        // Resolving states in transitions

        this.built = true;
    }

    @Override
    public void enterThemeDeclaration(QuizzParser.ThemeDeclarationContext ctx) {
        Theme theme = new Theme(ctx.themeName.getText());
        theme.setPrimaryColor(ctx.primary.getText());
        theme.setSecondaryColor(ctx.secondary.getText());
        theme.setFont(ctx.fontFamily.getText());
        declaredThemes.add(theme);
    }

    @Override
    public void enterGridDeclaration(QuizzParser.GridDeclarationContext ctx) {
        grid = new GridLayout(ctx.gridName.getText());
        grid.setGap(Size.valueOf(ctx.gap.getText()));
    }

    @Override
    public void enterColumn(QuizzParser.ColumnContext ctx) {
        int currentNumberOfColumns = grid.getSizeByColumnIndex().keySet().size();
        grid.getSizeByColumnIndex().put(currentNumberOfColumns, Size.valueOf(ctx.columnSize.getText()));
    }

    @Override
    public void enterRow(QuizzParser.RowContext ctx) {
        row = new ArrayList<>();
        int currentNumberOfLines = grid.getSizeByRowIndex().keySet().size();
        grid.getSizeByRowIndex().put(currentNumberOfLines, Size.valueOf(ctx.rowSize.getText()));
    }

    @Override
    public void enterZone(QuizzParser.ZoneContext ctx) {
        Layout layout = null;

        for (Layout l : declaredLayouts)
            if (l.getName().equals(ctx.zoneName.getText()))
                layout = l;

        row.add(layout);
    }

    @Override
    public void exitRow(QuizzParser.RowContext ctx) {
        int currentNumberOfLines = grid.getLayouts().length;
        Layout rows[][] = new Layout[currentNumberOfLines + 1][];
        for (int i = 0; i < currentNumberOfLines; i++)
            rows[i] = grid.getLayouts()[i];
        rows[currentNumberOfLines] = row.toArray(new Layout[0]);
        grid.setLayouts(rows);
    }

    @Override
    public void exitGridDeclaration(QuizzParser.GridDeclarationContext ctx) {
        declaredLayouts.add(grid);
    }

    @Override
    public void enterBoxDeclaration(QuizzParser.BoxDeclarationContext ctx) {
        box = new BoxLayout(ctx.boxName.getText());
        box.setFlex(ctx.isFlex != null);
        if (ctx.direction != null)
            box.setDirection(Direction.valueOf(ctx.direction.getText()));
    }

    @Override
    public void enterQuizTitleBinding(QuizzParser.QuizTitleBindingContext ctx) {
        text.bindToQuizTitle();
    }

    @Override
    public void exitBoxContent(QuizzParser.BoxContentContext ctx) {
        box.setContents(new ArrayList<>(currentBoxContents));
        currentBoxContents.clear();
    }

    @Override
    public void exitBoxDeclaration(QuizzParser.BoxDeclarationContext ctx) {
        declaredLayouts.add(box);
    }

    @Override
    public void enterAppDeclaration(QuizzParser.AppDeclarationContext ctx) {
        app.setName(ctx.name.getText());
        app.setQuizPath(unquote(ctx.quizPath.getText()));
    }

    @Override
    public void enterLayoutAttribute(QuizzParser.LayoutAttributeContext ctx) {
        Layout layout = null;

        for (Layout l : declaredLayouts)
            if (l.getName().equals(ctx.layoutName.getText()))
                layout = l;

        if (layout != null)
            app.setLayout(layout);
    }

    @Override
    public void enterThemeAttribute(QuizzParser.ThemeAttributeContext ctx) {
        Theme theme = null;

        for (Theme t : declaredThemes)
            if (t.getName().equals(ctx.themeName.getText()))
                theme = t;

        if (theme != null)
            app.setTheme(theme);
    }

    @Override
    public void enterButton(QuizzParser.ButtonContext ctx) {
        ButtonComponent button = new ButtonComponent();
        if (ctx.functionName != null)
            button.setFunctionName(ctx.functionName.getText());
        if (ctx.textValue != null)
            button.setVariableName(ctx.textValue.getText());
        if (ctx.globalStyle() != null) {
            button.setAligment(Alignment.valueOf(ctx.globalStyle().textAlign.getText()));
        }
        currentBoxContents.add(button);

    }

    @Override
    public void enterCheckBox(QuizzParser.CheckBoxContext ctx) {
        CheckBoxComponent checkBox = new CheckBoxComponent();
        if (ctx.functionName != null)
            checkBox.setFunctionName(ctx.functionName.getText());
        if (ctx.option != null)
            checkBox.setVariableName(ctx.option.getText());
        else {
            checkBox.setVariableName("[]");
        }
        if (ctx.globalStyle() != null) {
            checkBox.setAligment(Alignment.valueOf(ctx.globalStyle().textAlign.getText()));
        }
        currentBoxContents.add(checkBox);

    }

    @Override
    public void enterTextInput(QuizzParser.TextInputContext ctx) {
        TextInputComponent textInput = new TextInputComponent();
        if (ctx.textValue != null)
            textInput.setValue(ctx.textValue.getText());
        if (ctx.fontSize != null)
            textInput.setFontSize(Integer.parseInt(ctx.fontSize.getText()));
        if (ctx.globalStyle() != null) {
            textInput.setAligment(Alignment.valueOf(ctx.globalStyle().textAlign.getText()));
        }
        currentBoxContents.add(textInput);

    }

    @Override
    public void enterText(QuizzParser.TextContext ctx) {
        text = new TextComponent();
        if (ctx.textValue != null)
            text.setValue(ctx.textValue.getText().substring(1, ctx.textValue.getText().length() - 1));
        if (ctx.fontSize != null)
            text.setFontSize(Integer.parseInt(ctx.fontSize.getText()));
        if (ctx.globalStyle() != null) {
            text.setAligment(Alignment.valueOf(ctx.globalStyle().textAlign.getText()));
        }
        currentBoxContents.add(text);
    }

    @Override
    public void enterQuestions(QuizzParser.QuestionsContext ctx) {
        box.setBindedToQuestions(true);
    }

    @Override
    public void enterNavigable(QuizzParser.NavigableContext ctx) {
        Navigation navigation= new Navigation();
        if(ctx.label() != null)
            navigation.setNextLabel(ctx.label().labelValue.getText()
                    .substring(1, ctx.label().labelValue.getText().length() - 1));
        if(ctx.backward() != null){
            navigation.setOnlyNext(false);}
        else{
            navigation.setOnlyNext(true);}
        box.setNavigation(navigation);
        box.setBindedToQuestions(true);
    }
    @Override
    public void enterBackward(QuizzParser.BackwardContext ctx) {
        if(ctx.label() != null)
            box.getNavigation().setPreviousLabel(ctx.label().labelValue.getText()
                    .substring(1, ctx.label().labelValue.getText().length() - 1));
    }
}
