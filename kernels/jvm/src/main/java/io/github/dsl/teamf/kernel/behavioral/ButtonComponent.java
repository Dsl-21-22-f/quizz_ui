package io.github.dsl.teamf.kernel.behavioral;

import io.github.dsl.teamf.kernel.generator.Visitor;
import io.github.dsl.teamf.kernel.structural.ui.Size;

public class ButtonComponent extends UiComponent{

    private Size size=Size.small;
    private String color;
    private String functionName;
    private Size margin=Size.small;
    private Boolean primary=true;
    private String variableName;
    private Align align=Align.center;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public Align getAlign() {
        return align;
    }

    public void setAlign(Align align) {
        this.align = align;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public Size getMargin() {
        return margin;
    }

    public void setMargin(Size margin) {
        this.margin = margin;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }
}
