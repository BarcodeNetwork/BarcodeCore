package io.lumine.mythic.lib.api.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * For any other C# dev out there who may not be familiar with lambdas
 * and all that, I present to you the Java version of the <code>ref</code>
 * keyword.
 * @see #RefExample()
 * @author Gunging
 */
@SuppressWarnings("unused")
public class Ref<E> {

    /**
     * Value wrapped by this Ref. Modify it anywhere, and the original
     * Ref reference will know about it.
     */
    @Nullable E ref;

    /**
     * Initialize the value of this <code>ref</code>.
     */
    public Ref(@Nullable E e ) { ref = e; }

    /**
     * A ref with a null value.
     */
    public Ref() { ref = null; }

    /**
     * The value enclosed within this ref
     */
    @Nullable public E getValue() { return ref; }

    /**
     * The value enclosed within this ref, or the default if its null.
     */
    @NotNull public E getValue(@NotNull E def) { if (ref == null) { return def; } else { return ref; } }

    /**
     * Change the value enclosed within this ref.
     */
    public void setValue(@Nullable E e ){ this.ref = e; }

    public static <S> void setValue(@Nullable Ref<S> ref, @Nullable S value) { if (ref != null) { ref.setValue(value); } }

    //region Example Code Snippet
    /**
     * A sample method showing how to use this class. A code snippet.
     */
    void RefExample() {

        /*
         *  Start with a normal double and its analogous Ref
         */
        double d_primitive = 20D;
        Double d_class = 20D;
        Ref<Double> d_ref = new Ref<>(20D);

        /*
         *  Send them through some method.
         *  This method adds 10 to their values.
         */
        RefExample_Process(d_primitive, d_class, d_ref);

        /*
         * What are their final values?
         */
        Print("Primitive: " + d_primitive);
        Print("Class: " + d_class);
        Print("Ref: " + d_ref.getValue());

        /*
         * Output:
         *
         * Primitive: 20
         * Class: 20
         * Ref: 30
         *
         * The whole point of this is that the value of the Ref
         * is affected by what happened within the method, unlike
         * the other two versions which are oblivious.
         */
    }

    /**
     * Used in {@link #RefExample()}. Adds 10 to all these quantities (locally).
     */
    @SuppressWarnings("UnusedAssignment")
    void RefExample_Process(double d_primitive, Double d_class, Ref<Double> d_ref) {

        // Adds 10 to the primitive
        d_primitive += 10;

        // Adds 10 to the wrapped one
        d_class += 10;

        // Adds 10 to the double within the Ref
        d_ref.setValue(d_ref.getValue() + 10);
    }

    /**
     * Used in {@link #RefExample()}. Supposedly prints something onto the console.
     */
    void Print(String log) {}
    //endregion
}
