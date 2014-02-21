/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vi.machello.util;

/**
 *
 * @author Kyle D. Williams
 */
public class Pair<L, R> implements Comparable<Pair<L, R>> {

    private final L Left;
    private final R Right;

    public static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair<L, R>(left, right);
    }

    public Pair(L left, R right) {
        this.Left = left;
        this.Right = right;
    }

    public L getLeft() {
        return Left;
    }

    public R getRight() {
        return Right;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.Left != null ? this.Left.hashCode() : 0);
        hash = 53 * hash + (this.Right != null ? this.Right.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pair<L, R> other = (Pair<L, R>) obj;
        if (this.Left != other.Left && (this.Left == null || !this.Left.equals(other.Left))) {
            return false;
        }
        if (this.Right != other.Right && (this.Right == null || !this.Right.equals(other.Right))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        sb.append(Left).append(",").append(Right).append("]");
        return sb.toString();
    }

    private int cmp(Object lhs, Object rhs) {
        if (lhs == rhs) {
            return 0;
        } else if (lhs != null && Comparable.class.isAssignableFrom(lhs.getClass())) {
            return ((Comparable) lhs).compareTo(rhs);
        }
        return (lhs == null ? 0 : lhs.hashCode()) - (rhs == null ? 0 : rhs.hashCode());
    }

    @Override
    public int compareTo(Pair<L, R> o) {
        int diff = cmp(Left, o.Left);
        if (diff == 0) {
            diff = cmp(Right, o.Right);
        }
        return diff;
    }
}