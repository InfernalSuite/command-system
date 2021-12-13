package core.api.command;

import core.api.command.libs.LoggerCommandExecutor;
import core.api.command.libs.TestParameterHolder;
import net.endrealm.libraries.api.commands.CommandFactory;
import net.endrealm.libraries.api.commands.CommandManager;
import net.endrealm.libraries.api.commands.CommandWrapper;
import net.endrealm.libraries.api.lang.LanguageManager;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class CommandTest {

    @Test
    public void test() {
        LanguageManager languageManager = new LanguageManager(new File("C:\\Users\\Johannes\\IdeaProjects\\GitLab\\atom-library\\lang"));
        CommandManager commandManager = new CommandManager(languageManager);
        CommandFactory commandFactory = new CommandFactory(commandManager);

        // Create test command
        commandFactory.createCommand("base.test", new TestParameterHolder())
                .addAlias("test")
                .addPerms("command.test")
                .setExecutor((executor, parameterHolder) -> {
                    assertNotNull("Missing non optional parameter", parameterHolder.getArg0().getValue());
                    assertNotNull("Missing non optional parameter", parameterHolder.getArg1().getValue());
                    executor.sendMessage(String.format("You did test! with %s and %d", parameterHolder.getArg0().getValue(), parameterHolder.getArg1().getValue()));
                    return true;
                })
                .register();

        // Create test 2 command
        commandFactory.createCommand("base.test.2", new TestParameterHolder())
                .addAlias("test2")
                .addPerms("command.test")
                .setParentCommand("base.test")
                .setExecutor((executor, parameterHolder) -> {
                    assertNotNull("Missing non optional parameter", parameterHolder.getArg0().getValue());
                    assertNotNull("Missing non optional parameter", parameterHolder.getArg1().getValue());
                    executor.sendMessage("You did test2");
                    return true;
                })
                .register();

        // Test for test command
        {
            String[] args = new String[] {"test", "text", "13"};

            List<CommandWrapper> chain = commandManager.getCommandChain(args);

            assertNotNull("No command chain found", chain);
            assertTrue("Error in command chain search process", chain.size() > 0);
            assertEquals("Didnt find the correct command","base.test", chain.get(chain.size()-1).getCommandBase().getName());
        }

        // Test for test 2 command
        {
            String[] args = new String[] {"test", "test2", "text", "15"};

            List<CommandWrapper> chain = commandManager.getCommandChain(args);

            assertNotNull("No command chain found", chain);
            assertEquals("Error in command chain search process", 2, chain.size());
            assertEquals("Didnt find the correct command","base.test.2", chain.get(chain.size()-1).getCommandBase().getName());
        }

        commandManager.execute(new LoggerCommandExecutor(true), new String[] {"test","text","123"});
        commandManager.execute(new LoggerCommandExecutor(true), new String[] {"test","test2","text","123"});

        commandManager.execute(new LoggerCommandExecutor(false), new String[] {"test","text","123"});
        commandManager.execute(new LoggerCommandExecutor(false), new String[] {"test","test2","text","123"});


    }
}
