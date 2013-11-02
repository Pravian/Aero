package net.pravian.bukkitlib.generator.skygrid;

import java.util.Random;

public final class RandomSeries {

    private int[] reset;
    private int[] series;
    private int pos;

    public RandomSeries(int size) {
        this.reset = new int[size];
        for (int i = 0; i < size; i++) {
            this.reset[i] = i;
        }
        reset();
    }

    public void reset() {
        this.pos = 0;
        this.series = ((int[]) this.reset.clone());
    }

    public int next(Random random) {
        int slot = this.pos + random.nextInt(this.series.length - this.pos);
        int temp = this.series[this.pos];
        this.series[this.pos] = this.series[slot];
        this.series[slot] = temp;
        return this.series[(this.pos++)];
    }
}