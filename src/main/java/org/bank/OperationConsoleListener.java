package org.bank;

import org.bank.operations.ConsoleOperationType;
import org.bank.operations.OperationCommandProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class OperationConsoleListener {

    private final Scanner scanner;
    private final Map<ConsoleOperationType, OperationCommandProcessor> processorsMap;

    public OperationConsoleListener(
            Scanner scanner,
            List<OperationCommandProcessor> processorList

            ) {
        this.scanner = scanner;
        this.processorsMap = processorList
                .stream()
                .collect(Collectors.toMap(
                        OperationCommandProcessor::getOperationType,
                        processor -> processor
                ));
    }


    public void  listenUpdates() {
        while (!Thread.currentThread().isInterrupted()) {
            ConsoleOperationType operationType = listenNextOperation();
            if (operationType == null) {
                return;
            }
            processNextOperation(operationType);

        }
    }

    private ConsoleOperationType listenNextOperation() {
        System.out.println("\nPlease type next operations: ");
        prinAllAvailableOperations();
        System.out.println();
        while (!Thread.currentThread().isInterrupted()) {
            String nextOperation = scanner.nextLine();
            try {
                return ConsoleOperationType.valueOf(nextOperation);
            } catch (IllegalArgumentException e) {
                System.out.println("No such command found");
            }
        }
        return null;
    }

    private void prinAllAvailableOperations() {
        processorsMap.keySet()
                .forEach(System.out::println);
    }

    private void processNextOperation(ConsoleOperationType operation) {
        try {
            OperationCommandProcessor processor = processorsMap.get(operation);
            processor.processOperation();
        } catch (Exception e) {
            System.out.printf(
                    "Error executing command %s: error=%s%n", operation,
                    e.getMessage()
            );
        }
   }

    public void start() {
        System.out.println("Console listener started");
    }

    public void stop() {
        System.out.println("Console listener stopped");
    }
}
