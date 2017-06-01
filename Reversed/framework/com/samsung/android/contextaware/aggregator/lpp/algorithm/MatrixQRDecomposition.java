package com.samsung.android.contextaware.aggregator.lpp.algorithm;

import java.io.Serializable;

public class MatrixQRDecomposition implements Serializable {
    private static final long serialVersionUID = 1;
    private double[][] QR;
    private double[] Rdiag = new double[this.f164n];
    private int f163m;
    private int f164n;

    public MatrixQRDecomposition(Matrix matrix) {
        this.QR = matrix.getArrayCopy();
        this.f163m = matrix.getRowDimension();
        this.f164n = matrix.getColumnDimension();
        for (int i = 0; i < this.f164n; i++) {
            int i2;
            double d = 0.0d;
            for (i2 = i; i2 < this.f163m; i2++) {
                d = Matrix.hypot(d, this.QR[i2][i]);
            }
            if (d != 0.0d) {
                double[] dArr;
                if (this.QR[i][i] < 0.0d) {
                    d = -d;
                }
                for (i2 = i; i2 < this.f163m; i2++) {
                    dArr = this.QR[i2];
                    dArr[i] = dArr[i] / d;
                }
                dArr = this.QR[i];
                dArr[i] = dArr[i] + 1.0d;
                for (int i3 = i + 1; i3 < this.f164n; i3++) {
                    double d2 = 0.0d;
                    for (i2 = i; i2 < this.f163m; i2++) {
                        d2 += this.QR[i2][i] * this.QR[i2][i3];
                    }
                    d2 = (-d2) / this.QR[i][i];
                    for (i2 = i; i2 < this.f163m; i2++) {
                        dArr = this.QR[i2];
                        dArr[i3] = dArr[i3] + (this.QR[i2][i] * d2);
                    }
                }
            }
            this.Rdiag[i] = -d;
        }
    }

    public Matrix getH() {
        Matrix matrix = new Matrix(this.f163m, this.f164n);
        double[][] array = matrix.getArray();
        for (int i = 0; i < this.f163m; i++) {
            for (int i2 = 0; i2 < this.f164n; i2++) {
                if (i >= i2) {
                    array[i][i2] = this.QR[i][i2];
                } else {
                    array[i][i2] = 0.0d;
                }
            }
        }
        return matrix;
    }

    public Matrix getQ() {
        Matrix matrix = new Matrix(this.f163m, this.f164n);
        double[][] array = matrix.getArray();
        for (int i = this.f164n - 1; i >= 0; i--) {
            int i2;
            for (i2 = 0; i2 < this.f163m; i2++) {
                array[i2][i] = 0.0d;
            }
            array[i][i] = 1.0d;
            for (int i3 = i; i3 < this.f164n; i3++) {
                if (this.QR[i][i] != 0.0d) {
                    double d = 0.0d;
                    for (i2 = i; i2 < this.f163m; i2++) {
                        d += this.QR[i2][i] * array[i2][i3];
                    }
                    d = (-d) / this.QR[i][i];
                    for (i2 = i; i2 < this.f163m; i2++) {
                        double[] dArr = array[i2];
                        dArr[i3] = dArr[i3] + (this.QR[i2][i] * d);
                    }
                }
            }
        }
        return matrix;
    }

    public Matrix getR() {
        Matrix matrix = new Matrix(this.f164n, this.f164n);
        double[][] array = matrix.getArray();
        for (int i = 0; i < this.f164n; i++) {
            for (int i2 = 0; i2 < this.f164n; i2++) {
                if (i < i2) {
                    array[i][i2] = this.QR[i][i2];
                } else if (i == i2) {
                    array[i][i2] = this.Rdiag[i];
                } else {
                    array[i][i2] = 0.0d;
                }
            }
        }
        return matrix;
    }

    public boolean isFullRank() {
        for (int i = 0; i < this.f164n; i++) {
            if (this.Rdiag[i] == 0.0d) {
                return false;
            }
        }
        return true;
    }

    public Matrix solve(Matrix matrix) {
        if (matrix.getRowDimension() != this.f163m) {
            throw new IllegalArgumentException("Matrix row dimensions must agree.");
        } else if (isFullRank()) {
            int i;
            int i2;
            int i3;
            double[] dArr;
            int columnDimension = matrix.getColumnDimension();
            double[][] arrayCopy = matrix.getArrayCopy();
            for (i = 0; i < this.f164n; i++) {
                for (i2 = 0; i2 < columnDimension; i2++) {
                    double d = 0.0d;
                    for (i3 = i; i3 < this.f163m; i3++) {
                        d += this.QR[i3][i] * arrayCopy[i3][i2];
                    }
                    d = (-d) / this.QR[i][i];
                    for (i3 = i; i3 < this.f163m; i3++) {
                        dArr = arrayCopy[i3];
                        dArr[i2] = dArr[i2] + (this.QR[i3][i] * d);
                    }
                }
            }
            for (i = this.f164n - 1; i >= 0; i--) {
                for (i2 = 0; i2 < columnDimension; i2++) {
                    dArr = arrayCopy[i];
                    dArr[i2] = dArr[i2] / this.Rdiag[i];
                }
                for (i3 = 0; i3 < i; i3++) {
                    for (i2 = 0; i2 < columnDimension; i2++) {
                        dArr = arrayCopy[i3];
                        dArr[i2] = dArr[i2] - (arrayCopy[i][i2] * this.QR[i3][i]);
                    }
                }
            }
            return new Matrix(arrayCopy, this.f164n, columnDimension).getMatrix(0, this.f164n - 1, 0, columnDimension - 1);
        } else {
            throw new RuntimeException("Matrix is rank deficient.");
        }
    }
}
