package com.samsung.android.contextaware.aggregator.lpp.algorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StreamTokenizer;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Vector;

public class Matrix implements Cloneable, Serializable {
    private static final long serialVersionUID = 1;
    private double[][] f158A;
    private int f159m;
    private int f160n;

    public Matrix(int i, int i2) {
        this.f159m = i;
        this.f160n = i2;
        this.f158A = (double[][]) Array.newInstance(Double.TYPE, new int[]{i, i2});
    }

    public Matrix(int i, int i2, double d) {
        this.f159m = i;
        this.f160n = i2;
        this.f158A = (double[][]) Array.newInstance(Double.TYPE, new int[]{i, i2});
        for (int i3 = 0; i3 < i; i3++) {
            for (int i4 = 0; i4 < i2; i4++) {
                this.f158A[i3][i4] = d;
            }
        }
    }

    public Matrix(double[] dArr, int i) {
        this.f159m = i;
        this.f160n = i != 0 ? dArr.length / i : 0;
        if (this.f160n * i != dArr.length) {
            throw new IllegalArgumentException("Array length must be a multiple of m.");
        }
        this.f158A = (double[][]) Array.newInstance(Double.TYPE, new int[]{i, this.f160n});
        for (int i2 = 0; i2 < i; i2++) {
            for (int i3 = 0; i3 < this.f160n; i3++) {
                this.f158A[i2][i3] = dArr[(i3 * i) + i2];
            }
        }
    }

    public Matrix(double[][] dArr) {
        this.f159m = dArr.length;
        this.f160n = dArr[0].length;
        for (int i = 0; i < this.f159m; i++) {
            if (dArr[i].length != this.f160n) {
                throw new IllegalArgumentException("All rows must have the same length.");
            }
        }
        this.f158A = dArr;
    }

    public Matrix(double[][] dArr, int i, int i2) {
        this.f158A = dArr;
        this.f159m = i;
        this.f160n = i2;
    }

    private void checkMatrixDimensions(Matrix matrix) {
        if (matrix.f159m != this.f159m || matrix.f160n != this.f160n) {
            throw new IllegalArgumentException("Matrix dimensions must agree.");
        }
    }

    public static Matrix constructWithCopy(double[][] dArr) {
        int length = dArr.length;
        int length2 = dArr[0].length;
        Matrix matrix = new Matrix(length, length2);
        double[][] array = matrix.getArray();
        for (int i = 0; i < length; i++) {
            if (dArr[i].length != length2) {
                throw new IllegalArgumentException("All rows must have the same length.");
            }
            for (int i2 = 0; i2 < length2; i2++) {
                array[i][i2] = dArr[i][i2];
            }
        }
        return matrix;
    }

    public static double hypot(double d, double d2) {
        double d3;
        if (Math.abs(d) > Math.abs(d2)) {
            d3 = d2 / d;
            return Math.abs(d) * Math.sqrt((d3 * d3) + 1.0d);
        } else if (d2 == 0.0d) {
            return 0.0d;
        } else {
            d3 = d / d2;
            return Math.abs(d2) * Math.sqrt((d3 * d3) + 1.0d);
        }
    }

    public static Matrix identity(int i, int i2) {
        Matrix matrix = new Matrix(i, i2);
        double[][] array = matrix.getArray();
        int i3 = 0;
        while (i3 < i) {
            int i4 = 0;
            while (i4 < i2) {
                array[i3][i4] = i3 == i4 ? 1.0d : 0.0d;
                i4++;
            }
            i3++;
        }
        return matrix;
    }

    public static Matrix random(int i, int i2) {
        Matrix matrix = new Matrix(i, i2);
        double[][] array = matrix.getArray();
        for (int i3 = 0; i3 < i; i3++) {
            for (int i4 = 0; i4 < i2; i4++) {
                array[i3][i4] = Math.random();
            }
        }
        return matrix;
    }

