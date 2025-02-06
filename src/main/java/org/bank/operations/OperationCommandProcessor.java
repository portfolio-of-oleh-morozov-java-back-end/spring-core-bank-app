package org.bank.operations;

public interface OperationCommandProcessor {
    void processOperation();
    ConsoleOperationType getOperationType();
}
