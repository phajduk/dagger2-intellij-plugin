package net.pawelhajduk;

import com.intellij.codeInsight.generation.actions.BaseGenerateAction;

public class GenerateInjectedConstructor extends BaseGenerateAction {
    public GenerateInjectedConstructor() {
        super(new GenerateInjectedConstructorHandler());
    }
}