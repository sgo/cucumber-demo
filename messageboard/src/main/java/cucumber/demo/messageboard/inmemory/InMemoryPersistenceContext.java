package cucumber.demo.messageboard.inmemory;

import cucumber.demo.messageboard.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;

/**
 * In memory persistence strategy developed while satisfying the acceptance tests.
 *
 * While an in memory strategy is not useful for production it is useful fr development because it is:
 * - fast
 * - simple
 * - instructing you what to expect of a real persistence strategy
 *
 * Simply put the tests for this in memory implementation were used to extract contract tests (see persistence package) which can be used to quickly test
 * an alternate strategy for functional equivalence. (see the messageboardx-sql module for an example)
 */
public class InMemoryPersistenceContext extends PersistenceContext {
    private List<String> messageDatabase = new ArrayList<>();

    {
        findAllMessagesCommand = new InMemoryFindAllMessagesCommand() {{
            db = messageDatabase;
        }};
        addMessageCommand = new InMemoryAddMessageCommand() {{
            db = messageDatabase;
        }};
        removeMessageCommand = new InMemoryRemoveMessageCommand() {{
            db = messageDatabase;
        }};
        clearCommand = new InMemoryClearCommand() {{
            db = messageDatabase;
        }};
    }
}
