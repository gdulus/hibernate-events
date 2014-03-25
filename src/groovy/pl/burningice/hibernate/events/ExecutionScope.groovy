package pl.burningice.hibernate.events

/**
 * @author Pawel Gdula
 */
public enum ExecutionScope {

    WITH_TRANSACTION,
    TRANSACTION_COMMITTED,
    TRANSACTION_ROLLBACK
}