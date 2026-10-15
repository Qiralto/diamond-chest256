package fr.coffrediamant;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.GenericContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * 5 pages x 52 real slots, with 2 reserved navigation buttons.
 * Pages 1-4: 52 slots each; page 5: 48 slots; exactly 256 slots total.
 * Vanilla 9x6 client menu: no custom client-side screen or packets needed.
 */
public record DiamondChestPage(DiamondChestEntity chest, int page) implements MenuProvider {
    static final int PAGE_SLOTS = 52;
    static final int LAST_PAGE = 4;

    @Override public Component getDisplayName() {
        return Component.literal("Coffre diamant 256 - Page " + (page + 1) + "/5");
    }

    @Override public GenericContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new PageMenu(syncId, playerInventory, new PageInventory(chest, page), chest, page);
    }

    private static final class PageInventory implements Container {
        private final DiamondChestEntity backing;
        private final int page;
        private final SimpleContainer buttons = new SimpleContainer(2);
        PageInventory(DiamondChestEntity backing, int page) {
            this.backing = backing;
            this.page = page;
            ItemStack previous = new ItemStack(Items.ARROW);
            previous.set(net.minecraft.core.component.DataComponents.CUSTOM_NAME,
                    Component.literal(page == 0 ? "Première page" : "Page précédente"));
            ItemStack next = new ItemStack(Items.ARROW);
            next.set(net.minecraft.core.component.DataComponents.CUSTOM_NAME,
                    Component.literal(page == LAST_PAGE ? "Dernière page" : "Page suivante"));
            buttons.setItem(0, previous);
            buttons.setItem(1, next);
        }
        private int index(int slot) {
            int real = page * PAGE_SLOTS + slot;
            return slot >= 0 && slot < PAGE_SLOTS && real < DiamondChestEntity.CAPACITY ? real : -1;
        }
        @Override public int getContainerSize() { return 54; }
        @Override public boolean isEmpty() { return backing.isEmpty(); }
        @Override public ItemStack getItem(int slot) {
            int real = index(slot);
            return real >= 0 ? backing.getItem(real) : (slot >= 52 && slot <= 53 ? buttons.getItem(slot - 52) : ItemStack.EMPTY);
        }
        @Override public ItemStack removeItem(int slot, int count) {
            int real = index(slot);
            return real >= 0 ? backing.removeItem(real, count) : ItemStack.EMPTY;
        }
        @Override public ItemStack removeItemNoUpdate(int slot) {
            int real = index(slot);
            return real >= 0 ? backing.removeItemNoUpdate(real) : ItemStack.EMPTY;
        }
        @Override public void setItem(int slot, ItemStack stack) {
            int real = index(slot);
            if (real >= 0) backing.setItem(real, stack);
        }
        @Override public boolean canPlaceItem(int slot, ItemStack stack) { return index(slot) >= 0; }
        @Override public boolean stillValid(Player player) { return backing.stillValid(player); }
        @Override public void clearContent() { /* The page view must never clear the full inventory. */ }
        @Override public void setChanged() { backing.setChanged(); }
    }

    private static final class PageMenu extends GenericContainerMenu {
        private final DiamondChestEntity chest;
        private final int page;
        PageMenu(int id, Inventory playerInventory, Container inventory, DiamondChestEntity chest, int page) {
            super(net.minecraft.world.inventory.MenuType.GENERIC_9x6, id, playerInventory, inventory, 6);
            this.chest = chest;
            this.page = page;
        }
        @Override public void clicked(int slot, int button, ClickType clickType, Player player) {
            if (slot == 52 || slot == 53) {
                if (player instanceof ServerPlayer serverPlayer) {
                    int newPage = Math.max(0, Math.min(LAST_PAGE, page + (slot == 52 ? -1 : 1)));
                    if (newPage != page) serverPlayer.openMenu(new DiamondChestPage(chest, newPage));
                }
                return;
            }
            super.clicked(slot, button, clickType, player);
        }
        @Override public ItemStack quickMoveStack(Player player, int index) {
            if (index == 52 || index == 53) return ItemStack.EMPTY;
            return super.quickMoveStack(player, index);
        }
    }
}
