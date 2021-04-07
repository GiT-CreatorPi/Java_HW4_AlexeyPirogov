//Дисциплина: Java.Уровень 1
//Домашнее задание №: 4 "Java.Крестики-нолики в процедурном стиле"
//Студент: Алексей Пирогов
//Дата: 07.04.2021

import java.util.Scanner;

public class HomeWorkApp_4 {

    public static int SIZE = 3;
    public static int DIFFICULT = 0;
    public static int LEN_WIN_LINE = SIZE - DIFFICULT;
    public static char[][] MAP;
    public static int[] CELL_YX = new int[2];
    public static Scanner in = new Scanner(System.in);
    public static int DOT_2 = 2, DOT_4 = 4;
    public static char DOT_X = 'X', DOT_O = 'O', DOT = '.';
    public static int COUNTER_HUMAN_STEPS = 0;
    public static int COUNTER_COMPUTER_STEPS = 0;
    public static boolean HUMAN_WINNER = false;
    public static boolean PC_WINNER = false;
    public static boolean NO_WINNER = false;
    public static int EXIT = 0;

    public static void main(String[] args) {
       //старт игрового цикла
        do {
            //цикл для ввода размерности игрового поля
            do {
               System.out.print("Введите размер игрового поля в интервале от 3 до 8: ");
               SIZE = in.nextInt();
               if (SIZE >= 3 && SIZE <= 8) break;
            } while (true);

            //выбор выигрышной серии для игровых полей более 3-х
            if (SIZE != 3) {
               do {
                   System.out.print("Выберите размер выигрышной серии: 0 - равна размерности игрового поля (" + SIZE + "), 1 - на единицу меньше (" + (SIZE - 1) + ") : ");
                   DIFFICULT = in.nextInt();
                   if (DIFFICULT == 0 || DIFFICULT == 1) break;
               } while (true);
           }

           createMatrix(SIZE);     //создание и инициализация ирового поля размерностью SIZE
           printMatrixWithNumber(SIZE); //печать игрового поля

           do {    //цикл до наступления победы
               checkWinner();      //проверка ничьей или выигрыша одного из игроков
               if (NO_WINNER == true || HUMAN_WINNER == true || PC_WINNER == true)
                   break; //выход из игры если установлен один из флагов
               stepHuman();        //ход игрока
               printMatrixWithNumber(SIZE);    //печать игрового поля
               checkWinner();      //проверка ничьей или выигрыша одного из игроков
               if (NO_WINNER == true || HUMAN_WINNER == true || PC_WINNER == true)
                   break; //выход из игры если установлен один из флагов
               stepPC();           //ход компьютера
               printMatrixWithNumber(SIZE);    //печать игрового поля
           } while (true);

           //вывод информации о победе или ничьей
           if (NO_WINNER == true) {
               System.out.println("\nНичья!");
           } else if (HUMAN_WINNER == true) {
               System.out.println("\nВы выиграли! С " + COUNTER_HUMAN_STEPS + " хода.");
           } else {
               System.out.println("\nВыиграл компьютер! С " + COUNTER_COMPUTER_STEPS + " хода.");
           }

           //повтор игрового цикла или выход из игры
           System.out.print("\nПродолжить игру? Введите \"1\" - да, \"0\" - нет: ");
           EXIT = in.nextInt();
           if (EXIT == 1) continue; //повтор игрового цикла
           else break;  //выход из игры
       } while (true);

       //формирование отличного настроения у играющего :)
       System.out.print("\nХорошего дня! Спасибо за игру! :)\n\n");
    }

    //--------- Описание методов используемых в игре --------

