package Controller;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class Tool {
	public static void saveObject(Serializable object, String path) {
		try {
			ObjectOutputStream objstream = new ObjectOutputStream(
					new FileOutputStream(path));
			objstream.writeObject(object);
			objstream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static Object loadObject(String path) throws IOException {
		Object object = null;
		try {
			ObjectInputStream objstream = new ObjectInputStream(
					new FileInputStream(path));
			object = objstream.readObject();
			objstream.close();
		} catch (IOException e) {
			throw e;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return object;
	}
}
