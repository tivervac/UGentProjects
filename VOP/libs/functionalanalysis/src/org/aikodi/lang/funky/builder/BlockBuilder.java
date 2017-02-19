package org.aikodi.lang.funky.builder;

import com.google.java.contract.Ensures;
import org.aikodi.chameleon.util.Box;
import org.aikodi.lang.funky.behavior.Block;
import org.aikodi.lang.funky.behavior.LabeledBehavior;
import org.aikodi.lang.funky.behavior.description.Description;

import java.util.function.Consumer;

/**
 * A builder that builds a block that consists of a number
 * of behavior objects.
 * <p>
 * The block is passed to the {@link #consumer()} when it is closed
 * using {@link #endBlock()}.
 *
 * @param <P> The type of the parent builder.
 * @author Marko van Dooren
 */
public class BlockBuilder<P> extends Builder<P, Block> {

    private Block _block;

    /**
     * Create a new block builder with the given parent builder and consumer.
     * The objects that are created will be passed to the consumer.
     * <p>
     * Both parameters can be null.
     *
     * @param parent   The parent builder.
     * @param consumer A consumer that will process the created objects.
     */
    @Ensures({"parent() == parent",
            "consumer() == consumer"})
    public BlockBuilder(P parent, Consumer<Block> consumer) {
        super(parent, consumer);
        _block = new Block();
        consumer().accept(_block);
    }

    /**
     * @return The block that is being constructed.
     */
    public Block block() {
        return _block;
    }

    /**
     * @return a nested builder for actually constructing behavior
     * objects other than blocks.
     */
    protected BehaviorBuilder<BlockBuilder<P>> nested() {
        return new BehaviorBuilder<BlockBuilder<P>>(this, b -> {
            block().add(b);
        });
    }

    /**
     * Add a step to the block.
     *
     * @param text     A textual description of the behavior of the step.
     * @param executor The fully qualified name of the entity that executes
     *                 the step.
     * @return this
     */
    public BlockBuilder<P> step(String text, String executor) {
        nested().step(text, executor);
        return this;
    }

    /**
     * Add a step to the block.
     *
     * @param description A description of the behavior of the step.
     * @param executor    The fully qualified name of the entity that executes
     *                    the step.
     * @return this
     */
    public BlockBuilder<P> step(Description description, String executor) {
        nested().step(description, executor);
        return this;
    }

    /**
     * Add a labeled step and return a builder to create the actual
     * behavior of the labeled step.
     *
     * @param text The name of the label.
     * @return a builder for creating the actual behavior of the labeled
     * step. The parent of the builder is this, and the consumer
     * of the builder added the created object at the end of the
     * block being constructed.
     */
    public BehaviorBuilder<BlockBuilder<P>> label(String text) {
        return label(text, l -> {
        });
    }

    public BlockBuilder<P> returnTo(String targetStep, String executor) {
        nested().returnTo(targetStep, executor);
        return this;
    }

    /**
     * Add a labeled step and return a builder to create the actual
     * behavior of the labeled step.
     * <p>
     * An additional consumer can be passed to easily extract the labeled
     * behavior object without stopping the method chaining. Since
     * a consumer cannot assign to a non-final external variable, you should
     * let the peeking consumer store the created object in a {@link Box}.
     * <p>
     * <pre>
     * {@code
     * final Box<LabeledBehavior> box = new Box<>();
     * blockBuilder.label("my label", l -> {box.set(l);})
     *             .step("description of the step")
     * }
     * </pre>
     *
     * @param text   The name of the label.
     * @param peeker An additional consumer that will also received the
     *               created labeled behavior object.
     * @return a builder for creating the actual behavior of the labeled
     * step. The parent of the builder is this, and the consumer
     * of the builder added the created object at the end of the
     * block being constructed.
     */
    public BehaviorBuilder<BlockBuilder<P>> label(String text, Consumer<LabeledBehavior> peeker) {
        return nested().label(text, peeker);
    }

    public BehaviorBuilder<BlockBuilder<P>> alternate(String target) {
        return nested().alternate(target);
    }

    public BehaviorBuilder<BlockBuilder<P>> exception(String target) {
        return nested().exception(target);
    }

    /**
     * Create a builder to construct a while loop.
     *
     * @return a builder that creates a while loop. The parent of the builder
     * is this block builder. The consumer will add the created loop
     * at the end of this block.
     */
    public WhileBuilder<BlockBuilder<P>> doWhile() {
        return nested().doWhile();
    }

    /**
     * Create a builder for a nested block.
     *
     * @return a builder that creates a block. The parent of the builder
     * is this block builder. The consumer will add the created block
     * at the end of this block.
     */
    public BlockBuilder<BlockBuilder<P>> openBlock() {
        BlockBuilder<BlockBuilder<P>> builder = new BlockBuilder<BlockBuilder<P>>(this, b -> {
            _block.add(b);
        });
        return builder;
    }

    /**
     * Close this block by passing the created block to the consumer,
     * and returning the parent builder.
     *
     * @return the parent builder.
     */
    public P endBlock() {
        return parent();
    }
}
