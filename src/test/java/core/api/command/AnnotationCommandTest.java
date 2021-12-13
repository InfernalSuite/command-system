package core.api.command;

import core.api.command.libs.ComplexCommandContainer;
import core.api.command.libs.LoggerCommandExecutor;
import core.api.command.libs.SimpleCommandContainer;
import core.api.command.libs.injection.ComplexInvocationTarget;
import core.api.command.libs.injection.ComplexInvocationTargetInjector;
import core.api.command.libs.params.CustomParameterProvider;
import net.endrealm.libraries.api.commands.CommandFactory;
import net.endrealm.libraries.api.commands.CommandManager;
import net.endrealm.libraries.api.commands.CommandRegistry;
import net.endrealm.libraries.api.lang.LanguageManager;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class AnnotationCommandTest {

    private CommandManager commandManager;
    private CommandFactory commandFactory;
    private CommandRegistry commandRegistry;

    @Before
    public void prepare() {
        LanguageManager languageManager = new LanguageManager(new File("lang"));

        this.commandManager = new CommandManager(languageManager);
        this.commandFactory = new CommandFactory(commandManager);
        this.commandRegistry = new CommandRegistry(commandFactory);
        commandRegistry.addParameterResolver(new CustomParameterProvider());
    }

    @Test
    public void testBasicCommandRegistrationAndExecution() {
        SimpleCommandContainer commandContainer = new SimpleCommandContainer();

        commandRegistry.register(commandContainer);

        // Call a valid
        commandManager.execute(new LoggerCommandExecutor(true), new String[] {"command1", "myText", "10"});
        commandManager.execute(new LoggerCommandExecutor(true), new String[] {"command1", "command2", "myText", "10"});
        commandManager.execute(new LoggerCommandExecutor(false), new String[] {"command4"});

        // Call invalid
        commandManager.execute(new LoggerCommandExecutor(true), new String[] {"command1", "myText", "31"});

        assertEquals("Method command1 was not invoked", 1, commandContainer.command1Called);
        assertEquals("Method command2 was not invoked", 1, commandContainer.command2Called);
        assertEquals("Method command4 was not invoked", 1, commandContainer.command4Called);

    }

    @Test
    public void testCommandParameterInjection() {

        ComplexInvocationTarget complexInvocationTarget = new ComplexInvocationTarget();
        commandRegistry.addInjector(new ComplexInvocationTargetInjector(complexInvocationTarget));


        ComplexCommandContainer complexCommandContainer = new ComplexCommandContainer();
        commandRegistry.register(complexCommandContainer);

        commandManager.execute(new LoggerCommandExecutor(true), new String[] {"command1"});
        commandManager.execute(new LoggerCommandExecutor(true), new String[] {"command2"});

        assertEquals("Method command1 was not invoked", 1, (int) complexInvocationTarget.get(1));
        assertEquals("Method command2 was not invoked", 1, (int) complexInvocationTarget.get(2));

    }

    @Test
    public void testCommandParameterCompletion() {



        SimpleCommandContainer commandContainer = new SimpleCommandContainer();
        commandRegistry.register(commandContainer);

        List<String> completions = commandManager.getCompletions(new LoggerCommandExecutor(true), new String[] {"command3", "2", "ELEM"});

        System.out.println(completions);
    }
}
