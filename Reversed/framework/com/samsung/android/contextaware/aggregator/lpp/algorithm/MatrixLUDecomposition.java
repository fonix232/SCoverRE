package com.samsung.android.contextaware.aggregator.lpp.algorithm;

import java.io.Serializable;

public class MatrixLUDecomposition implements Serializable {
    private static final long serialVersionUID = 1;
    private double[][] LU;
    private int f161m;
    private int f162n;
    private int[] piv = new int[this.f161m];
    private int pivsign;

    public MatrixLUDecomposition(Matrix matrix) {
        int i;
        this.LU = matrix.getArrayCopy();
        this.f161m = matrix.getRowDimension();
        this.f162n = matrix.getColumnDimension();
        for (i = 0; i < this.f161m; i++) {
            this.piv[i] = i;
        }
        this.pivsign = 1;
        double[] dArr = new double[this.f161m];
        int i2 = 0;
        while (i2 < this.f162n) {
            int i3;
            for (i = 0; i < this.f161m; i++) {
                dArr[i] = this.LU[i][i2];
            }
            for (i = 0; i < this.f161m; i++) {
                double[] dArr2 = this.LU[i];
                double d = 0.0d;
                for (i3 = 0; i3 < Math.min(i, i2); i3++) {
                    d += dArr2[i3] * dArr[i3];
                }
                double d2 = dArr[i] - d;
                dArr[i] = d2;
                dArr2[i2] = d2;
            }
            int i4 = i2;
            for (i = i2 + 1; i < this.f161m; i++) {
                if (Math.abs(dArr[i]) > Math.abs(dArr[i4])) {
                    i4 = i;
                }
            }
            if (i4 != i2) {
                for (i3 = 0; i3 < this.f162n; i3++) {
                    double d3 = this.LU[i4][i3];
                    this.LU[i4][i3] = this.LU[i2][i3];
                    this.LU[i2][i3] = d3;
                }
                i3 = this.piv[i4];
                this.piv[i4] = this.piv[i2];
                this.piv[i2] = i3;
                this.pivsign = -this.pivsign;
            }
            if (i2 < this.f161m && this.LU[i2][i2] != 0.0d) {
                for (i = i2 + 1; i < this.f161m; i++) {
                    double[] dArr3 = this.LU[i];
                    dArr3[i2] = dArr3[i2] / this.LU[i2][i2];
                }
            }
            i2++;
        }
    }

    public double det() {
        if (this.f161m != this.f162n) {
            throw new IllegalArgumentException("Matrix must be square.");
        }
        double d = (double) this.pivsign;
        for (int i = 0; i < this.f162n; i++) {
            d *= this.LU[i][i];
        }
        return d;
    }

    public double[] getDoublePivot() {
        double[] dArr = new double[this.f161m];
        for (int i = 0; i < this.f161m; i++) {
            dArr[i] = (double) this.piv[i];
        }
        return dArr;
    }

    public Matrix getL() {
        Matrix matrix = new Matrix(this.f161m, this.f162n);
        double[][] array = matrix.getArray();
        for (int i = 0; i < this.f161m; i++) {
            for (int i2 = 0; i2 < this.f162n; i2++) {
                if (i > i2) {
                    array[i][i2] = this.LU[i][i2];
                } else if (i == i2) {
                    array[i][i2] = 1.0d;
                } else {
                    array[i][i2] = 0.0d;
                }
            }
        }
        return matrix;
    }

    public int[] getPivot() {
        int[] iArr = new int[this.f161m];
        for (int i = 0; i < this.f161m; i++) {
            iArr[i] = this.piv[i];
        }
        return iArr;
    }

    public Matrix getU() {
        Matrix matrix = new Matrix(this.f162n, this.f162n);
        double[][] array = matrix.getArray();
        for (int i = 0; i < this.f162n; i++) {
            for (int i2 = 0; i2 < this.f162n; i2++) {
                if (i <= i2) {
                    array[i][i2] = this.LU[i][i2];
                } else {
                    array[i][i2] = 0.0d;
                }
            }
        }
        return matrix;
    }

    public boolean isNonsingular() {
        for (int i = 0; i < this.f162n; i++) {
            if (this.LU[i][i] == 0.0d) {
                return false;
            }
        }
        return true;
    }

    public Matrix solve(Matrix matrix) {
        if (matrix.getRowDimension() != this.f161m) {
            throw new IllegalArgumentException("Matrix row dimensions must agree.");
        } else if (isNonsingular()) {
            int i;
            int i2;
            int i3;
            double[] dArr;
            int columnDimension = matrix.getColumnDimension();
            Matrix matrix2 = matrix.getMatrix(this.piv, 0, columnDimension - 1);
            double[][] array = matrix2.getArray();
            for (i = 0; i < this.f162n; i++) {
                for (i2 = i + 1; i2 < this.f162n; i2++) {
                    for (i3 = 0; i3 < columnDimension; i3++) {
                        dArr = array[i2];
                        dArr[i3] = dArr[i3] - (array[i][i3] * this.LU[i2][i]);
                    }
                }
            }
            for (i = this.f162n - 1; i >= 0; i--) {
                for (i3 = 0; i3 < columnDimension; i3++) {
                    dArr = array[i];
                    dArr[i3] = dArr[i3] / this.LU[i][i];
                }
                for (i2 = 0; i2 < i; i2++) {
                    for (i3 = 0; i3 < columnDimension; i3++) {
                        dArr = array[i2];
                        dArr[i3] = dArr[i3] - (array[i][i3] * this.LU[i2][i]);
                    }
                }
            }
            return matrix2;
        } else {
            throw new RuntimeException("Matrix is singular.");
        }
    }
}
