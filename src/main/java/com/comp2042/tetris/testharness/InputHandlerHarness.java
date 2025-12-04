package com.comp2042.tetris.testharness;

import com.comp2042.tetris.controller.InputHandler;
import com.comp2042.tetris.controller.IGameController;
import com.comp2042.tetris.controller.InputEventListener;
import com.comp2042.tetris.controller.command.DefaultCommandRegistry;
import com.comp2042.tetris.controller.command.DefaultInputCommandFactory;
import com.comp2042.tetris.controller.command.InputCommandFactory;
import com.comp2042.tetris.controller.command.CommandRegistry;
import com.comp2042.tetris.model.event.MoveEvent;
import com.comp2042.tetris.model.event.EventSource;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;

/**
 * Simple harness to verify InputHandler horizontal inversion behaviour.
 */
public class InputHandlerHarness {
    public static void main(String[] args) {
        // suppliers for pause/gameover (not paused, not gameover)
        BooleanSupplier notPaused = () -> false;
        BooleanSupplier notGameOver = () -> false;

        CommandRegistry commandRegistry = new DefaultCommandRegistry();
        InputCommandFactory inputCommandFactory = new DefaultInputCommandFactory();

        InputHandler inputHandler = new InputHandler(notPaused, notGameOver, commandRegistry, inputCommandFactory);

        // Stub controller to capture events
        RecordingController controller = new RecordingController();
        inputHandler.setGameController(controller);

        System.out.println("=== InputHandler inversion test ===");

        // Ensure normal behaviour
        inputHandler.setInvertHorizontal(false);
        controller.reset();
        inputHandler.moveLeft();
        System.out.println("After moveLeft with invert=false -> last event: " + controller.getLast());
        controller.reset();
        inputHandler.moveRight();
        System.out.println("After moveRight with invert=false -> last event: " + controller.getLast());

        // Enable inversion
        inputHandler.setInvertHorizontal(true);
        controller.reset();
        inputHandler.moveLeft();
        System.out.println("After moveLeft with invert=true -> last event: " + controller.getLast());
        controller.reset();
        inputHandler.moveRight();
        System.out.println("After moveRight with invert=true -> last event: " + controller.getLast());

        // Done
        System.out.println("=== Test complete ===");
    }

    private static class RecordingController implements IGameController, InputEventListener {
        private final AtomicReference<String> last = new AtomicReference<>("<none>");

        void reset() { last.set("<none>"); }
        String getLast() { return last.get(); }

        @Override
        public void onDownEvent(MoveEvent event) { last.set("DOWN (" + event.getEventSource() + ")"); }

        @Override
        public void onLeftEvent(MoveEvent event) { last.set("LEFT (" + event.getEventSource() + ")"); }

        @Override
        public void onRightEvent(MoveEvent event) { last.set("RIGHT (" + event.getEventSource() + ")"); }

        @Override
        public void onRotateEvent(MoveEvent event) { last.set("ROTATE (" + event.getEventSource() + ")"); }

        @Override
        public void onInstantDropEvent(MoveEvent event) { last.set("INSTANT_DROP (" + event.getEventSource() + ")"); }

        @Override
        public void createNewGame() { last.set("CREATE_NEW_GAME"); }

        @Override
        public void setLevel(com.comp2042.tetris.model.level.GameLevel level) {
            last.set("SET_LEVEL:" + (level != null ? level.getName() : "null"));
        }
    }
}
