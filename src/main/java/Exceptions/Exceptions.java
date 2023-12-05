package Exceptions;

public class Exceptions {
    public static class InvalidInt extends Exception{
        public InvalidInt(){
            super("неправильно введен int");
        }
    }

    public static class InvalidRoomStatus extends Exception{
        public InvalidRoomStatus(){
            super("неправильно введен статус комнаты");
        }
    }

    public static class EmptyDataString extends Exception{
        public EmptyDataString() { super("пустая строка данных"); }
    }

    public static class InvalidDatesInput extends Exception{
        public InvalidDatesInput() { super("неправильно введены даты"); }
    }

    public static class InvalidRoomNumber extends Exception{
        public InvalidRoomNumber() { super("номер не существует или введён неправильно"); }
    }

    public static class InvalidFirstLastName extends Exception{
        public InvalidFirstLastName(){super("имя или фамииля не могут содержать цифр");}
    }

    public static class InvalidWorkExperience extends Exception{
        public InvalidWorkExperience(){super("неправильно введён стаж работника");}
    }
}
