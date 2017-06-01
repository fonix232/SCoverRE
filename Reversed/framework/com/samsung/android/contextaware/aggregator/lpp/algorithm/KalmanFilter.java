package com.samsung.android.contextaware.aggregator.lpp.algorithm;

import android.util.Log;

public class KalmanFilter {
    Matrix f156F;
    Matrix f157H;
    int MeasurementSize;
    Matrix P_k;
    Matrix Q_k;
    Matrix R_k;
    int StateSize;
    String TAG = "KalmanFilter";
    Matrix X_k;
    boolean flagInitializeCovariance = false;
    boolean flagInitializeMeasurementM = false;
    boolean flagInitializeState = false;
    boolean flagMeasurementNoise = false;
    boolean flagProcessNoise = false;
    boolean flagTransitionMatrix = false;

    public KalmanFilter(int i, int i2) {
        this.StateSize = i;
        this.MeasurementSize = i2;
        this.X_k = new Matrix(this.StateSize, 1);
        this.P_k = new Matrix(this.StateSize, this.StateSize);
        this.Q_k = new Matrix(this.StateSize, this.StateSize);
        this.R_k = new Matrix(this.MeasurementSize, this.MeasurementSize);
        this.f156F = new Matrix(this.StateSize, this.StateSize);
        this.f157H = new Matrix(this.MeasurementSize, this.StateSize);
    }

    public boolean MeasurementUpdate(Matrix matrix) {
        if (matrix.getRowDimension() == this.MeasurementSize && matrix.getColumnDimension() == 1) {
            Matrix matrix2 = new Matrix(this.StateSize, this.MeasurementSize);
            Matrix matrix3 = new Matrix(this.StateSize, this.MeasurementSize);
            matrix2 = new Matrix(this.MeasurementSize, this.MeasurementSize);
            matrix2 = new Matrix(this.MeasurementSize, this.MeasurementSize);
            matrix2 = new Matrix(this.MeasurementSize, 1);
            matrix2 = new Matrix(this.MeasurementSize, 1);
            Matrix matrix4 = new Matrix(this.StateSize, 1);
            Matrix matrix5 = new Matrix(this.StateSize, this.StateSize);
            if (this.flagProcessNoise && this.flagMeasurementNoise && this.flagInitializeMeasurementM) {
                matrix3.setMatrix(0, this.StateSize - 1, 0, this.MeasurementSize - 1, this.P_k.times(this.f157H.transpose()));
                matrix2.setMatrix(0, this.MeasurementSize - 1, 0, this.MeasurementSize - 1, this.f157H.times(matrix3));
                Matrix matrix6 = matrix2;
                matrix6.setMatrix(0, this.MeasurementSize - 1, 0, this.MeasurementSize - 1, matrix2.plus(this.R_k));
                matrix2.setMatrix(0, this.StateSize - 1, 0, this.MeasurementSize - 1, matrix3.times(matrix2.inverse()));
                matrix2.setMatrix(0, this.StateSize - 1, 0, 0, this.f157H.times(this.X_k));
                matrix2.setMatrix(0, this.MeasurementSize - 1, 0, 0, matrix.minus(matrix2));
                matrix4.setMatrix(0, this.StateSize - 1, 0, 0, this.X_k.plus(matrix2.times(matrix2)));
                new Matrix(this.MeasurementSize, 1).setMatrix(0, this.MeasurementSize - 1, 0, 0, matrix2.times(matrix2));
                this.X_k.setMatrix(0, this.StateSize - 1, 0, 0, matrix4);
                matrix5.setMatrix(0, this.StateSize - 1, 0, this.StateSize - 1, this.P_k.minus(matrix2.times(this.f157H.times(this.P_k))));
                this.P_k.setMatrix(0, this.StateSize - 1, 0, this.StateSize - 1, matrix5);
                return true;
            }
            Log.m31e(this.TAG, "cannot execute MeasurementUpdate(), check initialization ");
            return false;
        }
        Log.m31e(this.TAG, "Error in MeasurementUpdate(), meauserement matrix size is wrong!");
        return false;
    }

