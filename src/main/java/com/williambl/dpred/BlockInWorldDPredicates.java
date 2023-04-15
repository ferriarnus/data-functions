package com.williambl.dpred;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.material.Fluid;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.williambl.dpred.DatapackablePredicates.id;

public final class BlockInWorldDPredicates {
    public static final DPredicateType<BlockInWorld, ? extends Function<Boolean, ? extends DPredicate<BlockInWorld>>> CONSTANT = Registry.register(
            DPredicate.BLOCK_IN_WORLD_PREDICATE_TYPE_REGISTRY.registry(),
            id("constant"),
            DPredicate.<Boolean, BlockInWorld>create(
                    Codec.BOOL.fieldOf("value"),
                    (value, block) -> value
            )
    );

    public static final DPredicateType<BlockInWorld, ? extends Function<List<DPredicate<BlockInWorld>>, ? extends DPredicate<BlockInWorld>>> AND = Registry.register(
            DPredicate.BLOCK_IN_WORLD_PREDICATE_TYPE_REGISTRY.registry(),
            id("and"),
            DPredicate.<List<DPredicate<BlockInWorld>>, BlockInWorld>create(
                    DPredicate.BLOCK_IN_WORLD_PREDICATE_TYPE_REGISTRY.codec().listOf().fieldOf("predicates"),
                    (predicates, block) -> predicates.stream().allMatch(p -> p.test(block))
            )
    );

    public static final DPredicateType<BlockInWorld, ? extends Function<List<DPredicate<BlockInWorld>>, ? extends DPredicate<BlockInWorld>>> OR = Registry.register(
            DPredicate.BLOCK_IN_WORLD_PREDICATE_TYPE_REGISTRY.registry(),
            id("or"),
            DPredicate.<List<DPredicate<BlockInWorld>>, BlockInWorld>create(
                    DPredicate.BLOCK_IN_WORLD_PREDICATE_TYPE_REGISTRY.codec().listOf().fieldOf("predicates"),
                    (predicates, block) -> predicates.stream().anyMatch(p -> p.test(block))
            )
    );

    public static final DPredicateType<BlockInWorld, ? extends Function<DPredicate<BlockInWorld>, ? extends DPredicate<BlockInWorld>>> NOT = Registry.register(
            DPredicate.BLOCK_IN_WORLD_PREDICATE_TYPE_REGISTRY.registry(),
            id("not"),
            DPredicate.<DPredicate<BlockInWorld>, BlockInWorld>create(
                    DPredicate.BLOCK_IN_WORLD_PREDICATE_TYPE_REGISTRY.codec().fieldOf("predicate"),
                    (predicate, block) -> !predicate.test(block)
            )
    );

    public static final DPredicateType<BlockInWorld, ? extends Function<BlockPredicate, ? extends DPredicate<BlockInWorld>>> ADVANCEMENT_PREDICATE = Registry.register(
            DPredicate.BLOCK_IN_WORLD_PREDICATE_TYPE_REGISTRY.registry(),
            id("advancement_predicate"),
            DPredicate.<BlockPredicate, BlockInWorld>create(
                    DatapackablePredicates.ADVANCEMENT_BLOCK_PREDICATE_CODEC.fieldOf("predicate"),
                    (predicate, block) -> block.getLevel() instanceof ServerLevel sLevel && predicate.matches(sLevel, block.getPos())
            ));

    static void init() {}
}