    public static Matrix read(BufferedReader bufferedReader) throws IOException {
        StreamTokenizer streamTokenizer = new StreamTokenizer(bufferedReader);
        streamTokenizer.resetSyntax();
        streamTokenizer.wordChars(0, 255);
        streamTokenizer.whitespaceChars(0, 32);
        streamTokenizer.eolIsSignificant(true);
        Vector vector = new Vector();
        do {
        } while (streamTokenizer.nextToken() == 10);
        if (streamTokenizer.ttype == -1) {
            throw new IOException("Unexpected EOF on matrix read.");
        }
        int i;
        do {
            vector.addElement(Double.valueOf(streamTokenizer.sval));
        } while (streamTokenizer.nextToken() == -3);
        int size = vector.size();
        Object obj = new double[size];
        for (i = 0; i < size; i++) {
            obj[i] = ((Double) vector.elementAt(i)).doubleValue();
        }
        Vector vector2 = new Vector();
        vector2.addElement(obj);
        while (streamTokenizer.nextToken() == -3) {
            obj = new double[size];
            vector2.addElement(obj);
            int i2 = 0;
            while (i2 < size) {
                i = i2 + 1;
                obj[i2] = Double.valueOf(streamTokenizer.sval).doubleValue();
                if (streamTokenizer.nextToken() == -3) {
                    i2 = i;
                } else if (i < size) {
                    throw new IOException("Row " + vector2.size() + " is too short.");
                }
            }
            throw new IOException("Row " + vector2.size() + " is too long.");
        }
        Object[] objArr = new double[vector2.size()][];
        vector2.copyInto(objArr);
        return new Matrix(objArr);
    }

    public Matrix arrayLeftDivide(Matrix matrix) {
        checkMatrixDimensions(matrix);
        Matrix matrix2 = new Matrix(this.f159m, this.f160n);
        double[][] array = matrix2.getArray();
        for (int i = 0; i < this.f159m; i++) {
            for (int i2 = 0; i2 < this.f160n; i2++) {
                array[i][i2] = matrix.f158A[i][i2] / this.f158A[i][i2];
            }
        }
        return matrix2;
    }

    public Matrix arrayLeftDivideEquals(Matrix matrix) {
        checkMatrixDimensions(matrix);
        for (int i = 0; i < this.f159m; i++) {
            for (int i2 = 0; i2 < this.f160n; i2++) {
                this.f158A[i][i2] = matrix.f158A[i][i2] / this.f158A[i][i2];
            }
        }
        return this;
    }

    public Matrix arrayRightDivide(Matrix matrix) {
        checkMatrixDimensions(matrix);
        Matrix matrix2 = new Matrix(this.f159m, this.f160n);
        double[][] array = matrix2.getArray();
        for (int i = 0; i < this.f159m; i++) {
            for (int i2 = 0; i2 < this.f160n; i2++) {
                array[i][i2] = this.f158A[i][i2] / matrix.f158A[i][i2];
            }
        }
        return matrix2;
    }

    public Matrix arrayRightDivideEquals(Matrix matrix) {
        checkMatrixDimensions(matrix);
        for (int i = 0; i < this.f159m; i++) {
            for (int i2 = 0; i2 < this.f160n; i2++) {
                this.f158A[i][i2] = this.f158A[i][i2] / matrix.f158A[i][i2];
            }
        }
        return this;
    }

    public Matrix arrayTimes(Matrix matrix) {
        checkMatrixDimensions(matrix);
        Matrix matrix2 = new Matrix(this.f159m, this.f160n);
        double[][] array = matrix2.getArray();
        for (int i = 0; i < this.f159m; i++) {
            for (int i2 = 0; i2 < this.f160n; i2++) {
                array[i][i2] = this.f158A[i][i2] * matrix.f158A[i][i2];
            }
        }
        return matrix2;
    }

    public Matrix arrayTimesEquals(Matrix matrix) {
        checkMatrixDimensions(matrix);
        for (int i = 0; i < this.f159m; i++) {
            for (int i2 = 0; i2 < this.f160n; i2++) {
                this.f158A[i][i2] = this.f158A[i][i2] * matrix.f158A[i][i2];
            }
        }
        return this;
    }

    public Object clone() {
        return copy();
    }

    public double cond() {
        return new MatrixSingularValueDecomposition(this).cond();
    }

