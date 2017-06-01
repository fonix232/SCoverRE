package com.samsung.android.contextaware.aggregator.lpp.algorithm;

import java.io.Serializable;
import java.lang.reflect.Array;

public class MatrixSingularValueDecomposition implements Serializable {
    private static final long serialVersionUID = 1;
    private double[][] f165U;
    private double[][] f166V;
    private int f167m;
    private int f168n;
    private double[] f169s = new double[Math.min(this.f167m + 1, this.f168n)];

    public MatrixSingularValueDecomposition(Matrix matrix) {
        int i;
        int i2;
        double d;
        double[][] arrayCopy = matrix.getArrayCopy();
        this.f167m = matrix.getRowDimension();
        this.f168n = matrix.getColumnDimension();
        int min = Math.min(this.f167m, this.f168n);
        this.f165U = (double[][]) Array.newInstance(Double.TYPE, new int[]{this.f167m, min});
        this.f166V = (double[][]) Array.newInstance(Double.TYPE, new int[]{this.f168n, this.f168n});
        double[] dArr = new double[this.f168n];
        double[] dArr2 = new double[this.f167m];
        int min2 = Math.min(this.f167m - 1, this.f168n);
        int max = Math.max(0, Math.min(this.f168n - 2, this.f167m));
        int i3 = 0;
        while (i3 < Math.max(min2, max)) {
            double[] dArr3;
            if (i3 < min2) {
                this.f169s[i3] = 0.0d;
                for (i = i3; i < this.f167m; i++) {
                    this.f169s[i3] = Matrix.hypot(this.f169s[i3], arrayCopy[i][i3]);
                }
                if (this.f169s[i3] != 0.0d) {
                    if (arrayCopy[i3][i3] < 0.0d) {
                        this.f169s[i3] = -this.f169s[i3];
                    }
                    for (i = i3; i < this.f167m; i++) {
                        dArr3 = arrayCopy[i];
                        dArr3[i3] = dArr3[i3] / this.f169s[i3];
                    }
                    dArr3 = arrayCopy[i3];
                    dArr3[i3] = dArr3[i3] + 1.0d;
                }
                this.f169s[i3] = -this.f169s[i3];
            }
            for (i2 = i3 + 1; i2 < this.f168n; i2++) {
                if (i3 < min2 && this.f169s[i3] != 0.0d) {
                    d = 0.0d;
                    for (i = i3; i < this.f167m; i++) {
                        d += arrayCopy[i][i3] * arrayCopy[i][i2];
                    }
                    d = (-d) / arrayCopy[i3][i3];
                    for (i = i3; i < this.f167m; i++) {
                        dArr3 = arrayCopy[i];
                        dArr3[i2] = dArr3[i2] + (arrayCopy[i][i3] * d);
                    }
                }
                dArr[i2] = arrayCopy[i3][i2];
            }
            if ((i3 < min2 ? 1 : null) != null) {
                for (i = i3; i < this.f167m; i++) {
                    this.f165U[i][i3] = arrayCopy[i][i3];
                }
            }
            if (i3 < max) {
                dArr[i3] = 0.0d;
                for (i = i3 + 1; i < this.f168n; i++) {
                    dArr[i3] = Matrix.hypot(dArr[i3], dArr[i]);
                }
                if (dArr[i3] != 0.0d) {
                    if (dArr[i3 + 1] < 0.0d) {
                        dArr[i3] = -dArr[i3];
                    }
                    for (i = i3 + 1; i < this.f168n; i++) {
                        dArr[i] = dArr[i] / dArr[i3];
                    }
                    int i4 = i3 + 1;
                    dArr[i4] = dArr[i4] + 1.0d;
                }
                dArr[i3] = -dArr[i3];
                if (i3 + 1 < this.f167m && dArr[i3] != 0.0d) {
                    for (i = i3 + 1; i < this.f167m; i++) {
                        dArr2[i] = 0.0d;
                    }
                    for (i2 = i3 + 1; i2 < this.f168n; i2++) {
                        for (i = i3 + 1; i < this.f167m; i++) {
                            dArr2[i] = dArr2[i] + (dArr[i2] * arrayCopy[i][i2]);
                        }
                    }
                    for (i2 = i3 + 1; i2 < this.f168n; i2++) {
                        d = (-dArr[i2]) / dArr[i3 + 1];
                        for (i = i3 + 1; i < this.f167m; i++) {
                            dArr3 = arrayCopy[i];
                            dArr3[i2] = dArr3[i2] + (dArr2[i] * d);
                        }
                    }
                }
                if (1 != null) {
                    for (i = i3 + 1; i < this.f168n; i++) {
                        this.f166V[i][i3] = dArr[i];
                    }
                }
            }
            i3++;
        }
        int min3 = Math.min(this.f168n, this.f167m + 1);
        if (min2 < this.f168n) {
            this.f169s[min2] = arrayCopy[min2][min2];
        }
        if (this.f167m < min3) {
            this.f169s[min3 - 1] = 0.0d;
        }
        if (max + 1 < min3) {
            dArr[max] = arrayCopy[max][min3 - 1];
        }
        dArr[min3 - 1] = 0.0d;
        if (1 != null) {
            for (i2 = min2; i2 < min; i2++) {
                for (i = 0; i < this.f167m; i++) {
                    this.f165U[i][i2] = 0.0d;
                }
                this.f165U[i2][i2] = 1.0d;
            }
            for (i3 = min2 - 1; i3 >= 0; i3--) {
                if (this.f169s[i3] != 0.0d) {
                    for (i2 = i3 + 1; i2 < min; i2++) {
                        d = 0.0d;
                        for (i = i3; i < this.f167m; i++) {
                            d += this.f165U[i][i3] * this.f165U[i][i2];
                        }
                        d = (-d) / this.f165U[i3][i3];
                        for (i = i3; i < this.f167m; i++) {
                            dArr3 = this.f165U[i];
                            dArr3[i2] = dArr3[i2] + (this.f165U[i][i3] * d);
                        }
                    }
                    for (i = i3; i < this.f167m; i++) {
                        this.f165U[i][i3] = -this.f165U[i][i3];
                    }
                    this.f165U[i3][i3] = this.f165U[i3][i3] + 1.0d;
                    for (i = 0; i < i3 - 1; i++) {
                        this.f165U[i][i3] = 0.0d;
                    }
                } else {
                    for (i = 0; i < this.f167m; i++) {
                        this.f165U[i][i3] = 0.0d;
                    }
                    this.f165U[i3][i3] = 1.0d;
                }
            }
        }
        if (1 != null) {
            i3 = this.f168n - 1;
            while (i3 >= 0) {
                if (i3 < max && dArr[i3] != 0.0d) {
                    for (i2 = i3 + 1; i2 < min; i2++) {
                        d = 0.0d;
                        for (i = i3 + 1; i < this.f168n; i++) {
                            d += this.f166V[i][i3] * this.f166V[i][i2];
                        }
                        d = (-d) / this.f166V[i3 + 1][i3];
                        for (i = i3 + 1; i < this.f168n; i++) {
                            dArr3 = this.f166V[i];
                            dArr3[i2] = dArr3[i2] + (this.f166V[i][i3] * d);
                        }
                    }
                }
                for (i = 0; i < this.f168n; i++) {
                    this.f166V[i][i3] = 0.0d;
                }
                this.f166V[i3][i3] = 1.0d;
                i3--;
            }
        }
        int i5 = min3 - 1;
        int i6 = 0;
        double pow = Math.pow(2.0d, -52.0d);
        double pow2 = Math.pow(2.0d, -966.0d);
        while (min3 > 0) {
            Object obj;
            i3 = min3 - 2;
            while (i3 >= -1 && i3 != -1) {
                if (Math.abs(dArr[i3]) <= ((Math.abs(this.f169s[i3]) + Math.abs(this.f169s[i3 + 1])) * pow) + pow2) {
                    dArr[i3] = 0.0d;
                } else {
                    i3--;
                }
            }
            if (i3 == min3 - 2) {
                obj = 4;
            } else {
                int i7 = min3 - 1;
                while (i7 >= i3 && i7 != i3) {
                    if (Math.abs(this.f169s[i7]) <= (pow * ((i7 != min3 ? Math.abs(dArr[i7]) : 0.0d) + (i7 != i3 + 1 ? Math.abs(dArr[i7 - 1]) : 0.0d))) + pow2) {
                        this.f169s[i7] = 0.0d;
                    } else {
                        i7--;
                    }
                }
                if (i7 == i3) {
                    obj = 3;
                } else if (i7 == min3 - 1) {
                    obj = 1;
                } else {
                    obj = 2;
                    i3 = i7;
                }
            }
            i3++;
            double d2;
            double d3;
            double d4;
            switch (obj) {
                case 1:
                    d2 = dArr[min3 - 2];
                    dArr[min3 - 2] = 0.0d;
                    for (i2 = min3 - 2; i2 >= i3; i2--) {
                        d = Matrix.hypot(this.f169s[i2], d2);
                        d3 = this.f169s[i2] / d;
                        d4 = d2 / d;
                        this.f169s[i2] = d;
                        if (i2 != i3) {
                            d2 = (-d4) * dArr[i2 - 1];
                            dArr[i2 - 1] = dArr[i2 - 1] * d3;
                        }
                        if (1 != null) {
                            for (i = 0; i < this.f168n; i++) {
                                d = (this.f166V[i][i2] * d3) + (this.f166V[i][min3 - 1] * d4);
                                this.f166V[i][min3 - 1] = ((-d4) * this.f166V[i][i2]) + (this.f166V[i][min3 - 1] * d3);
                                this.f166V[i][i2] = d;
                            }
                            break;
                        }
                    }
                    break;
                case 2:
                    d2 = dArr[i3 - 1];
                    dArr[i3 - 1] = 0.0d;
                    for (i2 = i3; i2 < min3; i2++) {
                        d = Matrix.hypot(this.f169s[i2], d2);
                        d3 = this.f169s[i2] / d;
                        d4 = d2 / d;
                        this.f169s[i2] = d;
                        d2 = (-d4) * dArr[i2];
                        dArr[i2] = dArr[i2] * d3;
                        if (1 != null) {
                            for (i = 0; i < this.f167m; i++) {
                                d = (this.f165U[i][i2] * d3) + (this.f165U[i][i3 - 1] * d4);
                                this.f165U[i][i3 - 1] = ((-d4) * this.f165U[i][i2]) + (this.f165U[i][i3 - 1] * d3);
                                this.f165U[i][i2] = d;
                            }
                            break;
                        }
                    }
                    break;
                case 3:
                    double max2 = Math.max(Math.max(Math.max(Math.max(Math.abs(this.f169s[min3 - 1]), Math.abs(this.f169s[min3 - 2])), Math.abs(dArr[min3 - 2])), Math.abs(this.f169s[i3])), Math.abs(dArr[i3]));
                    double d5 = this.f169s[min3 - 1] / max2;
                    double d6 = this.f169s[min3 - 2] / max2;
                    double d7 = dArr[min3 - 2] / max2;
                    double d8 = this.f169s[i3] / max2;
                    double d9 = dArr[i3] / max2;
                    double d10 = (((d6 + d5) * (d6 - d5)) + (d7 * d7)) / 2.0d;
                    double d11 = (d5 * d7) * (d5 * d7);
                    double d12 = 0.0d;
                    if (((d11 != 0.0d ? 1 : 0) | (d10 != 0.0d ? 1 : 0)) != 0) {
                        d12 = Math.sqrt((d10 * d10) + d11);
                        if (d10 < 0.0d) {
                            d12 = -d12;
                        }
                        d12 = d11 / (d10 + d12);
                    }
                    d2 = ((d8 + d5) * (d8 - d5)) + d12;
                    double d13 = d8 * d9;
                    i2 = i3;
                    while (i2 < min3 - 1) {
                        d = Matrix.hypot(d2, d13);
                        d3 = d2 / d;
                        d4 = d13 / d;
                        if (i2 != i3) {
                            dArr[i2 - 1] = d;
                        }
                        d2 = (this.f169s[i2] * d3) + (dArr[i2] * d4);
                        dArr[i2] = (dArr[i2] * d3) - (this.f169s[i2] * d4);
                        d13 = d4 * this.f169s[i2 + 1];
                        this.f169s[i2 + 1] = this.f169s[i2 + 1] * d3;
                        if (1 != null) {
                            for (i = 0; i < this.f168n; i++) {
                                d = (this.f166V[i][i2] * d3) + (this.f166V[i][i2 + 1] * d4);
                                this.f166V[i][i2 + 1] = ((-d4) * this.f166V[i][i2]) + (this.f166V[i][i2 + 1] * d3);
                                this.f166V[i][i2] = d;
                            }
                            break;
                        }
                        d = Matrix.hypot(d2, d13);
                        d3 = d2 / d;
                        d4 = d13 / d;
                        this.f169s[i2] = d;
                        d2 = (dArr[i2] * d3) + (this.f169s[i2 + 1] * d4);
                        this.f169s[i2 + 1] = ((-d4) * dArr[i2]) + (this.f169s[i2 + 1] * d3);
                        d13 = d4 * dArr[i2 + 1];
                        dArr[i2 + 1] = dArr[i2 + 1] * d3;
                        if (1 != null && i2 < this.f167m - 1) {
                            for (i = 0; i < this.f167m; i++) {
                                d = (this.f165U[i][i2] * d3) + (this.f165U[i][i2 + 1] * d4);
                                this.f165U[i][i2 + 1] = ((-d4) * this.f165U[i][i2]) + (this.f165U[i][i2 + 1] * d3);
                                this.f165U[i][i2] = d;
                            }
                            break;
                        }
                        i2++;
                    }
                    dArr[min3 - 2] = d2;
                    i6++;
                    break;
                case 4:
                    if (this.f169s[i3] <= 0.0d) {
                        this.f169s[i3] = this.f169s[i3] < 0.0d ? -this.f169s[i3] : 0.0d;
                        if (1 != null) {
                            for (i = 0; i <= i5; i++) {
                                this.f166V[i][i3] = -this.f166V[i][i3];
                            }
                        }
                    }
                    while (i3 < i5 && this.f169s[i3] < this.f169s[i3 + 1]) {
                        d = this.f169s[i3];
                        this.f169s[i3] = this.f169s[i3 + 1];
                        this.f169s[i3 + 1] = d;
                        if (1 != null && i3 < this.f168n - 1) {
                            for (i = 0; i < this.f168n; i++) {
                                d = this.f166V[i][i3 + 1];
                                this.f166V[i][i3 + 1] = this.f166V[i][i3];
                                this.f166V[i][i3] = d;
                            }
                            break;
                        }
                        if (1 != null && i3 < this.f167m - 1) {
                            for (i = 0; i < this.f167m; i++) {
                                d = this.f165U[i][i3 + 1];
                                this.f165U[i][i3 + 1] = this.f165U[i][i3];
                                this.f165U[i][i3] = d;
                            }
                            break;
                        }
                        i3++;
                    }
                    i6 = 0;
                    min3--;
                    break;
                default:
                    break;
            }
        }
    }

    public double cond() {
        return this.f169s[0] / this.f169s[Math.min(this.f167m, this.f168n) - 1];
    }

    public Matrix getS() {
        Matrix matrix = new Matrix(this.f168n, this.f168n);
        double[][] array = matrix.getArray();
        for (int i = 0; i < this.f168n; i++) {
            for (int i2 = 0; i2 < this.f168n; i2++) {
                array[i][i2] = 0.0d;
            }
            array[i][i] = this.f169s[i];
        }
        return matrix;
    }

    public double[] getSingularValues() {
        return this.f169s;
    }

    public Matrix getU() {
        return new Matrix(this.f165U, this.f167m, Math.min(this.f167m + 1, this.f168n));
    }

    public Matrix getV() {
        return new Matrix(this.f166V, this.f168n, this.f168n);
    }

    public double norm2() {
        return this.f169s[0];
    }

    public int rank() {
        double max = (((double) Math.max(this.f167m, this.f168n)) * this.f169s[0]) * Math.pow(2.0d, -52.0d);
        int i = 0;
        for (double d : this.f169s) {
            if (d > max) {
                i++;
            }
        }
        return i;
    }
}
