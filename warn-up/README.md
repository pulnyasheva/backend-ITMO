# warm-up-homework
1. Написать метод, который принимает на вход список целых чисел, вернуть из этого метода список чисел, где каждое число 
возведено в квадрат и к результату прибавлено 10. Требуется исключить из результирующего списка все числа, которые 
заканчиваются на 5 или 6. Для решения задания требуется использовать Stream API 

       Пример:

            square56(Arrays.asList(3, 1, 4)) -> [19, 11]
            square56(Arrays.asList(1)) -> [11]
            square56(Arrays.asList(2)) -> [14]


2. Написать метод, который на вход принимает список чисел от 1 до 100. В данном списке существуют дубликаты. Вернуть из 
метода мапу, в которой будет в качестве ключа числа, которое повторялось в списке, а значением – сколько раз это число 
повторяется. Для решения задания требуется использовать Stream API

   
3. Реализуйте многопоточную работу поставщика и потребителя на примере работы склада. Поток-поставщик привозит товары на
склад, а потоки-потребители в порядке очереди забирают товар со склада, если товара нет потоки-потребители просто ждут



4. *Создать 3 потока, каждый из которых будет выводить определенную цифру (1, 2, 3) 6 раз, чтобы получилось число 
123123123123123123. Для решения задания используйте wait(), notify(), notifyAll()
