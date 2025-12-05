package com.comp2042.tetris.controller.command;

import com.comp2042.tetris.controller.core.GameActionInvoker;
import com.comp2042.tetris.model.event.EventSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class GameCommandTest {

    private RecordingInvoker invoker;

    @BeforeEach
    void setUp() {
        invoker = new RecordingInvoker();
    }

    @Nested
    @DisplayName("MoveDownCommand Tests")
    class MoveDownCommandTests {

        @Test
        @DisplayName("execute invokes moveDown on invoker")
        void execute_invokesMoveDown() {
            MoveDownCommand command = new MoveDownCommand(invoker);

            command.execute();

            assertEquals(1, invoker.moveDownCalls);
        }

        @Test
        @DisplayName("multiple executions invoke moveDown multiple times")
        void execute_multipleInvocations() {
            MoveDownCommand command = new MoveDownCommand(invoker);

            command.execute();
            command.execute();
            command.execute();

            assertEquals(3, invoker.moveDownCalls);
        }
    }

    @Nested
    @DisplayName("MoveLeftCommand Tests")
    class MoveLeftCommandTests {

        @Test
        @DisplayName("execute invokes moveLeft on invoker")
        void execute_invokesMoveLeft() {
            MoveLeftCommand command = new MoveLeftCommand(invoker);

            command.execute();

            assertEquals(1, invoker.moveLeftCalls);
        }

        @Test
        @DisplayName("multiple executions invoke moveLeft multiple times")
        void execute_multipleInvocations() {
            MoveLeftCommand command = new MoveLeftCommand(invoker);

            command.execute();
            command.execute();

            assertEquals(2, invoker.moveLeftCalls);
        }
    }

    @Nested
    @DisplayName("MoveRightCommand Tests")
    class MoveRightCommandTests {

        @Test
        @DisplayName("execute invokes moveRight on invoker")
        void execute_invokesMoveRight() {
            MoveRightCommand command = new MoveRightCommand(invoker);

            command.execute();

            assertEquals(1, invoker.moveRightCalls);
        }

        @Test
        @DisplayName("multiple executions invoke moveRight multiple times")
        void execute_multipleInvocations() {
            MoveRightCommand command = new MoveRightCommand(invoker);

            command.execute();
            command.execute();

            assertEquals(2, invoker.moveRightCalls);
        }
    }

    @Nested
    @DisplayName("RotateCommand Tests")
    class RotateCommandTests {

        @Test
        @DisplayName("execute invokes rotate on invoker")
        void execute_invokesRotate() {
            RotateCommand command = new RotateCommand(invoker);

            command.execute();

            assertEquals(1, invoker.rotateCalls);
        }

        @Test
        @DisplayName("multiple executions invoke rotate multiple times")
        void execute_multipleInvocations() {
            RotateCommand command = new RotateCommand(invoker);

            command.execute();
            command.execute();
            command.execute();
            command.execute();

            assertEquals(4, invoker.rotateCalls);
        }
    }

    @Nested
    @DisplayName("InstantDropCommand Tests")
    class InstantDropCommandTests {

        @Test
        @DisplayName("execute invokes instantDrop on invoker")
        void execute_invokesInstantDrop() {
            InstantDropCommand command = new InstantDropCommand(invoker);

            command.execute();

            assertEquals(1, invoker.instantDropCalls);
        }

        @Test
        @DisplayName("multiple executions invoke instantDrop multiple times")
        void execute_multipleInvocations() {
            InstantDropCommand command = new InstantDropCommand(invoker);

            command.execute();
            command.execute();

            assertEquals(2, invoker.instantDropCalls);
        }
    }

    @Nested
    @DisplayName("Command Independence Tests")
    class CommandIndependenceTests {

        @Test
        @DisplayName("Different commands invoke different methods")
        void differentCommands_invokeDifferentMethods() {
            GameCommand moveDown = new MoveDownCommand(invoker);
            GameCommand moveLeft = new MoveLeftCommand(invoker);
            GameCommand moveRight = new MoveRightCommand(invoker);
            GameCommand rotate = new RotateCommand(invoker);
            GameCommand instantDrop = new InstantDropCommand(invoker);

            moveDown.execute();
            moveLeft.execute();
            moveRight.execute();
            rotate.execute();
            instantDrop.execute();

            assertEquals(1, invoker.moveDownCalls);
            assertEquals(1, invoker.moveLeftCalls);
            assertEquals(1, invoker.moveRightCalls);
            assertEquals(1, invoker.rotateCalls);
            assertEquals(1, invoker.instantDropCalls);
        }

        @Test
        @DisplayName("Commands with same invoker share state")
        void commandsWithSameInvoker_shareState() {
            GameCommand command1 = new MoveDownCommand(invoker);
            GameCommand command2 = new MoveDownCommand(invoker);

            command1.execute();
            command2.execute();

            assertEquals(2, invoker.moveDownCalls);
        }

        @Test
        @DisplayName("Commands with different invokers are independent")
        void commandsWithDifferentInvokers_areIndependent() {
            RecordingInvoker invoker2 = new RecordingInvoker();
            GameCommand command1 = new MoveDownCommand(invoker);
            GameCommand command2 = new MoveDownCommand(invoker2);

            command1.execute();
            command1.execute();
            command2.execute();

            assertEquals(2, invoker.moveDownCalls);
            assertEquals(1, invoker2.moveDownCalls);
        }
    }

    /**
     * Test double that records method invocations.
     */
    private static class RecordingInvoker implements GameActionInvoker {
        int moveDownCalls = 0;
        int moveDownWithSourceCalls = 0;
        int moveLeftCalls = 0;
        int moveRightCalls = 0;
        int rotateCalls = 0;
        int instantDropCalls = 0;

        @Override
        public void moveDown(EventSource source) {
            moveDownWithSourceCalls++;
        }

        @Override
        public void moveDown() {
            moveDownCalls++;
        }

        @Override
        public void moveLeft() {
            moveLeftCalls++;
        }

        @Override
        public void moveRight() {
            moveRightCalls++;
        }

        @Override
        public void rotate() {
            rotateCalls++;
        }

        @Override
        public void instantDrop() {
            instantDropCalls++;
        }
    }
}
