package net.pawelhajduk;

import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import net.pawelhajduk.GenerateInjectedConstructorHandler;

public class GenerateInjectedConstructor extends BaseGenerateAction {
    public GenerateInjectedConstructor() {
        super(new GenerateInjectedConstructorHandler());
    }
}