    //Метод для выдления памяти под матрицу игрового поля и её инициализации
    public static void createMatrix(int maxLen) {
        //инициализация переменных при повторном игровом цикле
        LEN_WIN_LINE = SIZE - DIFFICULT;
        COUNTER_HUMAN_STEPS = 0;
        COUNTER_COMPUTER_STEPS = 0;
        HUMAN_WINNER = false;
        PC_WINNER = false;
        NO_WINNER = false;

        MAP = new char[maxLen][maxLen];  //выделение памяти под игровое поле
        for (int i = 0; i < maxLen; i++)    //инициализация игрового поля символами DOT
            for (int j = 0; j < maxLen; j++)
                MAP[i][j] = DOT;
    }

    //Метод для печати матрицы и адресации игровых позиций
    public static void printMatrixWithNumber(int maxLen) {
        System.out.print("# ");     //начертание первой строки
        for (int i = 1; i < (maxLen + 1); i++)
            System.out.print(i + " ");
        System.out.print(" | X\t Выигрышная серия: " + LEN_WIN_LINE);   //с наименованием оси X и напоминанием о выигрыщной серии
        System.out.println();
        for (int i = 0; i < maxLen; i++) {  //вывод игрового поля
            System.out.print((i + 1) + " ");
            for (int j = 0; j < maxLen; j++)
                System.out.print(MAP[i][j] + " ");
            System.out.print("\n");
        }
        System.out.print("-\nY\n");         //с наименованием оси Y
    }

    //Метод для отражающий информацию ходе человека
    public static void stepHuman() {
        boolean regular;
        do {   //цикл пока ход пользователя не будет успешным
            System.out.print("Введите координаты в формате Y X: "); //считывание вводимых координат
            for (int i = 0; i < 2; i++) {
                CELL_YX[i] = in.nextInt();
                CELL_YX[i]--;
            }
            regular = checkDot(false);  //проверка ячейки на возможность хода, диалог с пользователем, если ход невозможен
            if (regular) {      //установка X, если ход человека удался
                MAP[CELL_YX[0]][CELL_YX[1]] = DOT_X;
                COUNTER_HUMAN_STEPS++;    //инкремент числа шагов
                break;  //выход из цикла и завершение метода
            }
        } while (true);
    }

    public static void stepPC() {
        boolean condition = false;
        System.out.print("Ход компьютера");
        //перебор комбинаций случайных чисел для выполения хода компьютера
        do {
            CELL_YX[0] = (int) (Math.random() * SIZE);   // получение случайного числа для Y
            CELL_YX[1] = (int) (Math.random() * SIZE);   // получение случайного числа для X
            condition = checkDot(true); // вызов метода без вывода на печать информации о логике хода компьютера
            if (condition) {      //установка true (DOT_O), если ход компьютера удался
                MAP[CELL_YX[0]][CELL_YX[1]] = DOT_O;
                System.out.print(" в ячейку: " + (CELL_YX[0] + 1) + " " + (CELL_YX[1] + 1) + "\n");   //информирование о ходе компьютера
                COUNTER_COMPUTER_STEPS++; //инкремент числа шагов
                break; //выход из цикла и завершение метода
            }
        } while (true);
    }

    //Метод для проверки возможность хода в ячейку, на занятость ячейки
    public static boolean checkDot(boolean stepPC) {
        boolean result = false; //переменные для хранения корректности хода и результата хода
        //Усл. № 1. Провекра принадлежности введённых значений множетству допустимых значений
        if (!(CELL_YX[0] >= 0 && CELL_YX[0] <= (SIZE - 1) && CELL_YX[1] >= 0 && CELL_YX[1] <= (SIZE - 1))) {  //информирование человека, что введены некорректные коррдинаты. Не выполняется для хода PC
            if (!stepPC) System.out.print("Введены некорректные координаты. ");
        } else if (MAP[CELL_YX[0]][CELL_YX[1]] == DOT_X || MAP[CELL_YX[0]][CELL_YX[1]] == DOT_O) { //Усл № 2. Провека доступности ячейки, когда человек или PC пытается сделать ход в ячейку
            if (!stepPC)
                System.out.print("Данная точка " + (CELL_YX[0] + 1) + " " + (CELL_YX[1] + 1) + " уже занята \"" + MAP[CELL_YX[0]][CELL_YX[1]] + "\", введите корридинаты другой точки.\n");
        } else {
            result = true; //установка true, если ход человека или компбютера удался, то есть успешно выполнено первое и второе условие
        }
        return result;
    }

