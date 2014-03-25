package pl.burningice.hibernate.events

/**
 * Currently supported event types
 * @author Pawel Gdula
 */
public enum ExecutionScope {

    WITH_TRANSACTION,
    TRANSACTION_COMMITTED,
    TRANSACTION_ROLLBACK
}