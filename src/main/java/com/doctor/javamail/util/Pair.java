package com.doctor.javamail.util;

/**
 * @author cuikexiang
 *
 *         Create At 2016年6月8日 下午3:04:29
 */
public class Pair<L, R> {
    private final L l;
    private final R r;

    public Pair(L l, R r) {
        this.l = l;
        this.r = r;
    }

    public L getL() {
        return l;
    }

    public R getR() {
        return r;
    }

    @Override
    public String toString() {
        return "[ L:" + l + "," + "R:" + r + " ]";
    }

    public static class PairBuider<L, R> {
        private L l;
        private R r;

        public PairBuider<L, R> setLeft(L l) {
            this.l = l;
            return this;
        }

        public PairBuider<L, R> setRight(R r) {
            this.r = r;
            return this;
        }

        public Pair<L, R> build() {
            return new Pair<L, R>(l, r);
        }
    }
}
