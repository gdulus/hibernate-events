package pl.burningice.hibernate.events

/**
 * @author Pawel Gdula
 */
public enum EventType {

    BEFORE_CREATE(ExecutionScope.WITH_TRANSACTION),
    BEFORE_UPDATE(ExecutionScope.WITH_TRANSACTION),
    BEFORE_DELETE(ExecutionScope.WITH_TRANSACTION),
    POST_CREATE(ExecutionScope.WITH_TRANSACTION),
    POST_UPDATE(ExecutionScope.WITH_TRANSACTION),
    POST_DELETE(ExecutionScope.WITH_TRANSACTION),
    COMMITTED_CREATE(ExecutionScope.TRANSACTION_COMMITTED),
    COMMITTED_UPDATE(ExecutionScope.TRANSACTION_COMMITTED),
    COMMITTED_DELETE(ExecutionScope.TRANSACTION_COMMITTED),
    ROLLBACK_CREATE(ExecutionScope.TRANSACTION_ROLLBACK),
    ROLLBACK_UPDATE(ExecutionScope.TRANSACTION_ROLLBACK),
    ROLLBACK_DELETE(ExecutionScope.TRANSACTION_ROLLBACK)

    public final ExecutionScope scope

    EventType(ExecutionScope scope) {
        this.scope = scope
    }
}