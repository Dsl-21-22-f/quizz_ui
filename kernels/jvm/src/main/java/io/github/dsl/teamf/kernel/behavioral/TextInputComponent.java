package io.github.dsl.teamf.kernel.behavioral;

import io.github.dsl.teamf.kernel.generator.Visitor;
import io.github.dsl.teamf.kernel.structural.ui.Size;

public class TextInputComponent extends UiComponent {
    private Size size= Size.medium;
    private Align textAlign = Align.center;
    private String placeholder;

    public TextInputComponent() {
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Align getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(Align textAlign) {
        this.textAlign = textAlign;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}