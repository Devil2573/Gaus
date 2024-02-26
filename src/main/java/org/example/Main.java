package org.example;

import java.io.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String[] matrixSize = br.readLine().split(" ");
            System.out.println(Arrays.toString(matrixSize));

            int matrixColumns = Integer.parseInt(matrixSize[1]);
            int matrixRows = Integer.parseInt(matrixSize[0]);

            Fractional[][] matrixOfCoef = new Fractional[matrixRows][matrixColumns];

            for (int i = 0; i != matrixRows; i++) {
                String[] vectorOfCoef = br.readLine().split(" ");
                for (int j = 0; j != matrixColumns; j++) {
                    matrixOfCoef[i][j] = Fractional.createFractional(vectorOfCoef[j]);
                }
            }

            String[] baseXtemp = br.readLine().split(" ");
            Integer[] baseX = new Integer[baseXtemp.length];
            Integer[] startColums = new Integer[baseXtemp.length];
            for (int i = 0; i < baseXtemp.length; i++) {
                baseX[i] = Integer.parseInt(baseXtemp[i]);
                startColums[i] = i + 1;
            }

            for (int i = 0; i != matrixOfCoef.length; i++) {
                System.out.println(Arrays.toString(matrixOfCoef[i]));
            }
            System.out.println(Arrays.toString(baseXtemp));
            System.out.println("\n");

            System.out.println("Меняем столбцы местами для удобства");
            swapColumns(matrixOfCoef, startColums, baseX);
            for (int i = 0; i != matrixOfCoef.length; i++) {
                System.out.println(Arrays.toString(matrixOfCoef[i]));
            }
            System.out.println("\n");

            matrixOfCoef = straightRunning(matrixOfCoef, matrixRows, matrixColumns);
            if (matrixOfCoef == null) {

                System.exit(0);
            }

            boolean flag = true;
            //текущая строчка
            int i = 0;
            //индекс строки с которой надо будет поменять текущую строку
            int coefSwap = 0;
            Fractional CoefMinusOne = new Fractional(-1, 1);
            Fractional CoefOne = new Fractional(1, 1);

            System.out.println("Обратный ход");
            //Обратный ход
            flag = true;
            i = matrixRows - 1; //i = 3 - 1 = 2
            System.out.println();
            for (int t = 0; t != matrixOfCoef.length; t++) {
                System.out.println(Arrays.toString(matrixOfCoef[t]));
            }
            System.out.println();
            while (flag) {
                //проверяем что число не равно нулю, чтобы сделать из него единицу
                if (matrixOfCoef[i][i].getNumerator() != 0) {
                    //пробегаем строчки ниже нашей
                    for (int j = i - 1; j != -1; j--) {// j = 2 - 1 = 1
                        if (matrixOfCoef[j][i].getNumerator() != 0) {
                            Fractional coef = Fractional.division(matrixOfCoef[j][i], matrixOfCoef[i][i]);
                            Fractional[] newRowCoef = matrixOfCoef[j];
                            for (int k = matrixColumns - 1; k != i - 1; k--) {//k = 5 - 1 = 4
                                Fractional subtractible = Fractional.multiplication(matrixOfCoef[i][k], coef);

                                newRowCoef[k] = Fractional.subtraction(matrixOfCoef[j][k], subtractible);
                            }
                            matrixOfCoef[j] = newRowCoef;
                        }
                    }
                }
                System.out.println();
                for (int t = 0; t != matrixOfCoef.length; t++) {
                    System.out.println(Arrays.toString(matrixOfCoef[t]));
                }
                System.out.println();
                i--;
                if (i == -1) {
                    flag = false;
                }
            }

            System.out.println("Final");
            for (int t = 0; t != matrixOfCoef.length; t++) {
                System.out.println(Arrays.toString(matrixOfCoef[t]));
            }
            System.out.println("\n");
            System.out.println("Возвращаем на место столбцы");
            swapColumns(matrixOfCoef, startColums, baseX);
            for (int t = 0; t != matrixOfCoef.length; t++) {
                System.out.println(Arrays.toString(matrixOfCoef[t]));
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("result.txt"))) {
                writer.write("Итоговая матрица\n");
                for (Fractional[] row : matrixOfCoef) {
                    for (Fractional element : row) {
                        writer.write(element.toString());
                        writer.write(" ");
                    }
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Ошибка при записи в файл: " + e.getMessage());
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    static void swapColumns(Fractional[][] matrix, Integer[] startColumns, Integer[] base) {
        Arrays.sort(startColumns);
        Arrays.sort(base);

        for (int i = 0; i != startColumns.length; i++) {
            if (startColumns[i] != base[i]) {
                for (int j = 0; j != matrix.length; j++) {
                    Fractional temp = matrix[j][startColumns[i] - 1];
                    matrix[j][startColumns[i] - 1] = matrix[j][base[i] - 1];
                    matrix[j][base[i] - 1] = temp;
                }
            }
        }

    }

    static Fractional[][] straightRunning(Fractional[][] matrixOfCoef, int matrixRows, int matrixColums) {
        boolean flag = true;
        //текущая строчка
        int i = 0;
        //индекс сттроки с которой надо будет поменять текущую строку
        int coefSwap = 0;
        Fractional CoefMinusOne = new Fractional(-1, 1);
        Fractional CoefOne = new Fractional(1, 1);
        Fractional CoefTakeOut = new Fractional(1, 1);

        Fractional[][] matrixDeterminant = new Fractional[matrixRows][matrixRows];
        for (int t = 0; t != matrixRows; t++) {
            for (int j = 0; j != matrixRows; j++) {
                matrixDeterminant[t][j] = matrixOfCoef[t][j];
            }
        }
        System.out.println("Прямой ход");
        //Прямой ход
        while (flag) {

            //проверяем что число не равно нулю, чтобы сделать из него единицу
            if (matrixOfCoef[i][i].getNumerator() != 0) {
                coefSwap = i;

                //если отрицательное число, то умножаем на -1
                if (!matrixOfCoef[i][i].isPositive()) {
                    Fractional[] newRowCoef = matrixOfCoef[i];
                    CoefTakeOut = Fractional.multiplication(CoefTakeOut, CoefMinusOne);
                    for (int k = i; k != matrixColums; k++) {

                        newRowCoef[k] = Fractional.multiplication(matrixOfCoef[i][k], CoefMinusOne);
                    }
                    //заменяем старые коэффициенты на новые
                    matrixOfCoef[i] = newRowCoef;
                }
                //если число не равно единичке, то делаем его единичкой
                if (!matrixOfCoef[i][i].isOne()) {
                    Fractional inverseCoef = Fractional.division(CoefOne, matrixOfCoef[i][i]);
                    Fractional[] newRowCoef = matrixOfCoef[i];
                    CoefTakeOut = Fractional.multiplication(CoefTakeOut, matrixOfCoef[i][i]);
                    for (int k = i; k != matrixColums; k++) {
                        newRowCoef[k] = Fractional.multiplication(matrixOfCoef[i][k], inverseCoef);
                    }
                    //заменяем старые коэффициенты на новые
                    matrixOfCoef[i] = newRowCoef;
                }

                //пробегаем строчки ниже нашей
                for (int j = i + 1; j != matrixRows; j++) {
                    //проверяем что число в столбце не равно нулю (то есть надо ли вычитать из строки)
                    if (matrixOfCoef[j][i].getNumerator() != 0) {
                        //коэффициент на который надо умножить нашу строку, чтобы вычесть из другой строки и получить нулевой столбец
                        Fractional coef = Fractional.division(matrixOfCoef[j][i], matrixOfCoef[i][i]);
                        //строка коэффициентов из которой вычитаем
                        Fractional[] newRowCoef = matrixOfCoef[j];
                        for (int k = i; k != matrixColums; k++) {
                            //число, которое вычитается
                            Fractional subtractible = Fractional.multiplication(matrixOfCoef[i][k], coef);
                            //заменяем число на разность
                            newRowCoef[k] = Fractional.subtraction(matrixOfCoef[j][k], subtractible);
                        }
                        //заменяем старые коэффициенты на новые
                        matrixOfCoef[j] = newRowCoef;
                    }
                }
                System.out.println();
                for (int t = 0; t != matrixOfCoef.length; t++) {
                    System.out.println(Arrays.toString(matrixOfCoef[t]));
                }
                i++;
                if (i == matrixRows) {
                    flag = false;
                }
                //если число все же равно нулю, то меняем строку на другую
            } else {
                coefSwap++;
                if (coefSwap == matrixRows) {
                    flag = false;
                } else {
                    Fractional[] swap = matrixOfCoef[coefSwap];
                    matrixOfCoef[coefSwap] = matrixOfCoef[i];
                    matrixOfCoef[i] = swap;
                }
            }

        }

        Fractional Determinant = CoefTakeOut;
        for (int t = 0; t != matrixRows; t++) {
            if (matrixOfCoef[t][t].getNumerator() == 0) {
                Determinant.setNumerator(0);
                break;
            }
        }
        System.out.println("Определитель");
        System.out.println(Determinant);
        if (Determinant.getNumerator() != 0) {
            return matrixOfCoef;
        } else {
            System.out.println("Решения нет");
            for (int t = 0; t != matrixRows; t++) {
                System.out.println(Arrays.toString(matrixDeterminant[t]));
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("result.txt"))) {
                writer.write("Решения нет\n");
                for (Fractional[] row : matrixDeterminant) {
                    for (Fractional element : row) {
                        writer.write(element.toString());
                        writer.write(" ");
                    }
                    writer.newLine();
                }
                writer.write("Определитель равен " + Determinant);
            } catch (IOException e) {
                System.out.println("Ошибка при записи в файл: " + e.getMessage());
            }
            return null;
        }
    }


}
