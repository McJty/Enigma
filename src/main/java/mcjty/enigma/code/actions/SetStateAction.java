package mcjty.enigma.code.actions;

import mcjty.enigma.code.Action;
import mcjty.enigma.code.EnigmaFunctionContext;
import mcjty.enigma.code.ExecutionException;
import mcjty.enigma.parser.Expression;
import mcjty.enigma.parser.ObjectTools;
import mcjty.enigma.progress.Progress;
import mcjty.enigma.progress.ProgressHolder;
import org.apache.commons.lang3.StringUtils;

public class SetStateAction extends Action {
    private final Expression<EnigmaFunctionContext> name;
    private final Expression<EnigmaFunctionContext> value;

    public SetStateAction(Expression<EnigmaFunctionContext> name, Expression<EnigmaFunctionContext>  value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public void dump(int indent) {
        System.out.println(StringUtils.repeat(' ', indent) + "Set State: " + name + "=" + value);
    }

    @Override
    public void execute(EnigmaFunctionContext context) throws ExecutionException {
        Progress progress = ProgressHolder.getProgress(context.getWorld());
        progress.setState(ObjectTools.asStringSafe(name.eval(context)), ObjectTools.asStringSafe(value.eval(context)));
        ProgressHolder.save(context.getWorld());
    }
}