    public Matrix copy() {
        Matrix matrix = new Matrix(this.f159m, this.f160n);
        double[][] array = matrix.getArray();
        for (int i = 0; i < this.f159m; i++) {
            for (int i2 = 0; i2 < this.f160n; i2++) {
                array[i][i2] = this.f158A[i][i2];
            }
        }
        return matrix;
    }

    public double det() {
        return new MatrixLUDecomposition(this).det();
    }

    public double get(int i, int i2) {
        return this.f158A[i][i2];
    }

    public double[][] getArray() {
        return this.f158A;
    }

    public double[][] getArrayCopy() {
        double[][] dArr = (double[][]) Array.newInstance(Double.TYPE, new int[]{this.f159m, this.f160n});
        for (int i = 0; i < this.f159m; i++) {
            for (int i2 = 0; i2 < this.f160n; i2++) {
                dArr[i][i2] = this.f158A[i][i2];
            }
        }
        return dArr;
    }

    public int getColumnDimension() {
        return this.f160n;
    }

    public double[] getColumnPackedCopy() {
        double[] dArr = new double[(this.f159m * this.f160n)];
        for (int i = 0; i < this.f159m; i++) {
            for (int i2 = 0; i2 < this.f160n; i2++) {
                dArr[(this.f159m * i2) + i] = this.f158A[i][i2];
            }
        }
        return dArr;
    }

    public Matrix getMatrix(int i, int i2, int i3, int i4) {
        Matrix matrix = new Matrix((i2 - i) + 1, (i4 - i3) + 1);
        double[][] array = matrix.getArray();
        for (int i5 = i; i5 <= i2; i5++) {
            int i6 = i3;
            while (i6 <= i4) {
                try {
                    array[i5 - i][i6 - i3] = this.f158A[i5][i6];
                    i6++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new ArrayIndexOutOfBoundsException("Submatrix indices");
                }
            }
        }
        return matrix;
    }

    public Matrix getMatrix(int i, int i2, int[] iArr) {
        Matrix matrix = new Matrix((i2 - i) + 1, iArr.length);
        double[][] array = matrix.getArray();
        for (int i3 = i; i3 <= i2; i3++) {
            int i4 = 0;
            while (i4 < iArr.length) {
                try {
                    array[i3 - i][i4] = this.f158A[i3][iArr[i4]];
                    i4++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new ArrayIndexOutOfBoundsException("Submatrix indices");
                }
            }
        }
        return matrix;
    }

    public Matrix getMatrix(int[] iArr, int i, int i2) {
        Matrix matrix = new Matrix(iArr.length, (i2 - i) + 1);
        double[][] array = matrix.getArray();
        int i3 = 0;
        while (i3 < iArr.length) {
            try {
                for (int i4 = i; i4 <= i2; i4++) {
                    array[i3][i4 - i] = this.f158A[iArr[i3]][i4];
                }
                i3++;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ArrayIndexOutOfBoundsException("Submatrix indices");
            }
        }
        return matrix;
    }

    public Matrix getMatrix(int[] iArr, int[] iArr2) {
        Matrix matrix = new Matrix(iArr.length, iArr2.length);
        double[][] array = matrix.getArray();
        int i = 0;
        while (i < iArr.length) {
            try {
                for (int i2 = 0; i2 < iArr2.length; i2++) {
                    array[i][i2] = this.f158A[iArr[i]][iArr2[i2]];
                }
                i++;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ArrayIndexOutOfBoundsException("Submatrix indices");
            }
        }
        return matrix;
    }

    public int getRowDimension() {
        return this.f159m;
    }

    public double[] getRowPackedCopy() {
        double[] dArr = new double[(this.f159m * this.f160n)];
        for (int i = 0; i < this.f159m; i++) {
            for (int i2 = 0; i2 < this.f160n; i2++) {
                dArr[(this.f160n * i) + i2] = this.f158A[i][i2];
            }
        }
        return dArr;
    }

    public Matrix inverse() {
        return solve(identity(this.f159m, this.f159m));
    }

    public Matrix minus(Matrix matrix) {
        checkMatrixDimensions(matrix);
        Matrix matrix2 = new Matrix(this.f159m, this.f160n);
        double[][] array = matrix2.getArray();
        for (int i = 0; i < this.f159m; i++) {
            for (int i2 = 0; i2 < this.f160n; i2++) {
                array[i][i2] = this.f158A[i][i2] - matrix.f158A[i][i2];
            }
        }
        return matrix2;
    }

