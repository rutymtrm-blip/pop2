package net.raphimc.immediatelyfast.event.events;

import net.raphimc.immediatelyfast.event.Event;
import net.raphimc.immediatelyfast.event.Listener;
import java.util.ArrayList;

public class RotationEvent extends Event<Listener> {
   private float pitch;
   private float yaw;
   private final float staticPitch;
   private final float staticYaw;
   private boolean strictMoveCorrection;

   public RotationEvent(float f, float f1, boolean flag) {
      this.pitch = f;
      this.yaw = f1;
      this.staticPitch = f;
      this.staticYaw = f1;
      this.strictMoveCorrection = flag;
   }

   public boolean isModified() {
      return this.pitch != this.staticPitch || this.yaw != this.staticYaw;
   }

   public void setPitch(float f) {
      if (Float.isNaN(f) || Float.isInfinite(f)) return;
      this.pitch = Math.max(-90.0F, Math.min(90.0F, f));
   }

   public float getPitch() {
      return this.pitch;
   }

   public void setYaw(float f) {
      if (Float.isNaN(f) || Float.isInfinite(f)) return;
      this.yaw = f;
   }

   public float getYaw() {
      return this.yaw;
   }

   public float getStaticPitch() {
      return this.staticPitch;
   }

   public float getStaticYaw() {
      return this.staticYaw;
   }

   public void setStrictMoveCorrection(boolean flag) {
      this.strictMoveCorrection = flag;
   }

   public boolean isStrictMoveCorrection() {
      return this.strictMoveCorrection;
   }

   @Override
   public void fire(ArrayList<Listener> listeners) {
      // Not used in this implementation
   }

   @Override
   public Class<Listener> getListenerType() {
      return Listener.class;
   }
}
