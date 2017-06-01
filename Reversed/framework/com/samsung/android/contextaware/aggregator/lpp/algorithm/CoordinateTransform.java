package com.samsung.android.contextaware.aggregator.lpp.algorithm;

public class CoordinateTransform {
    public static double[] enu2llh(double[] dArr, double[] dArr2) {
        return xyz2llh(enu2xyz(dArr, llh2xyz(dArr2)));
    }

    public static double[] enu2xyz(double[] dArr, double[] dArr2) {
        double[] dArr3 = new double[3];
        Matrix matrix = new Matrix(dArr, 3);
        double[] xyz2llh = xyz2llh(dArr2);
        double d = xyz2llh[0];
        double d2 = xyz2llh[1];
        double sin = Math.sin(d);
        double cos = Math.cos(d);
        double sin2 = Math.sin(d2);
        double cos2 = Math.cos(d2);
        Matrix matrix2 = new Matrix(3, 3);
        matrix2.set(0, 0, -sin2);
        matrix2.set(0, 1, cos2);
        matrix2.set(0, 2, 0.0d);
        matrix2.set(1, 0, (-sin) * cos2);
        matrix2.set(1, 1, (-sin) * sin2);
        matrix2.set(1, 2, cos);
        matrix2.set(2, 0, cos * cos2);
        matrix2.set(2, 1, cos * sin2);
        matrix2.set(2, 2, sin);
        dArr3[0] = dArr2[0] + matrix2.inverse().times(matrix).get(0, 0);
        dArr3[1] = dArr2[1] + matrix2.inverse().times(matrix).get(1, 0);
        dArr3[2] = dArr2[2] + matrix2.inverse().times(matrix).get(2, 0);
        return dArr3;
    }

    public static double[] llh2enu(double[] dArr, double[] dArr2) {
        double[] dArr3 = new double[3];
        double[] dArr4 = new double[3];
        double[] llh2xyz = llh2xyz(dArr);
        double[] llh2xyz2 = llh2xyz(dArr2);
        for (int i = 0; i < 3; i++) {
            dArr3[i] = llh2xyz[i];
            dArr4[i] = llh2xyz2[i];
        }
        return xyz2enu(dArr3, dArr4);
    }

    public static double[] llh2xyz(double[] dArr) {
        double[] dArr2 = new double[3];
        double d = dArr[0];
        double d2 = dArr[1];
        double d3 = dArr[2];
        double sqrt = Math.sqrt(1.0d - (0.996647189328169d * 0.996647189328169d));
        double sin = Math.sin(d);
        double cos = Math.cos(d);
        double cos2 = Math.cos(d2);
        double sin2 = Math.sin(d2);
        double d4 = 1.0d - (sqrt * sqrt);
        double sqrt2 = Math.sqrt((d4 * (Math.tan(d) * Math.tan(d))) + 1.0d);
        double d5 = ((6378137.0d * cos2) / sqrt2) + ((d3 * cos2) * cos);
        double d6 = ((6378137.0d * sin2) / sqrt2) + ((d3 * sin2) * cos);
        double sqrt3 = (((6378137.0d * d4) * sin) / Math.sqrt(1.0d - (((sqrt * sqrt) * sin) * sin))) + (d3 * sin);
        dArr2[0] = d5;
        dArr2[1] = d6;
        dArr2[2] = sqrt3;
        return dArr2;
    }

    public static double[] xyz2enu(double[] dArr, double[] dArr2) {
        double[] dArr3 = new double[3];
        Matrix matrix = new Matrix(dArr, 3);
        matrix = new Matrix(dArr2, 3);
        Matrix matrix2 = new Matrix(3, 1);
        matrix2.setMatrix(0, 2, 0, 0, matrix.plus(matrix.uminus()));
        double[] xyz2llh = xyz2llh(dArr2);
        double d = xyz2llh[0];
        double d2 = xyz2llh[1];
        double sin = Math.sin(d);
        double cos = Math.cos(d);
        double sin2 = Math.sin(d2);
        double cos2 = Math.cos(d2);
        Matrix matrix3 = new Matrix(3, 3);
        matrix3.set(0, 0, -sin2);
        matrix3.set(0, 1, cos2);
        matrix3.set(0, 2, 0.0d);
        matrix3.set(1, 0, (-sin) * cos2);
        matrix3.set(1, 1, (-sin) * sin2);
        matrix3.set(1, 2, cos);
        matrix3.set(2, 0, cos * cos2);
        matrix3.set(2, 1, cos * sin2);
        matrix3.set(2, 2, sin);
        dArr3[0] = matrix3.times(matrix2).get(0, 0);
        dArr3[1] = matrix3.times(matrix2).get(1, 0);
        dArr3[2] = matrix3.times(matrix2).get(2, 0);
        return dArr3;
    }

    public static double[] xyz2llh(double[] dArr) {
        double[] dArr2 = new double[3];
        double d = dArr[0];
        double d2 = dArr[1];
        double d3 = dArr[2];
        double d4 = d * d;
        double d5 = d2 * d2;
        double d6 = d3 * d3;
        double sqrt = Math.sqrt(1.0d - (0.996647189328169d * 0.996647189328169d));
        double d7 = sqrt * sqrt;
        double d8 = sqrt * 1.0033640898281078d;
        double sqrt2 = Math.sqrt(d4 + d5);
        double d9 = sqrt2 * sqrt2;
        double d10 = (54.0d * 4.0408299984087055E13d) * d6;
        double d11 = (((1.0d - d7) * d6) + d9) - (d7 * (4.0680631590769E13d - 4.0408299984087055E13d));
        double d12 = (((d7 * d7) * d10) * d9) / ((d11 * d11) * d11);
        double pow = Math.pow((1.0d + d12) + Math.sqrt((d12 * d12) + (2.0d * d12)), 0.3333333333333333d);
        double d13 = d10 / (((((((1.0d / pow) + pow) + 1.0d) * 3.0d) * (((1.0d / pow) + pow) + 1.0d)) * d11) * d11);
        double sqrt3 = Math.sqrt((((2.0d * d7) * d7) * d13) + 1.0d);
        double sqrt4 = ((-((d13 * d7) * sqrt2)) / (1.0d + sqrt3)) + Math.sqrt((((4.0680631590769E13d / 2.0d) * ((1.0d / sqrt3) + 1.0d)) - ((((1.0d - d7) * d13) * d6) / ((1.0d + sqrt3) * sqrt3))) - ((d13 * d9) / 2.0d));
        double d14 = (sqrt2 - (d7 * sqrt4)) * (sqrt2 - (d7 * sqrt4));
        double sqrt5 = Math.sqrt(d14 + d6);
        double sqrt6 = Math.sqrt(((1.0d - d7) * d6) + d14);
        double d15 = sqrt5 * (1.0d - (4.0408299984087055E13d / (6378137.0d * sqrt6)));
        double atan = Math.atan((((d8 * d8) * ((4.0408299984087055E13d * d3) / (6378137.0d * sqrt6))) + d3) / sqrt2);
        double atan2 = Math.atan(d2 / d);
        double d16 = d >= 0.0d ? atan2 : (d >= 0.0d || d2 < 0.0d) ? atan2 - 3.141592653589793d : 3.141592653589793d + atan2;
        dArr2[0] = atan;
        dArr2[1] = d16;
        dArr2[2] = d15;
        return dArr2;
    }
}
