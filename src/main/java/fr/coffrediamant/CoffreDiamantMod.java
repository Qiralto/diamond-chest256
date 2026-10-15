package fr.coffrediamant;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class CoffreDiamantMod implements ModInitializer {
    public static final String ID = "coffrediamant256";
    public static final ResourceLocation CHEST_ID = ResourceLocation.fromNamespaceAndPath(ID, "diamond_chest");
    public static Block CHEST;
    public static BlockEntityType<DiamondChestEntity> CHEST_ENTITY;

    @Override
    public void onInitialize() {
        CHEST = Registry.register(BuiltInRegistries.BLOCK, CHEST_ID,
                new DiamondChestBlock(Block.Properties.of().strength(5.0f).requiresCorrectToolForDrops()));
        Registry.register(BuiltInRegistries.ITEM, CHEST_ID,
                new BlockItem(CHEST, new Item.Properties()));
        CHEST_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(ID, "diamond_chest_entity"),
                FabricBlockEntityTypeBuilder.create(DiamondChestEntity::new, CHEST).build());
    }
}