    public boolean TimePropagation(double d) {
        Matrix matrix = new Matrix(this.StateSize, 1);
        Matrix matrix2 = new Matrix(this.StateSize, this.StateSize);
        Matrix matrix3 = new Matrix(this.StateSize, this.StateSize);
        Matrix matrix4 = new Matrix(this.StateSize, this.StateSize);
        if (this.flagTransitionMatrix && this.flagInitializeState && this.flagProcessNoise) {
            matrix.setMatrix(0, this.StateSize - 1, 0, 0, this.f156F.times(this.X_k));
            this.X_k.setMatrix(0, this.StateSize - 1, 0, 0, matrix);
            matrix3.setMatrix(0, this.StateSize - 1, 0, this.StateSize - 1, this.f156F.times(this.P_k));
            matrix4.setMatrix(0, this.StateSize - 1, 0, this.StateSize - 1, matrix3.times(this.f156F.inverse()));
            matrix2.setMatrix(0, this.StateSize - 1, 0, this.StateSize - 1, matrix4.plus(this.Q_k));
            this.P_k.setMatrix(0, this.StateSize - 1, 0, this.StateSize - 1, matrix2);
            return true;
        }
        Log.m31e(this.TAG, "cannot execute TimePropagation(), check initialization ");
        return false;
    }

    public double[] getCurrentState() {
        double[] dArr = new double[this.StateSize];
        for (int i = 0; i < this.StateSize; i++) {
            dArr[i] = this.X_k.get(i, 0);
        }
        return dArr;
    }

    public boolean setInitialCovariance(double[][] dArr) {
        if (dArr[0].length != this.StateSize || dArr.length != this.StateSize) {
            return false;
        }
        for (int i = 0; i < this.StateSize; i++) {
            for (int i2 = 0; i2 < this.StateSize; i2++) {
                this.P_k.set(i, i2, dArr[i][i2]);
            }
        }
        this.flagProcessNoise = true;
        return true;
    }

    public boolean setInitialState(double[] dArr) {
        if (dArr.length != this.StateSize) {
            return false;
        }
        for (int i = 0; i < dArr.length; i++) {
            this.X_k.set(i, 0, dArr[i]);
        }
        this.flagInitializeState = true;
        return true;
    }

    public boolean setMeasurementMatrix(double[][] dArr) {
        if (dArr[0].length != this.StateSize || dArr.length != this.MeasurementSize) {
            return false;
        }
        for (int i = 0; i < this.MeasurementSize; i++) {
            for (int i2 = 0; i2 < this.StateSize; i2++) {
                this.f157H.set(i, i2, dArr[i][i2]);
            }
        }
        this.flagInitializeMeasurementM = true;
        return true;
    }

    public boolean setMeasurementNoise(double[][] dArr) {
        if (dArr[0].length != this.MeasurementSize || dArr.length != this.MeasurementSize) {
            return false;
        }
        for (int i = 0; i < this.MeasurementSize; i++) {
            for (int i2 = 0; i2 < this.MeasurementSize; i2++) {
                this.R_k.set(i, i2, dArr[i][i2]);
            }
        }
        this.flagMeasurementNoise = true;
        return true;
    }

    public boolean setProcessNoise(double[][] dArr) {
        if (dArr[0].length != this.StateSize || dArr.length != this.StateSize) {
            return false;
        }
        for (int i = 0; i < this.StateSize; i++) {
            for (int i2 = 0; i2 < this.StateSize; i2++) {
                this.Q_k.set(i, i2, dArr[i][i2]);
            }
        }
        this.flagProcessNoise = true;
        return true;
    }

    public boolean setTransitionMatrix(double[][] dArr) {
        if (dArr[0].length != this.StateSize || dArr.length != this.StateSize) {
            return false;
        }
        for (int i = 0; i < this.StateSize; i++) {
            for (int i2 = 0; i2 < this.StateSize; i2++) {
                this.f156F.set(i, i2, dArr[i][i2]);
            }
        }
        this.flagTransitionMatrix = true;
        return true;
    }
}
