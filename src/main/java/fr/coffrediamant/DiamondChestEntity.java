package fr.coffrediamant;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/** Exposes all 256 slots via vanilla Container to other inventory mods. */
public final class DiamondChestEntity extends BlockEntity implements Container {
    public static final int CAPACITY = 256;
    private NonNullList<ItemStack> items = NonNullList.withSize(CAPACITY, ItemStack.EMPTY);

    public DiamondChestEntity(BlockPos pos, BlockState state) {
        super(CoffreDiamantMod.CHEST_ENTITY, pos, state);
    }

    @Override public int getContainerSize() { return CAPACITY; }
    @Override public boolean isEmpty() { return items.stream().allMatch(ItemStack::isEmpty); }
    @Override public ItemStack getItem(int slot) { return valid(slot) ? items.get(slot) : ItemStack.EMPTY; }
    @Override public ItemStack removeItem(int slot, int count) {
        if (!valid(slot)) return ItemStack.EMPTY;
        ItemStack result = ContainerHelper.removeItem(items, slot, count);
        if (!result.isEmpty()) setChanged();
        return result;
    }
    @Override public ItemStack removeItemNoUpdate(int slot) {
        return valid(slot) ? ContainerHelper.takeItem(items, slot) : ItemStack.EMPTY;
    }
    @Override public void setItem(int slot, ItemStack stack) {
        if (!valid(slot)) return;
        items.set(slot, stack);
        stack.limitSize(getMaxStackSize(stack));
        setChanged();
    }
    @Override public boolean stillValid(Player player) {
        return level != null && level.getBlockEntity(worldPosition) == this
            && player.distanceToSqr(worldPosition.getX() + .5, worldPosition.getY() + .5,
                                    worldPosition.getZ() + .5) <= 64;
    }
    @Override public void clearContent() { items.clear(); items = NonNullList.withSize(CAPACITY, ItemStack.EMPTY); setChanged(); }
    private boolean valid(int slot) { return slot >= 0 && slot < CAPACITY; }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, items, registries);
    }
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        items = NonNullList.withSize(CAPACITY, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items, registries);
    }
}
