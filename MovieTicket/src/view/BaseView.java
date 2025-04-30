package view;

import java.util.Scanner;

public class BaseView {

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
