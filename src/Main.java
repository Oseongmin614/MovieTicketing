import ui.MainView;

public class Main {
	public static void main(String[] args) {
		MainView Viewer = new MainView();
		try {
			Viewer.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
