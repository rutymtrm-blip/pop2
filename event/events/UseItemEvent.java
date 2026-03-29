package net.raphimc.immediatelyfast.event.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.raphimc.immediatelyfast.event.CancellableEvent;
import net.raphimc.immediatelyfast.event.Listener;
import java.util.ArrayList;

public class UseItemEvent extends CancellableEvent<Listener> {
   private final PlayerEntity player;
   private final Hand hand;
   private final HitResult target;
   private boolean cancelled = false;

   public UseItemEvent(PlayerEntity playerentity, Hand hand, HitResult hitresult) {
      this.player = playerentity;
      this.hand = hand;
      this.target = hitresult;
   }

   public PlayerEntity getPlayer() {
      return this.player;
   }

   public Hand getHand() {
      return this.hand;
   }

   public HitResult getTarget() {
      return this.target;
   }

   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   public boolean isCancelled() {
      return this.cancelled;
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
