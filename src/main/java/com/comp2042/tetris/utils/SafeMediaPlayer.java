package com.comp2042.tetris.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;

/**
 * A lightweight wrapper around JavaFX MediaPlayer accessed via reflection.
 * This avoids a compile-time dependency on javafx.media for IDEs that don't
 * have the module on the language server classpath. At runtime, if JavaFX
 * media is available this will delegate to the real classes; otherwise it
 * becomes a no-op player.
 */
public final class SafeMediaPlayer {
    private final Object media; // javafx.scene.media.Media instance or null
    private final Object player; // javafx.scene.media.MediaPlayer instance or null
    private final Method setVolumeMethod;
    private final Method playMethod;
    private final Method stopMethod;
    private final Method disposeMethod;
    private final Method setCycleCountMethod;
    private final Method setOnEndOfMediaMethod;
    private final Method getVolumeMethod;
    private final Method getTotalDurationMethod;
    private final Method seekMethod;
    private final Object durationZeroObject;

    public SafeMediaPlayer(URL mediaUrl) {
        Object m = null;
        Object p = null;
        Method setVol = null;
        Method play = null;
        Method stop = null;
        Method dispose = null;
        Method setCycle = null;
        Method setOnEndOfMedia = null;
        Method getVol = null;
        Method totalDur = null;
        // keep seek and duration ZERO in outer scope so we can assign to final fields after try
        Method seekM = null;
        Object durZero = null;
        try {
            Class<?> mediaClass = Class.forName("javafx.scene.media.Media");
            Class<?> playerClass = Class.forName("javafx.scene.media.MediaPlayer");
            Constructor<?> mediaCtor = mediaClass.getConstructor(String.class);
            m = mediaCtor.newInstance(mediaUrl.toString());
            Constructor<?> playerCtor = playerClass.getConstructor(mediaClass);
            p = playerCtor.newInstance(m);
            setVol = playerClass.getMethod("setVolume", double.class);
            play = playerClass.getMethod("play");
            stop = playerClass.getMethod("stop");
            dispose = playerClass.getMethod("dispose");
            setCycle = playerClass.getMethod("setCycleCount", int.class);
            // MediaPlayer#setOnEndOfMedia exists and accepts a Runnable
            try {
                setOnEndOfMedia = playerClass.getMethod("setOnEndOfMedia", Runnable.class);
            } catch (NoSuchMethodException ignore) {
                setOnEndOfMedia = null;
            }
            getVol = playerClass.getMethod("getVolume");
            // MediaPlayer#getTotalDuration exists and returns javafx.util.Duration
            try {
                totalDur = playerClass.getMethod("getTotalDuration");
            } catch (NoSuchMethodException ignore) {
                totalDur = null;
            }
            // Try to locate seek(Duration) and Duration.ZERO for reliable restarts
            try {
                Class<?> durationClass = Class.forName("javafx.util.Duration");
                seekM = playerClass.getMethod("seek", durationClass);
                try { durZero = durationClass.getField("ZERO").get(null); } catch (Throwable ignored) { durZero = null; }
            } catch (Throwable ignored) {
                seekM = null; durZero = null;
            }
            // assign to locals

        } catch (Throwable t) {
            // JavaFX media not available or failed to initialize - fall back to no-op
            m = null;
            p = null;
            setVol = null;
            play = null;
            stop = null;
            dispose = null;
            setCycle = null;
            setOnEndOfMedia = null;
            getVol = null;
        }
        media = m;
        player = p;
        setVolumeMethod = setVol;
        playMethod = play;
        stopMethod = stop;
        disposeMethod = dispose;
        setCycleCountMethod = setCycle;
        setOnEndOfMediaMethod = setOnEndOfMedia;
        getVolumeMethod = getVol;
        // assign the final field exactly once from the local variable
        getTotalDurationMethod = totalDur;
        seekMethod = seekM;
        durationZeroObject = durZero;
    }

    public boolean isAvailable() {
        return player != null;
    }

    public void play() {
        try {
            if (playMethod != null) playMethod.invoke(player);
        } catch (Throwable ignored) {
        }
    }

    public void stop() {
        try {
            if (stopMethod != null) stopMethod.invoke(player);
        } catch (Throwable ignored) {
        }
    }

    public void setVolume(double v) {
        try {
            if (setVolumeMethod != null) setVolumeMethod.invoke(player, v);
        } catch (Throwable ignored) {
        }
    }

    public double getVolume() {
        try {
            if (getVolumeMethod != null) {
                Object res = getVolumeMethod.invoke(player);
                if (res instanceof Double) return (Double) res;
            }
        } catch (Throwable ignored) {
        }
        return 0.0;
    }

    /**
     * Returns the total duration of the underlying media in seconds, or 0.0 if unavailable.
     */
    public double getTotalDurationSeconds() {
        try {
            if (getTotalDurationMethod == null) return 0.0;
            Object dur = getTotalDurationMethod.invoke(player);
            if (dur == null) return 0.0;
            // javafx.util.Duration has toSeconds() method
            try {
                Method toSeconds = dur.getClass().getMethod("toSeconds");
                Object res = toSeconds.invoke(dur);
                if (res instanceof Double) return (Double) res;
            } catch (NoSuchMethodException ignore) {
                // try getSeconds
                try {
                    Method getSeconds = dur.getClass().getMethod("toMillis");
                    Object ms = getSeconds.invoke(dur);
                    if (ms instanceof Double) return ((Double) ms) / 1000.0;
                } catch (Throwable ignored) {}
            }
        } catch (Throwable ignored) {}
        return 0.0;
    }

    public void setCycleCount(int c) {
        try {
            if (setCycleCountMethod != null) setCycleCountMethod.invoke(player, c);
        } catch (Throwable ignored) {
        }
    }

    public void dispose() {
        try {
            if (disposeMethod != null) disposeMethod.invoke(player);
        } catch (Throwable ignored) {
        }
    }

    /**
     * If JavaFX MediaPlayer is available, register a Runnable to be executed when media ends.
     * This is a no-op when JavaFX media is not available.
     */
    public void setOnEndOfMedia(Runnable r) {
        try {
            if (setOnEndOfMediaMethod != null) setOnEndOfMediaMethod.invoke(player, r);
        } catch (Throwable ignored) {
        }
    }

    /**
     * Attempt to restart playback from the beginning reliably.
     * If MediaPlayer.seek(Duration.ZERO) is available, use it; otherwise fall back to stop()/play().
     */
    public void playFromStart() {
        try {
            if (seekMethod != null && durationZeroObject != null) {
                // seek to zero then play
                seekMethod.invoke(player, durationZeroObject);
                if (playMethod != null) playMethod.invoke(player);
                return;
            }
        } catch (Throwable ignored) {
            // fall through to stop/play fallback
        }
        // fallback
        try {
            if (stopMethod != null) stopMethod.invoke(player);
        } catch (Throwable ignored) {}
        try { if (playMethod != null) playMethod.invoke(player); } catch (Throwable ignored) {}
    }

}