    public Matrix minusEquals(Matrix matrix) {
        checkMatrixDimensions(matrix);
        for (int i = 0; i < this.f159m; i++) {
            for (int i2 = 0; i2 < this.f160n; i2++) {
                this.f158A[i][i2] = this.f158A[i][i2] - matrix.f158A[i][i2];
            }
        }
        return this;
    }

    public double norm1() {
        double d = 0.0d;
        for (int i = 0; i < this.f160n; i++) {
            double d2 = 0.0d;
            for (int i2 = 0; i2 < this.f159m; i2++) {
                d2 += Math.abs(this.f158A[i2][i]);
            }
            d = Math.max(d, d2);
        }
        return d;
    }

    public double norm2() {
        return new MatrixSingularValueDecomposition(this).norm2();
    }

    public double normF() {
        double d = 0.0d;
        for (int i = 0; i < this.f159m; i++) {
            for (int i2 = 0; i2 < this.f160n; i2++) {
                d = hypot(d, this.f158A[i][i2]);
            }
        }
        return d;
    }

    public double normInf() {
        double d = 0.0d;
        for (int i = 0; i < this.f159m; i++) {
            double d2 = 0.0d;
            for (int i2 = 0; i2 < this.f160n; i2++) {
                d2 += Math.abs(this.f158A[i][i2]);
            }
            d = Math.max(d, d2);
        }
        return d;
    }

    public Matrix plus(Matrix matrix) {
        checkMatrixDimensions(matrix);
        Matrix matrix2 = new Matrix(this.f159m, this.f160n);
        double[][] array = matrix2.getArray();
        for (int i = 0; i < this.f159m; i++) {
            for (int i2 = 0; i2 < this.f160n; i2++) {
                array[i][i2] = this.f158A[i][i2] + matrix.f158A[i][i2];
            }
        }
        return matrix2;
    }

    public Matrix plusEquals(Matrix matrix) {
        checkMatrixDimensions(matrix);
        for (int i = 0; i < this.f159m; i++) {
            for (int i2 = 0; i2 < this.f160n; i2++) {
                this.f158A[i][i2] = this.f158A[i][i2] + matrix.f158A[i][i2];
            }
        }
        return this;
    }

    public void print(int i, int i2) {
        print(new PrintWriter(System.out, true), i, i2);
    }

    public void print(PrintWriter printWriter, int i, int i2) {
        NumberFormat decimalFormat = new DecimalFormat();
        decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        decimalFormat.setMinimumIntegerDigits(1);
        decimalFormat.setMaximumFractionDigits(i2);
        decimalFormat.setMinimumFractionDigits(i2);
        decimalFormat.setGroupingUsed(false);
        print(printWriter, decimalFormat, i + 2);
    }

    public void print(PrintWriter printWriter, NumberFormat numberFormat, int i) {
        printWriter.println();
        for (int i2 = 0; i2 < this.f159m; i2++) {
            for (int i3 = 0; i3 < this.f160n; i3++) {
                String format = numberFormat.format(this.f158A[i2][i3]);
                int max = Math.max(1, i - format.length());
                for (int i4 = 0; i4 < max; i4++) {
                    printWriter.print(' ');
                }
                printWriter.print(format);
            }
            printWriter.println();
        }
        printWriter.println();
    }

    public void print(NumberFormat numberFormat, int i) {
        print(new PrintWriter(System.out, true), numberFormat, i);
    }

    public int rank() {
        return new MatrixSingularValueDecomposition(this).rank();
    }

    public void set(int i, int i2, double d) {
        this.f158A[i][i2] = d;
    }

    public void setMatrix(int i, int i2, int i3, int i4, Matrix matrix) {
        for (int i5 = i; i5 <= i2; i5++) {
            int i6 = i3;
            while (i6 <= i4) {
                try {
                    this.f158A[i5][i6] = matrix.get(i5 - i, i6 - i3);
                    i6++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new ArrayIndexOutOfBoundsException("Submatrix indices");
                }
            }
        }
    }

