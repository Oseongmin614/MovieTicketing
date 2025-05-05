package ui;

import impl.MenuManger;

import java.util.Scanner;

public abstract class BaseView implements MenuManger {

    private Scanner sc;

    public BaseView() {
        sc = new Scanner(System.in);
    }

    protected String scanStr(String msg) {
        System.out.print(msg);
        return sc.nextLine();
    }

    protected int scanInt(String msg) {
        System.out.print(msg);
        int no = Integer.parseInt(sc.nextLine());
        return no;
    }

}