    //По сути нужно написать два условия для определения выигрышных комбинаций:
    //- первое условие позволяет определить выигрышную комбинацию, если размерность выигрышной комбинации совпадает с размерностью матрицы
    //- второе условие позовляет определить выигрышную комбинацию, если размерность выигрышной комбинации меньше на 1 по сравнению с размерностью матрицы
    //По требованиям задачи требуется реализовать метод в виде циклов.
    public static void checkWinner() {
        //Переменные для хранения сумм выигрышных комбинаций по строкам и столбцам для человека
        int sumRowHuman = 0, sumColumnHuman = 0, sumDiaHuman = 0, sumSDiaHuman = 0, sumHDiaHuman = 0, sumLDiaHuman = 0, sumSHDiaHuman = 0, sumSLDiaHuman = 0;
        //Константа для хранения признака выигрыша человека
        int checkWinHuman = LEN_WIN_LINE * DOT_2;
        //Переменные для хранения сумм выигрышных комбинаций по строкам и столбцам для компьютера
        int sumRowPC = 0, sumColumnPC = 0, sumDiaPC = 0, sumSDiaPC = 0, sumHDiaPC = 0, sumLDiaPC = 0, sumSHDiaPC = 0, sumSLDiaPC = 0;
        //Константа для хранения признака выигрыша компьютера
        int checkWinPC = LEN_WIN_LINE * DOT_4;
        //Переменные для расчёта ничей
        int noWin, sumAllCell = 0;

        //Шаг 1. Проверка равенства SIZE == LEN_WIN_LINE и расчёт выигрыша в диагоналях
        if (SIZE == LEN_WIN_LINE) {
            for (int i = 0; i < LEN_WIN_LINE; i++) { // цикл для расчёта выигрышной комбинации по правизу SIZE == LEN_WIN_LINE
                if (MAP[i][i] == DOT_X)  sumDiaHuman += DOT_2;
                if (MAP[i][i] == DOT_O)  sumDiaPC += DOT_4;
                if (MAP[i][LEN_WIN_LINE - 1 - i] == DOT_X)  sumSDiaHuman += DOT_2;
                if (MAP[i][LEN_WIN_LINE - 1 - i] == DOT_O)  sumSDiaPC += DOT_4;
            }
            //Проверка выигрыша в прямой и обратной диагонали для человека
            if ((sumDiaHuman == checkWinHuman) || (sumSDiaHuman == checkWinHuman)) HUMAN_WINNER = true;
            //Проверка выигрыша в прямой и обратной диагонали для компьютера
            if ((sumDiaPC == checkWinPC) || (sumSDiaPC == checkWinPC)) PC_WINNER = true;
            //Сброс переменных в ноль для предотвращения ложной установки флага humanWinner
            if (sumDiaHuman != checkWinHuman) sumDiaHuman = 0;
            if (sumSDiaHuman != checkWinHuman) sumSDiaHuman = 0;
            //Сброс переменных в ноль для предотвращения ложной установки флага pcWinner
            if (sumDiaPC != checkWinPC) sumDiaPC = 0;
            if (sumSDiaPC != checkWinPC) sumSDiaPC = 0;
        } else {    // Если равенство SIZE == LEN_WIN_LINE не выполняется, то расчёт выигрыша в подстроках диагоналей и смежных строках с диагоналями
            for (int i = 0; i <= SIZE - LEN_WIN_LINE; i++) {     //цикл для выборки элемента, задания сегмента
                for (int j = 0; j < LEN_WIN_LINE; j++) {   //цикл для отсчёта LEN_WIN_LINE элементов относительно выранного элемента
                    if (MAP[i + j][i + j] == DOT_X) sumDiaHuman += DOT_2;    //расчёт в подстроках прямых и обратных диагоналей
                    if (MAP[i + j][i + j] == DOT_O) sumDiaPC += DOT_4;
                    if (MAP[i + j][LEN_WIN_LINE - j - i] == DOT_X) sumSDiaHuman += DOT_2;
                    if (MAP[i + j][LEN_WIN_LINE - j - i] == DOT_O) sumSDiaPC += DOT_4;

                    if (i == 0) {   //проверка выигрышных комбинаций X и O в линиях расположенных под и над главной диагональю на разных итерациях цикла
                        if (MAP[i + j][j + 1] == DOT_X) sumHDiaHuman += DOT_2;   //верхние линии на итерации цикла i = 0
                        if (MAP[i + j][j + 1] == DOT_O) sumHDiaPC += DOT_4;
                        if (MAP[i + j][LEN_WIN_LINE - 1 - j] == DOT_X) sumSHDiaHuman += DOT_2;
                        if (MAP[i + j][LEN_WIN_LINE - 1 - j] == DOT_O) sumSHDiaPC += DOT_4;
                    } else {
                        if (MAP[i + j][j] == DOT_X) sumLDiaHuman += DOT_2;       //нижние линии на итерации цикла i = 1
                        if (MAP[i + j][j] == DOT_O) sumLDiaPC += DOT_4;
                        if (MAP[i + j][LEN_WIN_LINE - j] == DOT_X) sumSLDiaHuman += DOT_2;
                        if (MAP[i + j][LEN_WIN_LINE - j] == DOT_O) sumSLDiaPC += DOT_4;
                    }
                }
                //Проверка выигрыша для человека. Выигрыш достигается если суммма по строкам или столбцам равна константе checkWinHuman
                if ((sumDiaHuman == checkWinHuman) || (sumSDiaHuman == checkWinHuman) || (sumHDiaHuman == checkWinHuman) ||
                        (sumLDiaHuman == checkWinHuman) || (sumSHDiaHuman == checkWinHuman) || (sumSLDiaHuman == checkWinHuman))
                    HUMAN_WINNER = true;
                //Проверка выигрыша для компьютера. Выигрыш достигается если суммма по строкам или столбцам равна константе checkWinPC
                if ((sumDiaPC == checkWinPC) || (sumSDiaPC == checkWinPC) || (sumHDiaPC == checkWinPC) ||
                        (sumLDiaPC == checkWinPC) || (sumSHDiaPC == checkWinPC) || (sumSLDiaPC == checkWinPC))
                    PC_WINNER = true;
                //Сброс переменных в ноль на каждой из итераций счётчика i для предотвращения ложной установки флага humanWinner
                if (sumDiaHuman != checkWinHuman) sumDiaHuman = 0;   //сброс по главной и побочной диагонали
                if (sumSDiaHuman != checkWinHuman) sumSDiaHuman = 0;
                if (sumHDiaHuman != checkWinHuman) sumHDiaHuman = 0;    //сброс по строкам под и над гланой диагональю
                if (sumLDiaHuman != checkWinHuman) sumLDiaHuman = 0;
                if (sumSHDiaHuman != checkWinHuman) sumSHDiaHuman = 0;      //сброс по строкам под и над обратной диагональю
                if (sumSLDiaHuman != checkWinHuman) sumSLDiaHuman = 0;
                //Сброс переменных в ноль на каждой из итераций счётчика i для предотвращения ложной установки флага pcWinner
                if (sumDiaPC != checkWinPC) sumDiaPC = 0;    //сброс по главной и побочной диагонали
                if (sumSDiaPC != checkWinPC) sumSDiaPC = 0;
                if (sumHDiaPC != checkWinPC) sumHDiaPC = 0;     //сброс по строкам под и над гланой диагональю
                if (sumLDiaPC != checkWinPC) sumLDiaPC = 0;
                if (sumSHDiaPC != checkWinPC) sumSHDiaPC = 0;       //сброс по строкам под и над обратной диагональю
                if (sumSLDiaPC != checkWinPC) sumSLDiaPC = 0;
            }
        }

        //Шаг 2. Определение выигрышной позиции по строкам и столбцам
        if (HUMAN_WINNER == false && PC_WINNER == false)  {  //проверка условия, что не достигнут выигрыш, если выигрыш достигнут в диагоналях, то выход
            if (SIZE == LEN_WIN_LINE) {      //если имеет место быть равенство размера метрицы и выигрышной комбинации, то
                for (int i = 0; i < LEN_WIN_LINE; i++) {     //цикл по строкам/столбцам
                    for (int j = 0; j < LEN_WIN_LINE; j++) {     //цикл по столбцам/строкам. Строгое ограничение справа, а то адресация элемента с адреоом SIZE!!!
                        if (MAP[i][j] == DOT_X) sumRowHuman += DOT_2;    //расчёт ссумм X и O по строкам и столбцам
                        if (MAP[j][i] == DOT_X) sumColumnHuman += DOT_2;
                        if (MAP[i][j] == DOT_O) sumRowPC += DOT_4;
                        if (MAP[j][i] == DOT_O) sumColumnPC += DOT_4;
                    }
                    //Проверка выигрыша для человека. Выигрыш достигается если суммма по строкам или столбцам равна константе checkWinHuman
                    if ((sumRowHuman == checkWinHuman) || (sumColumnHuman == checkWinHuman)) HUMAN_WINNER = true;
                    //Проверка выигрыша для компьютера. Выигрыш достигается если суммма по строкам или столбцам равна константе checkWinPC
                    if ((sumRowPC == checkWinPC) || (sumColumnPC == checkWinPC)) PC_WINNER = true;

                    //Сброс переменных в ноль на каждой из итераций счётчика k для предотвращения ложной установки флага humanWinner
                    if (sumRowHuman != checkWinHuman) sumRowHuman = 0;
                    if (sumColumnHuman != checkWinHuman) sumColumnHuman = 0;
                    //Сброс переменных в ноль на каждой из итераций счётчика k для предотвращения ложной установки флага pcWinner
                    if (sumRowPC != checkWinPC) sumRowPC = 0;
                    if (sumColumnPC != checkWinPC) sumColumnPC = 0;
                }
            } else { //если нет равенства выигрышной комбинации и размерности используемого игрового поля, то расчёт выигрышных комбинация в SIZE -1

                //Перебор условий для каждой из итерации k и l, по сути несколько if "свернуто" в двух итерационные циклы for задающий сегмен и смещение
                //для адресации матриц LEN_WIN_LINE на LEN_WIN_LINE матрицы SIZE на SIZE

                //Смещение по строкам матрицы размером LEN_WIN_LINE на LEN_WIN_LINE внутри матрицы SIZE на SIZE
                for (int k = 0; k <= SIZE - LEN_WIN_LINE; k++)  {   // Цикл для адресации первого элемента выигрышной комбинации в строке/стобце
                    //Смещение по столбцам матрицы размером LEN_WIN_LINE на LEN_WIN_LINE внутри матрицы SIZE на SIZE
                    for (int l = 0; l <= SIZE - LEN_WIN_LINE; l++) {    // Цикл для адресации первого элемента выигрышной комбинации в строке/стобце
                        //Поиски выигрышной комбинациии вунутри матрицы LEN_WIN_LINE на LEN_WIN_LINE для строк и столбцов
                        for (int i = 0; i < LEN_WIN_LINE; i++) {        // Цикл по строкам/столбцам. Адресация по типу сегмент + смещение + перебор строк + перебор элементов
                            //Перебор элементов строк и столбцов
                            for (int j = 0; j < LEN_WIN_LINE; j++) {    // Цикл по столбцам/строкм. Адресация по типу сегмент + смещение + перебор строк + перебор элементов
                                if (MAP[k + i][l + j] == DOT_X) sumRowHuman += DOT_2;  //расчёт ссумм X и O по строкам и столбцам
                                if (MAP[l + j][k + i] == DOT_X) sumColumnHuman += DOT_2;
                                if (MAP[k + i][l + j] == DOT_O) sumRowPC += DOT_4;
                                if (MAP[l + j][k + i] == DOT_O) sumColumnPC += DOT_4;
                                //Проверка выигрыша осуществляется вконце цикла по строкам/столбцам (вконце цикла j)
                                //Проверка выигрыша для человека. Выигрыш достигается если суммма по строкам или столбцам равна константе checkWinHuman
                                if ((sumRowHuman == checkWinHuman) || (sumColumnHuman == checkWinHuman)) HUMAN_WINNER = true;
                                //Проверка выигрыша для компьютера. Выигрыш достигается если суммма по строкам или столбцам равна константе pcWinner
                                if ((sumRowPC == checkWinPC) || (sumColumnPC == checkWinPC)) PC_WINNER = true;
                            }
                            //Сброс накопительных переменных в ноль на каждой новой итераций счётчика i для предотвращения ложной установки флага humanWinner
                            if (sumRowHuman != checkWinHuman) sumRowHuman = 0;
                            if (sumColumnHuman != checkWinHuman) sumColumnHuman = 0;
                            //Сброс накопительных переменных в ноль на каждой новой итераций счётчика i для предотвращения ложной установки флага pcWinner
                            if (sumRowPC != checkWinPC) sumRowPC = 0;
                            if (sumColumnPC != checkWinPC) sumColumnPC = 0;
                        }
                    }
                }
            }
        }

        //Расчёт условия наступления ничьей в зависимости от введённоё размерности матрицы
        if (SIZE % 2 == 0) {    //выбор условия для игровых полей разных рамеров согласно введённой размерности матрицы
            noWin = ((SIZE * SIZE) / 2) * DOT_4 + ((SIZE * SIZE) / 2) * DOT_2; //Если игровое поле размерностью 2n, где n принимает значения [2;4], то ничья достигается при SIZE^2/2 * 4 + SIZE^2/2 * 2.
        } else {
            noWin = ((SIZE * SIZE - 1) / 2) * DOT_4 + ((SIZE * SIZE - 1) / 2) * DOT_2 + DOT_2; //Если игровое поле размерностью 2n + 1, где n принимает значения [1;4], то ничья достигается при (SIZE^2 - 1)/2 * 4 + (SIZE^2 - 1)/2 * 2 + 2
        }

        //Проверка матрицы с ходами игроков в числовом виде на равенство сумме заданной логикой выбора (см. if (SIZE % 2 == 0)),
        //Набор циклов для проверки матрицы на полное заполнение клеток
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if(MAP[i][j] == DOT_X) {     //Если элемент равен X, то увеличиваем сумму на 2
                    sumAllCell += DOT_2;
                } else if(MAP[i][j] == DOT_O) {      //Если элемент равен O, то увеличиваем сумму на 4
                    sumAllCell += DOT_4;
                } else {
                    sumAllCell += 0;   //Если элемент не равен X или O, то сумма остётся неизменной
                }

        //Обработка ситуации когда осталась одна свободная клетка и следующий ход будет выигрышный для ходящей стороны
        if ((sumAllCell >= noWin) && (HUMAN_WINNER == true) && (PC_WINNER == false)) HUMAN_WINNER = true;
        if ((sumAllCell >= noWin) && (HUMAN_WINNER == false) && (PC_WINNER == true)) PC_WINNER = true;
        //Обработка ситуациии когда все клетки заполнены и не установлен флаг победителя
        if ((sumAllCell >= noWin) && (HUMAN_WINNER == false)  && (PC_WINNER == false)) NO_WINNER = true;
    }
}