    public void setMatrix(int i, int i2, int[] iArr, Matrix matrix) {
        for (int i3 = i; i3 <= i2; i3++) {
            int i4 = 0;
            while (i4 < iArr.length) {
                try {
                    this.f158A[i3][iArr[i4]] = matrix.get(i3 - i, i4);
                    i4++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new ArrayIndexOutOfBoundsException("Submatrix indices");
                }
            }
        }
    }

    public void setMatrix(int[] iArr, int i, int i2, Matrix matrix) {
        int i3 = 0;
        while (i3 < iArr.length) {
            try {
                for (int i4 = i; i4 <= i2; i4++) {
                    this.f158A[iArr[i3]][i4] = matrix.get(i3, i4 - i);
                }
                i3++;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ArrayIndexOutOfBoundsException("Submatrix indices");
            }
        }
    }

    public void setMatrix(int[] iArr, int[] iArr2, Matrix matrix) {
        int i = 0;
        while (i < iArr.length) {
            try {
                for (int i2 = 0; i2 < iArr2.length; i2++) {
                    this.f158A[iArr[i]][iArr2[i2]] = matrix.get(i, i2);
                }
                i++;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ArrayIndexOutOfBoundsException("Submatrix indices");
            }
        }
    }

    public Matrix solve(Matrix matrix) {
        return this.f159m == this.f160n ? new MatrixLUDecomposition(this).solve(matrix) : new MatrixQRDecomposition(this).solve(matrix);
    }

    public Matrix solveTranspose(Matrix matrix) {
        return transpose().solve(matrix.transpose());
    }

    public MatrixSingularValueDecomposition svd() {
        return new MatrixSingularValueDecomposition(this);
    }

    public Matrix times(double d) {
        Matrix matrix = new Matrix(this.f159m, this.f160n);
        double[][] array = matrix.getArray();
        for (int i = 0; i < this.f159m; i++) {
            for (int i2 = 0; i2 < this.f160n; i2++) {
                array[i][i2] = this.f158A[i][i2] * d;
            }
        }
        return matrix;
    }

    public Matrix times(Matrix matrix) {
        if (matrix.f159m != this.f160n) {
            throw new IllegalArgumentException("Matrix inner dimensions must agree.");
        }
        Matrix matrix2 = new Matrix(this.f159m, matrix.f160n);
        double[][] array = matrix2.getArray();
        double[] dArr = new double[this.f160n];
        for (int i = 0; i < matrix.f160n; i++) {
            int i2;
            for (i2 = 0; i2 < this.f160n; i2++) {
                dArr[i2] = matrix.f158A[i2][i];
            }
            for (int i3 = 0; i3 < this.f159m; i3++) {
                double[] dArr2 = this.f158A[i3];
                double d = 0.0d;
                for (i2 = 0; i2 < this.f160n; i2++) {
                    d += dArr2[i2] * dArr[i2];
                }
                array[i3][i] = d;
            }
        }
        return matrix2;
    }

    public Matrix timesEquals(double d) {
        for (int i = 0; i < this.f159m; i++) {
            for (int i2 = 0; i2 < this.f160n; i2++) {
                this.f158A[i][i2] = this.f158A[i][i2] * d;
            }
        }
        return this;
    }

    public double trace() {
        double d = 0.0d;
        for (int i = 0; i < Math.min(this.f159m, this.f160n); i++) {
            d += this.f158A[i][i];
        }
        return d;
    }

    public Matrix transpose() {
        Matrix matrix = new Matrix(this.f160n, this.f159m);
        double[][] array = matrix.getArray();
        for (int i = 0; i < this.f159m; i++) {
            for (int i2 = 0; i2 < this.f160n; i2++) {
                array[i2][i] = this.f158A[i][i2];
            }
        }
        return matrix;
    }

    public Matrix uminus() {
        Matrix matrix = new Matrix(this.f159m, this.f160n);
        double[][] array = matrix.getArray();
        for (int i = 0; i < this.f159m; i++) {
            for (int i2 = 0; i2 < this.f160n; i2++) {
                array[i][i2] = -this.f158A[i][i2];
            }
        }
        return matrix;
    }
}
